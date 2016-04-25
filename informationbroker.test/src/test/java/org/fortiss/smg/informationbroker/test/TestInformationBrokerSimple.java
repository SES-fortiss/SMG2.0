package org.fortiss.smg.informationbroker.test;


import static org.junit.Assert.assertEquals;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.fortiss.smg.config.lib.Ops4JTestTime;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterfaceForICTProject;
import org.fortiss.smg.informationbroker.impl.InformationBrokerActivator;
import org.fortiss.smg.informationbroker.impl.persistency.PersistencyDBUtil;
import org.fortiss.smg.informationbroker.impl.persistency.PersistencyLog;
import org.fortiss.smg.informationbroker.impl.persistency.PersistencyQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.slf4j.LoggerFactory;

//@RunWith(PaxExam.class)
//@ExamReactorStrategy(PerMethod.class)
public class TestInformationBrokerSimple {

	//private InformationBrokerInterfaceForICTProject impl;
	private PersistencyLog impl;
	private PersistencyQuery implQuery;

	@Configuration
	public Option[] config() {
		// this is used to build up a default OSGi Container and inject the SMG scope
		// add here all API-libraries of the smg project on which your api & impl depend on
		Option[] defaultSpace = Ops4JTestTime.getOptions();
		Option[] currentSpace = options(
				mavenBundle("org.fortiss.smartmicrogrid", "informationbroker.api",
						"1.0-SNAPSHOT"),
						mavenBundle("org.fortiss.smartmicrogrid", "informationbroker.impl",
								"1.0-SNAPSHOT"));

		return OptionUtils.combine(defaultSpace, currentSpace);
	}

	@Before
	public void setUp() {

		String olddbConnectionString = "jdbc:mysql://localhost:3306/ses_open_database";
		String dbConnectionString = olddbConnectionString;
		
		String queryOlddbConnectionString = "jdbc:mysql://localhost:3306/fortisstest";
		String queryDbConnectionString = queryOlddbConnectionString;
		String dbictprojectConnectionString = olddbConnectionString;
		String quryDbictprojectConnectionString = olddbConnectionString;
		String dbUser = "fortiss";
		String dbPassword = "foo";


		org.slf4j.Logger logger = LoggerFactory.getLogger(InformationBrokerActivator.class);
		PersistencyDBUtil dbUtil = new PersistencyDBUtil(olddbConnectionString, dbConnectionString, dbictprojectConnectionString, dbUser, dbPassword, logger);
		PersistencyDBUtil queryDbUtil = new PersistencyDBUtil(queryOlddbConnectionString, queryDbConnectionString, quryDbictprojectConnectionString, dbUser, dbPassword, logger);
		impl = new PersistencyLog(dbUtil, null, logger);
		implQuery = new PersistencyQuery(queryDbUtil, null, logger);
	}

	@After
	public void tearDown(){
		// TODO do some cleanup
	}

	@Test(timeout=1000000)
	public void testYourMethod(){

		String devId = "lastTest";
		String deviceRoom = "UtilityRoom";
		String hrName = "";
		String tech = "enOcean";
		String devType = "org.fortiss.ses.Temperature";
		String sensorOrActuator = "Sensor";
		Double minRange =  -10.0;
		Double maxRange = 25.0;
		impl.onDoubleEventReceivedForICTProject(deviceRoom, hrName, devId, tech, devType, 
				sensorOrActuator, minRange, maxRange);


		//assertEquals(0, "");


		DeviceId testing = new DeviceId("a","b");

		//DeviceContainer test = new DeviceContainer(new DeviceId("a","b"), "c");
		//test.setHrName("hello world");


		//assertEquals("Hello smg",impl.doSomething("hi"));
	}


	@Test 
	public void testLastSeen(){
		String devId = "battery_temperature";
		String wrapperId = "sma.sunny.wrapper.web";
		System.out.println(implQuery.getLastseen(devId, wrapperId).toString());
	}
}