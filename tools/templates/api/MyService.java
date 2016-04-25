package org.fortiss.smg.#bundle#.api;

import org.fortiss.smg.ambulance.api.HealthCheck;
import java.util.concurrent.TimeoutException;

public interface #Bundle#Interface extends HealthCheck {
    String doSomething(String arg) throws TimeoutException;
}
