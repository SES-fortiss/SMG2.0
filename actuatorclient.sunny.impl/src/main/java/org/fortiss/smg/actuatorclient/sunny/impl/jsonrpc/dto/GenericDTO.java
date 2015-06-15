/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.sunny.impl.jsonrpc.dto;

import java.util.Random;

public class GenericDTO {
	public String version = "1.0";
	public String proc;
	public String id;

	public String getVersion() {
		return version;
	}

	public GenericDTO(String proc) {
		this.proc = proc; 
		this.id = ""+new Random().nextInt();
	}
	
	public GenericDTO() {
		// for unmarshalling
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProc() {
		return proc;
	}

	public void setProc(String proc) {
		this.proc = proc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}
