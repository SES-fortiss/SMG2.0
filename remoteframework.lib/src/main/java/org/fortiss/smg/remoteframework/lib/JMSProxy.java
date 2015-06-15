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

public class JMSProxy<T> extends GenericProxy<T> {

	protected JMSProxy(Class<T> klass, String queue, int timeout) {
		super(klass, queue, timeout);
	}

	@Override
	public T init() throws IOException, TimeoutException {
		//return JMSRemote.getProxy(queue, klass, "");
		return null;
	}

	@Override
	public void destroy() throws IOException {
		// TODO 	
	}

	@Override
	public T initLoop() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
