/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatormaster.api.events;


// use JsonTypeInfo.Id.CLASS for class naming

// Specify sub-classes using @JsonSubTypes annotation
// without this, deserializer will not be able to locate sub-types to use
/*
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({  
    @Type(value=DoubleEvent.class, name="doubleEvent"),  
})
*/


/*
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
*/
public abstract class AbstractDeviceEvent<E> {

	E value;

	protected AbstractDeviceEvent(){
		
	}
	
	protected AbstractDeviceEvent(E d){
		value  = d;
	}
	
	public E getValue() {
		return value;
	}

	public void setValue(E value) {
		this.value = value;
	}

	
	
}
