/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.impl.front;

import java.util.concurrent.TimeoutException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.fortiss.smg.usermanager.api.Key;
import org.fortiss.smg.webrest.impl.BundleFactory;
import org.fortiss.smg.webrest.impl.ParametersNotValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the user login
 */
@Path("push/devid")
public class Push {

	private static Logger logger = LoggerFactory.getLogger(Push.class);
	
    @POST
    @Path("/dev/{pubid}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response putTodo(@QueryParam("publickey") String publicKey) {

        int devId = 0;
		Key key;
		try {
			BundleFactory.getIKeyManager().setDevId(publicKey, devId );
			 key = BundleFactory.getIKeyManager().getKey(publicKey);
		} catch (TimeoutException e) {
			logger .info("No connection?", e.fillInStackTrace());
			throw new ParametersNotValid("Unable to connect to KeyManager");
		}
        
        //if (key.getDevId().equals(devId)) {
		if (key.getDevId() == devId) {

            return Response.ok().build();
        } else {
            Response.serverError().build();
        }
        return null;

    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
            MediaType.TEXT_PLAIN })
    @Path("/dev/{pubid}")
    public Key getKey(@QueryParam("publickey") String publicKey) {
        Key key;
		try {
			key = BundleFactory.getIKeyManager().getKey(publicKey);
		} catch (TimeoutException e) {
			logger .info("No connection?", e.fillInStackTrace());
			throw new ParametersNotValid("Unable to connect to KeyManager");
		}
        return key;
    }

    @DELETE
    public void unregisterDevice(@QueryParam("publickey") String publicKey) {
        // set to nothing
        try {
        	// TODO: Do we need setDevId ? who specifies the DevID ?

			BundleFactory.getIKeyManager().setDevId(publicKey, 0);
		} catch (TimeoutException e) {
			logger .info("No connection?", e.fillInStackTrace());
			throw new ParametersNotValid("Unable to connect to KeyManager");
		}
    }

}
