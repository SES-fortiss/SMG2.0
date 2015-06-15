/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
/**
 * 
 */
package org.fortiss.smg.gamification.test;

import java.util.HashMap;

import org.apache.log4j.ConsoleAppender;
import org.fortiss.smg.actuatormaster.impl.ActuatorMasterActivator;
import org.fortiss.smg.ambulance.impl.AmbulanceActivator;
import org.fortiss.smg.containermanager.impl.ContainerManagerActivator;
import org.fortiss.smg.informationbroker.impl.InformationBrokerActivator;
import org.fortiss.smg.sqltools.lib.TestingDatabase;


/**
 * @author Pahlke
 *
 */
public class MockOtherBundles {
	public MockOtherBundles() throws ClassNotFoundException {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("sql.loc", TestingDatabase.getDBUrl());
		map.put("sql.user", TestingDatabase.getDBUser());
		map.put("sql.pass", TestingDatabase.getDBPassword());
		FakedBundleContext context = new FakedBundleContext(map);

		org.apache.log4j.Logger.getRootLogger().addAppender(
				new ConsoleAppender());

		OSGIRunner runnerAmbu = new OSGIRunner(context,
				new AmbulanceActivator());
		Thread t1 = new Thread(runnerAmbu);
		t1.run();

		OSGIRunner runnerInfo = new OSGIRunner(context,
				new InformationBrokerActivator());
		Thread t2 = new Thread(runnerInfo);
		t2.run();

		OSGIRunner runnerMaster = new OSGIRunner(context,
				new ActuatorMasterActivator());
		Thread t3 = new Thread(runnerMaster);
		t3.run();
		
		OSGIRunner runnerContainer = new OSGIRunner(context,
				new ContainerManagerActivator());
		Thread t4 = new Thread(runnerContainer);
		t4.run();


	}
}
