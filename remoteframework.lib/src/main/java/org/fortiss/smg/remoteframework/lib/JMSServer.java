package org.fortiss.smg.remoteframework.lib;

import java.io.IOException;

public class JMSServer<T> extends GenericServer<T> {

	protected JMSServer(Class<T> klass, T impl, String queue) {
		super(klass, impl, queue);
	}

	@Override
	public void init() throws IOException {
		/*
		JMSRemote.exposeServiceOnJMS(queue, impl, klass);
		*/
	}

	@Override
	public void destroy() throws IOException {
		// TODO !!
		
	}

}
