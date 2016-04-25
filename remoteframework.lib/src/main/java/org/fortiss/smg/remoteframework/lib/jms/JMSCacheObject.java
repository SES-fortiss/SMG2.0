package org.fortiss.smg.remoteframework.lib.jms;

import java.util.Date;

public class JMSCacheObject {

	private Object proxy;
	private Date validUntil;

	public JMSCacheObject(Object proxy) {
		this.proxy = proxy;
		this.validUntil = new Date(System.currentTimeMillis() + 1000 * 60 * 15);
		// every cache object should be valid for 15min
	}

	public Object getProxy() {
		return (validUntil.after(new Date()) ? this.proxy : null);
	}

}
