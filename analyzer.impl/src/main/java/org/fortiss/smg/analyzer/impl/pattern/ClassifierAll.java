/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl.pattern;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.databaseConnection.DatabaseRequestor;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;

/**
 * provides a standard approach if all available data sets should be merged to a
 * classification function and then classified with that function
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class ClassifierAll extends Classifier {

	/**
	 * all data sets that could be found in the database for the specific
	 * {@code startTimePoint}, {@code timeSpan} and {@code amount}
	 */
	private List<DataSet> allDataSets = new ArrayList<DataSet>();
	/**
	 * all data sets that were classified by the classification function
	 */
	private List<DataSet> similar = new ArrayList<DataSet>();
	/**
	 * the classification function
	 */
	private Interpolator interpol = new Interpolator();

	public ClassifierAll(DeviceId deviceID, 
			InformationBrokerInterface broker, int startTimePoint,
			int timeSpan, int amount, int tolerance) throws SQLException, TimeoutException {
		try {
			this.allDataSets = DatabaseRequestor.allAvailableDataSets(
					startTimePoint, timeSpan, amount, broker, deviceID);
		} catch (TimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		interpol.loessInterpolation(allDataSets);
		this.similar = Classifier.compareAll(allDataSets, tolerance, interpol);
	}

	public List<DataSet> getAllDataSets() {
		return allDataSets;
	}

	public List<DataSet> getSimilar() {
		return similar;
	}

	public Interpolator getInterpol() {
		return interpol;
	}

}
