package org.fortiss.smg.actuatorclient.enocean.impl.model;

import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.ActorStrategy;

public class Actor {
	/** The device will send this channel. Can be 0-127 */
	private int channel;

	private ActorStrategy strategy;
	private DeviceId deviceId;
	private String id;

	public Actor(int channel, ActorStrategy strategy , DeviceId deviceId ) {
		this.channel = channel;
		this.strategy = strategy;
		this.strategy.setActor(this);
		this.deviceId = deviceId;
	}

	public int getChannel() {
		return this.channel;
	}

	public DeviceId getDeviceId() {
		return this.deviceId;
	}

	
	/**
	 * Every COMMAND to an Actuator is redirected to it's strategy! See
	 * Strategies for Actuator-functionality
	 * 
	 * @param valueStr
	 * @param internalID
	 * @param delay
	 * @param tag
	 * @param execute
	 * @param valueIdentifier
	 */
	public void setString(String valueStr, String internalID, int delay, String tag, boolean execute,
			String valueIdentifier) {
		strategy.setString(valueStr, internalID, delay, tag, execute, valueIdentifier);
	}

	/**
	 * Every COMMAND to an Actuator is redirected to it's strategy! See
	 * Strategies for Actuator-functionality
	 * 
	 * @param internalIDToggle
	 * @param delay
	 * @param tag
	 * @param execute
	 * @param valueIdentifier
	 */
	public void toggle(String internalIDToggle, int delay, String tag, boolean execute, String valueIdentifier) {
		strategy.toggle(internalIDToggle, delay, tag, execute, valueIdentifier);
	}

	/**
	 * Every COMMAND to an Actuator is redirected to it's strategy! See
	 * Strategies for Actuator-functionality
	 * 
	 * @param valueBool
	 * @param internalID
	 * @param delay
	 * @param tag
	 * @param execute
	 * @param valueIdentifier
	 */
	public void setBoolean(boolean valueBool, String internalID, int delay, String tag, boolean execute,
			String valueIdentifier) {
		strategy.setBoolean(valueBool, internalID, delay, tag, execute, valueIdentifier);
	}

	/**
	 * Every COMMAND to an Actuator is redirected to it's strategy! See
	 * Strategies for Actuator-functionality
	 * 
	 * @param valueDbl
	 * @param unit
	 * @param internalID
	 * @param delay
	 * @param tag
	 * @param execute
	 * @param valueIdentifier
	 */
	public void setDouble(double valueDbl, SIUnitType unit, String internalID, int delay, String tag, boolean execute,
			String valueIdentifier) {
		strategy.setDouble(valueDbl, unit, internalID, delay, tag, execute, valueIdentifier);
	}

	/**
	 * Every COMMAND to an Actuator is redirected to it's strategy! See
	 * Strategies for Actuator-functionality
	 * 
	 * @param internalIDStop
	 * @param delay
	 * @param tag
	 * @param execute
	 */
	public void stopLastCmd(String internalIDStop, int delay, String tag, boolean execute) {
		strategy.stopLastCmd(internalIDStop, delay, tag, execute);
	}

	public ActorStrategy getStrategy() {
		return strategy;
	}

	public String getDescription() {
		return strategy.getDescription();
	}

	public void setId(String string) {
		this.id = string;
	}

	public String getId() {
		return id;
	}

}
