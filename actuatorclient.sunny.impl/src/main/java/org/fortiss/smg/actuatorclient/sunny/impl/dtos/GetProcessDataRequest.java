package org.fortiss.smg.actuatorclient.sunny.impl.dtos;

import java.util.ArrayList;

import org.fortiss.smg.actuatorclient.sunny.impl.jsonrpc.dto.GenericRequest;
import org.fortiss.smg.actuatorclient.sunny.impl.jsonrpc.dto.JSONRPCInputParam;

public class GetProcessDataRequest extends GenericRequest{

	private ArrayList<JSONRPCInputParam> devices;

	public GetProcessDataRequest() {
		super("GetProcessData");
		devices = new ArrayList<JSONRPCInputParam>();
		getParams().put("devices", devices);
	}
	
	public GetProcessDataRequest addDevice(GetProcessDataDevice dev) {
		devices.add(dev);
		return this;
	}
	
}
