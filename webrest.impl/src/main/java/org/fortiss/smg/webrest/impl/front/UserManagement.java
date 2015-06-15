/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */

package org.fortiss.smg.webrest.impl.front;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Handles the user login
 */
@Path("/user")
public class UserManagement {

    @Context
    HttpHeaders headers;

    @Context
    UriInfo uriInfo;

    @Context
    Request request;

    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    @Path("/checklogin")
    public boolean checklogin() {
        return false;
    }

    @POST
    @Path("/test")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String createFromPost(@FormParam("user") String user,
            @FormParam("password") String password) {
        return String.format("user = %s, password = %s", user, password);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getUser() {

        // if (auth.getCurrentUser(request.getHeader("Cookie")) == null) {
        // return "LOGGED_IN";
        // }else{
        // return "NOT_LOGGED_IN";
        // }

        return Response.ok("aaa")
                .cookie(new NewCookie("name", "Hello, world!")).build();
    }

    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    @Path("{user}")
    public String user(@PathParam("user") String devid) {
        ArrayList<String> a = new ArrayList<String>();
        // Device device = JMSFactory.getLocationManager().getDevice(deviceID);
        return String.format("devid = %s", devid);
    }

    @GET
    @Produces({ "application/xml", "application/json" })
    // @Path("users/{username: [a-zA-Z][a-zA-Z_0-9]}")
    @Path("{user}/password/{password}")
    public String user(@PathParam("user") String user,
            @PathParam("password") String password) {
        return String.format("user = %s, password = %s", user, password);
    }
}
