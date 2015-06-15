/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.sunny.impl;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.Device;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.GetProcessDataDevice;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.GetProcessDataRequest;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.GetProcessDataResponse;
import org.fortiss.smg.actuatorclient.sunny.impl.jsonrpc.dto.GenericRequest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class SunnyCommunicator {
	private String host ;
	
	public SunnyCommunicator(String host) {
		super();
		this.host = host;
	}
	
	public <T> T execute(GenericRequest request, Class<T> responseType) throws ClientProtocolException, IOException {
		Gson gson = new Gson();
		
		ObjectMapper mapper = new ObjectMapper();
		String req = mapper.writeValueAsString(request);
		
		String requestString = "RPC="+gson.toJson(request);
		//String requestString = "RPC="+req;
		HttpPost post = new HttpPost("http://" + this.host+"/rpc");
		post.setEntity(new StringEntity(requestString));
		
		HttpClient client = HttpClientBuilder.create().build();
		
		String resp = client.execute(post,new BasicResponseHandler());
		
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		//T foo =  mapper.readValue(resp, responseType);

		
		T foo = gson.fromJson(resp, responseType);
		return foo;
	}

	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		GetProcessDataRequest req = new GetProcessDataRequest(); 
		req.addDevice(new GetProcessDataDevice("SBU500EI:1260014177", "BatVtg"));
		
		GetProcessDataResponse result = new SunnyCommunicator("192.168.21.203").execute(req, GetProcessDataResponse.class);
		for( Device dev :   result.getDevices()){
			System.out.println(dev.getKey() + "-" + dev.getName() + "-"+ dev.getChannels().size());
		}
		
	}
	
}
