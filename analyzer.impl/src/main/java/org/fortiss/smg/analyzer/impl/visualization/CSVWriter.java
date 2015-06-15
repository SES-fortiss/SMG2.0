/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl.visualization;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.fortiss.smg.informationbroker.api.DoublePoint;

public class CSVWriter extends OutWriter {

	// TODO: remove if not needed anymore
	// saved old version from Sebastian
	// public void write(Map<String, List<DoubleDoublePointNew>> list,
	// FileOutputStream stream, String header) {
	//
	// BufferedWriter writer = null;
	// OutputStreamWriter ow = null;
	// try {
	// ow = new OutputStreamWriter(stream);
	// writer = new BufferedWriter(ow);
	// // header
	// writer.write(header);
	// for (DoubleDoublePointNew entry : list.entrySet().iterator().next()
	// .getValue()) {
	// writer.write("," + entry.getUnit().getDevid());
	// }
	// writer.newLine();
	//
	// // points
	// for (Entry<String, List<DoubleDoublePointNew>> entry : list
	// .entrySet()) {
	// writer.write(entry.getKey() + "");
	// for (DoubleDoublePointNew point : entry.getValue()) {
	// writer.write("," + String.format("%.4f", point.getValue()));
	// }
	// writer.newLine();
	// }
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// try {
	// writer.close();
	// ow.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// }
	/**
	 * writes the given data into a file.
	 * 
	 * @param values
	 *            key elements are treated as titles for the following list of
	 *            values
	 * @param stream
	 *            output stream
	 * @param header
	 *            provides a headline for key elements and each row of values
	 */
	@Override
	public void writeDouble(Map<String, List<Double>> values,
			FileOutputStream stream, List<String> header) {
		// values
		LinkedHashMap<String, List<String>> convertedMap = new LinkedHashMap<String, List<String>>();
		for (Entry<String, List<Double>> entry : values.entrySet()) {
			List<String> convString = new ArrayList<String>();
			for (Double valueDouble : entry.getValue()) {
				convString.add(String.format("%.4f", valueDouble));
			}
			convertedMap.put(entry.getKey(), convString);
		}
		writeGeneral(convertedMap, stream, header);
	}

	/**
	 * writes the given data into a file.
	 * 
	 * @param values
	 *            key elements are treated as titles for the following list of
	 *            values; the values are the values of each
	 *            {@code DoubleDoublePointNew}
	 * @param stream
	 *            output stream
	 * @param header
	 *            provides a headline for key elements and each row of values
	 */
	@Override
	public void writeDoublePoint(Map<String, List<DoublePoint>> values,
			FileOutputStream stream, List<String> header) {
		LinkedHashMap<String, List<String>> convertedValues = extractDoubleValues(values);
		writeGeneral(convertedValues, stream, header);
	}

	/**
	 * writes given data into a file
	 * 
	 * @param values
	 *            key elements are treated as titles for the following list of
	 *            values; the values are the values of each
	 *            {@code DoubleDoublePointNew}
	 * @param stream
	 *            output stream
	 * @param header
	 *            provides a headline for key elements; 
	 */
	@Override
	public void writeDoublePoint(Map<String, List<DoublePoint>> values,
			FileOutputStream stream, String header) {
		// fill header
		List<String> headerList = new ArrayList<String>();
		headerList.add(header);
		
		LinkedHashMap<String, List<String>> stringValueMap = extractDoubleValues(values);

		writeGeneral(stringValueMap, stream, headerList);
	}

	/**
	 * converts the {@code List<DoubleDoublePoint>} elements into
	 * {@code List<String>} elements by extracting the value of each
	 * {@code DoubleDoublePoint}; the key elements are just copied without
	 * modification
	 * 
	 * @param values
	 *            should be converted
	 * @return map with extracted values from {@code DoubleDoublePoints}
	 * @see #writeDoublePoint(Map, FileOutputStream, String)
	 * @see #writeDoublePoint(Map, FileOutputStream, List)
	 */
	private LinkedHashMap<String, List<String>> extractDoubleValues(
			Map<String, List<DoublePoint>> values) {
		// fill values
		LinkedHashMap<String, List<String>> stringValueMap = new LinkedHashMap<String, List<String>>();
		// iterates over the complete entries of the map
		for (Entry<String, List<DoublePoint>> entry : values.entrySet()) {
			List<String> stringValueList = new ArrayList<String>();
			for (DoublePoint doubleEntry : entry.getValue()) {
				if (doubleEntry.getValue() == null) {
					stringValueList.add(" ");
				} else {
					stringValueList.add(String.format("%.4f",
							doubleEntry.getValue()));
				}
			}
			stringValueMap.put(entry.getKey(), stringValueList);
		}
		return stringValueMap;
	}

	/**
	 * writes the given data into a file.
	 * 
	 * @param values
	 *            key elements are treated as titles for the following list of
	 *            values
	 * @param stream
	 *            output stream
	 * @param header
	 *            provides a headline for key elements and each row of values
	 */
	private void writeGeneral(Map<String, List<String>> values,
			FileOutputStream stream, List<String> header) {

		BufferedWriter writer = null;
		OutputStreamWriter ow = null;
		try {
			ow = new OutputStreamWriter(stream);
			writer = new BufferedWriter(ow);

			// header
			int counter = 0;
			for (String entry : header) {
				if (counter == 0) {
					writer.write(entry);
				} else {
					writer.write("," + entry);
				}
				counter++;
			}
			writer.newLine();

			// points
			for (Entry<String, List<String>> entry : values.entrySet()) {
				writer.write(entry.getKey() + "");
				for (String point : entry.getValue()) {
					if(point.equals("null")){
						writer.write(",");
					}else{
					writer.write("," + point);
				}}
				writer.newLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				ow.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
