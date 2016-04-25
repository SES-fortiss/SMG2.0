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