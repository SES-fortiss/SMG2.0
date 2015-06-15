/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.informationbroker.impl.persistency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;

public class PersistencyDBUtil {
	private Logger logger;
	
	private String olddburl;
	private String dburl;
	private String ictprojectdburl;
	private String dbuser;
	private String dbpassword;

	private Connection oldcon;
	private Connection con;
	private Connection ictprojectcon;
	/**
	 * Create DBUtil using database connection information, user credentials and an alarming component
	 * @param dburl Url of the mysql-database
	 * @param dbuser Username for the database
	 * @param dbpassword Password for the database
	 * @param logger 
	 */
	public PersistencyDBUtil(String olddburl, String dburl,String ictprojectdburl, String dbuser, String dbpassword, Logger logger) {
		super();
		this.olddburl = olddburl;
		this.dburl = dburl;
		this.ictprojectdburl = ictprojectdburl;
		this.dbuser = dbuser;
		this.dbpassword = dbpassword;
		this.logger = logger;
		logger.debug("PersistencyDBUtil: instanced for DB access: "+ dburl + ", ICTProjectUrl : "+ictprojectdburl+", "+ dbuser + ", *****");
	}
	
	/**
	 * When called this method checks if the connection is already open or establishes a new connection if not.
	 * 
	 * @return True if connection is available or could be established, false if an error occured.
	 */
	public synchronized boolean checkOrOpenDBConnection() {
		logger.trace("checkOrOpenDBConnection: Persistency attempting connect");
		try {
			if (con == null || con.isClosed()) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection(dburl, dbuser, dbpassword);
					logger.info("PersistencyDBUtil: checkOrOpenDBConnection: Persistency DB connection opened");
					return true;
				} catch (SQLException e) {
					logger.warn("PersistencyDBUtil: checkOrOpenDBConnection: SQL Connecting problem,", e);
					logger.warn("Please check: " +  this.dburl+","+this.dbuser+","+ this.dbpassword );
					return false;
				} catch (ClassNotFoundException e) {
					logger.error("PersistencyDBUtil: checkOrOpenDBConnection: jdbc Driver problem", e);
					return false;
				}
			} else {
				return true;
			}
		} catch (SQLException e) {
			logger.error("PersistencyDBUtil: checkOrOpenDBConnection: SQL Connection Check problem ", e);
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
				logger.info("PersistencyDBUtil: closeDBConnetion: PersitencyLog DB connection closed");
			}
			con = null;
		} catch (SQLException e) {
			logger.error("PersistencyDBUtil: closeDBConnetion: Failed closing PersistencyLog DB conneciton ", e);
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
	
	
	/* 
	 * support of old DB for Analyszer
	 */
	public synchronized boolean checkOrOpenoldDBConnection() {
		logger.trace("checkOrOpenoldDBConnection: Persistency attempting connect");
		try {
			if (oldcon == null || oldcon.isClosed()) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					oldcon = DriverManager.getConnection(olddburl, dbuser, dbpassword);
					logger.info("PersistencyDBUtil: checkOrOpenoldDBConnection: Persistency DB connection opened");
					return true;
				} catch (SQLException e) {
					logger.warn("PersistencyDBUtil: checkOrOpenoldDBConnection: SQL Connecting problem,", e);
					logger.warn("Please check:" + this.olddburl+","+this.dbuser+","+ this.dbpassword );
					return false;
				} catch (ClassNotFoundException e) {
					logger.error("PersistencyDBUtil: checkOrOpenoldDBConnection: jdbc Driver problem", e);
					return false;
				}
			} else {
				return true;
			}
		} catch (SQLException e) {
			logger.error("PersistencyDBUtil: checkOrOpenoldDBConnection: SQL Connection Check problem ", e);
			return false;
		}
	}
	
	/**
	 * Closes db-connection when called
	 */
	public synchronized void closeoldDBConnetion() {
		try {
			if (oldcon != null && !oldcon.isClosed()) {
				oldcon.close();
				logger.info("PersistencyDBUtil: closeoldDBConnetion: PersitencyLog DB connection closed");
			}
			oldcon = null;
		} catch (SQLException e) {
			logger.error("PersistencyDBUtil: closeoldDBConnetion: Failed closing PersistencyLog DB conneciton ", e);
			oldcon = null;
		}
	}
	public Connection getOldCon() {
		return oldcon;
	}
	
	
	/**
	 * When called this method checks if the connection is already open or establishes a new connection if not.
	 * 
	 * @return True if connection is available or could be established, false if an error occured.
	 */
	public synchronized boolean checkOrOpenictProjectDBConnection() {
		logger.trace("checkOrOpenictProjectDBConnection: Persistency attempting connect");
		try {
			if (ictprojectcon == null || ictprojectcon.isClosed()) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					ictprojectcon = DriverManager.getConnection(ictprojectdburl, dbuser, dbpassword);
					logger.info("PersistencyDBUtil: checkOrOpenictProjectDBConnection: Persistency DB connection opened");
					return true;
				} catch (SQLException e) {
					logger.warn("PersistencyDBUtil: checkOrOpenictProjectDBConnection: SQL Connecting problem,", e);
					logger.warn("Please check {}, {}, {}", this.ictprojectdburl,this.dbuser, this.dbpassword );
					return false;
				} catch (ClassNotFoundException e) {
					logger.error("PersistencyDBUtil: checkOrOpenictProjectDBConnection: jdbc Driver problem", e);
					return false;
				}
			} else {
				return true;
			}
		} catch (SQLException e) {
			logger.error("PersistencyDBUtil: checkOrOpenictProjectDBConnection: SQL Connection Check problem ", e);
			return false;
		}
	}
	
	/**
	 * Closes db-connection when called
	 */
	public synchronized void closeictProjectDBConnection() {
		try {
			if (ictprojectcon != null && !ictprojectcon.isClosed()) {
				ictprojectcon.close();
				logger.info("PersistencyDBUtil: closeictProjectDBConnection: PersitencyLog DB connection closed");
			}
			ictprojectcon = null;
		} catch (SQLException e) {
			logger.error("PersistencyDBUtil: closeictProjectDBConnection: Failed closing PersistencyLog DB conneciton ", e);
			ictprojectcon = null;
		}
	}
	/**
	 * Getter for the connection handled by this utility
	 * 
	 * @return The database-connection handled by this utility. May be closed. Make sure checkOrOpenictProjectDBConnection() is called before.
	 */
	public Connection getIctProjectCon() {
		return ictprojectcon;
	}
}
