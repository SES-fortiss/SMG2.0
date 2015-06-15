/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
/**
 * 
 */
package org.fortiss.smg.gamification.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.gamification.api.QuizQuestion;
import org.fortiss.smg.informationbroker.api.IDatabase;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.sqltools.lib.serialize.SimpleSerializer;
import org.slf4j.LoggerFactory;

/**
 * @author Pahlke
 * 
 */
public class QuizQuestionStore {

	private Map<Integer, QuizQuestion> questions;
	private GamificationImpl gamificationImpl;

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(QuizQuestionStore.class);
	IDatabase database;
	public static final String DB_NAME_QUIZQUESTIONS = "QuizManager_questions";
	private static final int TIMEOUTLONG = 5000;

	/**
	 * 
	 */
	public QuizQuestionStore(GamificationImpl gamificationImpl) {

		this.gamificationImpl = gamificationImpl;
		
		DefaultProxy<InformationBrokerInterface> clientInfo = new DefaultProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(), TIMEOUTLONG);

		try {
			InformationBrokerInterface broker = clientInfo.init();

			this.questions = new HashMap<Integer, QuizQuestion>();
			this.database = broker;

			/*
			 * Request Structure from Database (if exists)
			 */
			loadFromDB();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addQuizQuestion(QuizQuestion question) {
		if (!questions.containsKey(question.getId())) {
			questions.put(question.getId(), question);
		}
		saveQuizQuestion(question);
	}
	
	public boolean removeQuizQuestion(int questionID) {
		try {
			/**
			 * Check if User exists
			 */
			String sql = "SELECT *  FROM " + QuizQuestionStore.DB_NAME_QUIZQUESTIONS
					+ " WHERE `id` = '" + questionID + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Question to delete return 0
				 */
				return false;
			}
			sql = "DELETE FROM " + QuizQuestionStore.DB_NAME_QUIZQUESTIONS
					+ " WHERE `id` =  '" + questionID + "';";
			this.database.executeQuery(sql);

			this.questions.remove(questionID);
			return true;
		} catch (TimeoutException e) {
			QuizQuestionStore.logger.warn("SQL Statement error", e);
		}
		return false;
	}

	
	private boolean saveQuizQuestion(QuizQuestion question) {
		return SimpleSerializer.saveToDB(question.serialize(), database,
				QuizQuestionStore.DB_NAME_QUIZQUESTIONS);
	}


	private void loadFromDB() {

		try {
			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM " + QuizQuestionStore.DB_NAME_QUIZQUESTIONS;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				QuizQuestionStore.logger.warn("No quiz questionsfound");
			}
			/*
			 * go through results and create containers
			 */
			else {
				for (Map<String, Object> result : resultSet) {
					
					QuizQuestion question = new QuizQuestion(
							Integer.parseInt(result.get("id").toString()),
							result.get("questiontext").toString(),
							Integer.parseInt(result.get("level").toString()),
							result.get("firstanswer").toString(),
							result.get("secondanswer").toString(),
							result.get("thirdanswer").toString(),
							result.get("fourthanswer").toString(),
							Integer.parseInt(result.get("correctanswer").toString()));
					
					questions.put(question.getId(), question);
					logger.info("Quiz question added from DB: "
							+ question.getQuestionText());

				}
			}
		} catch (TimeoutException e) {
			QuizQuestionStore.logger.warn("SQL Statement error", e);
		}

	}

	/**
	 * @return the questions
	 */
	public Map<Integer, QuizQuestion> getQuestions() {
		return questions;
	}
	
	

}
