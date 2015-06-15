/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.usermanager.impl.key;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.informationbroker.api.IDatabase;
import org.fortiss.smg.sqltools.lib.builder.DeleteQuery;
import org.fortiss.smg.sqltools.lib.builder.InsertQuery;
import org.fortiss.smg.sqltools.lib.builder.SelectQuery;
import org.fortiss.smg.sqltools.lib.builder.UpdateQuery;
import org.fortiss.smg.usermanager.api.KeyManagerInterface;
import org.fortiss.smg.usermanager.api.Key;
import org.fortiss.smg.usermanager.api.Tuple;
import org.fortiss.smg.usermanager.api.User;
import org.fortiss.smg.usermanager.api.UserManagerInterface;
import org.fortiss.smg.usermanager.impl.UserManagerImpl;
import org.fortiss.smg.usermanager.impl.keys.util.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyManagerImpl implements KeyManagerInterface {

	public static final String DB_NAME = "UserManager_Devices";
	private IDatabase database;
	private static Logger logger = org.slf4j.LoggerFactory
			.getLogger(KeyManagerImpl.class);
	private UserManagerImpl userImpl;


	public KeyManagerImpl(IDatabase database) { 
		this.database = database;
		this.userImpl = new UserManagerImpl(database);
	}

	/**
	 * Create the signature of a public key and request
	 * 
	 * @param secret_key
	 * @param request
	 * @return response String
	 */
	public String calcSignature(String secret_key, String request) {
		String response = "";
		try {
			response = Signature.calculateRFC2104HMAC(request, secret_key);
		} catch (SignatureException e) {
			logger.debug("Unable to generate hmac-signature key:" + secret_key
					+ ",request:" + request);
		}
		return response;
	}

	/**
	 * Checks whether the signature of a public key is valid or not. Modifies
	 * lastSeen -> now
	 * 
	 * @param access_key
	 * @param request
	 * @param signature
	 * @return
	 * @throws TimeoutException
	 */
	public boolean checkSignature(String access_key, String request,
			String signature) throws TimeoutException {
		KeyDB key = getKeyDB(access_key);
		System.out.println(key);
		if (key != null) {
			if (key.getPrivateKey().length() >= 1) {
				String server_signature = calcSignature(key.getPrivateKey(),
						request);
				System.out.println("Check: " + server_signature + " = " + signature);
				if (server_signature.equals(signature)) {
					//LastSeen should be updated
					updateLastSeen(access_key);
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * generates public + private Key
	 * 
	 * @param userid
	 * @return tuple of the generate pair
	 * @throws TimeoutException
	 */
	public Tuple generateKeys(int userid, int deviceID) throws TimeoutException {

		if (userid < 1) {
			return null;
		}

		KeyDB key = new KeyDB();

		key.setUserId(userid);

		// unfortunately the same method is used for public key
		UUID random = UUID.randomUUID();
		key.setPublicKey(random.toString());

		// this seems to be be at least a bit secure
		SecureRandom random2 = new SecureRandom();
		// length = Number in BigInteger / 4
		String private_key = new BigInteger(350, random2).toString(32);

		// should be secure enough for the beginning (length: 40)
		key.setPrivateKey(private_key.substring(5, 15)
				+ private_key.substring(20, 25) + private_key.substring(35, 40)
				+ private_key.substring(42, 57) + private_key.substring(61, 66));

		key.setLastSeen(Calendar.getInstance().getTimeInMillis());
		key.setDevId(deviceID);
		key.setDevName("unknown");
		key.setOS("unknown");
		key.setMACAddress("unknown");

		logger.debug("Generating key pair.");

		InsertQuery query = new InsertQuery(KeyManagerImpl.DB_NAME);
		query.columns("publicKey", "privateKey", "UserID", "DeviceName", "MACAddress", "OS", "DeviceID","LastSeen");
		query.values( key.getPublicKey(),key.getPrivateKey(), key.getUserId(), key.getDevName(),
				key.getMACAddress(),key.getOS() , key.getDevId() ,key.getLastSeen());

		boolean result =  database.executeQuery(query.toString());

		if (!result)
			logger.debug("This UserName : " + key.getDevId()
					+ " already exists");

		return new Tuple(key.getPublicKey(), key.getPrivateKey());
	}

	public org.fortiss.smg.usermanager.api.Key getKey(String access_key)
			throws TimeoutException {
		Key key = getKeyDB(access_key);
		return key.clone();
	}

	/**
	 * Request a list of DoubleValues
	 * 
	 * @param access_key
	 *            String known as public Key
	 * 
	 * @return KeyDB
	 * @throws TimeoutException
	 */
	private KeyDB getKeyDB(String access_key) throws TimeoutException {
		KeyDB result = null;
		logger.debug("Get KeyDB of " + access_key);

		SelectQuery query = new SelectQuery();
		query.addFrom(KeyManagerImpl.DB_NAME);
		query.addWhere("publickey = '" + access_key.toString() +"'");

		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		if (resultSet != null && resultSet.size() == 1) {
			KeyDB key_db = new KeyDB();
			key_db.setPublicKey(resultSet.get(0).get("publickey").toString());
			key_db.setPrivateKey(resultSet.get(0).get("privatekey").toString());
			key_db.setDevName(resultSet.get(0).get("devicename").toString());
			key_db.setUserId(Integer.parseInt(resultSet.get(0).get("userid")
					.toString()));
			key_db.setDevId(Integer.parseInt(resultSet.get(0).get("deviceid")
					.toString()));
			key_db.setLastSeen(Long.parseLong(resultSet.get(0).get("lastseen").toString()));
			key_db.setOS(resultSet.get(0).get("os").toString());
			key_db.setMACAddress(resultSet.get(0).get("macaddress").toString());
			result = key_db;
		}
		return result;
	}

	/**
	 * Gives a list of all Keys
	 * @param userid
	 * @return
	 * @throws TimeoutException
	 */
	public List<String> getKeys(int userid) throws TimeoutException {
		logger.debug("Get Keys of user with user Id  " + userid);
		if (userid < 1)
			return null;
		List<String> result = new ArrayList<String>();

		SelectQuery query = new SelectQuery();
		query.addColumn("publickey");
		query.addFrom(KeyManagerImpl.DB_NAME);
		query.addWhere("UserID = " + userid);

		List<Map<String, Object>> resultSet;
		resultSet = database.getSQLResults(query.toString());

		for (Map<String, Object> publickey : resultSet) {
			result.add(publickey.get("publickey").toString());
		}
		return result;
	}

	/**
	 * Gives the userId from a public key
	 * 
	 * @param access_key
	 * 
	 * @return User
	 * @throws TimeoutException
	 */
	public User getUser(String access_key) throws TimeoutException {
		logger.debug("Get the user Information that is owner of the device with public key "
				+ access_key);
		KeyDB key = getKeyDB(access_key);
		int userId = 0;
		User user = null;
		if (key != null) {
			logger.debug("Get user of owner device with access key "
					+ access_key);

			SelectQuery query = new SelectQuery();
			query.addColumn("userid");
			query.addFrom(KeyManagerImpl.DB_NAME);
			query.addWhere("publicKey = '" + access_key +"'");

			List<Map<String, Object>> resultSet;
			resultSet = database.getSQLResults(query.toString());

			if (resultSet != null && resultSet.size() == 1) {
				userId = Integer.parseInt(resultSet.get(0).get("userid")
						.toString());
			} else {
				return null;
			}
			if (userId != 0) {
				user = userImpl.getUser(userId);
			}
			return user;
		}
		return user;

	}

	/**
	 * Destroys all keys in the database of a user. He will have to start from
	 * scratch.
	 * 
	 * @param userid
	 * 
	 * @return boolean true if deleting form DB is successful
	 */
	public boolean removeAllKeys(int userId) throws TimeoutException {
		logger.debug("removing all user keys of " + userId);
		boolean result = false;

		DeleteQuery deleteQuery = new DeleteQuery(KeyManagerImpl.DB_NAME );
		deleteQuery.addWhere("UserID = " + userId);
		result = database.executeQuery(deleteQuery.toString());
		
		if (!result)
			logger.debug("Could not delete user - sql problem");
		return result;
	}

	/**
	 * Removes a Key from the Manager This will sign the (mobile) application
	 * linked to it.
	 * 
	 * @param access_key
	 * 
	 * @return boolean true if deleting form DB is successful
	 */
	public boolean removeKey(String access_key) throws TimeoutException {
		logger.debug("removing public keys " + access_key);
		boolean result = false;

		DeleteQuery deleteQuery = new DeleteQuery(KeyManagerImpl.DB_NAME );
		deleteQuery.addWhere("publicKey = '" + access_key + "'");
		result = database.executeQuery(deleteQuery.toString());

		if (!result)
			logger.debug("Could not delete access_key - sql problem");
		return result;
	}

	/**
	 * Update last_Seen of device to current time with given access_key
	 * 
	 * @param access_key
	 *            String it is known as public key
	 * 
	 * @return boolean true if update in DB is successful
	 */
	private boolean updateLastSeen(String access_key) throws TimeoutException {
		logger.debug("updating last_Seen of public keys " + access_key);
		boolean result = false;
		Calendar calendar = Calendar.getInstance();
		
		UpdateQuery updateQuery = new UpdateQuery(KeyManagerImpl.DB_NAME);
		updateQuery.set("LastSeen",calendar.getTimeInMillis()+"");
		updateQuery.addWhere("publicKey = '" + access_key +"'");
		result = database.executeQuery(updateQuery.toString());

		if (!result)
			logger.debug("Could not update LastSeen - sql problem");
		return result;
	}

	/**
	 * Update Device Id of device with given access_key
	 * 
	 * @param access_key
	 *            String it is known as public key
	 * 
	 * @return boolean true if update in DB is successful
	 * @throws TimeoutException
	 */
	public boolean setDevId(String access_key, int devId)
			throws TimeoutException {
		logger.debug("updating last_Seen of public keys " + access_key);
		updateLastSeen(access_key);
		boolean result = false;
		Calendar calendar = Calendar.getInstance();

		UpdateQuery updateQuery = new UpdateQuery(KeyManagerImpl.DB_NAME);
		updateQuery.set("DeviceID",  Integer.toString(devId));
		updateQuery.set("LastSeen",  calendar.getTimeInMillis()+"");
		updateQuery.addWhere("publicKey = '" + access_key +"'");
		result = database.executeQuery(updateQuery.toString());

		if (!result)
			logger.debug("Could not Update devID - sql problem");
		return result;
	}

	/**
	 * Update OS of device with given access_key
	 * 
	 * @param access_key
	 *            String it is known as public key
	 * 
	 * @return boolean true if update in DB is successful
	 * @throws TimeoutException 
	 */
	public boolean setOS(String access_key, String setOS) throws TimeoutException {
		logger.debug("updating last_Seen of public keys " + access_key);
		updateLastSeen(access_key);
		boolean result = false;
		Calendar calendar = Calendar.getInstance();

		UpdateQuery updateQuery = new UpdateQuery(KeyManagerImpl.DB_NAME);
		updateQuery.set("OS",  setOS);
		updateQuery.set("LastSeen",  calendar.getTimeInMillis()+"");
		updateQuery.addWhere("publicKey = '" + access_key +"'");
		result = database.executeQuery(updateQuery.toString());

		if (!result)
			logger.debug("Could not update OS  - sql problem");
		return result;
	}

	@Override
	public boolean isComponentAlive() throws TimeoutException {
		return true;
	}
}