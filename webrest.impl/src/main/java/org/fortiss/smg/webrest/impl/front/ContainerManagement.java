/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */

package org.fortiss.smg.webrest.impl.front;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;

import javax.ws.rs.core.UriInfo;

/**
 * Handles the user login
 */
@Path("/container")
public class ContainerManagement {

    @Context
    HttpHeaders headers;

    @Context
    UriInfo uriInfo;

    @Context
    Request request;


}
