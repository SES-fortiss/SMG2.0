/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.remoteframework.test.rabbit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.remoteframework.lib.RabbitRPCProxy;
import org.fortiss.smg.remoteframework.lib.RabbitRPCServer;
import org.fortiss.smg.remoteframework.lib.except.JsonRpcException;
import org.fortiss.smg.remoteframework.test.rabbit.schemas.JSONTestImpl;
import org.fortiss.smg.remoteframework.test.rabbit.schemas.JSONTestInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestClientServerTimeOut {

	private JSONTestInterface service;
	private JSONTestImpl impl;
	private RabbitRPCServer<JSONTestInterface> server;
	private RabbitRPCProxy<JSONTestInterface> client;

	@Before
	public void setUp() throws IOException, JsonRpcException, TimeoutException, InterruptedException{
	
		impl = new JSONTestImpl();
		String queueName = "Test-Queue-Time";
		
		server = new RabbitRPCServer<JSONTestInterface>(
				JSONTestInterface.class, impl, queueName);
		server.init();
		
		// destroy exisiting messages
		Thread.sleep(2000);

		client = new RabbitRPCProxy<JSONTestInterface>(JSONTestInterface.class, queueName, 1000);
		service = client.init();
	}

	@After
	public void tearDown() throws IOException {
		try {
			server.destroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(client != null){
		client.destroy();
		}
	}

	
	@Test
	public void testDummyCall() throws TimeoutException {
		assertEquals(impl.dummyCall(), service.dummyCall());
	}

	@Test (expected = TimeoutException.class)
	public void testTimeOut() throws TimeoutException {
		service.timeOut(600);
	}


}
