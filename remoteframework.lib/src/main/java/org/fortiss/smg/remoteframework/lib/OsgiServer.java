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
