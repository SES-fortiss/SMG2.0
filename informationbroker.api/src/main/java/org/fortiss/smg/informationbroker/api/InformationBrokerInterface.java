/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.informationbroker.api;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;

public interface InformationBrokerInterface extends HealthCheck, IDatabase {


	    public List<DoublePoint> getDoubleValue(DeviceId dev,
	           long from,
long to) throws TimeoutException ;

	    public List<DoublePoint>  getDoubleValueBefore(DeviceId dev,
	            long date) throws TimeoutException ;
 

	    public boolean isSIUnitType(String unit) throws TimeoutException;
	    
	   
	    
//	    public java.util.List<WrapperDataPoint> getWrapperinfo(
//	       String type);

	    	
}
