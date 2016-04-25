package org.fortiss.smg.analyzer.impl.databaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.Comparison;
import org.fortiss.smg.analyzer.impl.databaseConnection.SQL;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DataPoint;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class interacts with the database via the InformationBrokerInterface and
 * retrieves data sets from it
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */

public class DatabaseRequestor {

	private final static Logger logger = LoggerFactory
			.getLogger(DatabaseRequestor.class);

	/**
	 * searches database for all available data sets, which start at
	 * {@code startTimePoint} and last for {@code timeSpan}
	 * 
	 * @param startTimePoint
	 *            where each data set should start; use concrete Calendar
	 *            variables like {@code Calendar.MARCH} or
	 *            {@code Calendar.MONDAY} but not {@code Calendar.MONTH}
	 * @param timeSpan
	 *            defines how long the time interval for each data set should
	 *            last as Calendar variable, e.g {@code Calendar.MONTH}
	 * @param amount
	 *            defines the amount of {@code timeSpan} the data set should
	 *            last; for example with {@code amount = 3} and
	 *            {@code timeSpan = Calendar.MONTH} the data set would last for
	 *            3 months
	 * @param broker
	 *            database interface
	 * @param deviceID
	 *            device of which data should be collected
	 * @return all data sets in chronologic order
	 * @throws TimeoutException
	 *             cannot connect to database
	 * @throws SQLException
	 *             no data found in database
	 * @throws IllegalArgumentException
	 *             {@code broker == null}, {@code DeviceId == null} or
	 *             {@code startTimePoint} is not a valid Calendar field
	 */
	public static List<DataSet> allAvailableDataSets(int startTimePoint,
			int timeSpan, int amount, InformationBrokerInterface broker,
			DeviceId deviceID) throws TimeoutException, SQLException,
			IllegalArgumentException {
		int interval = 0;
		int abstractTime = 0;
		try{
		interval = initInterval(startTimePoint);
		}
		catch (IllegalArgumentException e){
			logger.warn("allAvailableDataSets: "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		try{
		abstractTime = initAbstractTime(startTimePoint);
		}catch (IllegalArgumentException e){
			logger.warn("allAvailableDataSets: "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		List<DataSet> allDataSets = new ArrayList<DataSet>();

		Calendar startDataSet = Calendar.getInstance();
		Calendar stopDataSet = Calendar.getInstance();
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance(); // current timeSpan

		// initializes start and end of the time in which data intervals will be
		// collected
		DataPoint latest = null;
		try {
			latest = SQL.newest(deviceID, broker);
		} catch (IllegalArgumentException e) {
			logger.warn("allAvailableDataSets: " + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (TimeoutException e) {
			logger.warn("allAvailableDataSets: no database connection possible "
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			logger.warn("allAvailableDataSets: no newest data point found "
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		}
		DataPoint oldest = null;
		try {
			oldest = SQL.oldest(deviceID, broker);
		} catch (IllegalArgumentException e) {
			logger.warn("allAvailableDataSets: " + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (TimeoutException e) {
			logger.warn("allAvailableDataSets: no database connection possible "
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			logger.warn("allAvailableDataSets: no oldest data point found "
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		}
		startCal.setTimeInMillis(oldest.getTime());
		stopCal.setTimeInMillis(latest.getTime());

		logger.info("allAvailableDataSets: searching for data from "
				+ startCal.getTime() + " to " + stopCal.getTime());

		// sets start of first data interval
		startDataSet.setTimeInMillis(oldest.getTime());
		try {
			Comparison.initDate(startDataSet, abstractTime);
		} catch (IllegalArgumentException e) {
			logger.warn("value for 'startDataSet' not supperted "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		startDataSet.set(abstractTime, startTimePoint);

		// searches data for available data intervals if interval dosen't
		// contain values the interval is left out
		while (startDataSet.compareTo(stopCal) <= 0) {
			stopDataSet.setTimeInMillis(startDataSet.getTimeInMillis());
			stopDataSet.add(timeSpan, amount);
			// creates a new dataSet
			DataSet dataSet = new DataSet(startDataSet.getTimeInMillis(), stopDataSet.getTimeInMillis(), deviceID);
			// fills dataSet with data
			try {
				DatabaseRequestor.fetchesDataSet(dataSet, broker);
			} catch (TimeoutException e) {
				logger.warn("allAvailableDataSets: no database connection possible "
						+ e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (SQLException e) {
				logger.warn("allAvailableDataSets: no data found while fetching "
						+ e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (IllegalArgumentException e) {
				logger.warn("allAvailableDataSets: " + e.getMessage());
				e.printStackTrace();
				throw e;
			}
			// checks if there is data for this time span in the database
			if (dataSet.getDataList() == null
					|| dataSet.getDataList().isEmpty()) {
				startDataSet.add(interval, 1);
			} else {
				allDataSets.add(dataSet);
				startDataSet.add(interval, 1);
			}
		}
		return allDataSets;
	}
	// TODO: implement a new allAvailableData method which supports this
	// heading:
	/*
	 * private List<DataSet> allAvailableDataSetsGeneral(Calendar start,
	 * Calendar stop, DeviceId device, long interval, InformationBrokerInterface
	 * broker) {
	 */

	/**
	 * Prerequisite: data should be initialized correctly (not null) fetches
	 * data from database and modifies the list in the {@code data} Object;
	 * NOTE: if data has the same value for startDate and stopDate, the newest
	 * element in the database will be fetched
	 * 
	 * @param data
	 *            data element which should be initialized with data; should
	 *            provide start and stop time of data collection, as well as the
	 *            device, the data should be collected from
	 * @param broker
	 *            database interface
	 * @throws TimeoutException
	 *             cannot connect to database
	 * @throws SQLException
	 *             if no data found in the database
	 * @throws IllegalArgumentException
	 *             if {@code data == null} or {@code broker == null}
	 */
	public static void fetchesDataSet(DataSet data,
			InformationBrokerInterface broker) throws TimeoutException,
			SQLException, IllegalArgumentException {
		if (broker == null) {
			throw new IllegalArgumentException(
					"InformationBrokerInterface is not allowed to be null");
		}
		if (data == null) {
			throw new IllegalArgumentException(
					"DataSet is not allowed to be null");
		}
		List<DoublePoint> fetchList = new ArrayList<DoublePoint>();
		try {
			fetchList = broker.getDoubleValue(data.getDeviceId(), data.getStartDate(), data.getStopDate());
		} catch (TimeoutException e) {
			logger.warn("fetchesDataSet: Timeout: If trying to fetch a large amount of data correct the timeout value of DefaultProxy<InformationBrokerInterface> in AnalyzerActivator. "
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		}
		if (fetchList == null || fetchList.isEmpty()) {
			logger.warn("fetchesDataSet: fetched an empty list for Device "
					+ data.getDeviceId() + " from "
					+ data.getStartDate()+ " to "
					+ data.getStopDate() + " - returning null");
			throw new SQLException("no data found in database");
		}
		Collections.reverse(fetchList);
		List<DoublePoint> convertList = new ArrayList<DoublePoint>();
		for (Iterator<DoublePoint> iterator = fetchList.iterator(); iterator
				.hasNext();) {
			DoublePoint singleEle = (DoublePoint) iterator.next();
			convertList.add(singleEle);
		}
		data.setDataList(convertList);
	}

	/**
	 * initializes interval with values
	 * 
	 * @param startTimePoint
	 *            concrete start point of data collection
	 * @return interval till the next startTimePoint
	 * @throws IllegalArgumentException
	 *             if startTimePoint is not valid
	 */
	private static int initInterval(int startTimePoint)
			throws IllegalArgumentException {
		if (startTimePoint == Calendar.MONDAY
				|| startTimePoint == Calendar.TUESDAY
				|| startTimePoint == Calendar.WEDNESDAY
				|| startTimePoint == Calendar.THURSDAY
				|| startTimePoint == Calendar.FRIDAY) {
			return Calendar.WEEK_OF_YEAR;
		} else if (startTimePoint == Calendar.JANUARY
				|| startTimePoint == Calendar.FEBRUARY
				|| startTimePoint == Calendar.MARCH
				|| startTimePoint == Calendar.APRIL
				|| startTimePoint == Calendar.MAY
				|| startTimePoint == Calendar.JUNE
				|| startTimePoint == Calendar.JULY
				|| startTimePoint == Calendar.AUGUST
				|| startTimePoint == Calendar.SEPTEMBER
				|| startTimePoint == Calendar.OCTOBER
				|| startTimePoint == Calendar.NOVEMBER
				|| startTimePoint == Calendar.DECEMBER) {
			return Calendar.YEAR;
		} else {
			logger.warn("initIntervAbstract: value for startTimePoint not supported");
			throw new IllegalArgumentException(
					"startTimePoint value not supported");
		}
	}

	/**
	 * sets value of the abstract time
	 * 
	 * @param startTimePoint
	 *            concrete start point of data collection
	 * @return abstract time value of startTimePoint
	 * @throws IllegalArgumentException
	 *             if startTimePoint is not valid
	 */
	private static int initAbstractTime(int startTimePoint)
			throws IllegalArgumentException {
		if (startTimePoint == Calendar.MONDAY
				|| startTimePoint == Calendar.TUESDAY
				|| startTimePoint == Calendar.WEDNESDAY
				|| startTimePoint == Calendar.THURSDAY
				|| startTimePoint == Calendar.FRIDAY) {
			return Calendar.DAY_OF_WEEK;
		} else if (startTimePoint == Calendar.JANUARY
				|| startTimePoint == Calendar.FEBRUARY
				|| startTimePoint == Calendar.MARCH
				|| startTimePoint == Calendar.APRIL
				|| startTimePoint == Calendar.MAY
				|| startTimePoint == Calendar.JUNE
				|| startTimePoint == Calendar.JULY
				|| startTimePoint == Calendar.AUGUST
				|| startTimePoint == Calendar.SEPTEMBER
				|| startTimePoint == Calendar.OCTOBER
				|| startTimePoint == Calendar.NOVEMBER
				|| startTimePoint == Calendar.DECEMBER) {
			return Calendar.MONTH;
		} else {
			logger.warn("initAbstractTime: value for startTimePoint not supported");
			throw new IllegalArgumentException(
					"value for startTimePoint not supported");
		}
	}

}
