package org.fortiss.smg.webrest.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.fortiss.smg.webrest.test.server.MockWrapperServerControl;
import org.fortiss.smg.webrest.test.util.AuthenticationHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogin {

    private static final Logger logger = LoggerFactory
            .getLogger(TestLogin.class);

    private static MockWrapperServerControl server;

	private static int port;

    @BeforeClass
    public static void startServer() {
        TestLogin.server = new MockWrapperServerControl();
        port = server.start();
    }

    @AfterClass
    public static void stopServer() {
        TestLogin.server.stop();
    }

    @Test
    public void validLogin() {
       // for (int i = 0; i < 10; i++) {
            validLoginHelper("health/online");
       // }
    }

    public void validLoginHelper(String s) {
        String url = "";
        try {
            url = AuthenticationHelper.generateValidURL( s, port);
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            Assert.fail("Unsupported url");
        }
        String out = "";
        try {
            out = AuthenticationHelper.testResourceAtUrl(url);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.debug("baa", e.fillInStackTrace());
            Assert.fail("login falied");
        }
        if (out.length() < 1) {
            Assert.fail("no response");
        }
    }

    @Test
    public void wrongLogin() {
        ArrayList<String> url = AuthenticationHelper
                .wrongLogins("health/online", port);
        String out = "";
        System.out.println("--------Spamming with wrong logins");
        for (Object element : url) {
            String url_s = (String) element;
            boolean fail = false;
            try {
                out = AuthenticationHelper.testResourceAtUrl(url_s);
                if (out.length() > 1) {
                    Assert.fail("login succeded");
                }
            } catch (Exception e) {
                fail = true;
            }
            if (fail == false) {
                Assert.fail("login succeded");
            }

        }
        System.out.println("--------End Spamming with wrong logins");
    }

}
