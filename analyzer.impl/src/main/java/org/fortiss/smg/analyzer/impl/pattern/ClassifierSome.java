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
 * provides a standard approach where some data sets are used as basis for the
 * classification function and all available data sets will then be classified
 * with this function
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class ClassifierSome extends Classifier {

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
	private List<DataSet> classifyingDataSet = new ArrayList<DataSet>();
	/**
	 * all data sets that were classified by the classification function
	 */
	private List<DataSet> similar = new ArrayList<DataSet>();

	public ClassifierSome(DeviceId deviceID,
			InformationBrokerInterface broker, int startTimePoint,
			int timeSpan, int amount) throws TimeoutException, SQLException {
		try {
			this.allDataSets = DatabaseRequestor.allAvailableDataSets(
					startTimePoint, timeSpan, amount,  broker, deviceID);
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
	 * {@code elements}
	 * 
	 * @param elements
	 *            which should be used as classifier
	 * @param tolerance
	 *            the maximum deviation the points of a data set may be apart
	 *            from the points of the interpolated function. The deviation is
	 *            calculated on the average distance; use for example {@code 30}
	 *            for a tolerance of 30%
	 */
	public void classifySome(List<DataSet> elements, int tolerance) {
		this.classifyingDataSet.addAll(elements);
		interpol.loessInterpolation(elements);
		this.similar = Classifier.compareAll(allDataSets, tolerance, interpol);
	}

	public List<DataSet> getAllDataSets() {
		return allDataSets;
	}

	public Interpolator getInterpol() {
		return interpol;
	}

	public List<DataSet> getClassifyingDataSet() {
		return classifyingDataSet;
	}

	public List<DataSet> getSimilar() {
		return similar;
	}

}
