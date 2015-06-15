/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatormaster.api.events;



public class DoubleEvent extends AbstractDeviceEvent<Double> {

	double maxAbsError =0.0;
	
	protected DoubleEvent() {
		super();
	}
	
	public double getMaxAbsError() {
		return maxAbsError;
	}

	public void setMaxAbsError(double maxAbsError) {
		this.maxAbsError = maxAbsError;
	}

	public DoubleEvent(double value) {
		super(value);
	}

	@Override
	public String toString() {
		return "DoubleEvent [maxAbsError=" + maxAbsError + ", value=" + value
				+ "]";
	}
	




	
	
}
