/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.utils;

import static org.fortiss.smg.optimizer.dao.DatabaseDao.getDatabaseDao;
import static org.fortiss.smg.optimizer.utils.Tools.dateToString;

import java.util.Date;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.optimizer.data.Specification;
import org.fortiss.smg.optimizer.impl.OptimizerImpl;
import org.slf4j.LoggerFactory;

/**
 * Utilities for exporting data.
 * 
 * @author Cheng Zhang
 * @version 1.0
 */
public class Export {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(OptimizerImpl.class);

	private static String executetime;

	public static void setExecuteTime() {
		executetime = dateToString(new Date());
	}

	public static String getExecuteTime() {
		if (executetime == null || executetime.isEmpty())
			setExecuteTime();
		return executetime;
	}

	public static void info(String message) {
		logger.info(message);
		// log.info(message);
	}

	public static void info(String message, Specification specification) {
		logger.info(message);
		try {
			getDatabaseDao()
					.writeSpecification(specification, getExecuteTime());
		} catch (TimeoutException e) {
			logger.warn("unable to store:" + message);
		}
	}

	public static void info(String message, Object objects) {
		logger.info(message);
		try {
			getDatabaseDao().writeArray(objects, getExecuteTime());
		} catch (TimeoutException e) {
			logger.warn("unable to store:" + message);
		}
	}

	public static void info(String message, double[] pool, int type,
			int operationType) {
		logger.info(message);
		getDatabaseDao().writeDB(pool, type, operationType, getExecuteTime());
	}

	public static void finer(String message) {
		logger.debug(message);
		// log.finer(message);
	}

	public static void fine(String message) {
		logger.debug(message);
		// logger.fine(message);
	}
}
