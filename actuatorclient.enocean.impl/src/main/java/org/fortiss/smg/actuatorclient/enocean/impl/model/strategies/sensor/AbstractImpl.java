package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor;

import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;

public abstract class AbstractImpl {

    protected ActuatorClientImpl impl;
	
    public void setImpl(ActuatorClientImpl impl2){
		this.impl = impl2;
	}
    
}
