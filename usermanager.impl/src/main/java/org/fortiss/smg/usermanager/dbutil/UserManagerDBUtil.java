/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.usermanager.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserManagerDBUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(UserManagerDBUtil.class);
	
	
	private String dburl;
	private String dbuser;
	private String dbpassword;
	private Connection con;

	/**
	 * Create DBUtil using database connection information, user credentials and an alarming component
	 * @param dburl Url of the mysql-database
	 * @param dbuser Username for the database
	 * @param dbpassword Password for the database
	 * 
	 */
	public UserManagerDBUtil(String dburl, String dbuser, String dbpassword) {
		super();
		this.dburl = dburl;
		this.dbuser = dbuser;
		this.dbpassword = dbpassword;
		logger.debug("UserManagerDBUtil: instanced for DB access: "+ dburl + ", "+ dbuser + ", *****");
	}
	
	/**
	 * When called this method checks if the connection is already open or establishes a new connection if not.
	 * 
	 * @return True if connection is available or could be established, false if an error occured.
	 */
	public synchronized boolean checkOrOpenDBConnection() {
		logger.trace("checkOrOpenDBConnection: UserManagerDBUtil attempting connect");
		try {
			if (con == null || con.isClosed()) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection(dburl, dbuser, dbpassword);
					logger.info("UserManagerDBUtil: checkOrOpenDBConnection: UserManager.impl DB connection opened");
					return true;
				} catch (SQLException e) {
					logger.warn("UserManagerDBUtil: checkOrOpenDBConnection: SQL Connecting problem,", e);
					logger.warn("Please check {}, {}, {}", this.dburl,this.dbuser, this.dbpassword );
					return false;
				} catch (ClassNotFoundException e) {
					logger.error("UserManagerDBUtil: checkOrOpenDBConnection: jdbc Driver problem", e);
					return false;
				}
			} else {
				return true;
			}
		} catch (SQLException e) {
			logger.error("UserManagerDBUtil: checkOrOpenDBConnection: SQL Connection Check problem ", e);
			return false;
		}
	}
	
	/**
	 * Closes db-connection when called
	 */
	public synchronized void closeDBConnetion() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
				logger.info("UserManagerDBUtil: closeDBConnetion: UserManager.impl DB connection closed");
			}
			con = null;
		} catch (SQLException e) {
			logger.error("UserManagerDBUtil: closeDBConnetion: Failed closing UserManager.impl DB conneciton ", e);
			con = null;
		}
	}
	
	/**
	 * Getter for the connection handled by this utility
	 * 
	 * @return The database-connection handled by this utility. May be closed. Make sure checkOrOpenDBConnection() is called before.
	 */
	public Connection getCon() {
		return con;
	}

	
	
}
