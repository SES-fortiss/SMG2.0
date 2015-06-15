/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
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
