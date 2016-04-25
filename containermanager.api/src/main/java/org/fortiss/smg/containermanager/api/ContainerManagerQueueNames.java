package org.fortiss.smg.containermanager.api;

public class ContainerManagerQueueNames {

    public static String getContainerManagerInterfaceQueue(){
        return "containermanager-1";
    }

    public static String getContainerManagerInterfaceQueryQueue(){
        return "containermanager-query-1";
    }
    
	public static String getContainerManagerListenerQueue() {
		 return "containermanager-listener-1";
	}
}
