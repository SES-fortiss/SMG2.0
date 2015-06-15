/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.siemens.impl;


import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import ch.iec._61400.ews._1_0.ServicePortType;

public class SiemensClientNew {
    private static ServicePortType service;
    private static String assocId;

 
    
    
    /**
     * TODO: this method is never called
     * 
     * @throws DatatypeConfigurationException
     */
    /*
    private static void subscribe() throws DatatypeConfigurationException {
        AddSubscriptionRequest req = new AddSubscriptionRequest();
        req.setAssocID(assocId);
        req.setUUID(UUID.randomUUID().toString());
        req.setRCBType(TRCBType.BRCB);
        req.setRCBRef("/");
        req.setDatSet("C1/LLN0.Measure");
        TTrgCond tTrgCond = new TTrgCond();
        tTrgCond.setGenrInterg(true);

        req.setTrgOp(tTrgCond);
        AddSubscriptionResponse resp = service.addSubscription(req);
        System.out.println(resp);
        if (resp.getServiceError().isEmpty()) {
            ReportRequest reportRequest = new ReportRequest();
            reportRequest.setAssocID(assocId);
            reportRequest.setUUID(UUID.randomUUID().toString());
            reportRequest.setMaxResponseTime(DatatypeFactory.newInstance()
                    .newDuration(1000000l));
            ReportResponse represp = service.report(reportRequest);
            System.out.println(represp);
        } else {
            System.out.println("error: " + resp.getServiceError());
        }
    }*/

    static ServicePortType getService(String host, String port) {
    	ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
    	
   	
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("org.apache.cxf.stax.allowInsecureParser", "1");
            System.setProperties(properties);
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        	factory.setServiceClass(ServicePortType.class);
        	String url = "http://" + host + ":" + port + "/61400";
        	factory.setAddress(url);
        	Thread.currentThread().setContextClassLoader(oldClassLoader);
        	return (ServicePortType) factory.create();
        
        } finally {
        	Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

}
