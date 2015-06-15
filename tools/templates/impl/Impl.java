/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.#bundle#.impl;

import org.fortiss.smg.#bundle#.api.#Bundle#Interface;
import org.fortiss.smg.ambulance.api.HealthCheck;
import org.slf4j.LoggerFactory;

public class #Bundle#Impl implements #Bundle#Interface {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(#Bundle#Impl.class);

		public String doSomething(String s){
			return "Hello smg";
		}

		@Override
		public boolean isComponentAlive() {
			// TODO Auto-generated method stub
			return false;
		}
}
