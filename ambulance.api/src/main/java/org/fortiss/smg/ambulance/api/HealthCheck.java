/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.ambulance.api;

import java.util.concurrent.TimeoutException;

public interface HealthCheck {

	boolean isComponentAlive() throws TimeoutException ;

}
