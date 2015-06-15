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
 * provides a standard approach where a single data set is used as basis for the
 * classification function and all available data sets will then be classified
 * with this function
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class ClassifierSingle extends Classifier {

	/**
	 * all data sets that could be found in the database for the specific
	 * {@code startTimePoint}, {@code timeSpan} and {@code amount}
	 */
	private List<DataSet> allDataSets = new ArrayList<DataSet>();
	/**
	 * the classification function
	 */
	private Interpolator interpol = new Interpolator();
	/**
	 * data set which is used as basis for the classification function
	 */
	private DataSet classifyingDataSet = null;
	/**
	 * all data sets that were classified by the classification function
	 */
	private List<DataSet> similar = new ArrayList<DataSet>();

	public ClassifierSingle(DeviceId deviceID, 
			InformationBrokerInterface broker, int startTimePoint,
			int timeSpan, int amount) throws TimeoutException, SQLException {
		try {
			this.allDataSets = DatabaseRequestor.allAvailableDataSets(
					startTimePoint, timeSpan, amount, broker, deviceID);
		}  catch (TimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * checks whether the elements of {@code allAnalyzers} are classified by
	 * {@code element}
	 * 
	 * @param element
	 *            which should be used as basis for the classification function
	 * @param tolerance
	 *            the maximum deviation the points of a data set may be apart
	 *            from the points of the interpolated function. The deviation is
	 *            calculated on the average distance; use for example {@code 30}
	 *            for a tolerance of 30%
	 */
	public void classifySingle(DataSet element, int tolerance) {
		this.classifyingDataSet = element;
		List<DataSet> interpolationDataSets = new ArrayList<DataSet>();
		interpolationDataSets.add(classifyingDataSet);
		interpol.loessInterpolation(interpolationDataSets);
		this.similar = Classifier.compareAll(allDataSets, tolerance, interpol);
	}

	public List<DataSet> getAllDataSets() {
		return allDataSets;
	}

	public Interpolator getInterpol() {
		return interpol;
	}

	public DataSet getClassifyingDataSet() {
		return classifyingDataSet;
	}

	public List<DataSet> getSimilar() {
		return similar;
	}
}
