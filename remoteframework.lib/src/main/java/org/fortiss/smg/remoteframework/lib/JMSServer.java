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
