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
