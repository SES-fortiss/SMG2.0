package org.fortiss.smg.analyzer.api;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.containermanager.api.devices.DeviceId;


public interface AnalyzerInterface extends HealthCheck {

	/**
	 * Prerequisite: Use valid initialization for the Calendar objects and
	 * Device (not null). <br>
	 * Calculates the sum of the values found in the database from
	 * {@code startTime} till {@code stopTime} for a specific device
	 * 
	 * @param startTime
	 *            the time when the data collection should start
	 * @param stopTime
	 *            the time when the data collection should stop
	 * @param device
	 *            the device, from which data should be collected
	 * @return the sum of all values found
	 * @throws NoDataFoundException
	 *             no data was found in the database
	 * @throws TimeoutException
	 *             connection to database timed out
	 * @throws IllegalArgumentException
	 *             if {@code startTime, stopTime and/or device} is null
	 */
	public Double getSum(long startTime, long stopTime, DeviceId device)
			throws NoDataFoundException, TimeoutException,
			IllegalArgumentException;

	/**
	 * Prerequisite: Use valid initialization for the Calendar objects and
	 * Device (not null). <br>
	 * Calculates the arithmetic mean for of the values found in the database
	 * from {@code startTime} till {@code stopTime} for a specific device
	 * 
	 * @param startTime
	 *            the time when the data collection should start
	 * @param stopTime
	 *            the time when the data collection should stop
	 * @param device
	 *            the device, from which data should be collected
	 * @return the arithmetic mean of all values found
	 * @throws NoDataFoundException
	 *             no data was found in the database
	 * @throws TimeoutException
	 *             connection to database timed out
	 * @throws IllegalArgumentException
	 *             if {@code startTime, stopTime and/or device} is null
	 */
	public Double getArithmeticMean(long startTime, long stopTime,
			DeviceId device) throws NoDataFoundException, TimeoutException,
			IllegalArgumentException;

	/**
	 * Prerequisite: Use valid initialization for the Calendar objects and
	 * Device (not null). <br>
	 * Calculates the arithmetic mean over time (like an integral) for of the
	 * values found in the database from {@code startTime} till {@code stopTime}
	 * for a specific device; the time span used is 1 hour. This function can be
	 * used for calculation for example watt hours.
	 * 
	 * @param startTime
	 *            the time when the data collection should start
	 * @param stopTime
	 *            the time when the data collection should stop
	 * @param device
	 *            the device, from which data should be collected
	 * @return the arithmetic mean over time of all values found
	 * @throws NoDataFoundException
	 *             no data was found in the database
	 * @throws TimeoutException
	 *             connection to database timed out
	 * @throws IllegalArgumentException
	 *             if {@code startTime, stopTime and/or device} is null
	 */
	public Double getArithmeticMeanByTime(long startTime,
			long stopTime, DeviceId device) throws NoDataFoundException,
			TimeoutException, IllegalArgumentException;

	/**
	 * Prerequisite: Use valid initialization for the Calendar objects and
	 * Device (not null). <br>
	 * Calculates the maximal value of the values found in the database from
	 * {@code startTime} till {@code stopTime} for a specific device
	 * 
	 * @param startTime
	 *            the time when the data collection should start
	 * @param stopTime
	 *            the time when the data collection should stop
	 * @param device
	 *            the device, from which data should be collected
	 * @return the maximum value of all values found
	 * @throws NoDataFoundException
	 *             no data was found in the database
	 * @throws TimeoutException
	 *             connection to database timed out
	 * @throws IllegalArgumentException
	 *             if {@code startTime, stopTime and/or device} is null
	 */
	public Double getMax(long startTime, long stopTime, DeviceId device)
			throws NoDataFoundException, TimeoutException,
			IllegalArgumentException;

	/**
	 * Prerequisite: Use valid initialization for the Calendar objects and
	 * Device (not null). <br>
	 * Calculates the minimal value of the values found in the database from
	 * {@code startTime} till {@code stopTime} for a specific device
	 * 
	 * @param startTime
	 *            the time when the data collection should start
	 * @param stopTime
	 *            the time when the data collection should stop
	 * @param device
	 *            the device, from which data should be collected
	 * @return the minimal value of all values found
	 * @throws NoDataFoundException
	 *             no data was found in the database
	 * @throws TimeoutException
	 *             connection to database timed out
	 * @throws IllegalArgumentException
	 *             if {@code startTime, stopTime and/or device} is null
	 */
	public Double getMin(long startTime, long stopTime, DeviceId device)
			throws NoDataFoundException, TimeoutException,
			IllegalArgumentException;

	/**
	 * Prerequisite: initialize the data sets with not null Calendar objects. <br>
	 * Evaluates the energy consumption of a data set to another; this is used
	 * to rate a consumption as low, normal, high or extreme in comparison to
	 * another data set
	 * 
	 * @param setCurrent
	 *            the data set, which is compared
	 * @param setReference
	 *            the data set, which is used as reference point
	 * @param percentage
	 *            a percentage to distinguish between normal and high
	 *            consumption; use for example 30.0 for 30%; <br>
	 *            be careful to choose a reasonable value here! An indication
	 *            for a meaningless percentage can (not must!) be a {@code true}
	 *            value in more than one field of the array. EXAMPLE: if you
	 *            choose the percentage too high, the current set might fulfill
	 *            both, case2 and case4 (see below): the mean consumption is
	 *            higher then the maximal reference consumption, but also lower
	 *            then the mean reference threshold. This could be either due to
	 *            a minimal difference in the values of both sets, or a too high
	 *            percentage.
	 * @return an array representing the rating of a data set. The array has the
	 *         size 4, with
	 *         <ul>
	 *         <li>array[0] == true -> low
	 *         <li>array[1] == true -> normal
	 *         <li>array[2] == true -> high
	 *         <li>array[3] == true -> extreme consumption.
	 *         </ul>
	 *         The fields are assigned according to this differentiation:
	 *         <ul>
	 *         <li>CASE1: {@code setCurrent.mean < setReference.mean} ->
	 *         array[0] = true (low consumption)
	 *         <li>CASE2:
	 *         {@code setReference.mean <= setCurrent.mean <= threshold (= setReference.mean*(1 + percentage))}
	 *         -> array[1] = true (normal consumption)
	 *         <li>CASE3:
	 *         {@code threshold < setCurrent.mean <= setReference.max} ->
	 *         array[2] = true (high consumption)
	 *         <li>CASE4: {@code setReference.max < setCurrent.mean} -> array[3]
	 *         = true (extreme consumption)
	 *         </ul>
	 *         Case1 and case3 can appear together, all other cases are
	 *         distinct.
	 * @throws NoDataFoundException
	 *             no data was found in the database
	 * @throws TimeoutException
	 *             connection to database timed out
	 * @throws IllegalArgumentException
	 *             if {@code setCurrent and/or setReference} are null or don't
	 *             provide any data, or start/stop dates are null, or devices
	 *             are null
	 */
	public boolean[] getConsumptionRating(DataSet setCurrent,
			DataSet setReference, double percentage)
			throws NoDataFoundException, TimeoutException,
			IllegalArgumentException;

	/**
	 * Calculates the correlation coefficient for two data sets according to
	 * pearson correlation. The function is symmetric, so it doesn't matter
	 * which set is the x- and ySet. Both data sets are representing attributes
	 * that are compared in terms of their correlation. <br>
	 * Choose the start and stop time of both intervals in a way, such that the
	 * intervals have the same length (for example a day).
	 * 
	 * 
	 * @param xSet
	 *            represents one attribute. Use the constructor
	 *            DataSet(Calendar, Calendar, deviceId); the data list will be
	 *            fetched in this method, so you don't need to worry about it
	 * @param ySet
	 *            represents the other attribute. Use the constructor
	 *            DataSet(Calendar, Calendar, deviceId); the data list will be
	 *            fetched in this method, so you don't need to worry about it
	 * @param numberOfPoints
	 *            defines how many points should be compared for the correlation
	 *            analysis
	 * @return the correlation coefficient
	 * @throws TimeoutException
	 *             connection to database timed out
	 * @throws NoDataFoundException
	 *             no data was found in the database
	 * @throws IllegalArgumentException
	 *             if data sets are null
	 */
	public double getCorrelationTwoDevices(DataSet xSet, DataSet ySet,
			int numberOfPoints) throws TimeoutException, NoDataFoundException,
			IllegalArgumentException;
	
	
	
	/**
	 * Get weekly consumption of device
	 * 
	 * @param  String DeviceID
	 * @param long unixTimeStamp
	 * @param String WrapperTag	        
	 *            
	 * @return double value 
	 */
	
	public double getWeeklyConsumption(String devId , String wrapperId , long unixTimeStamp) ;
	
	/**
	 * Get daily consumption of device in whole week that contains the given date
	 * 
	 * @param DeviceID
	 * @param Calendar date	 in Millis     
	 * @param String WrapperTag	   
	 * 
	 * @return HashMap<Double,Long> value 
	 */
	public HashMap<Long,Double> getDailyCunsumptionOfWeek(String devId , String wrapperId , long unixTimeStamp);
	
	/**
	 * Get  monthly consumption of device
	 * 
	 * @param DeviceID
	 * @param Long date        
	 * @param String WrapperTag	
	 *            
	 * @return double value 
	 */
	

	public double getMonthlyConsumption(String devId , String wrapperId, long unixTimeStamp);

	/**
	 * Get daily consumption of device in whole Month that contains the given date
	 * 
	 * @param DeviceID
	 * @param long  date	 in Millis       
	 * @param String WrapperTag	   
	 * 
	 * @return HashMap<Double,Long> value 
	 */
	
	public HashMap<Long,Double> getDailyCunsumptionOfMonth(String devId , String wrapperId , long unixTimeStamp);
	
	
	/**
	 * Get yearly consumption of device 
	 * 
	 * @param DeviceID 
	 * @param String WrapperTag	
	 * @param Long  date
	 * 	        
	 *            
	 * @return double value 
	 */
	
	public double getYearlyConsumption(String devId , String wrapperId,  long unixTimeStamp);
	
	
	/**
	 * Get monthly consumption of device in whole year that contains the given date
	 * 
	 * @param DeviceID
	 * @param long date	 in Millis     
	 * @param String WrapperTag	   
	 * 
	 * @return HashMap<Double,Long> value 
	 */
	public HashMap<Long,Double> getMonthlyCunsumptionOfYear(String devId , String wrapperId , long unixTimeStamp);
	
	
}


