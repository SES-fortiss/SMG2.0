package org.fortiss.smg.analyzer.impl.visualization;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.fortiss.smg.informationbroker.api.DoublePoint;

public abstract class OutWriter {

	public abstract void writeDoublePoint(Map<String, List<DoublePoint>> values,
			FileOutputStream stream, String header);
	
	public abstract void writeDoublePoint(Map<String, List<DoublePoint>> values,
			FileOutputStream stream, List<String> header);
	
	public abstract void writeDouble(Map<String, List<Double>> values,
			FileOutputStream stream, List<String> header);
}
