package org.fortiss.smg.testing;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.ConsoleAppender;
import org.fortiss.smg.sqltools.lib.TestingDatabase;


public class MockOtherBundles {

	FakedBundleContext context;
	
	public MockOtherBundles(ArrayList<String> bundles) throws ClassNotFoundException {
		

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("sql.loc", TestingDatabase.getDBUrl());
		map.put("sql.user", TestingDatabase.getDBUser());
		map.put("sql.pass", TestingDatabase.getDBPassword());
		context = new FakedBundleContext(map);

		org.apache.log4j.Logger.getRootLogger().addAppender(
				new ConsoleAppender());


		if (bundles.contains("Ambulance")) {
			OSGIRunner runnerAmbu = new OSGIRunner(context,
					new org.fortiss.smg.ambulance.impl.AmbulanceActivator());
			Thread t1 = new Thread(runnerAmbu);
			t1.run();
			System.out.println("Started: Ambulance");
		}
		if (bundles.contains("InformationBroker")) {
			OSGIRunner runnerInfo = new OSGIRunner(context,
					new org.fortiss.smg.informationbroker.impl.InformationBrokerActivator());
			Thread t2 = new Thread(runnerInfo);
			t2.run();
			System.out.println("Started: InformationBroker");
		}
		if (bundles.contains("ActuatorMaster")) {
			OSGIRunner runnerMaster = new OSGIRunner(context,
					new org.fortiss.smg.actuatormaster.impl.ActuatorMasterActivator());
			Thread t3 = new Thread(runnerMaster);
			t3.run();
			System.out.println("Started: ActuatorMaster");
		}
		if (bundles.contains("ContainerManager")) {
			OSGIRunner runnerContainer = new OSGIRunner(context,
					new org.fortiss.smg.containermanager.impl.ContainerManagerActivator());
			Thread t4 = new Thread(runnerContainer);
			t4.run();
			System.out.println("Started: ContainerManager");
		}
		if (bundles.contains("UserManager")) {
			OSGIRunner runnerUser = new OSGIRunner(context,
					new org.fortiss.smg.usermanager.impl.UserManagerActivator());
			Thread t5 = new Thread(runnerUser);
			t5.run();
			System.out.println("Started: UserManager");
		}
		
		
		


	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public Object getContext() {
		// TODO Auto-generated method stub
		return context;
	}
	
}
