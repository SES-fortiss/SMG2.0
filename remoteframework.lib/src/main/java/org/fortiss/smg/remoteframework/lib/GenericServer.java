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
