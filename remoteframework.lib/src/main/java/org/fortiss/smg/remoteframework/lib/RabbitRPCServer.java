package org.fortiss.smg.remoteframework.lib;

import java.io.IOException;

public class RabbitRPCServer<T> extends GenericServer<T> {

	private ServerThread thread;

	public RabbitRPCServer(Class<T> klass, T impl, String queue) {
		super(klass, impl, queue);
	}

	@Override
	public void init() throws IOException {

		thread = new ServerThread(klass, impl, queue);
	}

	@Override
	public void destroy() throws IOException {
		thread.stop();
		
	}

}
