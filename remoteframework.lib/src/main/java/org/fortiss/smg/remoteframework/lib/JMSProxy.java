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
