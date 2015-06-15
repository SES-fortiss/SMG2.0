/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.ambulance.api;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.remoteframework.lib.DefaultProxy;


public class AmbulanceMessenger implements Runnable {

	private static final int TIMEOUTSHORT = 2000;
	private static final int TIMEOUTLONG = 5000;
	
	private volatile boolean isKilled;
	private String name;
	private String queue;
	private IOnRegisterComplete registerCall;
	
	public interface IOnRegisterComplete {
		public void onRegisterComplete();
	}
	
	public static void register(String name, String queue, IOnRegisterComplete registerCall){
		AmbulanceMessenger  impl = new AmbulanceMessenger();
		impl.name = name;
		impl.queue = queue;
		impl.registerCall = registerCall;
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(impl);
	}

	public void run(){
		// say hello to ambulance
		DefaultProxy<AmbulanceInterface> ambulanceClient = new DefaultProxy<AmbulanceInterface>(
				AmbulanceInterface.class,
				AmbulanceQueueNames.getAmbulanceQueue(), TIMEOUTSHORT);
		AmbulanceInterface ambuInt;
		for(int i=0; i< 100; i++){
			try {
				ambuInt = ambulanceClient.initLoop();
				ambuInt.registerComponent(queue,
						name);
				break;
			}	catch (IOException e) {
				waitOrIsKilled();
			} catch (TimeoutException e) {
				waitOrIsKilled();
			}
		}
		try {
			ambulanceClient.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registerCall.onRegisterComplete();
	}

	
	protected  void kill(){
		isKilled = true;
	}
	
	protected void waitOrIsKilled() {
		if (!isKilled) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
