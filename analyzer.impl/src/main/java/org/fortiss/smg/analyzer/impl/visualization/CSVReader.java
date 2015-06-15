/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.analyzer.impl.visualization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class CSVReader {

	public static LinkedHashMap<String,String[]> read(String fileLocation){
		BufferedReader reader = null;
		String line = "";
		String csvSplitBy = ",";
		
		LinkedHashMap<String, String[]> foundData = new LinkedHashMap<String, String[]>();
		

		try {
			reader = new BufferedReader(new FileReader(fileLocation));
			while((line = reader.readLine()) != null){
				//use comma as separator
				String[] singleEntry = line.split(csvSplitBy);
				String header = singleEntry[0];
				String[] elementsWithoutHeader = new String[singleEntry.length-1];
				for(int i = 1; i<singleEntry.length; i++){
					elementsWithoutHeader[i-1] = singleEntry[i];
				}
				foundData.put(header, elementsWithoutHeader);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return foundData;
	}
}
