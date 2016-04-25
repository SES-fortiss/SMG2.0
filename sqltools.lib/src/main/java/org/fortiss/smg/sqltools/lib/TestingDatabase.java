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
