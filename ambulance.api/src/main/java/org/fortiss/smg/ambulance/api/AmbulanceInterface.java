/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.ambulance.api;

import java.util.Map;
import java.util.concurrent.TimeoutException;

public interface AmbulanceInterface {
    void registerComponent( String queueName, String name) throws TimeoutException;
    void unregisterComponent(String queueName) throws TimeoutException ;
    int numberRegisteredComponents() throws TimeoutException ;
    Map<String,String> getRegisteredComponents() throws TimeoutException ;
   
    /**
     * 
     * @return hashmap with queueName and status
     */
    Map<String,Boolean> checkSystem();
}
