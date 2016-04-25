package org.fortiss.smg.actuatorclient.sunny.impl.dtos;

import java.util.Collection;

import org.fortiss.smg.actuatorclient.sunny.impl.jsonrpc.dto.GenericResponse;

public class GetDevicesResponse extends GenericResponse{
	private GetDevicesResult result;

	public Collection<Device> getDevices() {
		return result.devices;
	}
	
	public void setResult(GetDevicesResult result) {
		this.result = result;
	}

	private static class GetDevicesResult {
		Collection<Device> devices;
	}
}
