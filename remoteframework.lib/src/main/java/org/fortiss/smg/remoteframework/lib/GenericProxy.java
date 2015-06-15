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
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeoutException;

public abstract class GenericProxy<T> {

	Class<T> klass;
	String queue;
	int timeout;

	protected GenericProxy(Class<T> klass, String queue, int timeout) {
	    this.klass = klass;
		this.queue = queue;
		this.timeout = timeout;
	}

	public abstract T init() throws IOException, TimeoutException;
	public abstract T initLoop() throws IOException;
	
	
	public abstract void destroy() throws IOException;

	
}
