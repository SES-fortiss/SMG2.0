/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
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
