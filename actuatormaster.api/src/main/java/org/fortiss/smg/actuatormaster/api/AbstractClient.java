/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatormaster.api;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.remoteframework.lib.DefaultServer;
import org.slf4j.LoggerFactory;

public abstract class AbstractClient extends AbstractConnector {

	protected IActuatorClient impl;

	private static org.slf4j.Logger alogger = LoggerFactory
			.getLogger(AbstractClient.class);

	public String getQueue() {
		return clientId;
	}

	protected void registerAsClientAtServer(final IActuatorClient impl,
			String name, IOnConnectListener listener)
			throws InterruptedException {
		this.impl = impl;
		this.name = name;
		this.listener = listener;
		// start in parallel
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(this);
	}

	protected void __registerClientAtServer(final IActuatorClient impl,
			String name) {
		__connectToMaster();
		clientId = null;
		// TODO: should we make this endless?
		// for(int i=0; i<100; i++){

		try {
			while (!master.isRegisteredClient(name)) {
				clientId = master.registerClient(name);
				waitOrIsKilled();
				break;
			}
		} catch (TimeoutException e) {
			alogger.error(this.name + ": Unable to connect to master");
			waitOrIsKilled();
		}

		String queue = getQueue();
		server = new DefaultServer<IActuatorClient>(IActuatorClient.class,
				impl, queue);
		alogger.debug("New client created with queue:" + queue);
		try {
			server.init();
		} catch (IOException e) {
			alogger.error("Unable to create server");
		}

	}

	@Override
	public void run() {
		__registerClientAtServer(this.impl, this.name);
		alogger.error("onSuccess[]");
		listener.onSuccessFullConnection();
	}

}
