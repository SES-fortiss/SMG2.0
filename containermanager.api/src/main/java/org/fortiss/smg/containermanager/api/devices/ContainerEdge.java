package org.fortiss.smg.containermanager.api.devices;


public class ContainerEdge {
	String parent;
	String child;
	
	EdgeType type;

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public ContainerEdge(String parent, String child, EdgeType type) {
		super();
		this.parent = parent;
		this.child = child;
		this.type = type;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}

	public EdgeType getType() {
		return type;
	}

	public void setType(EdgeType type) {
		this.type = type;
	}
	
}

