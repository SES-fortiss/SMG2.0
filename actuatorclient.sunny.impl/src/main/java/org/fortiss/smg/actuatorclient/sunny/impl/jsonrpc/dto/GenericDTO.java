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
