/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.impl.jersey;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.fortiss.smg.webrest.impl.LoginFilter;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHolder;

public class ServerController {

	private Server server;
	private int port = 8091;

	public void start(int port, ServletHolder servHolder) {
		this.port = port;
		// does not work :-(
		servHolder.setInitParameter(
				"com.sun.jersey.config.property.resourceConfigClass",
				"org.fortiss.smg.webrest.impl.jersey.UriExtensionsConfig");

		// add here all packages you would like to be public
		servHolder.setInitParameter("com.sun.jersey.config.property.packages",
				"org.fortiss.smg.webrest.impl.front");

		// set alias
		// jerseyServletParams.put("alias", "/api");

		// servHolder.setInitParameter(
		// "com.sun.jersey.spi.container.ContainerRequestFilters",
		// "org.fortiss.smartmicrogrid.api.rest.test.server.MyApppFilter");

		// Integrate JSON with Jersey - IMPORTANT!
		servHolder.setInitParameter(
				"com.sun.jersey.api.json.POJOMappingFeature", "true");

		FilterHolder filHolder = new FilterHolder(new LoginFilter());

		filHolder.setInitParameter("filter-name", "Auth-Filter");
		filHolder.setInitParameter("urlPatterns", "*");
		filHolder.setInitParameter("servletNames", "");

		server = new Server(port);

		// Global context
		Context context = new Context(server, "/api", Context.SESSIONS);

		// context for servlet = everything
		context.addServlet(servHolder, "/*");
		context.addFilter(filHolder, "/*", ServletContextHandler.NO_SECURITY);

		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

}
