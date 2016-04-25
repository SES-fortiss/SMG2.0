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
