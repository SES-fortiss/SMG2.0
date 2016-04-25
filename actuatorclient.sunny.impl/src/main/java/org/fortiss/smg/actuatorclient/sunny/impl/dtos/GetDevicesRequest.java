package org.fortiss.smg.actuatorclient.sunny.impl.dtos;

import org.fortiss.smg.actuatorclient.sunny.impl.jsonrpc.dto.GenericRequest;

public class GetDevicesRequest extends GenericRequest{

	public GetDevicesRequest() {
		super("GetDevices");
	}
}
