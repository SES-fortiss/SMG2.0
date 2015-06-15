/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.impl.jersey;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.AmbulanceInterface;
import org.fortiss.smg.ambulance.api.AmbulanceQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.webrest.api.WebRestQueueNames;
import org.fortiss.smg.webrest.impl.BundleFactory;
import org.mortbay.jetty.servlet.ServletHolder;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class MainServerControl extends ServerController {

	public void start(int port) {
		super.start(port, new ServletHolder(new URIServletContainer(
				new JerseyApplication())));
	}

	@Override
	public void stop() {
		super.stop();

	}

	public static void main(String args[]) throws IOException, TimeoutException {
		DefaultProxy<AmbulanceInterface> ambulanceClient = new DefaultProxy<AmbulanceInterface>(
				AmbulanceInterface.class,
				AmbulanceQueueNames.getAmbulanceQueue(), 5000);
		AmbulanceInterface ambuInt = ambulanceClient.init();
		ambuInt.registerComponent(WebRestQueueNames.getWebRestInterfaceQueue(),
				"REST");
		ambulanceClient.destroy();

		BundleFactory.activate();

		// set here our port for REST
		MainServerControl serverREST = new MainServerControl();
		serverREST.start(8091);

	}

}
