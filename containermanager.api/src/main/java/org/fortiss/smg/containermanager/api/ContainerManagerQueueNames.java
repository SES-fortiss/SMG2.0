/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
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
