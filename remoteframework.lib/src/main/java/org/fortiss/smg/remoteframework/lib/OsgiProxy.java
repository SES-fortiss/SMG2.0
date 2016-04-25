package org.fortiss.smg.remoteframework.lib;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class OsgiProxy<T> extends GenericProxy<T> {

	private BundleContext ctx;

	public OsgiProxy(Class<T> klass, BundleContext ctx, int timeout) {
		super(klass, "", timeout);
		this.ctx = ctx;
	}

	@Override
	public T init() throws IOException, TimeoutException {
		ServiceReference ref = ctx.getServiceReference(klass.getName());
		T svc = (T) ctx.getService(ref);
		return svc;
	}

	@Override
	public void destroy() throws IOException {
		// TODO Auto-generated method stubd
	}

	@Override
	public T initLoop() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
