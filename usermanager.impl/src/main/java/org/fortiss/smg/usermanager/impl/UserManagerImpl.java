/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.usermanager.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.codec.digest.DigestUtils;
import org.fortiss.smg.informationbroker.api.IDatabase;
import org.fortiss.smg.sqltools.lib.builder.InsertQuery;
import org.fortiss.smg.sqltools.lib.builder.SelectQuery;
import org.fortiss.smg.sqltools.lib.builder.DeleteQuery;
import org.fortiss.smg.sqltools.lib.builder.UpdateQuery;
import org.fortiss.smg.usermanager.api.User;
import org.fortiss.smg.usermanager.api.UserManagerInterface;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

public class UserManagerImpl implements UserManagerInterface {

	// private UserManagerDBUtil dbUtil;
	private static Logger logger = org.slf4j.LoggerFactory
			.getLogger(UserManagerImpl.class);

	private static final int MIN_ROLE_TO_CREATERULE = 7;


	private static final String USERMANAGER_PROFILES_TABLE = "UserManager_Profiles";
	private static final String USERMANAGER_USERS_TABLE = "UserManager_Users";
	private static final String USERMANAGER_ROOMS_TABLE = "UserManager_Rooms";
	private static final String USERMANAGER_CONTACTS_TABLE = "UserManager_Contacts";
	private static final String USERMANAGER_DEVICES_TABLE = "UserManager_Devices";
	private static final String USERMANAGER_ROLE_TABLE = "UserManager_Role";
	private static final String CONTAINERMANAGER_DEVICES_TABLE = "ContainerManager_Devices";

	private IDatabase database;



	/**
	 * Constructor of UserManagerDBUtil-Logging-Component
	 * 
	 * @param dbUtil
	 * @param logger2
	 */

	public UserManagerImpl(IDatabase database) { 
		this.database = database;
	}


	@Override
	public boolean isComponentAlive() {
		return true;
	}

	/**
	 * Create new user
	 * 
	 * @param user
	 *            User type , it contains fields username, RolId,NickName ,
	 *            status of current logged in , ...
	 * @param password
	 *            String user's password that is hashed before written in
	 *            database
	 * @return boolean that whether the inserting new user was successful or not
	 */
	@Override
	public boolean createSimpleUser(String login, String password)
			throws TimeoutException {
		logger.debug("Insert user" + login);

		String hashpw = sha1(password); 
		Calendar calendar = Calendar.getInstance();
		
		InsertQuery query = new InsertQuery(USERMANAGER_USERS_TABLE);
		query.columns("Username","Nickname","Password","RoleID","LoggedIn","ActiveProfile","LastSeen");
		query.values(login,"",hashpw,7,0,"",calendar.getTimeInMillis());

		return  database.executeQuery(query.toString());
	}

	/**
	 * Create new user
	 * 
	 * @param user
	 *            User type , it contains fields username, RolId,NickName ,
	 *            status of current logged in , ...
	 * @param password
	 *            String user's password that is hashed before written in
	 *            database
	 * @return boolean that whether the inserting new user was successful or not
	 */
	@Override
	public boolean createUser(User user, String password)
			throws TimeoutException {
		logger.debug("Insert user" + user.getUserName());
		String hashpw = sha1(password);
		Calendar calendar = Calendar.getInstance();

		InsertQuery query = new InsertQuery(USERMANAGER_USERS_TABLE);
		query.columns("Username","Nickname","Password","RoleID","LoggedIn","ActiveProfile","LastSeen");
		query.values(user.getUserName(),user.getName(),hashpw,user.getRoleId(),user.getLoggedin(),
				user.getActiveProfile(),calendar.getTimeInMillis());
		boolean result = database.executeQuery(query.toString());

		if(!result)
			logger.debug("This User cannot be added to DB - sql probblem");

		return result;
	}

	/**
	 * Add User Contacts information to existing user
	 * 
	 * @param user
	 *            User type , it contains fields username, email ,phone,
	 *            primaryContactInfo , ....
	 * 
	 * @return boolean that whether the inserting the contact information of
	 *         user was successful or not
	 */
	@Override
	public boolean createUserContacts(User user) throws TimeoutException {
		logger.debug("Insert user contacts" + user.getUserName());

		boolean result = false;

		if (user.getPrimaryContactInfo() == null)
			user.setPrimaryContactInfo(user.getEmail());

		InsertQuery query = new InsertQuery(USERMANAGER_CONTACTS_TABLE);
		query.columns("EMail","Phone","UserID","PrimaryContactInfo");
		query.values(user.getEmail(),user.getPhone(),user.getId(),user.getPrimaryContactInfo());
		result = database.executeQuery(query.toString());
		if(!result){
			UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_CONTACTS_TABLE);
			updateQuery.set("PrimaryContactInfo", user.getPrimaryContactInfo());
			updateQuery.addWhere("UserID = " + user.getId());
			result = database.executeQuery(updateQuery.toString());
		}

		if (!result)
			logger.debug("UserContacts could not be created");

		return result;
	}

	/**
	 * Modify existing user
	 * 
	 * @param user
	 *            User type , it contains fields username, RolId,NickName ,
	 *            status of current loged in , ...
	 * @param password
	 *            String user's password that is hashed before written in
	 *            database
	 * @return boolean that whether the updating user was successful or not
	 */
	@Override
	public boolean modifyUser(User user, String password) throws Exception {
		if (validLogin(user.getUserName(), password)) {
			logger.debug("updating user : " + user.getUserName());
			Calendar calendar = Calendar.getInstance();
			UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_USERS_TABLE);
			updateQuery.set("nickname", user.getName());
			updateQuery.set("roleid", String.valueOf(user.getRoleId()));
			updateQuery.set("activeprofile", user.getActiveProfile());
			updateQuery.set("lastseen", calendar.getTimeInMillis()+"");
			updateQuery.addWhere("userid = " + user.getId());

			boolean result = database.executeQuery(updateQuery.toString());

			if(!result)
				logger.debug("Not such a user exists - sql problrm");

			return result;

		} else {
			logger.debug("Username or Password is wrong ");
			return false;
		}
	}

	/**
	 * Modify existing User Contacts information
	 * 
	 * @param user
	 *            User type , it contains fields username, email ,phone,
	 *            primaryContactInfo , ....
	 * 
	 * @return boolean that whether the update was successful or not
	 */
	@Override
	public boolean modifyUserContacs(User user, String password)
			throws Exception, TimeoutException {
		if (validLogin(user.getUserName(), password)) {
			logger.debug("updating cantact info of user : " + user.getId());

			if(user.getPrimaryContactInfo()== null)
				user.setPrimaryContactInfo(user.getEmail());

			UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_CONTACTS_TABLE);
			updateQuery.set("EMail", user.getEmail());
			updateQuery.set("Phone", String.valueOf(user.getPhone()));
			updateQuery.set("PrimaryContactInfo", user.getPrimaryContactInfo());
			updateQuery.addWhere("userid = " + user.getId());
			boolean result = database.executeQuery(updateQuery.toString());

			if (!result)
				logger.debug("Not such a user exists - sql problrm");
			return result;
		} else
			logger.debug(" Username or Password is wrong ");
		return false;

	}

	/**
	 * Modify user's password
	 * 
	 * @param username
	 *            String the username that user used to log in
	 * 
	 * @param password
	 *            String the password that user used to log in , that is hashed
	 *            before being searched in DB
	 * 
	 * @param newPassword
	 *            String the new password that user will use to log in , that is
	 *            hashed before being inserted to DB
	 * 
	 * 
	 * @return boolean that whether the update was successful or not
	 */
	@Override
	public boolean modifyPassword(String username, String password,
			String newPassword) throws Exception, TimeoutException {
		logger.debug("updating password" + username);
		if (validLogin(username, password)) {
			String hashpw = sha1(newPassword);
			Calendar calendar = Calendar.getInstance();
			UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_USERS_TABLE);
			updateQuery.set("Password", hashpw);
			updateQuery.set("lastseen", calendar.getTimeInMillis()+"");
			updateQuery.addWhere("Username = '" + username +"'");
			boolean result = database.executeQuery(updateQuery.toString());

			if (!result)
				logger.debug("Password could not be changed -sql problem");
			return result;
		}
		else
			logger.debug("wrong username or password - password could not be changed");
		return false;

	}

	@Override
	public boolean deleteUser(String username, String password)
			throws Exception {
		if (validLogin(username, password)) {
			return deleteUser(username);
		} else
			return false;
	}

	/**
	 * Delete Existing User incl. Profiles, Contacts, Devices, Room Association
	 * 
	 * @param username
	 *            String the username that user used to log in
	 * 
	 * @return boolean that whether the deletion was successful or not
	 */
	public boolean deleteUser(String username) throws TimeoutException {
		logger.debug("removing user " + username);
		int userId = 0;
		SelectQuery query = new SelectQuery();
		query.addColumn("UserID");
		query.addFrom(USERMANAGER_USERS_TABLE);
		query.addWhere("Username = '" + username.toLowerCase() + "'");

		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			userId = Integer.parseInt(set.get("userid").toString());
		}

		boolean result = false;
		if (userId > 0) {
			DeleteQuery deleteQuery = new DeleteQuery(USERMANAGER_USERS_TABLE );
			deleteQuery.addWhere("UserID = " + userId);
			result = database.executeQuery(deleteQuery.toString());
			if(result){
				deleteQuery = new DeleteQuery(USERMANAGER_CONTACTS_TABLE );
				deleteQuery.addWhere("UserID = " + userId);
				result = database.executeQuery(deleteQuery.toString());
				if(result){
					deleteQuery = new DeleteQuery(USERMANAGER_DEVICES_TABLE );
					deleteQuery.addWhere("UserID = " + userId);
					result = database.executeQuery(deleteQuery.toString());
					if(result){
						deleteQuery = new DeleteQuery(USERMANAGER_PROFILES_TABLE );
						deleteQuery.addWhere("UserID = " + userId);
						result = database.executeQuery(deleteQuery.toString());
						if (result){ 
							deleteQuery = new DeleteQuery(USERMANAGER_ROOMS_TABLE );
							deleteQuery.addWhere("UserID = " + userId);
							result = database.executeQuery(deleteQuery.toString());
						}
					}
				}
			}
			if (!result) 
				logger.debug("Could not delete user from tables - sql problem");
		} else 
			logger.debug("Could not delete user - UserID is null");

		return result;
	}

	/**
	 * Valid Login
	 * 
	 * @param username
	 *            String the username that user used to log in
	 * 
	 * @param password
	 *            String the password that user used to log in
	 * 
	 * @return boolean check whether the login is valid or not
	 */
	@Override
	public boolean validLogin(String username, String password)
			throws TimeoutException {
		logger.debug("check if the user is valid to login " + username);
		
		String result = validLogin(username);
		
		if (result.equals(sha1(password)))
			return true;
		else
			return false;

	}

	@Override
	public boolean validLoginHash(String username, Object passwordHash)
			throws Exception {
		String hashpwFromDB = validLogin(username);
		if (hashpwFromDB != null && hashpwFromDB.equals(passwordHash)) {
			logger.info("login successful for " + username);
			return true;
		} else {
			logger.info("login not successful for " + username);
			return false;
		}
	}

	/**
	 * Valid Login
	 * 
	 * @param username
	 *            String the username that user used to log in
	 * 
	 * @return String Hashed password of given username to compare if he is
	 *         valid or not
	 */
	public String validLogin(String username) throws TimeoutException {
		logger.debug("check if the user is valid to login " + username);
		String result = "";

		SelectQuery query = new SelectQuery();
		query.addColumn("Password");
		query.addFrom(USERMANAGER_USERS_TABLE);
		query.addWhere("Username = '" + username + "'");

		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result = set.get("password").toString();
		}

		if(result == null || resultSet.size()>1)
			logger.debug("Received more than one or null passwords");

		return result;
	}

	/**
	 * Get Valid User
	 * 
	 * @param username
	 *            String the username that user used to log in
	 * 
	 * @param password
	 *            String the password that user used to log in
	 * 
	 * @return User check whether the user is valid , if yes then return the
	 *         whole user information
	 */
	@Override
	public User getValidUser(String username, String password) throws Exception {
		String hashpwFromDB = validLogin(username);

		//if (BCrypt.checkpw(password, hashpwFromDB)) {
		//if (BCrypt.checkpw(password, result))
		if (hashpwFromDB.equals(sha1(password))) {
			return getUserByName(username);
		} else
			return null;
	}

	/**
	 * get User
	 * 
	 * @param username
	 *            String the username that user used to log in
	 * 
	 * @return User the whole user information
	 */
	@Override
	public User getUserByName(String username) throws TimeoutException {
		logger.debug("Get user : " + username);
		User result = null;

		SelectQuery query = new SelectQuery();
		query.addColumn("u.userid", "u.username","u.nickname","c.email","u.roleid","u.activeprofile","c.phone","c.primarycontactinfo");
		query.addFrom(USERMANAGER_USERS_TABLE + " u" );
		query.leftjoin(USERMANAGER_CONTACTS_TABLE + " c", "u.username = '" + username.toLowerCase() + "' AND u.userid = c.userid" );
		List<Map<String, Object>> resultSet = database.getSQLResults(query.toString());

		if(resultSet != null) 
			for (Map<String, Object> set : resultSet) {
				User user_db = new User(true, 
						Long.parseLong(set.get("userid").toString()), 
						set.get("username").toString(), 
						set.get("nickname")+"", 
						set.get("email")+"",
						Long.parseLong(set.get("roleid").toString()),
						0, 
						set.get("phone")+"", 
						null, 
						set.get("activeprofile")+"", 
						set.get("primarycontactinfo")+"");
				
				if (user_db.getActiveProfile() != "") {
					HashMap<String, Double> hm = getUserActiveProfileDetail(
							user_db.getId(), user_db.getActiveProfile());
					user_db.setProfileDetails(hm.entrySet());
				}
				result = user_db;
			}
		else 
			logger.debug("Such a User is not existing - sql problem");
		return result;
	}

	/**
	 * get User
	 * 
	 * @param userId
	 *            long the user ID
	 * 
	 * @return User the whole user information
	 * @throws Exception
	 */
	@Override
	public User getUser(long userId) throws TimeoutException {
		logger.debug("Get user : " + userId);
		User result = null;

		SelectQuery query = new SelectQuery();

		query.addColumn("u.userid", "u.username","u.nickname","c.email","u.roleid","u.activeprofile","c.phone","c.primarycontactinfo");
		query.addFrom(USERMANAGER_USERS_TABLE + " u");
		query.leftjoin(USERMANAGER_CONTACTS_TABLE + " c ", "u.UserID = c.UserID");
		query.addWhere("u.UserId = " + userId);


		List<Map<String, Object>> resultSet;



		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			User user_db = new User(true, 
					Long.parseLong(set.get("userid").toString()), 
					set.get("username").toString(), 
					set.get("nickname")+"", 
					set.get("email")+"",
					Long.parseLong(set.get("roleid").toString()),
					Long.parseLong(set.get("roleid").toString()), 
					set.get("phone")+"", 
					null, 
					set.get("activeprofile")+"", 
					set.get("primarycontactinfo")+"");

			if (user_db.getActiveProfile() != "") {
				HashMap<String, Double> hm = getUserActiveProfileDetail(
						user_db.getId(), user_db.getActiveProfile());
				user_db.setProfileDetails(hm.entrySet());
			}
			result = user_db;

		}
		return result;

	}

	/**
	 * Get User Active Profile
	 * 
	 * @param userId
	 *            long user's Id
	 * 
	 * @param activeProfileName
	 *            String the user profile name that is activated
	 * 
	 * @return HashMap<String,Double> the map of profile Item and their value
	 */
	public HashMap<String, Double> getUserActiveProfileDetail(Long userId,
			String activeProfileName) throws TimeoutException {
		logger.debug("Get User : " + userId + "Active Profile Details : "
				+ activeProfileName);
		HashMap<String, Double> hm = new HashMap<String, Double>();


		SelectQuery query = new SelectQuery();
		query.addColumn("ProfileItem", "ProfileValue");
		query.addFrom(USERMANAGER_PROFILES_TABLE);
		query.addWhere("UserID = " + userId);
		query.and("ProfileName = '" + activeProfileName + "'");

		List<Map<String, Object>> resultSet;

		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			hm.put(set.get("profileitem").toString(),
					Double.parseDouble(set.get("profilevalue").toString()));
		}
		return hm;

	}

	/**
	 * Login by device
	 * 
	 * @param macAddress
	 *            String macAddress of Device
	 * 
	 * @return String username of the user that assigned to this device
	 */

	@Override
	public String loginByDevice(String macAddress) throws Exception {
		logger.debug("Login By Device : " + macAddress);
		String result = null;

		SelectQuery query = new SelectQuery();
		query.addColumn("u.Username");
		query.addFrom(USERMANAGER_USERS_TABLE+" u", USERMANAGER_DEVICES_TABLE+" d");
		query.addWhere("u.UserID = d.UserID");
		query.and("d.MACAddress = " + macAddress);

		List<Map<String, Object>> resultSet;

		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result = set.get("username").toString();
		}

		return result;

	}

	/**
	 * Login status
	 * 
	 * @param username
	 *            String macAddress of Device
	 * 
	 * @return boolean false if this user is not logged in otherwise true.
	 */
	@Override
	public boolean loginStatus(String username) throws Exception {
		logger.debug("Check if user still login" + username);
		boolean loggedIn = false;
		int result = 0;

		SelectQuery query = new SelectQuery();
		query.addColumn("LoggedIn");
		query.addFrom(USERMANAGER_USERS_TABLE);
		query.addWhere("Username = '" + username.toLowerCase() +"'");

		List<Map<String, Object>> resultSet;

		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result = Integer.parseInt(set.get("loggedin").toString());
		}
		if (result == 1)
			loggedIn = true;
		return loggedIn;

	}

	/**
	 * Login status
	 * 
	 * @param userId
	 *            Long userId
	 * 
	 * @return boolean false if this user is not logged in otherwise true.
	 */
	public boolean loginStatus(Long userId) throws Exception {
		logger.debug("Check if user still login" + userId);
		boolean loggedIn = false;
		int result = 0;

		SelectQuery query = new SelectQuery();
		query.addColumn("LoggedIn");
		query.addFrom(USERMANAGER_USERS_TABLE);
		query.addWhere("UserID = " + userId);

		List<Map<String, Object>> resultSet;

		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result = Integer.parseInt(set.get("loggedin").toString());
		}
		if (result == 1)
			loggedIn = true;
		return loggedIn;

	}

	/**
	 * Assign user to room
	 * 
	 * @param userId
	 *            Long user Id
	 * @param containerId
	 *            Long container Id of the room that will assign to the user
	 * 
	 * @return boolean true if the room is assigned to user with guest role
	 *         successfully otherwise false.
	 */
	@Override
	public boolean assignUsertoRoom(Long userId, String containerId)
			throws Exception {
		boolean result = assignUserToRoom(userId, containerId, "Guest");
		if (result)
			return true;
		else
			return false;
	}

	/**
	 * Assign user to room with specific role
	 * 
	 * @param userId
	 *            Long user Id
	 * @param containerId
	 *            Long container Id of the room that will
	 * 
	 * @param roleName
	 *            String role name that user will have in this specific room
	 * 
	 * @return boolean true if the room is assigned to user with specific role
	 *         successfully otherwise false.
	 */
	@Override
	public boolean assignUsertoRoom(Long userId, String containerId,
			String roleName) throws Exception {
		boolean result = assignUserToRoom(userId, containerId, roleName);
		if (result)
			return true;
		else
			return false;
	}

	/**
	 * Assign user to room with specific role
	 * 
	 * @param userId
	 *            Long user Id
	 * @param containerId
	 *            String container Id of the room that will
	 * 
	 * @param roleName
	 *            String role name that user will have in this specific room
	 * 
	 * @return boolean true if the room is assigned to user with specific role
	 *         successfully and inserted in DB otherwise false.
	 */
	public boolean assignUserToRoom(Long userId, String containerId,
			String roleName) throws Exception {
		logger.debug("assign User : " + userId + "to ContainerId:"
				+ containerId + "RoleName :" + roleName);
		boolean result = false;
		Integer roleId = getRoleId(roleName);
		if (roleId < 0 || roleId > MIN_ROLE_TO_CREATERULE)
			return false;

		InsertQuery query = new InsertQuery(USERMANAGER_ROOMS_TABLE);
		query.columns("UserID","ContainerID","RoleID");
		query.values(userId,containerId,roleId);
		result = database.executeQuery(query.toString());

		if(!result){
			UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_ROOMS_TABLE);
			updateQuery.set("CurrentLocation","1");
			updateQuery.set("RoleID",roleId.toString());
			updateQuery.addWhere("UserID = " + userId);
			result = database.executeQuery(updateQuery.toString());
		}

		if (!result)
			logger.debug("Could not assign user " + userId + " to room "
					+ containerId + " - sql problem");
		return result;
	}

	/**
	 * get RoleId
	 * 
	 * @param roleName
	 *            String roleName
	 * 
	 * @return Long the id of the role name in DB
	 */
	public Integer getRoleId(String roleName) throws TimeoutException {
		logger.debug("Get RoleId of : " + roleName);
		Integer result = -1;

		SelectQuery query = new SelectQuery();
		query.addColumn("RoleID");
		query.addFrom(USERMANAGER_ROLE_TABLE);
		query.addWhere("RoleName = '" + roleName + "'");

		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			if(set.get("roleid")!=null)
				result = Integer.parseInt(set.get("roleid").toString());
		}
		if (result > MIN_ROLE_TO_CREATERULE || result < 0)
			result = this.getRoleId("Guest");
		//		logger.debug("Could not get RoleID " + roleName + " setting to Guest - sql problem");
		return result;
	}

	/**
	 * Dissociate user from room
	 * 
	 * @param userId
	 *            Long user Id
	 * @param containerId
	 *            String container Id of the room that will have in this
	 *            specific room
	 * 
	 * @return boolean true if the room is assigned to user with specific role
	 *         successfully otherwise false.
	 */
	@Override
	public boolean dissociateUserfromRoom(Long userId, String containerId)
			throws Exception {
		logger.debug("Dissociate User : " + userId + "From room : "
				+ containerId);

		DeleteQuery deleteQuery = new DeleteQuery( USERMANAGER_ROOMS_TABLE );
		deleteQuery.addWhere("UserID = " + userId);
		deleteQuery.and("ContainerID = " + containerId);
		boolean result = database.executeQuery(deleteQuery.toString());

		if (!result)
			logger.debug("Could not dissociate user " + userId
					+ " from container " + containerId + "- sql problem");
		return result;
	}

	/**
	 * Assign user to role
	 * 
	 * @param userId
	 *            Long user Id
	 * @param roleId
	 *            Long role Id
	 * 
	 * @return boolean true if the role is assigned to user and inserted to DB
	 *         successfully otherwise false.
	 */
	@Override
	public boolean assignRoletoUser(Long userId, Long roleId) throws Exception {
		logger.debug("assign User to Role " + userId + roleId);
		Calendar calendar = Calendar.getInstance();
		
		UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_USERS_TABLE);
		updateQuery.set("RoleID",roleId.toString());
		updateQuery.set("lastseen", calendar.getTimeInMillis()+"");
		updateQuery.addWhere("UserID = " + userId);
		boolean result = database.executeQuery(updateQuery.toString());

		if (!result)
			logger.debug("Could not assign role " + roleId + " to user "
					+ userId + " - sql problem");
		return result;

	}

	/**
	 * get assigned rooms for userID
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * @return List<String> of ContainerID where UserID hat role > Guest
	 */
	@Override
	public List<String> getAssignedRooms(Long userId) throws Exception {
		logger.debug("get Rooms assigned to UserId: " + userId);
		List<String> result = new ArrayList<String>();
		List<Map<String, Object>> resultSet;
		SelectQuery query = new SelectQuery();
		query.addColumn("ContainerID");
		query.addFrom(USERMANAGER_ROOMS_TABLE);
		query.addWhere("UserID = '" + userId + "'");
		query.and("RoleID < " + this.getRoleId("Guest"));

		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result.add(set.get("containerid").toString());
		}


		return result;
	}

	/**
	 * Get User Role in Specific Room
	 * 
	 * @param userId
	 *            Long user Id
	 * @param containerId
	 *            Long container Id
	 * 
	 * @return String RoleName of the user in specific room
	 */
	@Override
	public String getUserRole(Long userId, String containerId) throws Exception {
		logger.debug("get User : " + userId + " Role in room : " + containerId);
		String result = "";

		SelectQuery query = new SelectQuery();
		query.addColumn("role.RoleName");
		query.addFrom(USERMANAGER_ROLE_TABLE + " role");
		query.join(USERMANAGER_ROOMS_TABLE + " room", "room.UserID = " + userId +
				" AND " + "room.ContainerID = "+ containerId +" AND " +"room.RoleID = role.RoleID" );

		List<Map<String, Object>> resultSet;

		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result = set.get("rolename").toString();
		}

		if (result.equals(""))
			result = "Guest";

		return result;

	}

	/**
	 * get user role in specific room
	 * 
	 * @param userId
	 *            Long user Id
	 * @param containerId
	 *            Long container Id
	 * 
	 * @return Long roleId of the user in specific room
	 */
	public Long getUserRoomRole(Long userId, String containerId)
			throws Exception {
		logger.debug("get User Role" + userId + "  in containerId : "
				+ containerId);
		Long result = null;

		SelectQuery query = new SelectQuery();
		query.addColumn("RoleID");
		query.addFrom(USERMANAGER_ROLE_TABLE);
		query.addWhere("UserID = '" + userId + "' ");
		query.and("ContainerID = '" + containerId +"'");
		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result = Long.parseLong(set.get("roleid").toString());
		}
		return result;
	}

	/**
	 * set Login status of User
	 * 
	 * @param userId
	 *            Long user Id
	 * @param loggedIn
	 *            boolean if user is logged in or not
	 * 
	 * @return boolean whether the new status is updated in DB successfully or
	 *         not
	 */
	@Override
	public boolean setLoginStatus(Long userId, boolean loggedIn)
			throws Exception {
		logger.debug("Update User " + userId + "to Login Status: " + loggedIn);
		int status = 0;
		if (loggedIn)
			status = 1;
		if (loginStatus(userId) && loggedIn) {
			logger.debug("User was logged in already!");
			return false;
		}
		Calendar calendar = Calendar.getInstance();

		UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_USERS_TABLE);
		updateQuery.set("LoggedIn",String.valueOf(status));
		updateQuery.set("lastseen", calendar.getTimeInMillis()+"");
		updateQuery.addWhere("UserID = " + userId);
		boolean result = database.executeQuery(updateQuery.toString());

		if (!result)
			logger.debug("Could not set login status for userID " + userId
					+ " to " + loggedIn + " - sql problem");
		return result;
	}

	/**
	 * Attach device to User
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * @param deviceId
	 *            Long Id of Device that will be assigned to the user
	 * 
	 * @param macAddress
	 *            String mac address of device
	 * 
	 * @param primaryDevices
	 *            boolean if this device is the primary device of the user as
	 *            several devices can assign to a user
	 * 
	 * @return boolean whether attaching the device to user is inserted in DB
	 *         successfully or not
	 */
	@Override
	public boolean attachDevicetoUser(Long userId, Long deviceId,
			String macAddress, boolean primaryDevice, String deviceName, String os)
					throws Exception {
		logger.debug("attach Device: " + deviceId + " to User : " + userId);
		int primary = 0;
		if (primaryDevice)
			primary = 1;

		InsertQuery query = new InsertQuery(USERMANAGER_DEVICES_TABLE);
		query.columns("UserID","MACAddress","PrimaryDevice","DeviceID","DeviceName","OS");
		query.values(userId,macAddress,primary,deviceId,deviceName,os);
		boolean result = database.executeQuery(query.toString());

		if(!result){
			UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_DEVICES_TABLE);
			updateQuery.set("PrimaryDevice",String.valueOf(primary));
			updateQuery.addWhere("UserID = " + userId);
			result = database.executeQuery(updateQuery.toString());
		}

		if (!result)
			logger.debug("Could not attach device " + deviceId + ","
					+ deviceName + " to userID " + userId + " - sql problem");
		return result;
	}

	/**
	 * Detach device to User
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * @param deviceId
	 *            Long Id of Device that will be detached from the user
	 * 
	 * @return boolean whether detaching the device from user by deletting it
	 *         form DB successfully done or not
	 */
	@Override
	public boolean detachDevicefromUser(Long userId, Long deviceId)
			throws Exception {
		logger.debug("dettach Device : " + deviceId + "from User : " + userId);

		DeleteQuery deleteQuery = new DeleteQuery( USERMANAGER_DEVICES_TABLE );
		deleteQuery.addWhere("UserID = " + userId);
		deleteQuery.and("DeviceID = " + deviceId);
		boolean result = database.executeQuery(deleteQuery.toString());

		if (!result)
			logger.debug("Could not deattach device " + deviceId
					+ " from userID " + userId + " - sql problem");
		return result;
	}

	/**
	 * Get DEviceOwner
	 * 
	 * @param deviceId
	 *            Long Id of Device that belongs to the user
	 * 
	 * @return Long userId of the owner of Device
	 */
	@Override
	public long getDeviceOwner(Long deviceId) throws Exception {
		logger.debug("get the Owner of Device : " + deviceId);
		long result = 0;

		SelectQuery query = new SelectQuery();
		query.addColumn("UserID");
		query.addFrom(USERMANAGER_DEVICES_TABLE);
		query.addWhere("DeviceID = " + deviceId);

		List<Map<String, Object>> resultSet;

		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result = Long.parseLong(set.get("userid").toString());
		}

		return result;

	}

	/**
	 * Create new User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * @param items
	 *            List<String> list of name of items
	 * 
	 * @param itemsValue
	 *            List<Double> list of value of items
	 * 
	 * @param profileName
	 *            String the name of profile that these items belongs to it
	 * 
	 * @return boolean whether inserting new profile to DB is successful or not
	 */
	@Override
	public boolean addNewUserProfile(Long userId, List<String> items,
			List<Double> itemsValue, String profileName) throws Exception {
		boolean result = false;
		HashMap<String, Double> deviseTypeToValue = hashMapDeviceTypeToValue(
				items, itemsValue);
		HashMap<Integer, Double> deviceIdTovalue = hashMapDeviceIdToValue(
				deviseTypeToValue, hashMapDeviceTypeToID());
		result = createNewUserProfile(userId, deviceIdTovalue, profileName);
		return result;
	}

	/**
	 * Create new User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * 
	 * @param items
	 *            HashMap<String,Double> hash map of items and values of them
	 * 
	 * 
	 * @param profileName
	 *            String the name of profile that these items belongs to it
	 * 
	 * @return boolean whether inserting new profile to DB is successful or not
	 */
	@Override
	public boolean addNewUserProfile(Long userId,
			HashMap<Integer, Double> items, String profileName)
					throws Exception {
		boolean result = false;
		result = createNewUserProfile(userId, items, profileName);
		return result;
	}

	/**
	 * Create new User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * 
	 * @param items
	 *            HashMap<String,Double> hash map of items and values of them
	 * 
	 * 
	 * @param profileName
	 *            String the name of profile that these items belongs to it
	 * 
	 * @return boolean whether inserting new profile to DB is successful or not
	 */
	public boolean createNewUserProfile(Long userId,
			HashMap<Integer, Double> deviceIdTovalue, String profileName)
					throws Exception {
		logger.debug("add New Profile : " + profileName + "to User : " + userId);
		boolean result = false;

		for (Map.Entry<Integer, Double> entry : deviceIdTovalue.entrySet()) {

			InsertQuery query = new InsertQuery(USERMANAGER_PROFILES_TABLE);

			query.columns("ProfileName","UserID","ProfileItem","ProfileValue");
			query.values(profileName,userId,entry.getKey(),entry.getValue());
			result = database.executeQuery(query.toString());

			if(!result){
				UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_PROFILES_TABLE);
				updateQuery.set("ProfileItem", entry.getKey().toString());
				updateQuery.set("ProfileValue",""+entry.getValue().toString());
				updateQuery.addWhere("UserID = " + userId);
				result = database.executeQuery(updateQuery.toString());
			}
		}

		if (!result) {
			logger.debug("Could not create user profile - sql problem");
			return false;
		}

		return result;
	}

	/**
	 * Get possible profile items
	 * 
	 * @return List<String> List of profile Items that user able to chose and
	 *         modify their value
	 */
	@Override
	public List<String> getPossibleProfileItems() throws Exception {
		logger.debug("get Possible Profile Items ");
		List<String> result = new ArrayList<String>();

		SelectQuery query = new SelectQuery();
		query.addColumn("SMGDeviceType");
		query.addFrom(CONTAINERMANAGER_DEVICES_TABLE);
		query.addWhere("AllowedUserProfile = '1'");

		List<Map<String, Object>> resultSet;

		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result.add(set.get("smgdevicetype").toString());
		}

		return result;
	}

	/**
	 * Modify User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * @param items
	 *            List<String> list of name of items
	 * 
	 * @param itemsValue
	 *            List<Double> list of value of items
	 * 
	 * @param profileName
	 *            String the name of profile that these items belongs to it
	 * 
	 * @return boolean whether updating the profile in DB is successful or not
	 */
	@Override
	public boolean modifyUserProfile(Long userId, List<String> items,
			List<Double> itemsValue, String profileName) throws Exception {
		logger.debug("Update Profile : " + profileName + "of User : " + userId);
		boolean result = false;
		HashMap<String, Double> deviseTypeToValue = hashMapDeviceTypeToValue(
				items, itemsValue);
		HashMap<Integer, Double> deviceIdTovalue = hashMapDeviceIdToValue(
				deviseTypeToValue, hashMapDeviceTypeToID());
		result = updateUserProfile(userId, deviceIdTovalue, profileName);
		return result;
	}

	/**
	 * Modify User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * 
	 * @param items
	 *            HashMap<String,Double> hash map of items and values of them
	 * 
	 * 
	 * @param profileName
	 *            String the name of profile that these items belongs to it
	 * 
	 * @return boolean whether updating the profile in DB is successful or not
	 */
	@Override
	public boolean modifyUserProfile(Long userId,
			HashMap<Integer, Double> items, String profileName)
					throws Exception {
		boolean result = false;
		result = updateUserProfile(userId, items, profileName);

		return result;
	}

	/**
	 * Modify User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * 
	 * @param items
	 *            HashMap<String,Double> hash map of items and values of them
	 * 
	 * 
	 * @param profileName
	 *            String the name of profile that these items belongs to it
	 * 
	 * @return boolean whether updating the profile in DB is successful or not
	 */
	public boolean updateUserProfile(Long userId,
			HashMap<Integer, Double> deviceIdToValueHM, String profileName)
					throws Exception {
		logger.debug("Update user : " + userId + " profile : " + profileName);
		boolean result = false;

		for (Map.Entry<Integer, Double> entry : deviceIdToValueHM.entrySet()) {

			UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_PROFILES_TABLE);

			updateQuery.set("ProfileValue",String.valueOf(entry.getValue()));
			updateQuery.addWhere("UserID = " + userId);
			updateQuery.and("ProfileItem = '"+String.valueOf(entry.getKey())+"'");
			updateQuery.and ("ProfileName = '" + profileName.toLowerCase()+"'");
			result = database.executeQuery(updateQuery.toString());

			if (!result) {
				logger.debug("Could not update user profile - sql problem");
				return false;
			}
		}

		return result;

	}

	/**
	 * Delete User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * 
	 * @param profileName
	 *            String the name of profile that these items belongs to it
	 * 
	 * @return boolean whether deleting the profile from DB is successful or not
	 */
	@Override
	public boolean deleteUserProfile(Long userId, String profileName)
			throws Exception {
		logger.debug("removing user Profile " + profileName, userId);

		DeleteQuery deleteQuery = new DeleteQuery( USERMANAGER_PROFILES_TABLE );
		deleteQuery.addWhere("UserID = " + userId);
		deleteQuery.and("ProfileName = '" + profileName.toLowerCase() +"'");
		boolean result = database.executeQuery(deleteQuery.toString());

		if (!result) {
			logger.debug("Could not delete user profile " + profileName
					+ " - sql problem");
			return false;
		}
		return result;

	}

	/**
	 * Activate User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 * 
	 * 
	 * @param profileName
	 *            String the name of profile that these items belongs to it
	 * 
	 * @return boolean whether updating the profile in DB is successful or not
	 */
	@Override
	public boolean activateUserProfile(Long userId, String profileName)
			throws Exception {
		logger.debug("Activate Profile : " + profileName + "of User :  "
				+ userId);
		Calendar calendar = Calendar.getInstance();
		
		UpdateQuery updateQuery = new UpdateQuery(USERMANAGER_USERS_TABLE);
		updateQuery.set("ActiveProfile",profileName.toLowerCase());
		updateQuery.set("lastseen", calendar.getTimeInMillis()+"");
		updateQuery.addWhere("UserID = " + userId);
		boolean result = database.executeQuery(updateQuery.toString());

		if (!result) {
			logger.debug("Could not set active user profile " + profileName
					+ " - sql problem");
			return false;
		}
		return result;

	}

	/**
	 * HashMap Device type to google device Id
	 * 
	 * 
	 * 
	 * @return HashMap<String,Integer> hash map of SMG Device Type to Device
	 *         code(Google device Id)
	 */
	@Override
	public HashMap<String, Integer> hashMapDeviceTypeToID() throws Exception {
		logger.debug("HashMap Device Type to ID");
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		SelectQuery query = new SelectQuery();
		query.addColumn("DeviceCode","SMGDeviceType");
		query.addFrom(CONTAINERMANAGER_DEVICES_TABLE);

		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result.put(set.get("smgdevicetype").toString(),
					Integer.parseInt(set.get("devicecode").toString()));
		}
		return result;
	}

	/**
	 * Get Id Of DeviceType
	 * 
	 * @param deviceTypeToDeviceID
	 *            HashMap<String,Integer> Hash map of device type to device code
	 * 
	 * @param deviceType
	 *            String SMG device type
	 * 
	 * @return int Device Id of given device type
	 */
	@Override
	public int getIdOfDeviceType(HashMap<String, Integer> deviceTypeToDeviceID,
			String deviceType) {
		int result = 0;
		for (Map.Entry<String, Integer> entry : deviceTypeToDeviceID.entrySet()) {
			if (entry.getKey() == deviceType) {
				result = ((int) entry.getValue());
				break;
			}

		}
		return result;
	}

	/**
	 * Hash Map Device type to value
	 * 
	 * @param deviceTypeToDeviceID
	 *            HashMap<String,Integer> Hash map of device type to device code
	 * 
	 * @param devicesValue
	 *            String SMG device type
	 * 
	 * @return HashMap<String,Integer> Hash map of device type to value
	 */
	@Override
	public HashMap<String, Double> hashMapDeviceTypeToValue(
			List<String> devicesType, List<Double> devicesValue) {
		HashMap<String, Double> hm = new HashMap<String, Double>();
		Integer counter = 0;
		for (String deviceType : devicesType) {
			hm.put(deviceType, new Double(devicesValue.get(counter)));
			counter++;
		}
		return hm;
	}

	/**
	 * Hash Map Device Id to Value
	 * 
	 * @param devicesTypeToValue
	 *            HashMap<String,Double> Hash map of device type to device value
	 * 
	 * @param deviceTypeToDeviceID
	 *            HashMap<String,Integer> Hash map of device type to device code
	 * 
	 * @return HashMap<Integer,Double> Hash map of device ID to value
	 */
	@Override
	public HashMap<Integer, Double> hashMapDeviceIdToValue(
			HashMap<String, Double> devicesTypeToValue,
			HashMap<String, Integer> deviceTypeToDeviceID) {
		HashMap<Integer, Double> hm = new HashMap<Integer, Double>();

		String type = "";
		Double value = 0.0;
		for (Map.Entry<String, Double> valueEntry : devicesTypeToValue
				.entrySet()) {
			type = (String) valueEntry.getKey();
			value = (Double) valueEntry.getValue();
			for (Map.Entry<String, Integer> idEntry : deviceTypeToDeviceID
					.entrySet()) {
				if (((String) idEntry.getKey()).equals(type)) {
					hm.put((int) idEntry.getValue(), value);
					break;
				}
			}
		}
		return hm;
	}

	/**
	 * get User Active Profile Detail
	 * 
	 * @param userID
	 *            Long user ID
	 * 
	 * 
	 * @return HashMap<String,Double> Hash map of active profile items to value
	 */
	@Override
	public HashMap<String, Double> getUserActiveProfileDetail(Long userId)
			throws Exception {
		logger.debug("Get User Active Profile Detail" + userId);
		HashMap<String, Double> result = new HashMap<String, Double>();
		SelectQuery query = new SelectQuery();
		query.addColumn("p.ProfileItem","p.ProfileValue");
		query.addFrom("" + USERMANAGER_USERS_TABLE+" u ", ""+USERMANAGER_PROFILES_TABLE + " p");
		query.addWhere("u.UserID = '" + userId + "'");
		query.and("p.UserID = '" + userId +"'");
		query.and("p.ProfileName = u.ActiveProfile");
		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result.put(set.get("profileitem").toString(),
					Double.parseDouble(set.get("profilevalue").toString()));
		}

		return result;
	}

	/**
	 * get User specific profile details
	 * 
	 * @param userID
	 *            Long user ID
	 * @param ProfileName
	 *            String profileName
	 * 
	 * @return HashMap<String,Double> Hash map of given profile items to value
	 */
	@Override
	public HashMap<String, Double> getProfile(String profileName, long userId)
			throws Exception {
		logger.debug("Get profile for userID " + userId + " profilename: "
				+ profileName);

		HashMap<String, Double> result = new HashMap<String, Double>();
		SelectQuery query = new SelectQuery();
		query.addColumn("ProfileItem","ProfileValue");
		query.addFrom(USERMANAGER_PROFILES_TABLE);
		query.addWhere("UserID = '" + userId + "'");
		query.and("ProfileName = '" + profileName.toLowerCase() + "'");
		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result.put(set.get("profileitem").toString(),
					Double.parseDouble(set.get("profilevalue").toString()));
		}
		return result;
	}

	/**
	 * get User All Profile Names
	 * 
	 * 
	 * @param userID
	 *            Long user ID
	 * 
	 * 
	 * @return List<String> name of all the profiles that the given user have
	 */
	@Override
	public List<String> getUserAllProfileNames(Long userId) throws Exception {
		logger.debug("get all of User's ProfileNames : " + userId);
		List<String> result = new ArrayList<String>();
		SelectQuery query = new SelectQuery();
		query.addColumn("ProfileName");
		query.addFrom(USERMANAGER_PROFILES_TABLE);
		query.addWhere("UserID = '" + userId +"'");
		query.groupBy("ProfileName");

		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			result.add(set.get("profilename").toString().toLowerCase());
		}
		return result;
	}

	public static String MySQLPassword(String plainText)
			throws UnsupportedEncodingException {
		byte[] utf8 = plainText.getBytes("UTF-8");
		return "*" + DigestUtils.shaHex(DigestUtils.sha(utf8)).toUpperCase();
	}

	/**
	 * Valid User To Create Rule
	 * 
	 * @param username
	 *            String userName
	 * 
	 * @param containerId
	 *            String containerId
	 * 
	 * @return boolean false if this user is valid otherwise true.
	 */
	@Override
	public boolean validUserToCreateRule(String username, String containerId)
			throws Exception {

		logger.debug("Check if user valid to create Rule" + username);
		boolean valid = false;
		int result = Integer.MAX_VALUE;

		SelectQuery query = new SelectQuery();

		query.addColumn("r.RoleID");
		query.addFrom(USERMANAGER_USERS_TABLE+" u");
		query.join(USERMANAGER_ROOMS_TABLE+" r","u.Username = '"+username.toLowerCase()
				+"' AND " + "u.UserID = r.UserID AND " + "r.ContainerID = "+containerId);

		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> set : resultSet) {
			if (set.get("roleid") != null) {
				result = Integer.parseInt(set.get("roleid").toString());
			}
		}

		if (result <= MIN_ROLE_TO_CREATERULE)
			valid = true;

		return valid;

	}


	private String sha1(String input) {
		MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(input.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		return "";
	}

}
