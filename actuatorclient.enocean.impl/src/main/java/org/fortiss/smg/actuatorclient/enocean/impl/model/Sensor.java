package org.fortiss.smg.actuatorclient.enocean.impl.model;

import org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.SensorStrategy;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.containermanager.api.devices.DeviceId;

public class Sensor {
	/** The sensor's EnOcean-ID from the telegrams. */
	private String id;
	private SensorStrategy strategy;
	private DeviceId deviceId ;
	private int deviceCode ;

	public Sensor(String id, SensorStrategy strategy ,DeviceId deviceId, int deviceCode) {
		super();
		this.id = id;
		this.strategy = strategy;
		this.strategy.setSensor(this);
		this.deviceId = deviceId;
		this.deviceCode = deviceCode;
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
	
	public DeviceId getDeviceId() {
		return this.deviceId;
	}

	public String getDescription() {
		return strategy.getDescription();
	}
	
	public int getDeviceCode() {
		return this.deviceCode;
	}

}
