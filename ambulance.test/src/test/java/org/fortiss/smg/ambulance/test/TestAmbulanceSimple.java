/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.ambulance.test;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;

import org.fortiss.smg.ambulance.api.AmbulanceInterface;
import org.fortiss.smg.ambulance.impl.AmbulanceImpl;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.remoteframework.lib.DefaultServer;
import org.fortiss.smg.remoteframework.lib.except.JsonRpcException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAmbulanceSimple {

	private AmbulanceImpl impl;
	DefaultProxy<AmbulanceInterface> client;
        AmbulanceInterface service;
	DefaultServer<AmbulanceInterface> server;
	private AmbulanceImpl ambu;

	@Before
	public void setUp() throws IOException, JsonRpcException, TimeoutException,
			InterruptedException {
		impl = new AmbulanceImpl();
		SecureRandom random = new SecureRandom();
		String queueName = "Ambulance-Test-Queue-"+ new BigInteger(130, random).toString(32);


		// destroy exisiting messages
		Thread.sleep(500);

		server = new DefaultServer<AmbulanceInterface>(AmbulanceInterface.class, impl, queueName);
		server.init();

		client = new DefaultProxy<AmbulanceInterface>(AmbulanceInterface.class,queueName, 1000);
		service = client.init();
		ambu = new AmbulanceImpl();
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

	@Test(timeout=5000)
	public void testAddComponent() throws TimeoutException {
		ambu.registerComponent("fooq", "bar");
		Assert.assertEquals(1, ambu.numberRegisteredComponents());
		ambu.unregisterComponent("fooq");
		Assert.assertEquals(0, ambu.numberRegisteredComponents());
	}
}
