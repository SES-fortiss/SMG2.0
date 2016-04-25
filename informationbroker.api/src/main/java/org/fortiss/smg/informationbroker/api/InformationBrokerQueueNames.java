package org.fortiss.smg.informationbroker.api;

public class InformationBrokerQueueNames {

    public static String getQueryQueue(){
        return "informationbroker-query-1";
    }

	public static String getListenerQueue() {
		 return "informationbroker-listener-1";
	}

}
