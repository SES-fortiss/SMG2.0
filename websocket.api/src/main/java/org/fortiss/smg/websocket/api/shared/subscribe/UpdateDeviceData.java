package org.fortiss.smg.websocket.api.shared.subscribe;

import org.fortiss.smg.containermanager.api.devices.SIDeviceType;

public class UpdateDeviceData<T> {
	
	/**
	 * device with conId
	 */
	String conId;
	/**
	 * type of device
	 */
	SIDeviceType type;
	
	/**
	 * command
	 */
	double command;

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public SIDeviceType getType() {
		return type;
	}

	public void setType(SIDeviceType type) {
		this.type = type;
	}

	public double getCommand() {
		return command;
	}

	public void setCommand(double command) {
		this.command = command;
	}

	public UpdateDeviceData(String conId, SIDeviceType type, double command) {
		super();
		this.conId = conId;
		this.type = type;
		this.command = command;
	}

	public UpdateDeviceData() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}
