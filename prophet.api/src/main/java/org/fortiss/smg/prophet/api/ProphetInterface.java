/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.prophet.api;

import org.fortiss.smg.ambulance.api.HealthCheck;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public interface ProphetInterface extends HealthCheck {
    String doSomething(String arg) throws TimeoutException;
    
    /**
     * Provides the production forecast starting a starttime in long (Unix Time Stamp) for e.g. 24 hours in 30 minute intervals for PV  
     * connected to the roof-container. If no forecast could be generated a Map of zero values is generated
     * and provided. 
     * @param starttime
     * @param durationHours
     * @param intervalMinutes
     * @param containerId
     * @return
     * @throws TimeoutException
     */
    HashMap<Long,Double> getProductionForecast(long starttime, int durationHours, int intervalMinutes, String containerId) throws TimeoutException; 
   
    /**
     * Provides the consumption foreast starting a starttime in long (Unix Time Stamp) for e.g. 24 hours in 15 minute intervals for either 
     * the whole building (if null) or for a given   
     * connected to the roof-container. If no forecast could be generated a Map of zero values is generated
     * and provided.
     * @param starttime
     * @param durationHours
     * @param intervalMinutes
     * @param containerId
     * @return
     * @throws TimeoutException
     */
    HashMap<Long,Double> getConsumptionForecast(long starttime, int durationHours, int intervalMinutes, String containerId) throws TimeoutException;
    
}
