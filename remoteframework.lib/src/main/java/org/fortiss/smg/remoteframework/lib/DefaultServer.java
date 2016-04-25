package org.fortiss.smg.remoteframework.lib;

import java.io.IOException;

public class DefaultServer<T> extends GenericServer<T>{

	RabbitRPCServer<T> server;
	
	public DefaultServer(Class<T> klass, T impl, String queue) {
		super(klass, impl, queue);
		server = new RabbitRPCServer<T>(klass, impl, queue);
	}

	@Override
	public void init() throws IOException {
		server.init();
	}

	@Override
	public void destroy() throws IOException {
		server.destroy();
	}

}
