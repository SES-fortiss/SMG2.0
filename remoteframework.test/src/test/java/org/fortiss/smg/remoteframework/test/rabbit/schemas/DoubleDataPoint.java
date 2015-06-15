/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.remoteframework.test.rabbit.schemas;

import java.util.Date;

public class DoubleDataPoint {
	public int a;
	public String b;
	public long r;
	public Employee ee;
	
	public DoubleDataPoint(){
		
	}
	
	public DoubleDataPoint(int a, String b, long r, Employee ee) {
		super();
		this.a = a;
		this.b = b;
		this.r = r;
		this.ee = ee;
	}
	public DoubleDataPoint(int a, String b, long r) {
		super();
		this.a = a;
		this.b = b;
		this.r = r;
		ee = new Employee("Peter", "Blabla Street", 1, 2);
		}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a;
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + (int) (r ^ (r >>> 32));
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
		DoubleDataPoint other = (DoubleDataPoint) obj;
		if (a != other.a)
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (r != other.r)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DoubleDataPoint [a=" + a + ", b=" + b + ", r=" + r + "]";
	}	

}
