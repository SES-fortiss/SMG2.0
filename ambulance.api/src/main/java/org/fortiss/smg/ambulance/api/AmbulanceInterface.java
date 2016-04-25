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
