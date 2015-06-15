/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.testing;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.ConsoleAppender;
/*import org.fortiss.smg.actuatormaster.impl.ActuatorMasterActivator;
import org.fortiss.smg.ambulance.impl.AmbulanceActivator;
import org.fortiss.smg.containermanager.impl.ContainerManagerActivator;
import org.fortiss.smg.informationbroker.impl.InformationBrokerActivator;
import org.fortiss.smg.usermanager.impl.UserManagerActivator;
*/
import org.fortiss.smg.sqltools.lib.TestingDatabase;


public class MockOtherBundles {

	
	
	public MockOtherBundles(ArrayList<String> bundles) throws ClassNotFoundException {
		

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("sql.loc", TestingDatabase.getDBUrl());
		map.put("sql.user", TestingDatabase.getDBUser());
		map.put("sql.pass", TestingDatabase.getDBPassword());
		FakedBundleContext context = new FakedBundleContext(map);

		org.apache.log4j.Logger.getRootLogger().addAppender(
				new ConsoleAppender());


		if (bundles.contains("Ambulance")) {
			OSGIRunner runnerAmbu = new OSGIRunner(context,
					new org.fortiss.smg.ambulance.impl.AmbulanceActivator());
			Thread t1 = new Thread(runnerAmbu);
			t1.run();
		}
		if (bundles.contains("InformationBroker")) {
			OSGIRunner runnerInfo = new OSGIRunner(context,
					new org.fortiss.smg.informationbroker.impl.InformationBrokerActivator());
			Thread t2 = new Thread(runnerInfo);
			t2.run();
		}
		if (bundles.contains("ActuatorMaster")) {
			OSGIRunner runnerMaster = new OSGIRunner(context,
					new org.fortiss.smg.actuatormaster.impl.ActuatorMasterActivator());
			Thread t3 = new Thread(runnerMaster);
			t3.run();
		}
		if (bundles.contains("ContainerManager")) {
			OSGIRunner runnerContainer = new OSGIRunner(context,
					new org.fortiss.smg.containermanager.impl.ContainerManagerActivator());
			Thread t4 = new Thread(runnerContainer);
			t4.run();
		}
		if (bundles.contains("UserManager")) {
			OSGIRunner runnerUser = new OSGIRunner(context,
					new org.fortiss.smg.usermanager.impl.UserManagerActivator());
			Thread t5 = new Thread(runnerUser);
			t5.run();
		}
		
		
		


	}
	
}
