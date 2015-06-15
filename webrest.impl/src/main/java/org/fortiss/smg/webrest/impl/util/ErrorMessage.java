/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.impl.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ErrorMessage extends WebApplicationException {

    public ErrorMessage(Response.Status status, String message) {
        super(Response.status(status).entity(message)
                .type(MediaType.TEXT_PLAIN).build());
    }
}