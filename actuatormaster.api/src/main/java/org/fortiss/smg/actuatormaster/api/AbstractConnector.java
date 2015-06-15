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
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.remoteframework.lib.DefaultServer;
import org.slf4j.LoggerFactory;

public abstract class AbstractConnector implements Runnable  {

	private static final int TIMEOUTSHORT = 2000;
	private static final int TIMEOUTLONG = 5000;
	
	DefaultServer<?> server;
	private DefaultProxy<IActuatorMaster> proxyMaster;
	protected IActuatorMaster master;
	protected String clientId;

	protected String name;
	protected Thread thread;
	private volatile boolean isKilled = false;
	protected IOnConnectListener listener;

	public interface IOnConnectListener {
		public void onSuccessFullConnection();
	}

	private static org.slf4j.Logger alogger = LoggerFactory
			.getLogger(AbstractConnector.class);

	public void destroy() {
		isKilled = true;
		try {
			if (proxyMaster != null && server != null) {
			proxyMaster.destroy();
			server.destroy();
			}
		} catch (IOException e) {
			alogger.error("Unable to destroy connection to master");
			e.printStackTrace();
		} catch (NullPointerException e) {
			alogger.error("No  successful init");
			e.printStackTrace();
		}

	}

	protected void __connectToMaster() {
		if (proxyMaster == null) {
			// TODO: should we make this endless?
			while(true) {
			//for (int i = 0; i < 100; i++) {
				proxyMaster = new DefaultProxy<IActuatorMaster>(
						IActuatorMaster.class,
						ActuatorMasterQueueNames
								.getActuatorMasterInterfaceQueue(), TIMEOUTLONG);
				try {
					master = proxyMaster.init();
					break;
				} catch (IOException e) {
					alogger.error(this.name + ": Unable to connect to master");
					waitOrIsKilled();
				} catch (TimeoutException e) {
					alogger.error(this.name
							+ ": Unable to connect to master (Timeout).");
					waitOrIsKilled();
				}
				waitOrIsKilled();
			}
		}

	}

	protected void waitOrIsKilled() {
		if (!isKilled) {
			try {
				Thread.sleep(TIMEOUTLONG);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
