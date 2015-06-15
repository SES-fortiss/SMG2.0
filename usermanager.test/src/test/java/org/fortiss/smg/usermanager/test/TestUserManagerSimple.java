/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.usermanager.test;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.sqltools.lib.TestingDatabase;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.fortiss.smg.usermanager.api.User;
import org.fortiss.smg.usermanager.api.UserManagerInterface;
import org.fortiss.smg.usermanager.dbutil.UserManagerDBUtil;
import org.fortiss.smg.usermanager.impl.UserManagerImpl;
import org.fortiss.smg.usermanager.impl.key.KeyManagerImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;


public class TestUserManagerSimple {

	private UserManagerInterface impl;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(TestUserManagerSimple.class);
	private ContainerManagerInterface containerImpl;
	@Before
	public void setUp() throws IOException, TimeoutException, ClassNotFoundException {
		String dburl =    "jdbc:"+  TestingDatabase.getDBUrl();
		String dbuser = TestingDatabase.getDBUser();
		String dbpassword = TestingDatabase.getDBPassword();
		
		TestingDBUtil db = new TestingDBUtil();
		
		System.out.println("Statement created...");
		String sql = "TRUNCATE UserManager_Users";
		db.executeQuery(sql);
		sql = "TRUNCATE UserManager_Contacts";
		db.executeQuery(sql);
		sql = "TRUNCATE UserManager_Profiles";
		db.executeQuery(sql);
		sql = "TRUNCATE UserManager_Devices";
		//db.executeQuery(sql);
		sql = "TRUNCATE UserManager_Rooms";
		db.executeQuery(sql);
		
		//ContainerManager_Devices required
		sql = "TRUNCATE ContainerManager_Devices";
		db.executeQuery(sql);
		sql = "INSERT INTO `ContainerManager_Devices` (`DeviceCode`, `DeviceType`, `SMGDeviceType`, `AllowedUserProfile`, `MinUpdateRate`, `MaxUpdateRate`, `AcceptsCommands`, `HasValue`, `RangeMin`, `RangeMax`, `RangeStep`, `CommandMinRange`, `CommandMaxRange`, `CommandRangeStep`, `CommandRangeStepType`, `HumanReadableName`, `Description`) VALUES "+
"(13, 'AMBIENT_TEMPERATURE', 'Temperature', 1, 1000, 3600000, 0, 0, -50, 100, 0.1, -1, -1, -1, 'NONE', 'Thermometer', 'Ambient temperature sensor in Celsius'),"+
"(12, 'RELATIVE_HUMIDITY', 'Humidity', 1, 1000, 3600000, 0, 0, 0, 100, 1, -1, -1, -1, 'NONE', '', ''),"+
"(5, 'LIGHT', 'Brightness', 1, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'LINEAR', '', ''),"+
"(136, '', 'Blinds', 1, 1000, 3600000, 1, 0, 0, 100, 1, 0, 100, 1, 'LINEAR', '', ''),"+
"(151, '', 'Window', 1, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', ''),"+
"(152, '', 'Door', 1, 1000, 3600000, 0, 0, 0, 0, 0, -1, -1, -1, 'NONE', '', '')";
		db.executeQuery(sql);
		
		//UserManagerDBUtil dbUtil = new UserManagerDBUtil(dburl, dbuser, dbpassword);
		
		impl = new UserManagerImpl(db); //dbUtil);//, logger);

	}

	@Test
	public void TestUser() throws Exception{
		User user = new User();
		user.setUserName("foo");
		String password = "test";
		user.setName("foo");
		assertEquals(true, impl.createUser(user, password));

		user.setName("test");
		
		System.out.println(user.getId());

		user.setId(1); //impl.getValidUser(user.getUserName(), password).getId());
		
		Assert.assertTrue(impl.modifyUser(user, password));

		assertEquals("test",user.getName());

		user.setEmail("foo@test.com");
		user.setPrimaryContactInfo("foo@test.com");

		Assert.assertTrue(impl.createUserContacts(user));

		assertEquals("foo@test.com",impl.getUserByName(user.getUserName()).getEmail());
		
		assertEquals("foo@test.com",impl.getUser(user.getId()).getEmail());
		
		user.setPhone("012345678");		
		user.setPrimaryContactInfo( user.getPhone());
		Assert.assertTrue(impl.modifyUserContacs(user, password));

		Assert.assertTrue(impl.setLoginStatus(user.getId(), false));

		Assert.assertFalse(impl.loginStatus(user.getUserName()));

		Long devId =  (long) 1;
		Assert.assertTrue(impl.attachDevicetoUser(user.getId(), devId, "1111111111", true, "TestDeviceName","TestsOS"));

		assertEquals(user.getId(),impl.getDeviceOwner(devId));

		assertEquals("foo",impl.loginByDevice("1111111111"));

		Assert.assertTrue(impl.detachDevicefromUser(user.getId(), devId));

		Long roleId =  (long) 5;
		Assert.assertTrue(impl.assignRoletoUser(user.getId(), roleId));

		assertEquals("Guest",impl.getUserRole(user.getId(), "1"));
		
		Assert.assertTrue(impl.assignUsertoRoom(user.getId(),"1","God"));
		
		Assert.assertTrue(impl.assignUsertoRoom(user.getId(),"3","Employee"));

		Assert.assertTrue(impl.assignUsertoRoom(user.getId(),"2"));
		
		Assert.assertEquals(2, impl.getAssignedRooms(user.getId()).size());

		assertEquals("God",impl.getUserRole(user.getId(), "1"));
		
		Assert.assertTrue(impl.validUserToCreateRule("foo", "1"));

		Assert.assertTrue(impl.dissociateUserfromRoom(user.getId(), "1"));
		
		Assert.assertTrue(impl.dissociateUserfromRoom(user.getId(), "2"));

//		Object pass = "$2a$10$$2a$10$NCzVXcraakNNG9I8mtR1buc7EZXSUUswr/G1LrvxdE8G7PWXwA5KO";	
//		Assert.assertTrue(impl.validLoginHash("foo", pass));		
		
		
		
		
		Assert.assertTrue(impl.modifyPassword("foo", "test", "foo"));
		Assert.assertTrue(impl.modifyPassword("foo", "foo", "test"));
		
		System.out.println(impl.validLogin("foo", "test"));
		
		
		
		
		List<Double> itemvalues = new ArrayList<Double>(Arrays.asList(76.2,40.3,44.23,9.1,2.0,0.0));
		List<String> items = impl.getPossibleProfileItems();
		Assert.assertTrue(impl.addNewUserProfile(user.getId(), items, itemvalues, "Summer"));

		itemvalues = new ArrayList<Double>(Arrays.asList(76.2,40.3,44.23,9.1,2.0,0.0));
		items = impl.getPossibleProfileItems();
		
		Assert.assertTrue(impl.addNewUserProfile(user.getId(), items, itemvalues, "Winter"));

		itemvalues = new ArrayList<Double>(Arrays.asList(83.2,27.3,33.23,8.1,2.0,0.0));
		items = impl.getPossibleProfileItems();
		System.out.println(items);
		Assert.assertTrue(impl.modifyUserProfile(user.getId(), items, itemvalues, "Winter"));
		
		Assert.assertTrue(impl.activateUserProfile(user.getId(), "Winter"));

		assertEquals("winter",impl.getUserByName("foo").getActiveProfile());
		
		assertEquals("winter",impl.getUserAllProfileNames(user.getId()).get(1));

		Assert.assertTrue(impl.deleteUserProfile(user.getId(), "Winter"));
		
		assertEquals("summer",impl.getUserAllProfileNames(user.getId()).get(0));
		
		Assert.assertTrue(impl.deleteUserProfile(user.getId(), "summer"));
			
		Assert.assertTrue(impl.deleteUser("foo", "test"));

	}


}
