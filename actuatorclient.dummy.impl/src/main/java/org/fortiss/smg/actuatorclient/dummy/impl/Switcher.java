package org.fortiss.smg.actuatorclient.dummy.impl;

public class Switcher {

	double status = 0.0;
	public Switcher() {
		
	}
	
	public Switcher(double status) {
		this.status = status;
		
	}
	
	public void setStatus(double status) {
		this.status = status;
	}
	
	public double getStatus() {
		return status;
	}
	
	
}
