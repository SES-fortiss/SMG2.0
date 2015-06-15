/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.postman.test;

import static org.junit.Assert.*;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;

import junit.framework.Assert;

import org.fortiss.smg.config.lib.Ops4JTestTime;
import org.fortiss.smg.postman.api.NotificationType;
import org.fortiss.smg.postman.impl.PostmanImpl;
import org.fortiss.smg.usermanager.api.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

public class TestPostmanSimple {

	// @Inject
	// private PostmanImpl helloService;
/*
	@Configuration
	public Option[] config() {

		Option[] defaultSpace = Ops4JTestTime.getOptions();
		Option[] currentSpace = options(
				mavenBundle("org.fortiss.smartmicrogrid", "postman.api",
						"1.0-SNAPSHOT"),
				mavenBundle("org.fortiss.smartmicrogrid", "postman.impl",
						"1.0-SNAPSHOT"));

		return OptionUtils.combine(defaultSpace, currentSpace);
	}
*/
	private static MockOtherBundles mocker;
	private PostmanImpl impl;
	private User user;

	@BeforeClass
	public static void setUpDataBase() throws SQLException,
			ClassNotFoundException {
		mocker = new MockOtherBundles();
	}

	
	@Before
	public void setUp() {

		impl = new PostmanImpl("Fortiss.Coffee@fortiss.org","merkur.fortiss.org","25","192.168.21.240");
		user = new User();
		user.setEmail("sergiu.soima@gmail.com");
		user.setName("Sergiu");
		user.setUserName("sergiu.soima");
		
	}

	@After
	public void tearDown() {
		
	}

	@Test(timeout = 5000)
	public void mailTest() {
		impl.crashReport("Foo", "Sudden");
		//impl.sendMail(user, "Test", "Test mail" , NotificationType.HardwareError);
		Assert.assertTrue(severAvailabilityCheck(impl.getMailHost(),Integer.parseInt(impl.getMailPort())));
	}

	@Test(timeout = 5000)
	public void irclTest() {
		Assert.assertTrue(severAvailabilityCheck(impl.getIrcServer(),6667));
		impl.sendIRCMessage("SMG2 Postman Test", "##");
	}
	
	@Test(timeout = 999999999)
	public void Twittertest(){
		//impl.tweet("2nd SMG Tweet");
		Socket sock = new Socket();
		InetSocketAddress addr = new InetSocketAddress("www.twitter.com",80);
		try {
			sock.connect(addr, 3000);
		} catch (Exception e) {
			fail("Connection failed");
			System.out.println("Connection failed");
		} finally {
			try {
				sock.close();
			} catch (Exception e) {
			}
		}
		
	}

	
	public static boolean severAvailabilityCheck(String server, int port) { 
	    try {
	    	Socket s = new Socket(server, port);
	        return true;
	    } catch (IOException ex) {
	        /* ignore */
	    }
	    return false;
	}
}
