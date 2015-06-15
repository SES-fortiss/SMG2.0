/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.test.front;

import org.fortiss.smg.webrest.impl.types.TestXMLObject;
import org.fortiss.smg.webrest.test.server.MockWrapperServerControl;
import org.fortiss.smg.webrest.test.util.ClientHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestHealth {

    private static MockWrapperServerControl server;
	private static int port;

    @BeforeClass
    public static void startServer() {
        TestHealth.server = new MockWrapperServerControl();
        port = server.start();
    }

    @AfterClass
    public static void stopServer() {
        TestHealth.server.stop();
    }

    @Test
    public void testStatus() {

        String requestURL = "C";

        /* Those two check needs to be done everytime for GET */
        // Check if the reponse is valid
        Assert.assertTrue(ClientHelper.checkResponse(requestURL, port));
        // Check if JSON, XML etc. are equal
        // Here you need to change the Type of String to whatever object you
        // expect
        String response = ClientHelper.checkEquality(requestURL, String.class, port);

        // Now compare the result with what you expect it to be
        Assert.assertEquals("200", response);
    }

    @Test
    public void testXML() {

        String requestURL = "health/testxml/" + "abc";

        /* Those two check needs to be done everytime for GET */
        // Check if the reponse is valid
        Assert.assertTrue(ClientHelper.checkResponse(requestURL, port));
        // Check if JSON, XML etc. are equal
        // Here you need to change the Type of String to whatever object you
        // expect
        TestXMLObject response = ClientHelper.checkEquality(requestURL,
                TestXMLObject.class, port);

        // Now compare the result with what you expect it to be
        Assert.assertEquals("abc", response.getName());
    }
    

}
