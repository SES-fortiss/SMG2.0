/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.remoteframework.lib.jms;

public class MiscConstants {
	public static final String JNDI_OPTS = "jndiInitialContextFactory"
			+ "=org.apache.activemq.jndi.ActiveMQInitialContextFactory"
			+ "&jndiConnectionFactoryName=ConnectionFactory&jndiURL=tcp://localhost:61616"
			+
			// +
			// "&jndiConnectionFactoryName=ConnectionFactory&jndiURL=tcp://livinglab.fortiss.org:443"
			// +
			"&jms.useAsyncSend=false&jms.deliveryMode=NON_PERSISTENT&jms.maxConcurrentTasks=10&jms.useAsyncSend=false&jms.timeToLive=5000&jms.copyMessageOnSend=false";

	public static final String QUEUE_PREFIX = "jms:queue:";
	public static final String TOPIC_PREFIX = "jms:topic:";
}
