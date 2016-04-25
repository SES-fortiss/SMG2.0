package org.fortiss.smg.ambulance.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.LoggerFactory;

public class AmbulanceLooper implements Runnable  {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(AmbulanceLooper.class);
	
	AmbulanceImpl ambulance;
	
	public AmbulanceLooper(AmbulanceImpl ambulance) {
		this.ambulance = ambulance;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		Map<String, Boolean> components = this.ambulance.checkSystem();
		int counter = 0;
		for (String queue : components.keySet()) {
			if (!components.get(queue)) {
				counter++;
				/**
				 * heal
				 */
				logger.debug("Start healing for: " + queue + " returned " + components.get(queue) );
			}
			
		}
		if (components.size() == 0) {
			logger.info("No registered components yet");
		}
		else if (counter == 0) {
			logger.info("SMG System is up and running - no Problems detected");
		}
		
	}

	
	
	
}
