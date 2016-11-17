package org.fortiss.smg.prophet.helper.weather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WeatherOwmGenerationMapper {

	/*
	 * See for conditions http://openweathermap.org/weather-conditions
	 */
	Map<Double,ArrayList<Integer>> factorMap = new HashMap<Double,ArrayList<Integer>>();   
	
	
	public WeatherOwmGenerationMapper() {
		factorMap.put(1.0, new ArrayList<Integer>(Arrays.asList(800)));
		factorMap.put(0.95, new ArrayList<Integer>(Arrays.asList(801)));
		factorMap.put(0.85, new ArrayList<Integer>(Arrays.asList(802)));
		factorMap.put(0.75, new ArrayList<Integer>(Arrays.asList(803)));
		factorMap.put(0.55, new ArrayList<Integer>(Arrays.asList(600,615)));
		factorMap.put(0.4, new ArrayList<Integer>(Arrays.asList(300,500,600,611,615,620)));
		factorMap.put(0.2, new ArrayList<Integer>(Arrays.asList(200,804)));
		factorMap.put(0.1, new ArrayList<Integer>(Arrays.asList(301,310,501,520,601,612,616)));
	}
	
	
	public double getGenerationFactor(int weatherCode) {
		for (double key : factorMap.keySet()) {
			if (factorMap.get(key).contains(weatherCode)) {
				return key;
			}
		}
		
		return 0.0;
	}
	
	/*
	 * when clouds are considered use the mean
	 */
	public double getGenerationFactor(int weatherCode, int clouds) {
		
		for (double key : factorMap.keySet()) {
			if (factorMap.get(key).contains(weatherCode)) {
				/*
				 * weight 1:4
				 */
				//System.out.println("test " + rain);
				// 100% clouds lead to reduction of 50 %
			
					System.out.println("test " + Math.min(((key+((1-((clouds/100.0)*0.5))))/2.0),1.0));
					return Math.min(((key+((1-((clouds/100.0)*0.5))))/2.0),1.0);
				//in case rain is more than 4mm 100% clouds lead to reduction of 75 %
			//		return Math.min(((key+((1-((clouds/100.0)*0.75))))/2.0),1.0);
				
				
				
			}
		}
		
		return 0.0;
	}
	
	
}
