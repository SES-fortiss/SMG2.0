/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.informationbroker.api;

public class InformationBrokerQueueNames {

    public static String getQueryQueue(){
        return "informationbroker-query-1";
    }

	public static String getListenerQueue() {
		 return "informationbroker-listener-1";
	}

}
