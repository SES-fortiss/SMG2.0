/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.sqltools.lib.builder;

public class IllegalQueryException extends RuntimeException {

	private static final long serialVersionUID = -4847411495350655382L;

	public IllegalQueryException() { }

	public IllegalQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalQueryException(String message) {
		super(message);
	}

	public IllegalQueryException(Throwable cause) {
		super(cause);
	}

}
