package org.fortiss.smg.containermanager.api.devices;


public class SingleContainerEdge {

	public SingleContainerEdge(String child, EdgeType type) {
		super();
		this.setChild(child);
		this.setType(type);
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getChild() == null) ? 0 : getChild().hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SingleContainerEdge other = (SingleContainerEdge) obj;
		if (getChild() == null) {
			if (other.getChild() != null)
				return false;
		} else if (!getChild().equals(other.getChild()))
			return false;
		return true;
	}




	private String child;
	private EdgeType type;


	public void setType(EdgeType type) {
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
	
}
