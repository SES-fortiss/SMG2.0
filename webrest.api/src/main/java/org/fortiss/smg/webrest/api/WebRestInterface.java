package org.fortiss.smg.webrest.api;

import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.HealthCheck;

public interface WebRestInterface extends HealthCheck {
    String doSomething(String arg) throws TimeoutException ;
}
