/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.impl.jersey;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class URIServletContainer extends ServletContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5062959913899530692L;


	public URIServletContainer(JerseyApplication jerseyApplication) {
		super(jerseyApplication);
	}


	@Override
	protected void configure(ServletConfig sc, ResourceConfig rc,
			WebApplication wa) {
		super.configure(sc, rc, wa);
		rc.getMediaTypeMappings().put("json", MediaType.APPLICATION_JSON_TYPE);
		rc.getMediaTypeMappings().put("xml", MediaType.APPLICATION_XML_TYPE);
		rc.getMediaTypeMappings().put("txt", MediaType.TEXT_PLAIN_TYPE);
		rc.getMediaTypeMappings().put("html", MediaType.TEXT_HTML_TYPE);
		rc.getMediaTypeMappings().put("xhtml", MediaType.APPLICATION_XHTML_XML_TYPE);
        MediaType jpeg = new MediaType("image", "jpeg");
        rc.getMediaTypeMappings().put("jpg", jpeg);
        rc.getMediaTypeMappings().put("jpeg", jpeg);
        rc.getMediaTypeMappings().put("zip", new MediaType("application", "x-zip-compressed"));
	}
	
	
	
}
