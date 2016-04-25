package com.siemens.ctic2.iec61850;

public class Data {
	private String dataName;
	private String dataRef;
	
	protected Data(String dataRef) {
		setDataRef(dataRef);
	}
	
	public String getDataRef() {
		return dataRef;
	}
	
	public void setDataRef(String dataRef) {
		this.dataRef = dataRef;
		
		int lastDot = dataRef.lastIndexOf('.');
		
		if (lastDot < 0) {
			throw new IllegalArgumentException("Wrong data reference format: " + dataRef);
		}
		
		dataName = dataRef.substring(lastDot + 1);
	}
	
	public String getDataName() {
		return dataName;
	}
}
