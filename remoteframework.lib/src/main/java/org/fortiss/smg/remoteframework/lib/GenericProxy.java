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
