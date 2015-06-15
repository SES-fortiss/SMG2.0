/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.test.server;

import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.webrest.impl.BundleFactory;
import org.fortiss.smg.webrest.impl.jersey.ServerController;
import org.fortiss.smg.webrest.test.util.ClientHelper;
import org.junit.Assert;
import org.mortbay.jetty.servlet.ServletHolder;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class MockWrapperServerControl extends ServerController {

	public void start(int port) {
		super.start(port, new ServletHolder(ServletContainer.class));

		// Have a fresh set of private keys
		ClientHelper.generateNewKeys();

		// Fake KeyManager
		BundleFactory.setKeyManager(ClientHelper.getMockKeyManager());

	}

	@Override
	public void stop() {
		super.stop();

		try {
			ClientHelper.getMockKeyManager().removeAllKeys(Const.getUserId());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("failed to remove keys");
		}
	}

	public int start() {
		Random rnd = new Random();
		int port =rnd.nextInt(20000);
		start(port);
		return port;
		
	}

}
