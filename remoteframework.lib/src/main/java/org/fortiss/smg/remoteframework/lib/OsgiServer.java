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

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class OsgiServer<T> extends GenericServer<T> {

	private ServiceRegistration reg;
	private BundleContext ctx;

	public OsgiServer(Class<T> klass, T impl, BundleContext ctx) {
		super(klass, impl, "");
		this.ctx = ctx;
	}

	@Override
	public void init() throws IOException {
		reg = ctx.registerService(klass.getName(), impl, null);
		
	}

	@Override
	public void destroy() throws IOException {
		if(reg != null){
			reg.unregister();
		}else{
			// TODO this might be an error
		}
	}

}
