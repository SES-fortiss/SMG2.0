package org.fortiss.smg.webrest.impl.types;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="node")
public class Children {

	String id;
	String type;
	
	public Children(){
		id="";
		type="";
	}
	
	public Children(String id, String type) {
		this.id= id;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
