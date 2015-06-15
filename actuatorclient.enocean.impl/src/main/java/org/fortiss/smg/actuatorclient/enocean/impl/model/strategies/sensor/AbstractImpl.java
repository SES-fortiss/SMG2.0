/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor;

import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;

public abstract class AbstractImpl {

    protected ActuatorClientImpl impl;
	
    public void setImpl(ActuatorClientImpl impl2){
		this.impl = impl2;
	}
    
}
