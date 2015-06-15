/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.test;

import java.util.Date;

import org.fortiss.smg.webrest.impl.Constants;
import org.fortiss.smg.webrest.impl.LoginFilter;
import org.junit.Assert;
import org.junit.Test;

public class TestNonce {

    @Test
    public void checkNonces() {

        long current_timestamp = new Date().getTime();

        long current_timestamp_fail = new Date().getTime()
                + Constants.MAX_TIMESTAMP_DIFFERENCE + 100;

        long before = System.nanoTime();
        int tests = 300;

        for (int i = 0; i < tests; i++) {

            // first request
            Assert.assertTrue(LoginFilter.checkNonce("abc" + i, "123",
                    current_timestamp));
            Assert.assertFalse(LoginFilter.checkNonce("abc" + i, "123",
                    current_timestamp));

            // another user
            Assert.assertTrue(LoginFilter.checkNonce("abcd" + i, "123",
                    current_timestamp));

            Assert.assertFalse(LoginFilter.checkNonce("abcd" + i, "123",
                    current_timestamp));

            // another request
            Assert.assertTrue(LoginFilter.checkNonce("abc" + i, "1234",
                    current_timestamp));

            Assert.assertFalse(LoginFilter.checkNonce("abc" + i, "1234",
                    current_timestamp));

            // Timeout the max
            Assert.assertTrue(LoginFilter.checkNonce("abc" + i, "123",
                    current_timestamp_fail));

            Assert.assertTrue(LoginFilter.checkNonce("abc" + i, "1234",
                    current_timestamp_fail));

            Assert.assertTrue(LoginFilter.checkNonce("abcd" + i, "123",
                    current_timestamp_fail));
        }
        System.out.println("Took " + (System.nanoTime() - before)
                / (double) tests / 1000 / 100 / 11 + "ms per nonce request");

    }
}
