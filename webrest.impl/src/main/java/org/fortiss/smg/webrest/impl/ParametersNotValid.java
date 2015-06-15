/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.impl;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
 * @author reviwed by Sergiu Soima
 * Why do we need this class?
 */
public class ParametersNotValid extends WebApplicationException {
    /**
     * 
     */
    private static final long serialVersionUID = 2057273745293008258L;

    public ParametersNotValid(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message)
                .type(MediaType.TEXT_PLAIN).build());
    }
}