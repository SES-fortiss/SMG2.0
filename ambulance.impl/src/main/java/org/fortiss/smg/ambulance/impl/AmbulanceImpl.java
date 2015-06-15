/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.ambulance.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.AmbulanceInterface;
import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.slf4j.LoggerFactory;

public class AmbulanceImpl implements AmbulanceInterface {


	private static final int TIMEOUTSHORT = 2000;
	private static final int TIMEOUTLONG = 5000;
	
	private HashMap<String,String> components;
	private ScheduledExecutorService executor;

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(AmbulanceImpl.class);
	
	public AmbulanceImpl() {
		components = new HashMap<String,String>();
	}

	public void activate() {
		
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new AmbulanceLooper(this), 0,
				20, TimeUnit.SECONDS);
		
		logger.info("Ambulance Service is running every 20 Seconds");
	}
	
	@Override
	public void registerComponent( String queueName, String name) {
		components.put(queueName, name);
		logger.debug("Component [name="+ name+", queue="+ queueName+"] registered");
		
	}

	@Override
	public void unregisterComponent(String queueName) {
		String name = null;
		if(!components.containsKey(queueName)){
			logger.debug("Component for Queue:"+ queueName +" wasn't registered");
		}else{
			name = components.get(queueName);
			logger.debug("Component [name="+ name+", queue="+ queueName+"] registered");
			components.remove(queueName);
		}
	}

	@Override
	public int numberRegisteredComponents() {
		return components.size();
	}

	@Override
	public Map<String, String> getRegisteredComponents() {
		return components;
	}

	@Override
	public Map<String, Boolean> checkSystem() {
		
		if(components.isEmpty()){
			return new HashMap<String,Boolean>();
		}
		
		Map<String, Boolean> map = new HashMap<String,Boolean>();
		
		for(String queue :components.keySet()){
			DefaultProxy<HealthCheck> healthCheck = new DefaultProxy<HealthCheck>(HealthCheck.class, queue, (components.size()+1)*TIMEOUTLONG);
			try {
				HealthCheck proxy = healthCheck.init();
				proxy.isComponentAlive();
				map.put(queue, true);
			} catch (IOException e) {
				logger.info("Healthcheck for "+ queue+ " failed. No connection");
				map.put(queue, false);
			} catch (TimeoutException e) {
				logger.info("Healthcheck for "+ queue+ " failed. Timeout exceded");
				map.put(queue, false);
			}finally{
				try {
					healthCheck.destroy();
				} catch (IOException e) {
					logger.info("Unable to close con. for queue:"+queue);
					map.put(queue, false);
				}
			}
			
			
		}
		
		return map;
		
	}

		
}
