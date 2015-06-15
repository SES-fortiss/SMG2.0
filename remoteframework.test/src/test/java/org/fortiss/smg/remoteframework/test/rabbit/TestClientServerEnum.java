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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerFunction;
import org.fortiss.smg.containermanager.api.devices.ContainerType;
import org.fortiss.smg.remoteframework.lib.RabbitRPCProxy;
import org.fortiss.smg.remoteframework.lib.RabbitRPCServer;
import org.fortiss.smg.remoteframework.lib.except.JsonRpcException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestClientServerEnum {

	private ContainerManagerInterface impl;
	RabbitRPCProxy<ContainerManagerInterface> client;
	ContainerManagerInterface service;
	RabbitRPCServer<ContainerManagerInterface> server;

	@Before
	public void setUp() throws IOException, JsonRpcException, TimeoutException,
			InterruptedException {
		impl = new EnumTestImpl();
		SecureRandom random = new SecureRandom();
		String queueName = "Test-Queue-"
				+ new BigInteger(130, random).toString(32);


		// destroy exisiting messages
		Thread.sleep(2000);

		server = new RabbitRPCServer<ContainerManagerInterface>(
				ContainerManagerInterface.class, impl, queueName);
		server.init();

		client = new RabbitRPCProxy<ContainerManagerInterface>(ContainerManagerInterface.class,
				queueName, 1000000);
		service = client.init();
	}

	@After
	public void tearDown() throws IOException {
		try {
		//	server.destroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (client != null) {
			client.destroy();
		}
	}

	@Test
	public void testGreeting() throws Exception {
		Container test = new Container("test", "test", ContainerType.ROOM, ContainerFunction.OFFICE, false);
		System.out.println(service.getContainer("abc"));
		assertEquals(test.getContainerFunction(),service.getContainer("abc").getContainerFunction());
	}
}


