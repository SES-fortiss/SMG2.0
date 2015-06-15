/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.gamification.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.gamification.api.GamificationGroup;
import org.fortiss.smg.gamification.impl.GamificationImpl;
import org.fortiss.smg.gamification.api.QuizQuestion;
import org.fortiss.smg.gamification.impl.QuizQuestionStore;
import org.fortiss.smg.gamification.api.SingleGamificationUser;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestGamificationImpl {

	private static MockOtherBundles mocker;

	@BeforeClass
	public static void setUpDataBase() throws SQLException,
			ClassNotFoundException {
		mocker = new MockOtherBundles();
	}

	private GamificationImpl impl;
	private TestingDBUtil db;

	@Before
	public void setUp() throws IOException, TimeoutException,
			ClassNotFoundException {

		Class.forName("com.mysql.jdbc.Driver");
		db = new TestingDBUtil();

		System.out.println("Statement created...");
		String sql = "TRUNCATE " + GamificationImpl.DB_NAME_GMUSERS;
		db.executeQuery(sql);
		sql = "TRUNCATE " + GamificationImpl.DB_NAME_GMGROUPS;
		db.executeQuery(sql);
		sql = "TRUNCATE " + GamificationImpl.DB_NAME_GMMEMBERSHIPS;
		db.executeQuery(sql);
		sql = "TRUNCATE " + QuizQuestionStore.DB_NAME_QUIZQUESTIONS;
		db.executeQuery(sql);
		sql = "TRUNCATE " + GamificationImpl.DB_NAME_GMHEXABUS_CHRONICLE;
		db.executeQuery(sql);
		sql = "TRUNCATE " + GamificationImpl.DB_NAME_GMSCORE_HISTORY;
		db.executeQuery(sql);
		sql = "TRUNCATE " + GamificationImpl.DB_NAME_GMACHIEVEMENTS;
		db.executeQuery(sql);
		sql = "TRUNCATE " + GamificationImpl.DB_NAME_GMPROPERTIES;
		db.executeQuery(sql);
		sql = "TRUNCATE " + GamificationImpl.DB_NAME_GMQUIZQUESTIONHISTORY;
		db.executeQuery(sql);
		sql = "TRUNCATE " + GamificationImpl.DB_NAME_GMDEVICES;
		db.executeQuery(sql);
		System.out.println("searching for informationbroker");
		DefaultProxy<InformationBrokerInterface> clientInfo = new DefaultProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(), 300);

		InformationBrokerInterface broker = clientInfo.init();
		System.out.println("found informationbroker");
		impl = new GamificationImpl();// broker);

		// SingleGamificationUser user = new SingleGamificationUser(0, "Test",
		// 1, "hexabus0", 5, 2);
		// SimpleSerializer.saveToDB(user.serialize(), broker,
		// GamificationImpl.DB_NAME_GMUSERS);
	}

	// public SingleGamificationUser dummyUser() {
	//
	// }

	@Test
	public void createAndRemoveGamificationUserTest() {
		System.out.println("Test startet jetzt");
		impl.createSingleGamificationUser(1, "Klaus Kleber");
		impl.createSingleGamificationUser(1, "Klaus Kleber 3");
		impl.createSingleGamificationUser(2, "Frank Meier");
		impl.createSingleGamificationUser(3, "Helene Fischer");

		impl.removeSingleGamificationUser(2);
	}

	@Test
	public void groupGamificationTest() {
		impl.createGamificationGroup(0, "TestGruppe0");
		impl.createGamificationGroup(1, "TestGruppe1");
		impl.createGamificationGroup(2, "TestGruppe2");
		impl.removeGamificationGroup(1);
	}

	@Test
	public void addScoreTest() {
		impl.createSingleGamificationUser(10, "Friedrich Kloß");
		impl.addPointsToGamificationUser(10, 55);
		impl.addPointsToGamificationUser(10, -20);
	}

	@Test
	public void parentChildTest() {
		impl.createSingleGamificationUser(1, "Klaus Kleber 3");
		impl.createSingleGamificationUser(2, "Frank Meier");
		impl.createSingleGamificationUser(3, "Klaus Müller");
		impl.createSingleGamificationUser(4, "Lisa Klempner");
		impl.createSingleGamificationUser(5, "Thomas Elsner");

		impl.createGamificationGroup(1, "fortiss");
		impl.createGamificationGroup(2, "Room50");
		impl.createGamificationGroup(3, "Room51");
		impl.createGamificationGroup(4, "Room52");

		impl.addGamificationUserToGroup(1, 2);
		impl.addGamificationUserToGroup(2, 3);
		impl.addGamificationUserToGroup(3, 4);
		impl.addGamificationUserToGroup(4, 2);
		impl.addGamificationUserToGroup(5, 4);
		// impl.removeGamificationGroupFromGroup(3, 1);
		// impl.removeGamificationUserFromGroup(1, 1);

		impl.addGamificationGroupToGroup(2, 1);
		impl.addGamificationGroupToGroup(3, 1);
		impl.addGamificationGroupToGroup(4, 1);

		GamificationGroup group1 = impl.getParentGroupOfGamificationUser(2);
		GamificationGroup group2 = impl.getParentGroupOfGamificationGroup(2);

		System.out.println("groupId: " + group1.getId() + ". groupName: "
				+ group1.getName());
		System.out.println("groupId: " + group2.getId() + ". groupName: "
				+ group2.getName());

		Map<Integer, SingleGamificationUser> memberUsers = impl
				.getMemberUsersOfGamificationGroup(4);
		assertEquals(memberUsers.get(3).getName(),"Klaus Müller");
		memberUsers = impl.getMemberUsersOfGamificationGroup(2);
		assertNull(memberUsers.get(3));
		
		Map<Integer, GamificationGroup> memberGroups = impl.getMemberGroupsOfGamificationGroup(1);
		assertNotNull(memberGroups.get(2));
		assertNotNull(memberGroups.get(3));
		assertNotNull(memberGroups.get(4));
		assertNull(memberGroups.get(5));
		System.out.println("Gruppentests durchlaufen\n");
		

	}
	
	
	@Test
	public void testRemovingElementsWithDependencies() {
		impl.createSingleGamificationUser(1, "Heinz Leiber");
		impl.createSingleGamificationUser(2, "Frank Meier");
		impl.createSingleGamificationUser(3, "Klaus Mueller");
		impl.createSingleGamificationUser(4, "Lisa Klempner");
		impl.createSingleGamificationUser(5, "Thomas Elsner");

		impl.createGamificationGroup(6, "fortiss");
		impl.createGamificationGroup(7, "Room50");
		impl.createGamificationGroup(8, "Room51");
		impl.createGamificationGroup(9, "Room52");

		impl.addGamificationUserToGroup(1, 7);
		impl.addGamificationUserToGroup(4, 7);
		impl.addGamificationUserToGroup(2, 8);
		impl.addGamificationUserToGroup(3, 9);
		impl.addGamificationUserToGroup(5, 9);

		impl.addGamificationGroupToGroup(7, 6);
		impl.addGamificationGroupToGroup(8, 6);
		impl.addGamificationGroupToGroup(9, 6);
		
//		impl.removeSingleGamificationUser(1);
//		impl.removeSingleGamificationUser(2);
//		impl.removeSingleGamificationUser(3);
//		impl.removeSingleGamificationUser(4);
//		impl.removeSingleGamificationUser(5);
		impl.removeGamificationGroup(9);
		impl.removeGamificationGroup(8);
		impl.removeGamificationGroup(6);
//		impl.removeGamificationGroup(7);
		
	}
	
	@Test
	public void addAndRemoveQuizQuestions() {
		QuizQuestion question = new QuizQuestion(1, "Blau und Gelb gemischt ergibt?", 0, "Rot", "Gruen", "Rosa", "Schwarz", 2);
		impl.getQuizQuestionStore().addQuizQuestion(question);
		question = new QuizQuestion(2, "2 + 2 = ?", 0, "4", "5", "2", "9", 1);
		impl.getQuizQuestionStore().addQuizQuestion(question);
		question = new QuizQuestion(3, "Hauptstadt von Deutschland?", 0, "Hamburg", "Bonn", "Berlin", "München", 3);
		impl.getQuizQuestionStore().addQuizQuestion(question);
		question = new QuizQuestion(4, "Hauptstadt von Italien?", 0, "Venedig", "Mailand", "Neapel", "Rom", 4);
		impl.getQuizQuestionStore().addQuizQuestion(question);
		question = new QuizQuestion(5, "Testfrage 5. Antwort: A", 0, "Haus", "Auto", "Hund", "Wolke", 1);
		impl.getQuizQuestionStore().addQuizQuestion(question);
		question = new QuizQuestion(6, "Testfrage 6. Antwort: C", 0, "Haus", "Auto", "Hund", "Wolke", 3);
		impl.getQuizQuestionStore().addQuizQuestion(question);
		question = new QuizQuestion(7, "Testfrage 7. Antwort: B", 0, "Haus", "Auto", "Hund", "Wolke", 2);
		impl.getQuizQuestionStore().addQuizQuestion(question);
		impl.getQuizQuestionStore().removeQuizQuestion(7);
		impl.createSingleGamificationUser(1, "TestUser1");
		impl.createSingleGamificationUser(2, "TestUser2");
		impl.getNewQuestionForUser(1);
		impl.getNewQuestionForUser(1);
		impl.getNewQuestionForUser(1);
		impl.getNewQuestionForUser(1);
		impl.getNewQuestionForUser(2);
		boolean answerFirstQuestion = impl.answerOpenQuestionForUser(1, 2, 1);
		boolean answerSecondQuestion = impl.answerOpenQuestionForUser(1, 3, 2);
		boolean answerSecondQuestionSecondTime = impl.answerOpenQuestionForUser(1, 3, 3);
		assertEquals(answerFirstQuestion, true);
		assertEquals(answerSecondQuestion, true);
		assertEquals(answerSecondQuestionSecondTime, false);
	}
	
	@Test
	public void addHexabusID() {
		impl.createSingleGamificationUser(1, "Heinz Leiber");
		impl.createSingleGamificationUser(2, "Frank Meier");
		impl.assignHexabusToUser(1, "asfonasdfn");
		impl.assignHexabusToUser(2, "seg:sdfjsdpoig");
		impl.removeHexabusFromUser(2, "seg:sdfjsdpoig");
		System.out.println("Test abgeschlossen\n");
	}
	
	//requires manual setting of user_score_history table
	@Test
	public void getUserFilteredPoints() {
		impl.createSingleGamificationUser(5, "Max Mustermann");
		impl.createSingleGamificationUser(7, "Matilde Knecht");
		impl.createSingleGamificationUser(10, "Regina Krefeld");
		long startTimeStamp = Calendar.getInstance().getTimeInMillis();
		impl.addPointsToGamificationUser(5, 20);
		impl.addPointsToGamificationUser(7, 30);
		impl.addPointsToGamificationUser(5, 50);
		impl.addPointsToGamificationUser(10, 100);
		impl.removeSingleGamificationUser(10);
		long stopTimeStamp = Calendar.getInstance().getTimeInMillis();
		impl.addPointsToGamificationUser(7, 40);
		impl.addPointsToGamificationUser(7, 50);
		
		impl.addPointsToGamificationUser(5,20);
		//TODO tests für gelöschte User und für Punkte danach und davor
	    Map<Integer,SingleGamificationUser> resultUsers = impl.getGamificationUsersFilterScore(startTimeStamp, stopTimeStamp);
	    assertEquals(2, resultUsers.size());
	    assertNotNull(resultUsers.get(5));
	    assertNotNull(resultUsers.get(7));
	    assertEquals(resultUsers.get(5).getScore(), 70);
	    assertEquals(resultUsers.get(7).getScore(), 30);
	}
	
	@Test
	public void testPropertyAssignments() {
		impl.createSingleGamificationUser(2, "Klaus Klaber");
		impl.createGamificationGroup(3, "TestGruppe3");
		impl.addGamificationUserToGroup(3, 2);
		impl.setUserProperty(2, 1, false);
		impl.setUserProperty(2, 3, false);
		impl.removePropertyFromUser(2, 3, false);
		impl.setUserProperty(2, 3, false);
		impl.setUserProperty(3, 1, true);
//		impl.assignPropertyToUser(2, 5);
	}
	
	@Test
	public void testAchievementAssignments() {
		impl.createSingleGamificationUser(2, "Klaus Klaber");
		impl.createGamificationGroup(3, "TestGruppe3");
		impl.setUserAchievement(2, 4, false);
//		impl.removeSingleGamificationUser(2);
	}
	
	@Test
	public void testAutomaticRemovingOfMembershipsAchievementsAdProperties() {
		impl.createSingleGamificationUser(1, "Klaus Klee");
		impl.createGamificationGroup(1, "Gruppe für Klaus Klee");
		impl.createGamificationGroup(2, "Zweite Gruppe");
		impl.addGamificationUserToGroup(1, 1);
		impl.addGamificationGroupToGroup(1, 2);
		impl.removeGamificationUserFromGroup(1, 1);
		impl.removeGamificationGroupFromGroup(1, 2);
		impl.removeSingleGamificationUser(1);
	}
	
	@Test
	public void testAddingAndRemovingOfDevices() {
		impl.addDeviceIDToGamificationSystem("asfjs:asff");
		impl.addDeviceIDToGamificationSystem("oijsodg:edfdssdf");
		impl.addDeviceIDToGamificationSystem("wuff");
		impl.addDeviceIDToGamificationSystem("blob");
		impl.createSingleGamificationUser(1, "Nimmt wuff");
		impl.createSingleGamificationUser(2, "hat keinen Hexabus");
		impl.assignHexabusToUser(1, "wuff");
		impl.assignHexabusToUser(1, "blob");
		impl.assignHexabusToUser(1, "sollteNichtKlappen");
		
		Set<String> testDevices = impl.getDevices();
		Set<String> testUnoccupiedDevices = impl.getUnoccupiedDevices();
		
		impl.removeDeviceIDFromGamificationSystem("asfjs:asff");
		Set<String> testDevicesAfterRemoving = impl.getDevices();
		Set<String> testUnOccupiedDevicesAfterRemoving = impl.getUnoccupiedDevices();
		
	}

}
