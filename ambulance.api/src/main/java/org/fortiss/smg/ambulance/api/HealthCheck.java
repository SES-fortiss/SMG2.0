package org.fortiss.smg.ambulance.api;

import java.util.concurrent.TimeoutException;

public interface HealthCheck {

	boolean isComponentAlive() throws TimeoutException ;

}
