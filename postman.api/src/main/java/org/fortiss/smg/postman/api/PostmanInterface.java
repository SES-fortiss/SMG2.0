package org.fortiss.smg.postman.api;

import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.usermanager.api.User;

public interface PostmanInterface extends HealthCheck {
    void sendMail(User user, String subject, String message, NotificationType type) throws TimeoutException ;
    void crashReport(String klass, String message) throws TimeoutException;
}
