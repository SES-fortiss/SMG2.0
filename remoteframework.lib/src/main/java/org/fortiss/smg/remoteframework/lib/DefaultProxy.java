/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.remoteframework.lib;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DefaultProxy<T> extends GenericProxy {

	private RabbitRPCProxy<T> proxy;

	public DefaultProxy(Class klass, String queue, int timeout) {
		super(klass, queue, timeout);
		proxy = new RabbitRPCProxy<T>(klass, queue, timeout);
	}

	@Override
	public T init() throws IOException, TimeoutException {
		return proxy.init();
	}

	@Override
	public void destroy() throws IOException {
		proxy.destroy();
	}

	@Override
	public T initLoop() throws IOException {
		return proxy.initLoop();
	}

}
