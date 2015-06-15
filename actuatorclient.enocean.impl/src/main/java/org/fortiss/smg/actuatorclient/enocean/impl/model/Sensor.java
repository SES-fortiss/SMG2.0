/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.model;

import org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.SensorStrategy;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;

public class Sensor {
	/** The sensor's EnOcean-ID from the telegrams. */
	private String id;
	private SensorStrategy strategy;


	public Sensor(String id, SensorStrategy strategy) {
		super();
		this.id = id;
		this.strategy = strategy;
		this.strategy.setSensor(this);
	}

	/**
	 * The wrapper receives a telegram and look for the Sensor-Object that matches the ID in
	 * the telegram - handleIncomingTelegram then receives the telegram to be processed
	 * 
	 * Every Sensor simply redirects the telegram to it's strategy for processing it there.
	 * 
	 * @param telegram
	 */
	public void handleIncomingTelegram(UniversalTelegram telegram) {
		strategy.handleTelegram(telegram);
	}
	
	public SensorStrategy getStrategy() {
		return strategy;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return strategy.getDescription();
	}


}
