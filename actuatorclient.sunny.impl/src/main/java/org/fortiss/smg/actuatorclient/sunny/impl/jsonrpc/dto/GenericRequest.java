/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.sunny.impl.jsonrpc.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class GenericRequest extends GenericDTO{
	public final String format = "JSON";
	public final Map<String,Collection<JSONRPCInputParam>> params = new HashMap<String, Collection<JSONRPCInputParam>>();
	
	public GenericRequest(String proc) {
		super(proc);
	}

	public String getVersion() {
		return version;
	}

	public String getFormat() {
		return format;
	}

	protected Map<String, Collection<JSONRPCInputParam>> getParams() {
		return params;
	}
	
	
	
}
