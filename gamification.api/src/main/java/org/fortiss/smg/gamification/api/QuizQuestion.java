/**
 * 
 */
package org.fortiss.smg.gamification.api;

import java.util.HashMap;

/**
 * @author Pahlke
 * 
 */
public class QuizQuestion {

	private final int id;
	private final String questionText;
	private final int level;
	private final String firstAnswer;
	private final String secondAnswer;
	private final String thirdAnswer;
	private final String fourthAnswer;
	private int correctAnswer;

	public QuizQuestion(int id, String questionText, int level, String firstAnswer,
			String secondAnswer, String thirdAnswer, String fourthAnswer,
			int correctAnswer) {
		super();
		this.id = id;
		this.questionText = questionText;
		this.level = level;
		this.firstAnswer = firstAnswer;
		this.secondAnswer = secondAnswer;
		this.thirdAnswer = thirdAnswer;
		this.fourthAnswer = fourthAnswer;
		this.correctAnswer = correctAnswer;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the firstAnswer
	 */
	public String getFirstAnswer() {
		return firstAnswer;
	}

	/**
	 * @return the secondAnswer
	 */
	public String getSecondAnswer() {
		return secondAnswer;
	}

	/**
	 * @return the thirdAnswer
	 */
	public String getThirdAnswer() {
		return thirdAnswer;
	}

	/**
	 * @return the fourthAnswer
	 */
	public String getFourthAnswer() {
		return fourthAnswer;
	}

	/**
	 * @return the correctAnswer
	 */
	public int getCorrectAnswer() {
		return correctAnswer;
	}
	
	public HashMap<String, Object> serialize(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", this.id);
		map.put("questionText", this.questionText);
		map.put("level", this.level);
		map.put("firstAnswer", this.firstAnswer);
		map.put("secondAnswer", this.secondAnswer);
		map.put("thirdAnswer", this.thirdAnswer);
		map.put("fourthAnswer", this.fourthAnswer);
		map.put("correctAnswer", this.correctAnswer);				
		return map;
	}


}
