/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.sqltools.lib;

public class TestingDatabase {

    private static final String DB_URL = "mysql://localhost:3306/fortissTest";
    private static final String DB_USER = "fortiss";
    private static final String DB_PASSWORD = "foo";

    public static String getDBPassword() {
        return TestingDatabase.DB_PASSWORD;
    }

    public static String getDBUrl() {
        return TestingDatabase.DB_URL;
    }

    public static String getDBUser() {
        return TestingDatabase.DB_USER;
    }
    
}
