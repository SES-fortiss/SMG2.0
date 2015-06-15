/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.annotation.XmlRootElement;

import org.fortiss.smg.analyzer.api.AnalyzerInterface;
import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.api.NoDataFoundException;
import org.fortiss.smg.analyzer.impl.calculations.Sum;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.ArithmeticMean;
import org.fortiss.smg.analyzer.impl.calculations.centralTendency.ArithmeticMeanTime;
import org.fortiss.smg.analyzer.impl.calculations.dispersion.Maximum;
import org.fortiss.smg.analyzer.impl.calculations.dispersion.Minimum;
import org.fortiss.smg.analyzer.impl.correlation.CorrelationCalculation;
import org.fortiss.smg.analyzer.impl.databaseConnection.DatabaseRequestor;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.slf4j.LoggerFactory;

/**
 * implements the interface
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */

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
		return false;
	}

	@SuppressWarnings("static-access")
	public void start() {
	}

	public void stopCore() {
		activator = false;
	}

	@Override
	public Double getSum(Calendar startTime, Calendar stopTime, DeviceId device)
			throws NoDataFoundException, TimeoutException,
			IllegalArgumentException {
		if (startTime == null || stopTime == null || device == null) {
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
	public Double getArithmeticMean(Calendar startTime, Calendar stopTime,
			DeviceId device) throws TimeoutException, NoDataFoundException,
			IllegalArgumentException {
		if (startTime == null || stopTime == null || device == null) {
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
	public Double getArithmeticMeanByTime(Calendar startTime,
			Calendar stopTime, DeviceId device) throws TimeoutException,
			IllegalArgumentException, NoDataFoundException {
		if (startTime == null || stopTime == null || device == null) {
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
	public Double getMax(Calendar startTime, Calendar stopTime, DeviceId device)
			throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		if (startTime == null || stopTime == null || device == null) {
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
	public Double getMin(Calendar startTime, Calendar stopTime, DeviceId device)
			throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		if (startTime == null || stopTime == null || device == null) {
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
				|| setCurrent.getStartDate() == null
				|| setCurrent.getStopDate() == null
				|| setCurrent.getDeviceId() == null
				|| setReference.getStartDate() == null
				|| setReference.getStopDate() == null
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
}
