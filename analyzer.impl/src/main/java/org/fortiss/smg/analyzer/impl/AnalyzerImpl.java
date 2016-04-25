package org.fortiss.smg.analyzer.impl;


import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.analyzer.api.AnalyzerInterface;
import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.api.NoDataFoundException;
import org.fortiss.smg.analyzer.impl.calculations.Sum;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.ArithmeticMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.ArithmeticMeanTime;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.CalculationMethods;
import org.fortiss.smg.analyzer.impl.calculations.dispersion.Maximum;
import org.fortiss.smg.analyzer.impl.calculations.dispersion.Minimum;
import org.fortiss.smg.analyzer.impl.correlation.CorrelationCalculation;
import org.fortiss.smg.analyzer.impl.databaseConnection.DatabaseRequestor;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.sqltools.lib.builder.SelectQuery;
import org.slf4j.LoggerFactory;


public class AnalyzerImpl extends Thread implements AnalyzerInterface {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(AnalyzerImpl.class);
	private boolean activator;
	private InformationBrokerInterface broker;


	public AnalyzerImpl(InformationBrokerInterface broker) {
		this.broker = broker;

	}

	@Override
	public boolean isComponentAlive() {
		// TODO Auto-generated method stub
		// Should test if for example the database connection is avaiable
		System.out.println("################CALL FROM DJANGO if Analyzer Alive!!!##################");
		return true;
	}

	@SuppressWarnings("static-access")
	public void start() {
	}

	public void stopCore() {
		activator = false;
	}

	@Override
	public Double getSum(long startTime, long stopTime, DeviceId device)
			throws NoDataFoundException, TimeoutException,
			IllegalArgumentException {
		if (startTime == 0 || stopTime == 0 || device == null) {
			logger.warn("getSum: at least one parameter == null");
			throw new IllegalArgumentException(
					"'startTime', 'stopTime' and/or 'device' were null");
		}
		Sum sum = new Sum();

		DataSet set = new DataSet(startTime, stopTime, device);
		try {
			DatabaseRequestor.fetchesDataSet(set, broker);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException("no data was found in the database");
		}

		// no catching of exception necessary - would have caused SQL exception
		// before
		return sum.calculate(set.getDataList());
	}

	@Override
	public Double getArithmeticMean(long startTime, long stopTime,
			DeviceId device) throws TimeoutException, NoDataFoundException,
			IllegalArgumentException {
		if (startTime == 0 || stopTime == 0 || device == null) {
			logger.warn("getSum: at least one parameter == null");
			throw new IllegalArgumentException(
					"'startTime', 'stopTime' and/or 'device' were null");
		}
		ArithmeticMean mean = new ArithmeticMean();

		DataSet set = new DataSet(startTime, stopTime, device);
		try {
			DatabaseRequestor.fetchesDataSet(set, broker);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException("no data was found in the database");
		}

		// no catching of exception necessary - would have caused SQL exception
		// before
		return mean.calculate(set.getDataList());
	}

	@Override
	public Double getArithmeticMeanByTime(long startTime,
			long stopTime, DeviceId device) throws TimeoutException,
			IllegalArgumentException, NoDataFoundException {
		if (startTime == 0 || stopTime == 0 || device == null) {
			logger.warn("getArithmeticMeanByTime: at least one parameter == null - returning null");
			throw new IllegalArgumentException(
					"'startTime', 'stopTime' and/or 'device' were null");
		}
		ArithmeticMeanTime meanTime = new ArithmeticMeanTime();

		DataSet set = new DataSet(startTime, stopTime, device);
		try {
			DatabaseRequestor.fetchesDataSet(set, broker);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException("no data was found in the database");
		}

		// won't cause exception: would have been caught as SQL exception
		// earlier
		return meanTime.calculate(set.getDataList());
	}

	@Override
	public Double getMax(long startTime, long stopTime, DeviceId device)
			throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		if (startTime == 0 || stopTime == 0 || device == null) {
			logger.warn("getSum: at least one parameter == null");
			throw new IllegalArgumentException(
					"'startTime', 'stopTime' and/or 'device' were null");
		}
		Maximum max = new Maximum();

		DataSet set = new DataSet(startTime, stopTime, device);
		try {
			DatabaseRequestor.fetchesDataSet(set, broker);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException("no data was found in the database");
		}
		return max.calculate(set.getDataList());
	}

	@Override
	public Double getMin(long startTime, long stopTime, DeviceId device)
			throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		if (startTime == 0 || stopTime == 0 || device == null) {
			logger.warn("getSum: at least one parameter == null");
			throw new IllegalArgumentException(
					"'startTime', 'stopTime' and/or 'device' were null");
		}
		Minimum min = new Minimum();

		DataSet set = new DataSet(startTime, stopTime, device);
		try {
			DatabaseRequestor.fetchesDataSet(set, broker);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException("no data found in the database");
		}
		return min.calculate(set.getDataList());
	}

	@Override
	public boolean[] getConsumptionRating(DataSet setCurrent,
			DataSet setReference, double percentage)
					throws IllegalArgumentException, NoDataFoundException,
					TimeoutException {
		if (setCurrent == null || setReference == null
				|| setCurrent.getStartDate() == 0
				|| setCurrent.getStopDate() == 0
				|| setCurrent.getDeviceId() == null
				|| setReference.getStartDate() == 0
				|| setReference.getStopDate() == 0
				|| setReference.getDeviceId() == null) {
			logger.warn("getConsumptionRating: at least one parameter == null, or not initialized correctly - returning null");
			throw new IllegalArgumentException(
					"at least one parameter is null or not initialized");
		}

		ArithmeticMean mean = new ArithmeticMean();
		Maximum max = new Maximum();

		try {
			DatabaseRequestor.fetchesDataSet(setReference, broker);
			DatabaseRequestor.fetchesDataSet(setCurrent, broker);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException("no data found in the database");
		}

		// exceptions of calculate won't occur - would have caused SQL exception
		// earlier
		Double currentMean = mean.calculate(setCurrent.getDataList());
		Double refMean = mean.calculate(setReference.getDataList());
		Double refMax = max.calculate(setReference.getDataList());

		boolean rating[] = new boolean[4];

		rating[0] = false;
		rating[1] = false;
		rating[2] = false;
		rating[3] = false;

		if (currentMean < refMean) {
			rating[0] = true;
		} else {
			if (currentMean <= (refMean + refMean * (percentage / 100.0))) {
				rating[1] = true;
			} else if (currentMean <= refMax) {
				rating[2] = true;
			}
			if (currentMean > refMax) {
				rating[3] = true;
			}
		}
		return rating;
	}

	@Override
	public double getCorrelationTwoDevices(DataSet xSet, DataSet ySet,
			int numberOfPoints) throws TimeoutException, NoDataFoundException {
		if(xSet == null){
			throw new IllegalArgumentException("DataSet == null is not valid!");
		}
		if(ySet == null){
			throw new IllegalArgumentException("DataSet == null is not valid!");
		}
		try {
			DatabaseRequestor.fetchesDataSet(xSet, broker);
			DatabaseRequestor.fetchesDataSet(ySet, broker);
		}  catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException("no data found in the database");
		}
		return CorrelationCalculation.pearsonCorrelation(xSet, ySet, numberOfPoints);
	}

	public long[] getfirstLastOfWeek(long unixTimeStamp){
		Calendar c = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c.setTimeInMillis(unixTimeStamp);
		int fDay, lDay, fMonth, lMonth , lYear, fYear;
		int cDay = c.get(Calendar.DAY_OF_WEEK); // returns which day of week it is , Monday =2 , Tuesday= 3 , Wednesday = 4 , ...  
		int cMonth = c.get(Calendar.MONTH); // returns number of month , it is indexed from 0 , so January is 0, February =1 , ... 
		int cYear = c.get(Calendar.YEAR); // returns which  year is it
		long[] result = new long[2];
		fMonth = lMonth = cMonth;
		lYear= fYear = cYear;
		lDay=fDay = c.get(c.DAY_OF_MONTH);

		// calculating the first day of week 
		if (cDay != 2){
			if(cDay == 1)
				fDay -= 6;
			else{
				fDay -= (cDay-2 );
			}
			if (fDay < 1) {
				c2.set(cYear,cMonth-1,lDay);
				int x = c2.getActualMaximum(Calendar.DAY_OF_MONTH); // return the last day of previous month to calculate the first day of week
				fDay = fDay +x;

				if(fMonth == 0){
					fMonth = 11;
					fYear--;
				}
				else
					fMonth--;
			}
		}
		//calculating the last day of week 
		if (cDay != 1){
			if(cDay == 6)
				lDay ++;
			else{
				lDay += (7-cDay+1); // +1 is there because we consider monday as first day of week
			}
			int lastAcceptableDay = c.getActualMaximum(Calendar.DAY_OF_MONTH); // get the date of last day of month 
			if(lDay >lastAcceptableDay){
				lDay = lDay - lastAcceptableDay;
				if(lMonth==11){
					lMonth = 0;
					lYear++;
				}else
					lMonth++;
			}
		}
		//		System.out.println(fYear+"/" + ++fMonth + "/"+ fDay +" bis "+ lYear+"/"+ ++lMonth +"/"+lDay);
		Calendar firstDay = Calendar.getInstance();
		firstDay.set(fYear, fMonth, fDay);
		Calendar lastDay = Calendar.getInstance();
		lastDay.set(lYear,lMonth,lDay);
		result[0]= firstDay.getTimeInMillis();
		result[1]= lastDay.getTimeInMillis();

		return result;
	} 

	@Override
	public double getWeeklyConsumption(String devId , String wrapperId , long unixTimeStamp) {
		long[] temp = getfirstLastOfWeek(unixTimeStamp);
		Calendar firstDay = Calendar.getInstance();
		firstDay.setTimeInMillis(temp[0]);;
		Calendar lastDay = Calendar.getInstance();
		lastDay.setTimeInMillis(temp[1]);
		DeviceId deviceId = new DeviceId(devId, wrapperId);

		return calculateConsumption(firstDay.getTimeInMillis(), lastDay.getTimeInMillis(), deviceId);
	}


	@Override
	public double getMonthlyConsumption(String devId , String wrapperId, long unixTimeStamp) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(unixTimeStamp);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		int lastDayOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(year,month,1,0,0,0);
		long firstDay=c.getTimeInMillis();
		c.set(year,month,lastDayOfMonth,23,59,59);
		long lastDay=c.getTimeInMillis();
		DeviceId deviceId = new DeviceId(devId, wrapperId);

		return  calculateConsumption(firstDay, lastDay, deviceId);

	}

	@Override
	public double getYearlyConsumption(String devId ,String wrapperId , long unixTimeStamp) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(unixTimeStamp);
		int year = c.get(Calendar.YEAR);
		DeviceId deviceId = new DeviceId(devId, wrapperId);
		c.set(year,1,1,0,0,0);
		long firstDay = c.getTimeInMillis();
		c.set(year, 12,31,23,59,59);
		long lastDay = c.getTimeInMillis();

		return  calculateConsumption(firstDay, lastDay, deviceId);

	}

	@Override
	public HashMap<Long, Double> getDailyCunsumptionOfWeek(String devId, String wrapperId, long unixTimeStamp) {

		HashMap<Long, Double> hmResult = new HashMap<Long, Double>();
		DeviceId deviceId = new DeviceId(devId, wrapperId);
		long[] temp = getfirstLastOfWeek(unixTimeStamp);
		Calendar firstDay = Calendar.getInstance();
		firstDay.setTimeInMillis(temp[0]);;
		Calendar lastDay = Calendar.getInstance();
		lastDay.setTimeInMillis(temp[1]);;
		double value = 0.0;
		Calendar temp1,temp2;

		if(firstDay.get(Calendar.MONTH) == lastDay.get(Calendar.MONTH)){
			for (int i = firstDay.get(Calendar.DATE); i <= lastDay.get(Calendar.DATE); i++) {
				temp1 = Calendar.getInstance();
				temp1.set(firstDay.get(Calendar.YEAR), firstDay.get(Calendar.MONTH), i , 0 , 0 , 0);
				temp2 = Calendar.getInstance();
				temp2.set(firstDay.get(Calendar.YEAR), firstDay.get(Calendar.MONTH), i , 23 , 59 , 59);

				value = calculateConsumption(temp1.getTimeInMillis(), temp2.getTimeInMillis(), deviceId);
				hmResult.put(temp1.getTimeInMillis(), value);
			}
		}else{
			int lastDayOfMonth = firstDay.getActualMaximum(firstDay.get(Calendar.MONTH));
			for (int i = firstDay.get(Calendar.DATE); i <= lastDayOfMonth; i++) {
				temp1 = Calendar.getInstance();
				temp1.set(firstDay.get(Calendar.YEAR), firstDay.get(Calendar.MONTH), i , 0 , 0 , 0);
				temp2 = Calendar.getInstance();
				temp2.set(firstDay.get(Calendar.YEAR), firstDay.get(Calendar.MONTH), i , 23 , 59 , 59);
				value = calculateConsumption(temp1.getTimeInMillis(), temp2.getTimeInMillis(), deviceId);
				hmResult.put(temp1.getTimeInMillis(), value);
			}
			for (int i = 1 ; i <= lastDay.DATE; i++) {
				temp1 = Calendar.getInstance();
				temp1.set(lastDay.YEAR, lastDay.MONTH, i , 0 , 0 , 0);
				temp2 = Calendar.getInstance();
				temp2.set(lastDay.YEAR, lastDay.MONTH, i , 23 , 59 , 59);
				value = 0.0;
				value = calculateConsumption(temp1.getTimeInMillis(), temp2.getTimeInMillis(), deviceId);
				hmResult.put(temp1.getTimeInMillis(), value);
			}

		}
		return hmResult;
	}

	@Override
	public HashMap<Long, Double> getDailyCunsumptionOfMonth(String devId,
			String wrapperId, long unixTimeStamp) {

		HashMap<Long,Double> hm = new HashMap<Long,Double>();
		Calendar firstDay = Calendar.getInstance();
		firstDay.setTimeInMillis(unixTimeStamp);
		int year = firstDay.get(Calendar.YEAR);
		int month = firstDay.get(Calendar.MONTH);
		int lastDayOfMonth = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH);
		Double result = 0.0;
		DeviceId deviceId = new DeviceId(devId, wrapperId);
		for (int i = 1; i <=lastDayOfMonth ; i++) {
			Calendar temp1 = Calendar.getInstance();
			temp1.set(year,month,i,0,0,0);
			Calendar temp2 = Calendar.getInstance();
			temp1.set(year,month,i,23,59,59);
			result = calculateConsumption(temp1.getTimeInMillis(), temp2.getTimeInMillis(), deviceId);
			hm.put(temp1.getTimeInMillis(), result);
		}

		return hm;
	}

	@Override
	public HashMap<Long, Double> getMonthlyCunsumptionOfYear(String devId,
			String wrapperId, long unixTimeStamp) {

		HashMap<Long, Double> hm = new HashMap<Long, Double>();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(unixTimeStamp);
		int year = c.get(Calendar.YEAR);

		for (int i = 0; i < 11; i++) {
			c.set(year, i,1,0,0,0);
			long temp = c.getTimeInMillis();
			hm.put(temp, getMonthlyConsumption(devId, wrapperId, temp));
		}

		return hm;
	}

	public Double calculateConsumption (long startDate , long lastDate ,DeviceId devId){

		List<Map<String, Object>> resultSet = null;
		double total = 0.0 ;
		List<Double> values = new ArrayList<Double>();
		List<Long> timeStamps = new ArrayList<Long>();
		String sql="";
		if(devId!=null)

			sql = "SELECT value ,timestamp FROM doubleevents WHERE devid = '"+ devId.getDevid() +"' AND timestamp >= " + startDate + " AND timestamp <= " +lastDate ;
		try {
			resultSet = broker.getSQLResults(sql);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Map<String, Object> set : resultSet) {
			values.add((Double) set.get("value"));
			timeStamps.add((Long) set.get("timestamp"));
		}

		if(resultSet!= null)
			for (int i = 0; i < values.size()-1; i++) {
				double value1 = (Double) values.get(i);
				double value2 = (Double) values.get(i+1);
				long time1 = (Long) timeStamps.get(i);
				long time2 = (Long) timeStamps.get(i+1);
				total += ((value1+value2)/2 ) * ((time2-time1)/1000) ;
			}
		
		return total ;
	}

}
