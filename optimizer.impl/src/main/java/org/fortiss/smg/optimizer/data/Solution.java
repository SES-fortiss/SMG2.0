/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.data;

import java.util.Arrays;

/***
 * 
 * @author Cheng Zhang
 * @version 1.0
 * 
 */
public class Solution {

	private boolean isFound;

	private Violation[] violations;

	public boolean isFound() {
		return isFound;
	}

	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}

	public Violation[] getViolations() {
		return violations;
	}

	public void setViolations(Violation[] violations) {
		this.violations = violations;
	}

	public Solution() {
		super();
		this.isFound = true;
	}

	public Solution(Violation[] violations) {
		super();
		this.isFound = false;
		this.violations = violations;

	}

	@Override
	public String toString() {
		return "[Solution is found:" + isFound + ". Violations:"
				+ Arrays.toString(violations) + "]";
	}
}
