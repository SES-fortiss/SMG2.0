/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.informationbroker.impl.persistency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.sql.Statement;
import org.fortiss.smg.actuatormaster.api.IActuatorListener;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterfaceForICTProject;
import org.fortiss.smg.informationbroker.impl.cache.CacheKey;
import org.fortiss.smg.informationbroker.impl.cache.LocalCacheManager;
import org.slf4j.Logger;

public class PersistencyLog implements IActuatorListener , InformationBrokerInterfaceForICTProject {

	private Logger logger;

	private PersistencyDBUtil dbUtil;

	private LocalCacheManager localCacheManager;

	/**
	 * Constructor of Persistency-Logging-Component
	 * 
	 * @param dbUtil
	 * @param localCacheManager
	 * @param logger2
	 */

	public PersistencyLog(PersistencyDBUtil dbUtil,
			LocalCacheManager localCacheManager, Logger logger) {
		this.dbUtil = dbUtil;
		this.localCacheManager = localCacheManager;
		this.logger = logger;
	}

	/**
	 * Synchronized Method to close db-connection in dbUtil
	 */
	public synchronized void closeDBConnetion() {
		dbUtil.closeDBConnetion();
	}

	/**
	 * Synchronized Method to close dbictproject-connection in dbUtil
	 */
	public synchronized void closeIctProjectConnection() {
		dbUtil.closeictProjectDBConnection();
	}

	
	/**
	 * Receive newDeviceEvents and save to database
	
	public void newDeviceEvent(String newDevOrigin, DeviceSpec spec) {
		// TODO SpecId not saved into database - add once necessary!
		logger.debug("PersistencyLog: received new newDevice event. logging to Database.");
		if (dbUtil.checkOrOpenDBConnection()) {
			try {
				PreparedStatement query = dbUtil.getCon().prepareStatement(
						"INSERT INTO DeviceEvent_Table(origin) VALUES(?)");
				query.setString(1, newDevOrigin);
				query.executeUpdate();
				query.close();
				logger.debug("PersistencyLog: DB Query " + query);
			} catch (SQLException e) {
				closeDBConnetion();
				logger.warn("PersistencyLog: SQL Statement error", e);
			}
		} else {
			logger.error("PersistencyLog: Event discarded: newDeviceEvent({})",
					newDevOrigin);
		}
	}
	 */





	private String translateDateToSQLTimeStamp(Date d) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}

	@Override
	public void onDoubleEventReceivedForICTProject(String deviceRoom, String hrName ,String devId, String tech,
			String devType , String sensorOrActuator , double minRange , double maxRange){
		logger.debug("received new double event. storing in Open Database for ICTProject");
		
		int generatedId = 0;
		int placeId = 0, infoId = 0;
		placeId = getPlaceIdOfReceiveddDubleEventForICTProject(deviceRoom,devId);
		infoId = getInformationIdOfReceivedDubleEventForICTProject(devType, devId);
		if (dbUtil.checkOrOpenictProjectDBConnection()) {
			try{
				PreparedStatement query = dbUtil
						.getIctProjectCon()
						.prepareStatement(
								"INSERT INTO `connected-devices`(placeid,name,productid,class,technology,"
										+ "sensor,actuator,informationid) "
										+ "VALUES(?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE placeid= ?",Statement.RETURN_GENERATED_KEYS);
				query.setInt(1,placeId);
				query.setString(2,hrName.toLowerCase());
				query.setString(3, tech.substring(0,1)+devId.toLowerCase());
				query.setString(4, "NODE".toLowerCase());
				query.setString(5, tech.toLowerCase());
				
				if (sensorOrActuator == "Sensor") {
					query.setInt(6, 1);
					query.setInt(7, 0);
				}else{
					query.setInt(7, 1);
					query.setInt(6, 0);
				}
				query.setInt(8, infoId);
				query.setInt(9, placeId);
				query.executeUpdate();
				ResultSet keys = query.getGeneratedKeys();
				keys.next();
				generatedId =keys.getInt(1);
				createNewRangeForICTProject(minRange, maxRange, generatedId);

				logger.debug("PersistencyLog: DB Query " + query);
				logger.debug("received new double event. logging to Database "
						+ devId + "Val: " );
				
				query.close();
				
			} catch (SQLException e) {
				closeIctProjectConnection();
				logger.warn("SQL Statement error", e);
			}
		}else {
			logger.error("Event discarded: doubleEvent({})",
					devId);
		}
		
	}
	public int getInformationIdOfReceivedDubleEventForICTProject(String devType,String devId){

		logger.debug("received new double event. retrieving the placeId of device in Open Database for ICTProject");
		int infoId = 0;
		if (dbUtil.checkOrOpenictProjectDBConnection()) {
			try{

				PreparedStatement query = dbUtil
						.getIctProjectCon()
						.prepareStatement(
								"SELECT information.id FROM types,information"
										+ " WHERE types.typename = ? AND types.id = information.typeid");

				query.setString(1,devType);
				ResultSet rs = query.executeQuery();
				if (rs.next()) {
					infoId=rs.getInt("id");
				}
				logger.debug("PersistencyLog: DB Query " + query);
				logger.debug("received new double event. location Information of device is retrieved from Database "
						+ devId + "Val: " );
				query.close();

			} catch (SQLException e) {
				closeIctProjectConnection();
				logger.warn("SQL Statement error", e);
			}
		}else {
			logger.error("Event discarded: doubleEvent({})",
					devId);
		}
		return infoId;

	}
	public int getPlaceIdOfReceiveddDubleEventForICTProject(String deviceRoom,String devId){

		logger.debug("received new double event. retrieving the placeId of device in Open Database for ICTProject");
		int placeId = 0;
		if (dbUtil.checkOrOpenictProjectDBConnection()) {
			try{
				PreparedStatement query = dbUtil
						.getIctProjectCon()
						.prepareStatement(
								"SELECT * FROM places WHERE name = ?");
				query.setString(1,deviceRoom);
				ResultSet rs = query.executeQuery();
				if (rs.next()) {
					placeId=rs.getInt("id");
				}

				logger.debug("PersistencyLog: DB Query " + query);
				logger.debug("received new double event. location Information of device is retrieved from Database "
						+ devId + "Val: " );
				query.close();

			} catch (SQLException e) {
				closeIctProjectConnection();
				logger.warn("SQL Statement error", e);
			}
		}else {
			logger.error("Event discarded: doubleEvent({})",
					devId);
		}
		return placeId;
	}

	public void createNewRangeForICTProject(double min, double max, int devId ) {

		logger.debug("creating a rang fo new Device. storing in Open Database for ICTProject");
		if (dbUtil.checkOrOpenictProjectDBConnection()) {
			try{
				PreparedStatement query = dbUtil
						.getIctProjectCon()
						.prepareStatement(
								"INSERT INTO ranges(deviceid,class,min,max)"
										+ "VALUES(?,?,?,?)");
				query.setInt(1,devId);
				query.setString(2, "SIMPLE".toLowerCase());
				query.setDouble(3,min);
				query.setDouble(4,max);
				query.executeUpdate();
				
				logger.debug("PersistencyLog: DB Query " + query);
				logger.debug("received new double event. logging to Database "
						+ devId + "Val: " );
				query.close();

			} catch (SQLException e) {
				closeIctProjectConnection();
				logger.warn("SQL Statement error", e);
			}
		}else {
			logger.error("No Range assigned to the device: deviceId({})",
					devId);
		}

	}

	@Override
	public void onDoubleEventReceived(DoubleEvent ev, DeviceId dev,
			String client) {

	
		logger.debug("received new double event. storing in Cache");
		DoublePoint point = new DoublePoint(ev.getValue(), ev.getMaxAbsError(),
				new Date().getTime());

		localCacheManager.doubleCache.store(new CacheKey(dev.getDevid(),
				dev.getWrapperId()), point);
	
		logger.debug("received new double event. logging to Database");
		if (dbUtil.checkOrOpenDBConnection()) {
			
			/*try {
				PreparedStatement duplicate = dbUtil
						.getCon()
						.prepareStatement(
								"SELECT * FROM DoubleEvent_Table WHERE timestamp = ? AND origin = ? AND unit = ?");
				duplicate.setString(1, translateDateToSQLTimeStamp(new Date()));
				duplicate.setString(2, origin);
				duplicate.setString(3, unit.toString());
				ResultSet rs = duplicate.executeQuery();

				if (!rs.isFirst()) {

					duplicate.close();
*/
			try{
					PreparedStatement query = dbUtil
							.getCon()
							.prepareStatement(
									"INSERT INTO DoubleEvents(devid,wrapperid,value,maxAbsError, timestamp) VALUES(?,?,?,?,?)");
					query.setString(1, dev.getDevid());
					query.setString(2, dev.getWrapperId());
					query.setDouble(3, ev.getValue());
					if (Double.isNaN(ev.getMaxAbsError())) {
						query.setNull(4, Types.DOUBLE);
					} else {
						query.setDouble(4, Double.isNaN(ev.getMaxAbsError()) ? null
								: ev.getMaxAbsError());
					}
					query.setLong(5, point.getTime());
					query.executeUpdate();
					logger.debug("PersistencyLog: DB Query " + query);
					logger.debug("received new double event. logging to Database "
							+ dev + "Val: "+ev.getValue() );
					query.close();
			/*	} else {

					duplicate.close();
				}
		*/
			} catch (SQLException e) {
				closeDBConnetion();
				logger.warn("SQL Statement error", e);
			}
		} else {
			logger.error("Event discarded: doubleEvent({},{})",
					dev, ev.getValue());
		}
		
	}

	@Override
	public void onDeviceEventReceived(DeviceEvent ev, String client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isComponentAlive() throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

}