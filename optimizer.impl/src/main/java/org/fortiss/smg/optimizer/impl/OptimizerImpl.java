/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.impl;

import static org.fortiss.smg.optimizer.dao.DatabaseDao.getDatabaseDao;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.informationbroker.api.IDatabase;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.optimizer.advisor.Advisor;
import org.fortiss.smg.optimizer.advisor.Suggestion;
import org.fortiss.smg.optimizer.api.OptimizerInterface;
import org.fortiss.smg.optimizer.comparison.Dummy;
import org.fortiss.smg.optimizer.comparison.No;
import org.fortiss.smg.optimizer.data.Interval;
import org.fortiss.smg.optimizer.data.Specification;
import org.fortiss.smg.optimizer.utils.Evaluation;
import org.fortiss.smg.optimizer.utils.Export;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.slf4j.LoggerFactory;

public class OptimizerImpl implements OptimizerInterface {

	/** Logger */
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(OptimizerImpl.class);

	/** Database broker */
	private IDatabase database;

	/** Set database broker */
	public void setDatabase(IDatabase database) {
		this.database = database;
		getDatabaseDao().setDatabase(this.database);
	}

	/** Default timeout */
	private static final int TIMEOUTLONG = 10000;

	/** Intervals */
	private Interval[] intervals;

	/** Default API */
	public String doSomething(String s) {
		return "Hello Optimizer";
	}

	/** Generate optimized schedule with the dummy advisor */
	public String doDummyOptimize() {
		Interval[] newIntervals = Interval.copy(intervals);
		Dummy.doOptimize(newIntervals);
		Export.setExecuteTime();
		Export.info("Dummy intervals:" + Arrays.toString(newIntervals),
				newIntervals);
		return "Successful do dummy optimize";
	}

	/** Generate schedule without optimization */
	public String doNoOptimize() {
		Interval[] newIntervals = Interval.copy(intervals);
		No.update(newIntervals);
		Export.setExecuteTime();
		Export.info("No intervals:" + Arrays.toString(newIntervals),
				newIntervals);
		return "Successful do no optimize";
	}

	/** Generate optimized schedule and suggestion */
	public String doOptimize() {
		if (intervals != null && intervals.length > 0) {
			Advisor.doOptimize(intervals);
			Suggestion.getSuggestion(intervals, Evaluation.cost(intervals));
			OptimizerImpl.logger.info("Optimized Intervals:"
					+ Arrays.toString(intervals));
			return "Successful do optimize";
		}
		return "Successful do optimize without data";
	}

	/** Generate optimized schedule and suggestion with a given start date */
	public String doOptimize(Date start) {
		loadIntervals(start);
		return doOptimize();
	}

	/**
	 * Constructor with given date, duration, consumption, generation, price of
	 * intervals and specification in the SMG
	 */
	public OptimizerImpl(String[] date, double duration, double[] consumption,
			double[] generation, double[] price, double chargeEfficiency,
			double dischargeEfficiency, double maximumCapacity,
			double initialCapacity, double maximumLineLoad,
			double maximumPowerOutput, double maximumPowerInput,
			boolean possibleFromBattery, boolean possibleFromGeneration) {
		this();
		// Generate intervals
		this.intervals = Interval.toIntervals(date, duration, consumption,
				generation, price);
		// Configuration of SMG
		Specification.getSpecification().setSpecification(chargeEfficiency,
				dischargeEfficiency, maximumCapacity, initialCapacity,
				maximumLineLoad, maximumPowerOutput, maximumPowerInput,
				possibleFromBattery, possibleFromGeneration);
	}

	/** Constructor */
	public OptimizerImpl() {
		DefaultProxy<InformationBrokerInterface> clientInfo = new DefaultProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(),
				OptimizerImpl.TIMEOUTLONG);
		try {
			setDatabase(clientInfo.init());
		} catch (IOException e) {
			OptimizerImpl.logger.warn("IO error", e);
		} catch (TimeoutException e) {
			OptimizerImpl.logger.warn("SQL Statement error", e);
		}
	}

	/** Constructor with a given start date */
	public OptimizerImpl(Date start) {
		this();
		loadIntervals(start);
	}

	/** load intervals with a given start date */
	private void loadIntervals(Date start) {
		try {
			intervals = getDatabaseDao().loadIntervals(start);
			getDatabaseDao().loadSpecification(null);
		} catch (TimeoutException e) {
			OptimizerImpl.logger.warn("SQL Statement error", e);
		}
	}

	@Override
	public boolean isComponentAlive() {
		return false;
	}
}
