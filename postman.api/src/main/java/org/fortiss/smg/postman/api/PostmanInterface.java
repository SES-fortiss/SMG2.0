/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.postman.api;

import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.usermanager.api.User;

public interface PostmanInterface  {
    void sendMail(User user, String subject, String message, NotificationType type) throws TimeoutException ;
    void crashReport(String klass, String message) throws TimeoutException;
}
