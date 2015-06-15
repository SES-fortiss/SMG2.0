/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.test.server;

public class Const {

    private static String accessKey;
    private static String secretKey;

  
    public static String getSecretKey() {
        return Const.secretKey;
    }

    public static String getAccessKey() {
        return Const.accessKey;
    }

    public static String getServerURL(int port2) {
        return "http://localhost:" + port2 + "/api/";
    }

    public static int getUserId() {
        return 10;
    }

    public static void setSecretKey(String secretKey) {
        Const.secretKey = secretKey;
    }

    public static void setAccessKey(String key) {
        Const.accessKey = key;
    }
}
