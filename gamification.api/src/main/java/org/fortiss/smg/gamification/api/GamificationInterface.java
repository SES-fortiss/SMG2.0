package org.fortiss.smg.gamification.api;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.HealthCheck;

public interface GamificationInterface extends HealthCheck {
    String doSomething(String arg) throws TimeoutException;
    
    //creates a new gamification user
    public void createSingleGamificationUser(int userManagerID, String name) throws TimeoutException;
    
    //removes a gamification user
    public boolean removeSingleGamificationUser(int userManagerID);
    
    //creates a new gamification group
    public void createGamificationGroup(int id, String name);
    
    //removes a gamification group
    public boolean removeGamificationGroup(int id);
    
    //adds points to a gamification user
    public boolean addPointsToGamificationUser(int userManagerID, int points) throws TimeoutException;

    //adds points to a gamification group
    public boolean addPointsToGamificationGroup(int groupID, int points);
    
    //returns copies(!) of all gamification users
    public Map<String, SingleGamificationUser> getGamificationUsers();
    
    //returns copies(!) of all gamification groups
    public Map<String,GamificationGroup> getGamificationGroups() throws TimeoutException;
    
    //adds the user with "userManagerID" to the group with "groupID"
    public boolean addGamificationUserToGroup(int userManagerID, int groupID);
    
    //starts the membership of the group "groupIDChild" in the group "groupIDParent"
    public boolean addGamificationGroupToGroup(int groupIDChild, int groupIDParent);
    
    //ends the membership of the user with "userManagerID" in the group with "groupID"
    public boolean removeGamificationUserFromGroup(int userManagerID, int groupID);
    
    //ends the membership relationship between two groups (child and parent)
    public boolean removeGamificationGroupFromGroup(int groupIDChild, int groupIDParent);

	//returns the gamification group where the single gamification user with "userManagerID" is a member
	public GamificationGroup getParentGroupOfGamificationUser(int userManagerID);

	//returns the gamification group where the group with "groupId" is a member
	public GamificationGroup getParentGroupOfGamificationGroup(int groupID);
	
	//returns all single gamification users that are members of the gamification group with "groupId"
	public Map<Integer,SingleGamificationUser> getMemberUsersOfGamificationGroup(int groupId);

	//returns all gamification groups that are members of the gamification group with "groupId"
	public Map<Integer,GamificationGroup> getMemberGroupsOfGamificationGroup(int groupId);
	
	//assigns a hexabus device to a gamification user
	public boolean assignHexabusToUser(int userManagerID, String hexabusID);
	
	//removes a hexabus device from a gamification user
	public boolean removeHexabusFromUser(int userManagerID, String hexabusID);
	
    //returns all the single gamification user with an edited score consisting
    //only of the points that were scored between the start- and the stopTimeStamp
    public Map<String, SingleGamificationUser> getGamificationUsersFilterScore(long startTimestamp, long endTimeStamp);

    //returns all gamificationGroups with an edited score consisting
    //only of the points that were scored between the start- and the stopTimeStamp
    public Map<String, GamificationGroup> getGamificationGroupsFilterScore(long startTimestamp, long endTimeStamp);
    
	//returns all Properties the participant fulfills
	//the second parameter specifies if the participant is a group or a single user
	public Map<Integer,Property> getPropertiesForUser(int ID, boolean userIsGroup);
	
	//returns all Achievements the participant has
	//the second parameter specifies if the participant is a group or a single user
	public Map<Integer,Achievement> getAchievementsForUser(int ID, boolean userIsGroup);

	//test function that checks if a participant has a certain achievement
	//the third parameter specifies if the participant is a group or a single user
	public boolean userHasAchievement(int participantID, int achievementID, boolean userIsGroup);

	//test function that checks if a participant fulfills a certain property
	//the third parameter specifies if the participant is a group or a single user
	public boolean userFullfillsProperty(int ID, int propertyID, boolean userIsGroup);
	
	//returns a question which
	//a) wasn't given to the user before
	//b) has the same difficulty level as the level of the user
	//if there is no more question that fulfills these conditions the function returns null
	public QuizQuestion getNewQuestionForUser(int userManagerID);
	
	//answer a question
	//this only works if it is a question that was asked (and not yet answered) to the specified user
	//if the answering event worked correctly this function returns 'true'
	//the possible answer values are 1,2,3,4
	//the function does NOT return the correctness of the answer.
	//In order to see whether the answer is correct one can check the QuizQuestion object
	//in which the correct answer is saved
	public boolean answerOpenQuestionForUser(int userManagerID, int questionID, int answer) throws TimeoutException;
    
	
	//gets all devices that are available in gamification context (ny now only hexabusses)
	public Set<String> getDevices();
	
	//gets all devices available for gamification that are not yet assigned to another user
	public Set<String> getUnoccupiedDevices();
	
}
