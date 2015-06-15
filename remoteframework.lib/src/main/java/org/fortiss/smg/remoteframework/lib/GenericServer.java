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

public abstract class GenericServer<T> {

	String queue;
	T impl;
	Class<T> klass;

	
	protected GenericServer(Class<T> klass, T impl, String queue){
		this.klass = klass;
		this.impl = impl;
		this.queue = queue;
	}
	
	public abstract void init() throws IOException;
	
	public abstract void destroy() throws IOException;
	
}
