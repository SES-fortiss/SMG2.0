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

public class AbstractListener extends AbstractConnector {

	private static org.slf4j.Logger alogger = LoggerFactory
			.getLogger(AbstractListener.class);
	
	public String queueId;

	private IActuatorListener impl;
	
	public static AbstractListener registerAsListenerAtServerStatic(final IActuatorListener impl, String name,  String queueId, IOnConnectListener listener) {
		AbstractListener impli = new AbstractListener();
		try {
			impli.registerAsListenerAtServer(impl, name, queueId, listener);
		} catch (InterruptedException e) {
			alogger.debug("starting failed", e);
		}
		return impli;
	}
	
	protected void registerAsListenerAtServer(final IActuatorListener impl, String name,  String queueId, IOnConnectListener listener) throws InterruptedException{
		this.impl = impl;
		this.name = name;
		this.listener = listener;
		this.queueId = queueId;
		// start in parallel
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(this);
	}
	
	protected void __registerClientAtServer(final IActuatorListener impl, String name, String queueId) {
		__connectToMaster();
		// TODO: should we make this endless?
		//for(int i=0; i<100; i++){
		try {
			while (!master.isRegisteredListener(name, queueId)) {
				master.registerListener(name, queueId);
				waitOrIsKilled();
				break;
			}
		} catch (TimeoutException e) {
			alogger.error(this.name +": Unable to connect to master");
			waitOrIsKilled();
		}
			
		server = new DefaultServer<IActuatorListener>(IActuatorListener.class,
				impl, queueId);
		alogger.debug("New client created with queue:"+ queueId);
		try {
			server.init();
		} catch (IOException e) {
			alogger.error("Unable to create server");
		}
	}
	
	@Override
	public void run() {
		__registerClientAtServer(impl, name, queueId);
		listener.onSuccessFullConnection();
	}
}
