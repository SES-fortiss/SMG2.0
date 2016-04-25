package org.fortiss.smg.gamification.impl;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.gamification.api.Achievement;
import org.fortiss.smg.gamification.api.GamificationGroup;
//import org.fortiss.smg.containermanager.impl.ContainerManagerImpl;
import org.fortiss.smg.gamification.api.GamificationInterface;
import org.fortiss.smg.gamification.api.GamificationParticipant;
import org.fortiss.smg.gamification.api.Property;
import org.fortiss.smg.gamification.api.PropertyAchievementIDs;
import org.fortiss.smg.gamification.api.QuizQuestion;
import org.fortiss.smg.gamification.api.SingleGamificationUser;
import org.fortiss.smg.informationbroker.api.IDatabase;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.sqltools.lib.serialize.SimpleSerializer;
import org.slf4j.LoggerFactory;

import properties.PropertyFactory;
import achievements.AchievementFactory;

public class GamificationImpl implements GamificationInterface {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(GamificationImpl.class);
	private Map<Integer, SingleGamificationUser> endUsers;
	private Map<Integer, GamificationGroup> gamificationGroups;
	private Map<Integer, Set<Integer>> propertyAchievementAssociations;
	private Map<Integer, Set<Integer>> achievementPropertyAssociations;
	private AchievementFactory achievementFactory = new AchievementFactory();
	private PropertyFactory propertyFactory = new PropertyFactory();
	private Map<Integer, Integer> pointsForQuestionDifficulty;
	
	IDatabase database;
	public static final String DB_NAME_GMUSERS = "gamificationmanager_end_users";
	public static final String DB_NAME_GMGROUPS = "GamificationManager_Groups";
	public static final String DB_NAME_GMMEMBERSHIPS = "GamificationManager_group_memberships";
	public static final String DB_NAME_GMHEXABUS_CHRONICLE = "user_hexabus_assignments_chronicle";
	public static final String DB_NAME_GMSCORE_HISTORY = "user_score_history";
	public static final String DB_NAME_GMPROPERTIES = "gamificationmanager_user_properties";
	public static final String DB_NAME_GMACHIEVEMENTS = "gamificationmanager_user_achievements";
	public static final String DB_NAME_GMQUIZQUESTIONHISTORY = "quizquestion_history";
	public static final String DB_NAME_GMDEVICES= "gamificationmanager_devices";
	
	private static final int TIMEOUTLONG = 5000;
//	private static final int properties = 1;
//	private static final int achievements = 2;
	
	private QuizQuestionStore quizQuestionStore;

	public GamificationImpl() {

		DefaultProxy<InformationBrokerInterface> clientInfo = new DefaultProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(), TIMEOUTLONG);

		try {
			InformationBrokerInterface broker = clientInfo.init();

			
			endUsers = new HashMap<Integer, SingleGamificationUser>();
			gamificationGroups = new HashMap<Integer, GamificationGroup>();
			database = broker;
			quizQuestionStore = new QuizQuestionStore(this);
			propertyAchievementAssociations = new HashMap<Integer, Set<Integer>>();
			achievementPropertyAssociations= new HashMap<Integer, Set<Integer>>();
			pointsForQuestionDifficulty = new HashMap<Integer, Integer>();
			pointsForQuestionDifficulty.put(0, 10);
			pointsForQuestionDifficulty.put(1, 18);
			pointsForQuestionDifficulty.put(2, 25);
			pointsForQuestionDifficulty.put(3, 31);
			pointsForQuestionDifficulty.put(4, 36);
			
			//setting up the dependencies between properties and achievements
			setupProperty(PropertyAchievementIDs.registerProperty);
			setupProperty(PropertyAchievementIDs.onePointProperty);
			setupProperty(PropertyAchievementIDs.hundredPointsProperty);
			setupAchievement(PropertyAchievementIDs.registerAchievement);
			setupAchievement(PropertyAchievementIDs.onePointAchievement);
			setupAchievement(PropertyAchievementIDs.hundredPointsAchievement);
			linkAchievementToProperty(PropertyAchievementIDs.registerAchievement,
					PropertyAchievementIDs.registerProperty);
			linkAchievementToProperty(PropertyAchievementIDs.onePointAchievement,
					PropertyAchievementIDs.onePointProperty);
			linkAchievementToProperty(PropertyAchievementIDs.hundredPointsAchievement,
					PropertyAchievementIDs.hundredPointsProperty);
			

			/*
			 * Request Structure from Database (if exists)
			 */
			loadfromDB();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createSingleGamificationUser(int userManagerID, String name) throws TimeoutException {
		SingleGamificationUser user = new SingleGamificationUser(userManagerID,
				name);
		addSingleGamificationUser(user);
		setUserProperty(userManagerID, PropertyAchievementIDs.registerProperty, false);
	}

	public void createGamificationGroup(int id, String name) {
		GamificationGroup group = new GamificationGroup(id, 0, 0, name);
		addGamificationGroup(group);
//		setUserProperty(id, PropertyAchievementIDs.registerProperty, true);
	}
	
	public boolean removeSingleGamificationUser(int userManagerID) {
		try {
			/**
			 * Check if User exists
			 */
			String sql = "SELECT *  FROM " + GamificationImpl.DB_NAME_GMUSERS
					+ " WHERE userManagerID = '" + userManagerID + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no User to delete return 0
				 */
				return false;
			}
			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMUSERS
					+ " WHERE `userManagerID` =  '" + userManagerID + "';";
			this.database.executeQuery(sql);

			this.endUsers.remove(userManagerID);
			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMMEMBERSHIPS
					+ " WHERE `childID` =  '" + userManagerID + "'"
					+ " AND `childIsGroup` = '0';";
			this.database.executeQuery(sql);

			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMPROPERTIES
					+ " WHERE `userID` =  '" + userManagerID + "'"
					+ " AND `userIsGroup` = '0';";
			this.database.executeQuery(sql);

			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMACHIEVEMENTS
					+ " WHERE `userID` =  '" + userManagerID + "'"
					+ " AND `userIsGroup` = '0';";
			this.database.executeQuery(sql);
			return true;
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}

		return false;
	}

	public boolean removeGamificationGroup(int id) {
		try {
			/**
			 * Check if Group exists
			 */
			String sql = "SELECT *  FROM " + GamificationImpl.DB_NAME_GMGROUPS
					+ " WHERE `id` = '" + id + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Group to delete return 0
				 */
				return false;
			}
			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMGROUPS
					+ " WHERE `id` =  '" + id + "';";
			
			this.database.executeQuery(sql);

			this.gamificationGroups.remove(id);
			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMMEMBERSHIPS
					+ " WHERE `childID` =  '" + id + "'"
					+ " AND `childIsGroup` =  '1'"
					+ " OR `parentID` =  '" + id
					+ "';";
			this.database.executeQuery(sql);
			
			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMPROPERTIES
					+ " WHERE `userID` =  '" + id + "'"
					+ " AND `userIsGroup` = '1';";
			this.database.executeQuery(sql);

			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMACHIEVEMENTS
					+ " WHERE `userID` =  '" + id + "'"
					+ " AND `userIsGroup` = '1';";
			this.database.executeQuery(sql);
			return true;
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}

		return false;
	}

	private void addSingleGamificationUser(SingleGamificationUser user) {
		if (!endUsers.containsKey(user.getUserManagerID())) {
			endUsers.put(user.getUserManagerID(), user);
		}
		saveSingleGamificationUser(user);
	}

	private void addGamificationGroup(GamificationGroup group) {
		if (!gamificationGroups.containsKey(group.getId())) {
			gamificationGroups.put(group.getId(), group);
		}
		saveGamificationGroup(group);
	}
	
	private boolean saveSingleGamificationUser(SingleGamificationUser user) {
		return SimpleSerializer.saveToDB(user.serialize(), database,
				GamificationImpl.DB_NAME_GMUSERS);
	}

	private boolean saveGamificationGroup(GamificationGroup group) {
		return SimpleSerializer.saveToDB(group.serialize(), database,
				GamificationImpl.DB_NAME_GMGROUPS);
	}

	public boolean updateSingleGamificationUser(SingleGamificationUser user) {
		return saveSingleGamificationUser(user);
	}

	public boolean updateGamificationGroup(GamificationGroup group) {
		return saveGamificationGroup(group);
	}

	// private boolean saveVirtualGamificationParticipant(GamificationGroup
	// virtualUser){
	//
	// System.out.println("Saving " + virtualUser.getId() + " " +
	// virtualUser.getName());
	//
	// if(!virtualParticipants.containsKey(virtualUser.getId())){
	// virtualParticipants.put(virtualUser.getId(), virtualUser);
	// }
	// return SimpleSerializer.saveToDB(virtualUser.serialize(), database,
	// GamificationImpl.DB_NAME_GMVIRTUALUSERS);
	// }

	private void loadfromDB() {

		try {
			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM " + GamificationImpl.DB_NAME_GMUSERS;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				GamificationImpl.logger.warn("No Gamification Users found");
			}
			/*
			 * go through results and create containers
			 */
			else {
				for (Map<String, Object> result : resultSet) {

					SingleGamificationUser user = new SingleGamificationUser(
							Integer.parseInt(result.get("level").toString()),
							Integer.parseInt(result.get("score").toString()),
							result.get("name").toString(),
							Integer.parseInt(result.get("usermanagerid")
									.toString()), result.get("hexabusid")
									.toString());
					this.endUsers.put(user.getUserManagerID(), user);
					logger.info("Gamification User added from DB: "
							+ user.getName());

				}
			}

			sql = "SELECT *  FROM " + GamificationImpl.DB_NAME_GMGROUPS;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				GamificationImpl.logger.warn("No Gamification Groups found");
			}
			/*
			 * go through results and create containers
			 */
			else {
				for (Map<String, Object> result : resultSet) {

					GamificationGroup group = new GamificationGroup(
							Integer.parseInt(result.get("id").toString()),
							Integer.parseInt(result.get("level").toString()),
							Integer.parseInt(result.get("score").toString()),
							result.get("name").toString());

					this.gamificationGroups.put(group.getId(), group);
					logger.info("Gamification Group added from DB: "
							+ group.getName());
				}
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
	}

	public String doSomething(String s) {
		return "Hello smg";
	}

	@Override
	public boolean isComponentAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addPointsToGamificationUser(int userManagerID, int points) throws TimeoutException {
		if (endUsers.containsKey(userManagerID)) {
			SingleGamificationUser user = endUsers.get(userManagerID);
			user.setScore(user.getScore() + points);
			updateSingleGamificationUser(user);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("userID", userManagerID);
			map.put("userIsGroup", 0);
			map.put("pointsAssigned", points);
			map.put("timestamp", Calendar.getInstance().getTimeInMillis());
			SimpleSerializer.saveToDB(map, database,
					GamificationImpl.DB_NAME_GMSCORE_HISTORY);
			
			//assignProperties/Achievements
			if(user.getScore() >= 100) {
				setUserProperty(userManagerID, PropertyAchievementIDs.hundredPointsProperty, false);
				setUserProperty(userManagerID, PropertyAchievementIDs.onePointProperty, false);
			}
			else if(user.getScore() < 100 && user.getScore() >= 0) {
				removePropertyFromUser(userManagerID, PropertyAchievementIDs.hundredPointsProperty, false);
				setUserProperty(userManagerID, PropertyAchievementIDs.onePointProperty, false);
			}
			else {
				removePropertyFromUser(userManagerID, PropertyAchievementIDs.hundredPointsProperty, false);
				removePropertyFromUser(userManagerID, PropertyAchievementIDs.onePointProperty, false);
			}
			return true;
		}
		return false;
	}

	public boolean addPointsToGamificationGroup(int groupID, int points) {
		if (gamificationGroups.containsKey(groupID)) {
			GamificationGroup group = gamificationGroups.get(groupID);
			group.setScore(group.getScore() + points);
			updateGamificationGroup(group);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("userID", groupID);
			map.put("userIsGroup", 1);
			map.put("pointsAssigned", points);
			map.put("timestamp", Calendar.getInstance().getTimeInMillis());
			SimpleSerializer.saveToDB(map, database,
					GamificationImpl.DB_NAME_GMSCORE_HISTORY);
			return true;
		}
		return false;
	}

	public Map<String, SingleGamificationUser> getGamificationUsers() {
		Map<String, SingleGamificationUser> clonedUsers = new HashMap<String, SingleGamificationUser>();
		if(endUsers != null) {
			Iterator<SingleGamificationUser> itr = endUsers.values().iterator();
			while(itr.hasNext()) {
				SingleGamificationUser user = itr.next();
				clonedUsers.put(""+user.getUserManagerID(), new SingleGamificationUser(user));
			}
		}
		return clonedUsers;
	}

	public Map<String, GamificationGroup> getGamificationGroups() throws TimeoutException{
		Map<String, GamificationGroup> clonedGroups = new HashMap<String, GamificationGroup>();
		logger.debug("Select Groups");
		if(gamificationGroups != null) {
			logger.debug("Groups exist");
			Iterator<GamificationGroup> itr = gamificationGroups.values().iterator();
			while(itr.hasNext()) {
				GamificationGroup group = itr.next();
				clonedGroups.put(""+group.getId(), new GamificationGroup(group));
			}
		}
		logger.info("Found "+ clonedGroups.size() + " groups");
		return clonedGroups;
	}

	public boolean addGamificationUserToGroup(int userManagerID, int groupID) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentID", groupID);
		map.put("childID", userManagerID);
		map.put("childIsGroup", 0);
		return SimpleSerializer.saveToDB(map, database,
				GamificationImpl.DB_NAME_GMMEMBERSHIPS);
	}

	public boolean addGamificationGroupToGroup(int groupIDChild,
			int groupIDParent) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("parentID", groupIDParent);
		map.put("childID", groupIDChild);
		map.put("childIsGroup", 1);

		return SimpleSerializer.saveToDB(map, database,
				GamificationImpl.DB_NAME_GMMEMBERSHIPS);
	}

	public boolean removeGamificationUserFromGroup(int userManagerID,
			int groupID) {
		try {

			/**
			 * Check if User exists
			 */
			String sql = "SELECT *  FROM "
					+ GamificationImpl.DB_NAME_GMMEMBERSHIPS
					+ " WHERE `childID` = '" + userManagerID
					+ "' AND `parentID` = '" + groupID + "' AND `childIsGroup` = '0';";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Container to delete return 0
				 */
				return false;
			}

			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMMEMBERSHIPS
					+ " WHERE `childID` = '" + userManagerID
					+ "' AND `parentID` = '" + groupID + "' AND `childIsGroup` = '0';";

			if (this.database.executeQuery(sql)) {
				return true;
			} else {
				return false;
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return false;
	}

	public boolean removeGamificationGroupFromGroup(int groupIDChild,
			int groupIDParent) {
		try {

			/**
			 * Check if User exists
			 */
			String sql = "SELECT *  FROM "
					+ GamificationImpl.DB_NAME_GMMEMBERSHIPS
					+ " WHERE `childID` = '" + groupIDChild
					+ "' AND `parentID` = '" + groupIDParent
					+ "' AND `childIsGroup` = '1';";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Container to delete return 0
				 */
				return false;
			}

			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMMEMBERSHIPS
					+ " WHERE `childID` = '" + groupIDChild
					+ "' AND `parentID` = '" + groupIDParent
					+ "' AND `childIsGroup` = '1';";

			if (this.database.executeQuery(sql)) {
				return true;
			} else {
				return false;
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return false;
	}

	public GamificationGroup getParentGroupOfGamificationUser(int userManagerID) {
		try {
			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM "
					+ GamificationImpl.DB_NAME_GMMEMBERSHIPS
					+ " WHERE childID = '" + userManagerID
					+ "' AND childIsGroup = '" + "0';";

			resultSet = this.database.getSQLResults(sql);
			int parentID = -1;

			if (resultSet == null || resultSet.size() < 1) {
				GamificationImpl.logger.warn("No Parent Group found");
			}
			/*
			 * go through results and create containers
			 */
			else {
				GamificationGroup group;
				HashMap<Integer, GamificationGroup> parentGroups = new HashMap<Integer, GamificationGroup>();
				for (Map<String, Object> result : resultSet) {

					parentID = Integer.parseInt(result.get("parentid")
							.toString());
					group = gamificationGroups.get(parentID);
					parentGroups.put(parentID, new GamificationGroup(group));
				}
				if (parentGroups.size() > 1) {
					GamificationImpl.logger
							.warn("More than one Parent Group found");
					return null;
				} else {
					return parentGroups.get(parentID);
				}
			}

			// Hier für die GamificationGroups
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return null;
	}

	public GamificationGroup getParentGroupOfGamificationGroup(int groupID) {
		try {
			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM "
					+ GamificationImpl.DB_NAME_GMMEMBERSHIPS
					+ " WHERE childID = '" + groupID + "' AND childIsGroup = '"
					+ "1';";

			resultSet = this.database.getSQLResults(sql);
			int parentID = -1;

			if (resultSet == null || resultSet.size() < 1) {
				GamificationImpl.logger.warn("No Parent Group found");
			}
			/*
			 * go through results and create containers
			 */
			else {
				GamificationGroup group;
				HashMap<Integer, GamificationGroup> parentGroups = new HashMap<Integer, GamificationGroup>();
				for (Map<String, Object> result : resultSet) {

					parentID = Integer.parseInt(result.get("parentid")
							.toString());
					group = gamificationGroups.get(parentID);
					parentGroups.put(parentID, new GamificationGroup(group));
				}
				if (parentGroups.size() > 1) {
					GamificationImpl.logger
							.warn("More than one Parent Group found");
					return null;
				} else {
					return parentGroups.get(parentID);
				}
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return null;
	}

	public Map<Integer, SingleGamificationUser> getMemberUsersOfGamificationGroup(
			int groupId) {
		try {
			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM "
					+ GamificationImpl.DB_NAME_GMMEMBERSHIPS
					+ " WHERE parentID = '" + groupId
					+ "' AND childIsGroup = '" + "0';";

			resultSet = this.database.getSQLResults(sql);
			int memberID = -1;

			if (resultSet == null || resultSet.size() < 1) {
				GamificationImpl.logger.warn("No Members found");
			}
			/*
			 * go through results and find userMembers to copy
			 */
			else {
				SingleGamificationUser user;
				Map<Integer, SingleGamificationUser> userMembers = new HashMap<Integer, SingleGamificationUser>();
				for (Map<String, Object> result : resultSet) {

					memberID = Integer.parseInt(result.get("childid")
							.toString());
					user = endUsers.get(memberID);
					userMembers.put(memberID, new SingleGamificationUser(user));
				}
				return userMembers;
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return null;
	}

	/**
	 * @return the quizQuestionStore
	 */
	public QuizQuestionStore getQuizQuestionStore() {
		return quizQuestionStore;
	}

	public Map<Integer, GamificationGroup> getMemberGroupsOfGamificationGroup(
			int groupId) {
		try {
			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM "
					+ GamificationImpl.DB_NAME_GMMEMBERSHIPS
					+ " WHERE parentID = '" + groupId
					+ "' AND childIsGroup = '" + "1';";

			resultSet = this.database.getSQLResults(sql);
			int memberID = -1;

			if (resultSet == null || resultSet.size() < 1) {
				GamificationImpl.logger.warn("No Member Groups found");
			}
			/*
			 * go through results and create groups
			 */
			else {
				GamificationGroup group;
				Map<Integer, GamificationGroup> memberGroups = new HashMap<Integer, GamificationGroup>();
				for (Map<String, Object> result : resultSet) {

					memberID = Integer.parseInt(result.get("childid")
							.toString());
					group = gamificationGroups.get(memberID);
					memberGroups.put(memberID, new GamificationGroup(group));
				}
				return memberGroups;
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return null;
	}

	public boolean assignHexabusToUser(int userManagerID, String hexabusID) {
		if(endUsers.containsKey(userManagerID) && getUnoccupiedDevices().contains(hexabusID)) {
			SingleGamificationUser user = endUsers.get(userManagerID);
			user.setHexabusID(hexabusID);
			updateSingleGamificationUser(user);
			HashMap<String, Object> assignChronicleMap = new HashMap<String, Object>();
			assignChronicleMap.put("userid", userManagerID);				
			assignChronicleMap.put("username", user.getName());
			assignChronicleMap.put("hexabusid", hexabusID);
			assignChronicleMap.put("action", "ASSIGNED");
			assignChronicleMap.put("timestamp", Calendar.getInstance().getTimeInMillis());
			SimpleSerializer.saveToDB(assignChronicleMap, database,
					GamificationImpl.DB_NAME_GMHEXABUS_CHRONICLE);

			return true;
		}
		return false;
	}

	public boolean removeHexabusFromUser(int userManagerID, String hexabusID) {
		if(endUsers.containsKey(userManagerID)) {
			SingleGamificationUser user = endUsers.get(userManagerID);
			user.setHexabusID("");
			updateSingleGamificationUser(user);
			HashMap<String, Object> assignChronicleMap = new HashMap<String, Object>();
			assignChronicleMap.put("userid", userManagerID);
			assignChronicleMap.put("username", user.getName());
			assignChronicleMap.put("hexabusid", hexabusID);
			assignChronicleMap.put("action", "REMOVED");
			assignChronicleMap.put("timestamp", Calendar.getInstance().getTimeInMillis());
			SimpleSerializer.saveToDB(assignChronicleMap, database,
					GamificationImpl.DB_NAME_GMHEXABUS_CHRONICLE);

			return true;
		}
		return false;
	}

	public Map<String, SingleGamificationUser> getGamificationUsersFilterScore(
			long startTimestamp, long endTimeStamp) {
		try {
			Map<String, SingleGamificationUser> users = getGamificationUsers();
			Iterator<SingleGamificationUser> itr = users.values().iterator();
			while(itr.hasNext()) {
				SingleGamificationUser user = itr.next();
				user.setScore(0);
			}

			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM " + GamificationImpl.DB_NAME_GMSCORE_HISTORY;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null) {
				return users;
			}
			/*
			 * go through results update score values
			 */
			else {
				for (Map<String, Object> result : resultSet) {
					long timestamp = Long.parseLong(result.get("timestamp").toString());
					int userIsGroup = Integer.parseInt(result.get("userisgroup").toString());
					if(timestamp >= startTimestamp && timestamp <= endTimeStamp && userIsGroup == 0) {
						SingleGamificationUser user = users.get(Integer.parseInt(result.get("userid").toString()));
						if(user != null) {
							user.setScore(user.getScore() + Integer.parseInt(result.get("pointsassigned").toString()));
						}
					}
				}
				return users;
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return null;
	}

	public Map<String, GamificationGroup> getGamificationGroupsFilterScore(
			long startTimestamp, long endTimeStamp) {
		try {
			Map<String, GamificationGroup> groups = getGamificationGroups();
			Iterator<GamificationGroup> itr = groups.values().iterator();
			while(itr.hasNext()) {
				GamificationGroup group = itr.next();
				group.setScore(0);
			}

			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM " + GamificationImpl.DB_NAME_GMSCORE_HISTORY;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null) {
				return groups;
			}
			/*
			 * go through results update score values
			 */
			else {
				for (Map<String, Object> result : resultSet) {
					long timestamp = Long.parseLong(result.get("timestamp").toString());
					int userIsGroup = Integer.parseInt(result.get("userisgroup").toString());
					if(timestamp >= startTimestamp && timestamp <= endTimeStamp && userIsGroup == 1) {
						GamificationGroup group = groups.get(Integer.parseInt(result.get("userid").toString()));
						if(group != null) {
							group.setScore(group.getScore() + Integer.parseInt(result.get("pointsassigned").toString()));
						}
					}
				}
				return groups;
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return null;
	}
	
	public Map<Integer, Property> getPropertiesForUser(int ID, boolean userIsGroup) {
		Map<Integer, Property> properties = new HashMap<Integer, Property>();
		int userGroup = 0;
		if(userIsGroup) {
			userGroup = 1;
		}
		String table = GamificationImpl.DB_NAME_GMPROPERTIES;
		String idColumnString = "propertyid";
		
		try {
			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM " + table
					+ " WHERE userID = '" + ID + "' "
					+ "AND userIsGroup = '" + userGroup
					+ "';";

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				return properties;
			}
			/*
			 * go through results and create containers
			 */
			else {
				for (Map<String, Object> result : resultSet) {
					int propertyID = Integer.parseInt(result.get(idColumnString).toString());
					properties.put(propertyID,propertyFactory.createProperty(propertyID));
				}
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return properties;
	}

	
	public Map<Integer,Achievement> getAchievementsForUser(int ID, boolean userIsGroup) {
		Map<Integer, Achievement> achievements = new HashMap<Integer, Achievement>();
		int userGroup = 0;
		if(userIsGroup) {
			userGroup = 1;
		}
		String table = GamificationImpl.DB_NAME_GMACHIEVEMENTS;
		String idColumnString = "achievementid";
		
		try {
			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM " + table
					+ " WHERE userID = '" + ID + "' "
					+ "AND userIsGroup = '" + userGroup
					+ "';";

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				return achievements;
			}
			/*
			 * go through results and create containers
			 */
			else {
				for (Map<String, Object> result : resultSet) {
					int achievementID = Integer.parseInt(result.get(idColumnString).toString());
					achievements.put(achievementID,achievementFactory.createAchievement(achievementID));
				}
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return achievements;
	}
	
	public boolean userHasAchievement(int participantID, int achievementID, boolean userIsGroup) {
		return getAchievementsForUser(participantID, userIsGroup).containsKey(achievementID);
	}

	public boolean userFullfillsProperty(int ID, int propertyID, boolean userIsGroup) {
		return getPropertiesForUser(ID, userIsGroup).containsKey(propertyID);
	}
	
	public boolean setUserAchievement(int ID, int achievementID, boolean userIsGroup) throws TimeoutException {
		Map<String, ? extends GamificationParticipant> participants;
		if(userIsGroup) {
			participants = getGamificationGroups();
		}
		else {
			participants = getGamificationUsers();
		}
		if(participants.containsKey(ID)) {
			HashMap<String, Object> assignPropertyMap = new HashMap<String, Object>();
			assignPropertyMap.put("userid", ID);				
			assignPropertyMap.put("achievementid", achievementID);
			if(userIsGroup) {
				assignPropertyMap.put("userisgroup", 1);
			}
			else {
				assignPropertyMap.put("userisgroup", 0);
			}
			SimpleSerializer.saveToDB(assignPropertyMap, database,
					GamificationImpl.DB_NAME_GMACHIEVEMENTS);
			return true;
		}
		return false;
	}
	
	//normally a user can't lose an achievement
	public boolean removeUserAchievement(int ID, int achievementID, boolean userIsGroup) throws TimeoutException {
		try {
			String sql;
			int userIsGroupEntry = 0;
			if(userIsGroup) {
				userIsGroupEntry = 1;
			}
			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMACHIEVEMENTS
					+ " WHERE `userID` =  '" + ID + "'"
					+ " AND `userIsGroup` = '" + userIsGroupEntry + "'"
					+ " AND `achievementID` = '" + achievementID + "';";
			this.database.executeQuery(sql);
			return true;
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return false;

	}
	
	public boolean setUserProperty(int ID, int propertyID, boolean userIsGroup) throws TimeoutException {
		Set<Integer> properties = getPropertiesForUser(ID, userIsGroup).keySet();
		if(!properties.contains(propertyID)) {
			boolean succesful = saveUserPropertyToDB(ID, propertyID, userIsGroup);
			if(!succesful) {
				return false;
			}
			//look at all achievements that contain this property
			//if some of them are fullfilled now
			if(propertyAchievementAssociations.containsKey(propertyID)) {
				for (Integer achievementID : propertyAchievementAssociations.get(propertyID)) {
					//see if user does not have the achievement yet
					if(!userHasAchievement(ID, achievementID, userIsGroup)) {
						if(achievementPropertyAssociations.containsKey(achievementID)) {
							boolean allPropertiesFullfilled = true;
							//see if all properties for the achievement are fullfilled now
							for(Integer iteratingPropertyID : achievementPropertyAssociations.get(achievementID)) {
								if(!userFullfillsProperty(ID, iteratingPropertyID, userIsGroup)) {
									allPropertiesFullfilled = false;
									break;
								}
							}
							if(allPropertiesFullfilled) {
								setUserAchievement(ID, achievementID, userIsGroup);
							}
						}
					}
					
				}
			}
		}
		return true;
	}
	
	private boolean setupAchievement(int achievementID) {
		if(achievementPropertyAssociations.containsKey(achievementID)) {
			return false;
		}
		else {
			achievementPropertyAssociations.put(achievementID, new HashSet<Integer>());
			return true;
		}
	}

	private boolean setupProperty(int propertyID) {
		if(propertyAchievementAssociations.containsKey(propertyID)) {
			return false;
		}
		else {
			propertyAchievementAssociations.put(propertyID, new HashSet<Integer>());
			return true;
		}
	}
	
	private boolean linkAchievementToProperty(int achievementID, int propertyID) {
		if(!propertyAchievementAssociations.containsKey(propertyID) || 
				!achievementPropertyAssociations.containsKey(achievementID)) {
			return false;
		}
		else {
			propertyAchievementAssociations.get(propertyID).add(achievementID);
			achievementPropertyAssociations.get(achievementID).add(propertyID);
		}
		return false;
	}

	
	private boolean saveUserPropertyToDB(int ID, int propertyID, boolean userIsGroup) throws TimeoutException {
		Map<String, ? extends GamificationParticipant> participants;
		if(userIsGroup) {
			participants = getGamificationGroups();
		}
		else {
			participants = getGamificationUsers();
		}
		if(participants.containsKey(ID)) {
			HashMap<String, Object> assignPropertyMap = new HashMap<String, Object>();
			assignPropertyMap.put("userid", ID);				
			assignPropertyMap.put("propertyid", propertyID);
			if(userIsGroup) {
				assignPropertyMap.put("userisgroup", 1);
			}
			else {
				assignPropertyMap.put("userisgroup", 0);
			}
			SimpleSerializer.saveToDB(assignPropertyMap, database,
					GamificationImpl.DB_NAME_GMPROPERTIES);
			return true;
		}
		return false;
	}
	
	public boolean removePropertyFromUser(int ID, int propertyID, boolean userIsGroup) {
		try {
			String sql;
			int userIsGroupEntry = 0;
			if(userIsGroup) {
				userIsGroupEntry = 1;
			}
			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMPROPERTIES
					+ " WHERE `userID` =  '" + ID + "'"
					+ " AND `userIsGroup` = '" + userIsGroupEntry + "'"
					+ " AND `propertyID` = '" + propertyID + "';";
			this.database.executeQuery(sql);
			return true;
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return false;
	}
	
	private Set<Integer> getAlreadyAskedQuestionsForUser(int userManagerID) {
		try {
			List<Map<String, Object>> resultSet;
			Set<Integer> questionSet = new HashSet<Integer>();
			String sql = "SELECT * FROM " + GamificationImpl.DB_NAME_GMQUIZQUESTIONHISTORY
					+ " WHERE `userManagerID` = '" + userManagerID + "';";

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				return questionSet;
			}
			else {
				for (Map<String, Object> result : resultSet) {
					questionSet.add(Integer.parseInt(result.get("questionid").toString()));
				}
				return questionSet;
			}

		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return null;
	}
	
	private void saveAskingQuestionEvent(int userManagerID, int questionID) {
		HashMap<String, Object> askingEvent = new HashMap<String, Object>();
		askingEvent.put("usermanagerid", userManagerID);				
		askingEvent.put("questionid", questionID);
		askingEvent.put("timestampofasking", Calendar.getInstance().getTimeInMillis());
		SimpleSerializer.saveToDB(askingEvent, database,
				GamificationImpl.DB_NAME_GMQUIZQUESTIONHISTORY);
	}

	public QuizQuestion getNewQuestionForUser(int userManagerID) {
		Map<Integer,QuizQuestion> questions = quizQuestionStore.getQuestions();
		if(!endUsers.containsKey(userManagerID)) {
			return null;
		}
		SingleGamificationUser user = endUsers.get(userManagerID);
		Set<Integer> alreadyAskedQuestions = getAlreadyAskedQuestionsForUser(userManagerID);
		
		//Maybe refactor later in order to only iterate through correct level
		for (QuizQuestion question : questions.values()) {
			if(!alreadyAskedQuestions.contains(question.getId()) && question.getLevel() == user.getLevel()) {
				saveAskingQuestionEvent(userManagerID, question.getId());
				return question;
			}
		}
		return null;
	}
	
	public void addDeviceIDToGamificationSystem(String deviceID) {
		HashMap<String, Object> deviceMap = new HashMap<String, Object>();
		deviceMap.put("id", deviceID);
		SimpleSerializer.saveToDB(deviceMap, database,
				GamificationImpl.DB_NAME_GMDEVICES);
	}
	
	public boolean removeDeviceIDFromGamificationSystem(String deviceID) {
		try {
			String sql = "SELECT *  FROM " + GamificationImpl.DB_NAME_GMDEVICES
					+ " WHERE `id` = '" + deviceID + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no User to delete return 0
				 */
				return false;
			}
			sql = "DELETE FROM " + GamificationImpl.DB_NAME_GMDEVICES
					+ " WHERE `id` =  '" + deviceID + "';";
			this.database.executeQuery(sql);
			return true;
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
		}
		return false;
	}
	
	
	
	private Set<Integer> getOpenQuestionsForUser(int userManagerID) {
		try {
			List<Map<String, Object>> resultSet;
			Set<Integer> openQuestionIDs = new HashSet<Integer>();
			String sql = "SELECT * FROM " + GamificationImpl.DB_NAME_GMQUIZQUESTIONHISTORY
					+ " WHERE `userManagerID` = " + userManagerID
					+ " AND `timestampOfAnswering` IS NULL";

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				return openQuestionIDs;
			}
			else {
				for (Map<String, Object> result : resultSet) {
					openQuestionIDs.add(Integer.parseInt(result.get("questionid").toString()));
				}
				return openQuestionIDs;
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
			return null;
		}
	}
	
	private boolean saveAnswer(int userMananagerID, int questionID, boolean answerCorrect) {
		try {
			String answerCorrectString = "0";
			if(answerCorrect) {
				answerCorrectString = "1";
			}
			
			String sql = "UPDATE " + GamificationImpl.DB_NAME_GMQUIZQUESTIONHISTORY
					+ " SET `answeredCorrectly` = '" + answerCorrectString 
					+ "', `timestampOfAnswering` = '" + Calendar.getInstance().getTimeInMillis()
					+ "' WHERE `userManagerID`= '" + userMananagerID
					+ "' AND `questionID` = '" + questionID + "';";

			this.database.executeQuery(sql);
			return true;

			} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
			return false;
		}

	}

	public boolean answerOpenQuestionForUser(int userManagerID, int questionID,
			int answerID) throws TimeoutException {
		Map<Integer,QuizQuestion> questions = quizQuestionStore.getQuestions();
		if(!endUsers.containsKey(userManagerID) || !questions.containsKey(questionID)||
				!getOpenQuestionsForUser(userManagerID).contains(questionID)) {
			return false;
		}
		QuizQuestion question = questions.get(questionID);
		boolean answerCorrect = question.getCorrectAnswer() == answerID;
		if(answerCorrect) {
			if(question.getLevel() <= 4) {
				addPointsToGamificationUser(userManagerID, pointsForQuestionDifficulty.get(question.getLevel()));
			}
			else {
				addPointsToGamificationUser(userManagerID, 40);
			}
		}
		return saveAnswer(userManagerID, questionID, answerCorrect);
	}

	public Set<String> getDevices() {
		try {
			Set<String> deviceIDs = new HashSet<String>();
			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM " + GamificationImpl.DB_NAME_GMDEVICES;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				return deviceIDs;
			}
			/*
			 * go through results and create containers
			 */
			else {
				for (Map<String, Object> result : resultSet) {
							deviceIDs.add(result.get("id").toString());
				}
				return deviceIDs;
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
			return null;
		}
	}

	public Set<String> getUnoccupiedDevices() {
		try {
			Set<String> deviceIDs = getDevices();
			Set<String> occupiedDeviceIDs = new HashSet<String>();
			List<Map<String, Object>> resultSet;
			String sql = "SELECT *  FROM " + GamificationImpl.DB_NAME_GMUSERS;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				return deviceIDs;
			}
			/*
			 * go through results and create containers
			 */
			else {
				for (Map<String, Object> result : resultSet) {
					//TODO testen was hier bei leeren hexabusIDs passiert
					if(result.get("hexabusid") != "") {
						occupiedDeviceIDs.add(result.get("hexabusid").toString());
					}
				}
				for(String deviceID : occupiedDeviceIDs) {
					deviceIDs.remove(deviceID);
				}
				return deviceIDs;
			}
		} catch (TimeoutException e) {
			GamificationImpl.logger.warn("SQL Statement error", e);
			return null;
		}
	}
	
}
