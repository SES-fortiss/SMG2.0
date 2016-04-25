package org.fortiss.smg.sqltools.lib.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.IDatabase;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.sqltools.lib.TestingDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestingDBUtil implements InformationBrokerInterface {
	private static final Logger logger = LoggerFactory
			.getLogger(TestingDBUtil.class);

	public String DBTABLE = "DoubleEvents";

	private String dburl;
	private String dbuser;
	private String dbpassword;
	private String dburlold;
	private String dbuserold;
	private String dbpasswordold;

	private Connection con	;
	private Connection conold;
	
	

	/**
	 * Create DBUtil using database connection information, user credentials and
	 * an alarming component
	 * 
	 * @param dburl
	 *            Url of the mysql-database
	 * @param dbuser
	 *            Username for the database
	 * @param dbpassword
	 *            Password for the database
	 * @param alarming
	 *            Alarming-proxy for triggering Alarms if db-connection fails
	 *            for any reason.
	 * @param dburlold
	 *            Url of the mysql-database
	 * @param dbuserold
	 *            Username for the database
	 * @param dbpasswordold
	 *            Password for the database
	 * 
	 */
	public TestingDBUtil() {
		super();
		this.dburl = "jdbc:" + TestingDatabase.getDBUrl();
		this.dbuser = TestingDatabase.getDBUser();
		this.dbpassword = TestingDatabase.getDBPassword();
		this.dburlold = dburl;
		this.dbuserold = dbuser;
		this.dbpasswordold = dbpassword;
		
		//System.out.println(DBTABLE);

	}

	/**
	 * When called this method checks if the connection is already open or
	 * establishes a new connection if not.
	 * 
	 * @return True if connection is available or could be established, false if
	 *         an error occured.
	 */
	public synchronized boolean checkOrOpenDBConnection() {
		try {
			if (con == null || con.isClosed()) {
				TestingDBUtil.logger.debug("PersistencyLog attempting connect");
				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager
							.getConnection(dburl, dbuser, dbpassword);
					TestingDBUtil.logger.info("DB connection opened");

					return true;
				} catch (SQLException e) {
					TestingDBUtil.logger.warn("SQL Connecting problem", e);

					return false;
				} catch (ClassNotFoundException e) {
					TestingDBUtil.logger.warn("jdbc Driver problem", e);

					return false;
				}
			} else {
				return true;
			}
		} catch (SQLException e) {
			TestingDBUtil.logger.warn("SQL Connection Check problem", e);
			return false;
		}
	}

	/**
	 * Closes db-connection when called
	 */
	public synchronized void closeDBConnection() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
				TestingDBUtil.logger.info("PersitencyLog DB connection closed");
			}
			con = null;
		} catch (SQLException e) {
			TestingDBUtil.logger.warn(
					"Failed closing PersistencyLog DB conneciton", e);
		}
	}

	/**
	 * Getter for the connection handled by this utility
	 * 
	 * @return The database-connection handled by this utility. May be closed.
	 *         Make sure checkOrOpenDBConnection() is called before.
	 */
	public Connection getCon() {
		return con;
	}

	public synchronized boolean checkOrOpenoldDBConnection() {
		logger.trace("checkOrOpenoldDBConnection: Persistency attempting connect");
		try {
			if (conold == null || conold.isClosed()) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					conold = DriverManager.getConnection(dburlold, dbuserold,
							dbpasswordold);
					logger.info("PersistencyDBUtil: checkOrOpenoldDBConnection: Persistency DB connection opened");
					return true;
				} catch (SQLException e) {
					logger.warn(
							"PersistencyDBUtil: checkOrOpenoldDBConnection: SQL Connecting problem,",
							e);
					logger.warn("Please check {}, {}, {}", this.dburlold,
							this.dbuserold, this.dbpasswordold);
					return false;
				} catch (ClassNotFoundException e) {
					logger.error(
							"PersistencyDBUtil: checkOrOpenoldDBConnection: jdbc Driver problem",
							e);
					return false;
				}
			} else {
				return true;
			}
		} catch (SQLException e) {
			logger.error(
					"PersistencyDBUtil: checkOrOpenoldDBConnection: SQL Connection Check problem ",
					e);
			return false;
		}
	}

	/**
	 * Closes db-connection when called
	 */
	public synchronized void closeoldDBConnetion() {
		try {
			if (conold != null && !conold.isClosed()) {
				conold.close();
				logger.info("PersistencyDBUtil: closeoldDBConnetion: PersitencyLog DB connection closed");
			}
			conold = null;
		} catch (SQLException e) {
			logger.error(
					"PersistencyDBUtil: closeoldDBConnetion: Failed closing PersistencyLog DB conneciton ",
					e);
			conold = null;
		}
	}

	public Connection getOldCon() {
		return conold;
	}

	@Override
	public List<Map<String, Object>> getSQLResults(String sql) {

		TestingDBUtil.logger.info("SQLResults: " + sql);

		if (checkOrOpenDBConnection()) {
			try {

				ResultSet rs = getCon().createStatement().executeQuery(sql);
				if (rs == null) {
					logger.info("SQL error for: {}", sql);
				} else {
					return ResultSetHelper.convert(rs);
				}

			} catch (SQLException e) {
				logger.info("SQL error for: " + sql, e.fillInStackTrace());
				return null;
			}
		} else {
			logger.info("DB Error");
			return null;
		}
		return null;
	}

	@Override
	public boolean executeQuery(String sql) {

		TestingDBUtil.logger.info("executeQuery: " + sql);
		if (checkOrOpenDBConnection()) {
			try {
				getCon().prepareStatement(sql).execute();
				return true;		
			} catch (SQLException e) {
				logger.info("SQL error for: " + sql, e.fillInStackTrace());
				return false;
			}
		} else {
			logger.info("DB Error");
			return false;
		}
	}

	@Override
	public List<Map<String, Object>> getSQLResultsoldDB(String sql) {

		if (checkOrOpenoldDBConnection()) {
			try {

				ResultSet rs = getOldCon().createStatement().executeQuery(sql);
				if (rs == null) {
					logger.info("SQL error for: {}", sql);
				} else {
					return ResultSetHelper.convert(rs);
				}

			} catch (SQLException e) {
				logger.info("SQL error for: " + sql, e.fillInStackTrace());
				return null;
			}
		} else {
			logger.info("DB Error");
			return null;
		}
		return null;
	}

	@Override
	public boolean executeQueryoldDB(String sql) {
		if (checkOrOpenoldDBConnection()) {
			try {
				return getOldCon().createStatement().execute(sql);
			} catch (SQLException e) {
				logger.info("SQL error for: " + sql, e.fillInStackTrace());
				return false;
			}
		} else {
			logger.info("DB Error");
			return false;
		}
	}

	@Override
	public boolean isComponentAlive() throws TimeoutException {
		return true;
	}

	@Override
	public List<DoublePoint> getDoubleValue(DeviceId dev, long from, long to)
			throws TimeoutException {
		List<DoublePoint> result = new ArrayList<DoublePoint>();
		System.out.println("TestDBUtil: getDoubleValue ");
		logger.info("TestDBUtil: getDoubleValue ");
		
		if (dev.getDevid() == null) {
			/*
			 * OLD DB for Analyzer use BooleanEvent_Table
			 * convert long to SQLtimestamp
			 */
			logger.info("OLD DB not supported by test !");
			System.out.println("OLD DB not supported by test !");
			//result = getBoolValue(dev.getWrapperId(), from, to);
			
		}
		
		
		else if (isSIUnitType(dev.getDevid())) {
			/*
			 * OLD DB for Analyzer use DoubleEvent_Table
			 * convert long to SQLtimestamp
			 */
			logger.info("OLD DB not supported by test !");
			System.out.println("OLD DB not supported by test !");
			//result = getDoubleValueOld(dev.getWrapperId(), SIUnitType.valueOf(dev.getDevid()), from, to);
			
		}
		else {
		
			
		
			logger.trace("PersistencyQuery: getDoubleValue using key "
					+ dev);
			logger.trace("PersistencyQuery: requesting from DB " + dev);
			if (checkOrOpenDBConnection()) {
				try {
					PreparedStatement ps;
					if (from == to) {
						ps = con
								.prepareStatement(
										"SELECT value, maxAbsError, timestamp FROM "+DBTABLE+" WHERE devid = ? AND wrapperid = ? ORDER BY timestamp DESC LIMIT 0,1");
						ps.setString(1, dev.getDevid());
						ps.setString(2, dev.getWrapperId());
						// ps.setTimestamp(2,
						// translateDateToSQLTimeStamp(from));
						// ps.setTimestamp(3, translateDateToSQLTimeStamp(to));
						logger.debug("PersistencyLog: DB Query "
								+ ps.toString());
					} else {

						ps = con
								.prepareStatement(
										"SELECT value, maxAbsError, timestamp FROM "+DBTABLE+" WHERE devid = ? AND wrapperid = ? AND timestamp >= ? AND timestamp <= ? ORDER BY timestamp DESC");
						ps.setString(1, dev.getDevid());
						ps.setString(2, dev.getWrapperId());
						/*ps.setLong(3, new Timestamp(from).getTime());
						ps.setLong(4, new Timestamp(to).getTime());
						*/
						ps.setLong(3, from);
						ps.setLong(4, to);
						
						
						System.out.println(ps.toString());
						/*
						 * USE INDEX (timestamp)
						 * "SELECT value, unit, maxAbsError, timestamp FROM DoubleEvent_Table, (SELECT unit AS u, MAX(timestamp) AS ts FROM DoubleEvent_Table WHERE origin = ? GROUP BY unit) AS foo WHERE timestamp = foo.ts AND unit = foo.u AND origin = ?"
						 * ); //deprecated with trigger and cached maxtimestamp!
						 * //
						 * "SELECT value, unit, maxAbsError, timestamp FROM DoubleEvent_Table, ( SELECT unit AS u, max_timestamp AS ts FROM max_timestamps_double WHERE origin = ?) AS foo WHERE timestamp = foo.ts AND unit = foo.u AND origin = ?"
						 * ); ps.setString(1, deviceId); ps.setString(2,
						 * deviceId); // TASK: this duplicate is not so //
						 * pretty..
						 */
						logger.debug("PersistencyQuery: DB Query: "
								+ ps.toString());

					}

					ResultSet queryResult = ps.executeQuery();

					while (queryResult.next()) {
						double value = queryResult.getDouble("value");
			
						double maxAbsError = queryResult
								.getDouble("maxAbsError");
						Long timestamp = queryResult.getLong("timestamp");
								

						DoublePoint point = new DoublePoint(value, maxAbsError,
								timestamp);

						result.add(point);
						

					}
					ps.close();
				} catch (SQLException e) {
					closeDBConnection();
					logger.warn("PersistencyQuery: SQL Statement error", e);
				}
			} else {
				logger.warn("PersistencyQuery: Could not execute Query - no DB-Connection!");
			}
		
		}
		
		if (result.isEmpty()) {
			logger.warn("PersistencyQuery: Requested Object (" + dev
					+ ") not in Database - returning null");
			// Sebi: dirty fix, but we can't return null because then we get a
			// javax.xml.ws.soap.SOAPFaultException: Error reading
			// XMLStreamReader.at org.apache.cxf.jaxws.JaxWsClientProxy
			/*
			 * ArrayList<DoubleDataPoint> dummy = new
			 * ArrayList<DoubleDataPoint>(); DoubleDataPoint ddp = new
			 * DoubleDataPoint(); ddp.setValue(-1);
			 * ddp.setUnit(SIUnitType.NONE); ddp.setMaxAbsError(-1);
			 * ddp.setTimestamp(new Date()); dummy.add(ddp); return dummy;
			 */
			return null;
		} else {
			return result;
		}
		
	}



	@Override
	public List<DoublePoint> getDoubleValueBefore(DeviceId dev, long date)
			throws TimeoutException {
		return null;
	}

	@Override
	public boolean isSIUnitType(String unit) throws TimeoutException {
		for (SIUnitType type : SIUnitType.values()) {
			if (type.toString().equals(unit)) {
				return true;
			}
		}
		return false;
	}

	public void setTable(String string) {
		DBTABLE = string;
		
	}

	public String getTable() {
		return DBTABLE;
		
	}

	@Override
	public Map<Long, Double> getLastseen(String devId, String wrapperId) {
		Map<Long, Double> result = new HashMap<Long, Double>();
		if (checkOrOpenDBConnection()) { 
			try {
				PreparedStatement ps; 

				ps = con.prepareStatement("SELECT timestamp , value  FROM DoubleEvents WHERE devid = ? AND wrapperid = ? ORDER BY timestamp DESC LIMIT 0,1");
				ps.setString(1, devId); 
				ps.setString(2,wrapperId);
				logger.debug("PersistencyLog: DB Query " + ps.toString()); 
				ResultSet queryResult = ps.executeQuery();

				while (queryResult.next()) {  
					result.put(queryResult.getLong("timestamp"), queryResult.getDouble("value")); 
				} 
				ps.close(); 
				closeDBConnection();
			} 
			catch (SQLException e) { 
				closeDBConnection();
				e.printStackTrace();
				logger.warn("PersistencyQuery: SQL Statement error", e); } } else {
					logger.warn("PersistencyQuery: Could not execute Query - no DB-Connection!"); 
				}
		if (result.size() == 0) { 
			logger.warn("PersistencyQuery: Requestet Object not in Database - returning null");
		}
		return result; 

	}
}
