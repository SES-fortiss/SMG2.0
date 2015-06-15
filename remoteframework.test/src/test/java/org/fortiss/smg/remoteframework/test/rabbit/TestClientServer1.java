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

import org.fortiss.smg.remoteframework.lib.RabbitRPCProxy;
import org.fortiss.smg.remoteframework.lib.RabbitRPCServer;
import org.fortiss.smg.remoteframework.lib.except.JsonRpcException;
import org.fortiss.smg.remoteframework.test.rabbit.schemas.Dog;
import org.fortiss.smg.remoteframework.test.rabbit.schemas.DoubleDataPoint;
import org.fortiss.smg.remoteframework.test.rabbit.schemas.JSONTestImpl;
import org.fortiss.smg.remoteframework.test.rabbit.schemas.JSONTestInterface;
import org.fortiss.smg.remoteframework.test.rabbit.schemas.Zoo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestClientServer1 {

	private JSONTestImpl impl;
	RabbitRPCProxy<JSONTestInterface> client;
	JSONTestInterface service;
	RabbitRPCServer<JSONTestInterface> server;

	@Before
	public void setUp() throws IOException, JsonRpcException, TimeoutException,
			InterruptedException {
		impl = new JSONTestImpl();
		SecureRandom random = new SecureRandom();
		String queueName = "Test-Queue-"
				+ new BigInteger(130, random).toString(32);


		// destroy exisiting messages
		Thread.sleep(2000);

		server = new RabbitRPCServer<JSONTestInterface>(
				JSONTestInterface.class, impl, queueName);
		server.init();

		client = new RabbitRPCProxy<JSONTestInterface>(JSONTestInterface.class,
				queueName, 1000);
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
		if (client != null) {
			client.destroy();
		}
	}

	@Test
	public void testGreeting() throws TimeoutException {
		assertEquals(impl.greeting("hi"), service.greeting("hi"));
	}

	@Test
	public void testGetMeBack() {
		assertEquals(impl.getMeBack(10), service.getMeBack(10));
	}

	@Test
	public void testSum() throws TimeoutException {
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(1);
		numbers.add(2);
		numbers.add(3);
		assertEquals(impl.sum(numbers), service.sum(numbers));
	}

	@Test
	public void testGetValues() throws TimeoutException {
		assertEquals(impl.getValues(2, 3), service.getValues(2, 3));
	}

	@Test
	public void testGetValueData() throws TimeoutException {
		DoubleDataPoint b = new DoubleDataPoint(1, "b", 3);
		List<DoubleDataPoint> response = service.getValueData(2, b);
		List<DoubleDataPoint> response2 = impl.getValueData(2, b);
		assertEquals(response2.size(), response.size());
		for (int i = 0; i < response.size(); i++) {
			response.get(i).equals(response2.get(i));
		}
	}

	@Test
	public void testTest() throws TimeoutException {
		service.test(true);
		assertTrue(impl.called);
	}

	@Test
	public void testDummyCall() throws TimeoutException {
		assertEquals(impl.dummyCall(), service.dummyCall());
	}
	
	@Test
	public void testParameterZoo() throws TimeoutException {
		Zoo foo = new Dog(12);
		assertEquals(12, impl.testZooParameter(foo ));
	}

	@Test
	public void testReturnZoo() throws TimeoutException{
		assertEquals("kitKat", impl.testZooRetun().getName());
	}
}
