/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;

public class RuleSystemDBUtil {
	private Logger logger;
	
	private String dburl;
	private String dbuser;
	private String dbpassword;
	private Connection con;
	
	public RuleSystemDBUtil(String dburl, String dbuser, String dbpassword, Logger logger){
		super();
		this.dburl = dburl;
		this.dbuser = dbuser;
		this.dbpassword = dbpassword;
		this.logger = logger;
		logger.debug("RuleSystemDBUtil: initiated for DB access: "+ dburl + ", "+ dbuser + ", *****");
	}
	/**
	 * When called this method checks if the connection is already open or establishes a new connection if not.
	 * 
	 * @return True if connection is available or could be established, false if an error occured.
	 */
	public synchronized boolean checkOrOpenDBConnection() {
		logger.trace("checkOrOpenDBConnection: RuleSystemDBUtil attempting DB connect");
		try {
			if (con == null || con.isClosed()) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection(dburl, dbuser, dbpassword);
					logger.info("RuleSystemDBUtil: checkOrOpenDBConnection: RuleSystem.impl DB connection opened");
					return true;
				} catch (SQLException e) {
					logger.warn("RuleSystemDBUtil: checkOrOpenDBConnection: SQL Connecting problem,", e);
					logger.warn("Please check {}, {}, {}", this.dburl,this.dbuser, this.dbpassword );
					return false;
				} catch (ClassNotFoundException e) {
					logger.error("RuleSystemDBUtil: checkOrOpenDBConnection: jdbc Driver problem", e);
					return false;
				}
			} else {
				return true;
			}
		} catch (SQLException e) {
			logger.error("RuleSystemDBUtil: checkOrOpenDBConnection: SQL Connection Check problem ", e);
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
				logger.info("RuleSystemDBUtil: closeDBConnetion: RuleSystem.impl DB connection closed");
			}
			con = null;
		} catch (SQLException e) {
			logger.error("RuleSystemDBUtil: closeDBConnetion: Failed closing RuleSystem.impl DB conneciton ", e);
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
