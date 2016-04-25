package org.fortiss.smg.analyzer.impl.databaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * provides database information concerning double events using SQL queries
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 */
public class SQL {

	private static final Logger logger = LoggerFactory.getLogger(SQL.class);

	/**
	 * get's the oldest entry in the database for the device
	 * 
	 * @param device
	 *            device, of which the oldest data point should be fetched
	 * @param broker
	 *            database interface
	 * @return the values of the database are converted into a DoublePoint
	 *         element
	 * @throws IllegalArgumentException
	 *             if device or broker == null
	 * @throws TimeoutException
	 *             no database connection possible
	 * @throws SQLException
	 *             no data in database
	 */
	public static DoublePoint oldest(DeviceId device,
			InformationBrokerInterface broker) throws IllegalArgumentException,
			TimeoutException, SQLException {
		return newestOrOldest(device, "ASC", broker);
	}

	/**
	 * get's the newest entry in the database for the device
	 * 
	 * @param device
	 *            device, of which the newest data point should be fetched
	 * @param broker
	 *            database interface
	 * @return the values of the database are converted into a DoublePoint
	 *         element
	 * @throws IllegalArgumentException
	 *             if device or broker == null
	 * @throws TimeoutException
	 *             no database connection possible
	 * @throws SQLException
	 *             no data in database
	 */
	public static DoublePoint newest(DeviceId device,
			InformationBrokerInterface broker) throws IllegalArgumentException,
			TimeoutException, SQLException {
		return newestOrOldest(device, "DESC", broker);
	}

	/**
	 * Gets the latest database entry for a specified device
	 * 
	 * @param device
	 *            device id, of which the data should be fetched
	 * @param order
	 *            defines in which order the sql statement should sort; possible
	 *            values: {@code ASC} and {@code DESC}
	 * @param broker
	 *            interface for database
	 * @return the latest entry found in the database
	 * @throws TimeoutException
	 *             no database connection is possible
	 * @throws IllegalArgumentException
	 *             broker == null
	 * @throws SQLException
	 *             no data found in database
	 */
	private static DoublePoint newestOrOldest(DeviceId device, String order,
			InformationBrokerInterface broker) throws TimeoutException,
			IllegalArgumentException, SQLException {
		if (broker == null) {
			logger.warn("newestOrOldest: database connection == null - throws IllegalArgumentException");
			throw new IllegalArgumentException("'broker' is null - not valid");
		}
		if (device == null) {
			logger.warn("newestOrOldest: DeviceId == null - throws IllegalArgumentException");
			throw new IllegalArgumentException("'device' is null - not valid");
		}
		try {
			if (!broker.isComponentAlive()) {
			}
		} catch (TimeoutException e1) {
			logger.warn("database connection not alive " + e1.getMessage());
			e1.printStackTrace();
			throw new TimeoutException("database connection not alive");
		}

		boolean boolEvent = isDataBoolean(device, broker);
		boolean newDB = chooseDatabase(device, broker);
		List<Map<String, Object>> sqlResult = new ArrayList<Map<String, Object>>();
		DoublePoint solution = null;
		if (newDB == false) {
			if (boolEvent == false) {
				try {
					sqlResult = broker.getSQLResultsoldDB("SELECT * FROM "
							+ "`DoubleEvent_Table` WHERE `origin` = '"
							+ device.getWrapperId().toString()
							+ "' AND `unit` = '" + device.getDevid().toString()
							+ "' ORDER BY `timestamp` " + order + " LIMIT 1");
				} catch (TimeoutException e) {
					logger.warn("newestOrOldest: no connection to database "
							+ e.getMessage());
					e.printStackTrace();
					throw e;
				}
			} else {
				try {
					sqlResult = broker.getSQLResultsoldDB("SELECT * FROM "
							+ "`BooleanEvent_Table` WHERE `origin` = '"
							+ device.getWrapperId().toString() + "' "
							+ "ORDER BY `timestamp` " + order + " LIMIT 1");
				} catch (TimeoutException e) {
					logger.warn("newestOrOldest: no connection to database "
							+ e.getMessage());
					e.printStackTrace();
					throw e;
				}
			}
		} else {
			try {
				sqlResult = broker
						.getSQLResults("SELECT * FROM `DoubleEvents` WHERE `devid` = '"
								+ device.getDevid()
								+ "' AND `wrapperid` = '"
								+ device.getWrapperId()
								+ "' ORDER BY `timestamp` "
								+ order
								+ " LIMIT 1");
			} catch (TimeoutException e) {
				logger.warn("newestOrOldest: no connection to database "
						+ e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}
		if (sqlResult == null || sqlResult.isEmpty()) {
			logger.warn("SQL.latest: sql query didn't provide any results");
			throw new SQLException("sql query didn't provide any results");
		} else {
			Map<String, Object> result = sqlResult.get(0);
			solution = convert(device, result, broker);
		}
		return solution;
	}

	/**
	 * extracts a {@code DoublePoint} out of the given SQL results
	 * 
	 * @param device
	 *            device the SQL result belongs to
	 * @param result
	 *            one list element of the SQL results
	 * @param broker
	 *            database interface
	 * @return information is mapped into a DoublePoint element
	 * @throws IllegalArgumentException
	 *             if result is null or empty
	 * @throws TimeoutException
	 *             connection to database not possible
	 */
	private static DoublePoint convert(DeviceId device,
			Map<String, Object> result, InformationBrokerInterface broker)
			throws IllegalArgumentException, TimeoutException {
		DoublePoint solution = null;
		boolean boolEvent = isDataBoolean(device, broker);
		boolean newDB = chooseDatabase(device, broker);

		/*
		 * result.get("string") return the value of the line, where column name
		 * == string
		 */
		if (result.isEmpty() || result == null) {
			logger.warn("SQL.latest: sql query didn't provide any results - "
					+ "null is returned");
			throw new IllegalArgumentException("result is empty");
		} else {
			if (newDB == false) {
				if (boolEvent == false) {
					solution = new DoublePoint((Double) result.get("value"),
							(Double) result.get("maxabserror"),
							(Long) result.get("timestamp"));
				} else {
					double val = 0.0;
					if ((Boolean) (result.get("value")) == true) {
						val = 1.0;
					} else {
						val = 0.0;
					}
					solution = new DoublePoint(val, 0.0,
							(Long) (result.get("timestamp")));
				}
			} else {
				solution = new DoublePoint((Double) (result.get("value")),
						(Double) (result.get("maxabserror")),
						(Long) result.get("timestamp"));
			}
			return solution;
		}
	}

	/**
	 * decides whether the device is stored in the old or new database
	 * 
	 * @param device
	 *            device which should be checked
	 * @param broker
	 *            interface for database
	 * @return true: device is stored in new database; false: device is stored
	 *         in old database
	 * @throws TimeoutException
	 *             if it is not possible to connect to the database
	 */
	private static boolean chooseDatabase(DeviceId device,
			InformationBrokerInterface broker) throws TimeoutException {
		if (device.getDevid() == null) {
			/*
			 * OLD DB for Analyzer use BooleanEvent_Table convert long to
			 * SQLtimestamp
			 */
			return false;
		} else {
			try {
				if (broker.isSIUnitType(device.getDevid())) {
					/*
					 * OLD DB for Analyzer use DoubleEvent_Table convert long to
					 * SQLtimestamp
					 */
					return false;
				} else {
					return true;
				}
			} catch (TimeoutException e) {
				logger.warn("chooseDatabase: no connection to database "
						+ e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * decides whether the database stores boolean or double events for the
	 * device
	 * 
	 * @param device
	 *            device which should be checked
	 * @param broker
	 *            interface for database
	 * @return true: database stores boolean events for this device; false:
	 *         database stores double events
	 * @throws TimeoutException
	 *             if it is not possible to connect to the database
	 */
	private static boolean isDataBoolean(DeviceId device,
			InformationBrokerInterface broker) throws TimeoutException {
		if (device.getDevid() == null) {
			/*
			 * OLD DB for Analyzer use BooleanEvent_Table convert long to
			 * SQLtimestamp
			 */
			return true;
		} else {
			try {
				if (broker.isSIUnitType(device.getDevid())) {
					/*
					 * OLD DB for Analyzer use DoubleEvent_Table convert long to
					 * SQLtimestamp
					 */
					return false;
				} else {
					return false;
				}
			} catch (TimeoutException e) {
				logger.warn("isDataBoolean: no database connection "
						+ e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}
	}
}
