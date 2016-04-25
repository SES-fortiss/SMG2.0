package org.fortiss.smg.webrest.test;

import org.fortiss.smg.webrest.impl.jersey.ServerController;
import org.fortiss.smg.webrest.test.server.Const;
import org.fortiss.smg.webrest.test.server.MockWrapperServerControl;
import org.fortiss.smg.webrest.test.util.ClientHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestServer {

    static MockWrapperServerControl server;
	private static int port;

    @BeforeClass
    public static void startServer() {
        TestServer.server = new MockWrapperServerControl();
        port = server.start();
    }

    @AfterClass
    public static void stopServer() {
        TestServer.server.stop();
    }

    @Test
    public void checkServer() {
        try {
            String result = ClientHelper.fetchResponse("health/online",
                    String.class, port);
            Assert.assertEquals("200", result);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Server is not going to start.");
        }

    }

    @Test
    public void testCompatibility() {
        String url = "health/online";

        /* This needs to be done everytime for GET */
        // Check if the reponse is valid
        Assert.assertTrue(ClientHelper.checkResponse(url, port));
        // Check if JSON, XML etc. are equal
        String response = ClientHelper.checkEquality(url, String.class, port);
        Assert.assertEquals(response, "200");
    }

}
