package org.fortiss.smg.rulesystem.test;


import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.ContainerManagerQueueNames;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
//import org.fortiss.smg.containermanager.impl.ContainerManagerImpl;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.rulesystem.api.Rule;
import org.fortiss.smg.rulesystem.impl.executor.Commander;
import org.fortiss.smg.rulesystem.impl.executor.RuleSystemDroolsDrl;
import org.fortiss.smg.rulesystem.impl.executor.RuleSystemDroolsImpl;
import org.fortiss.smg.rulesystem.impl.executor.SendEmail;
import org.fortiss.smg.rulesystem.impl.persistor.RuleSystemDBImpl;
import org.fortiss.smg.rulesystem.impl.ruleManager.RuleParserImpl;
import org.fortiss.smg.rulesystem.impl.ruleManager.RuleSystemImpl;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.fortiss.smg.sqltools.lib.TestingDatabase;
import org.fortiss.smg.sqltools.lib.serialize.SimpleSerializer;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import org.slf4j.LoggerFactory;

public class TestRuleSystemSimple {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(TestRuleSystemSimple.class);

	public RuleSystemDroolsImpl droolsImpl;
	public RuleSystemDBImpl dbImpl;
	public RuleSystemImpl impl;
	public RuleSystemDroolsDrl drlImpl;
	private TestingDBUtil db;
	private IActuatorClient ActuatorClient;
	private DeviceId devObj;
	private ContainerManagerInterface container;
	private SendEmail mail;
	
	private static org.fortiss.smg.testing.MockOtherBundles mocker;

	@BeforeClass
	public static void setUpDataBase() throws SQLException,
			ClassNotFoundException {
		ArrayList<String> bundles = new ArrayList<String>();

		bundles.add("Ambulance");
		bundles.add("InformationBroker");
		bundles.add("ActuatorMaster");
		bundles.add("ContainerManager");
		
		mocker = new org.fortiss.smg.testing.MockOtherBundles(bundles);
	}

	@Before
	public void setUp() throws IOException, TimeoutException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		db= new TestingDBUtil();
		
		System.out.println("Statement created...");
		String sql = "TRUNCATE Rule";
		db.executeQuery(sql);
		sql = "TRUNCATE Rule_Notification";
		db.executeQuery(sql);
		sql = "TRUNCATE Rule_Command";
		db.executeQuery(sql);
		System.out.println("Query executed...");

		System.out.println("searching for informationbroker");
		DefaultProxy<InformationBrokerInterface> clientInfo = new DefaultProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(), 300);

		InformationBrokerInterface broker = clientInfo.init();
		System.out.println("found informationbroker");
		
		db = new TestingDBUtil();
		dbImpl = new RuleSystemDBImpl(db);
		droolsImpl = new RuleSystemDroolsImpl(db);
		impl = new RuleSystemImpl();
		System.out.println("init RuleSystem");
		drlImpl = new RuleSystemDroolsDrl();

		
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("id", 1);
//		map.put("roomRoleId", 1);
//		map.put("user_id", 2);
//		SimpleSerializer.saveToDB(map, broker, "Room");
	}


	@After
	public void tearDown(){
		// TODO do some cleanup
	}

	//	@Test
	//	public void testXMLParser() throws Exception{
	//		try{
	//			RuleParserImpl parser = new RuleParserImpl();
	//		}
	//		catch ( Exception e){
	//			e.getMessage();
	//		}
	//	}

	/**
	 * Start: Testing Database related methods
	 * Testing the methods of RuleSystemDBImpl class
	 * @throws Exception	
	 */
	@Test
	public void testDBMethods() throws Exception{
		RuleParserImpl parser = new RuleParserImpl();
		
		String fileName = "/opt/felix/rules.xml";
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			fileName = "c:/opt/felix/rules.xml";
		}
		
		List<Rule> ruleElements = parser.parseXML(fileName);

		for(Rule rule : ruleElements){
			assertEquals(true, dbImpl.insertIntoRuleTable(rule));
		}

		List<Rule>cmdRules = dbImpl.getAllCmdRules();
		assertEquals(1,cmdRules.size());

		List<Rule>notifyRules = dbImpl.getAllNotifyRules();
		assertEquals(1,notifyRules.size());

		List<Rule>allDBRules = dbImpl.getAllRulesFromDB();
		assertEquals(2,allDBRules.size());


		assertEquals(true, dbImpl.deleteFromRuleTable("TestRule1"));

	}

	/**
	 * End: Testing Database related methods finished
	 */


	/**
	 * Start: Testing the methods of RuleSystemDroolsImpl class 
	 * 
	 * @throws Exception
	 */

//	@Test(timeout=500000)
//	public void testDRL() throws Exception{
//		try{
//			Rule rule = new Rule();
//			rule.setName("Rule Event_container");
//			rule.setCron(3);
//			rule.setRuleCondition("Events( origin == \"solarlog.wrapper.solar_generator_watt\")");
//			rule.setUserId(1);
//			rule.setContainerId("testContainerID");
//			rule.setRuleType("command");
//			rule.setConsequence("System.out.println( \"Rule Event_container :: output ::\")");
//			rule.setCommand(true);
//
//			logger.info("before addRule call");
//
//			assertEquals(true,impl.createRule(rule));
//			assertEquals(true,impl.removeRule(rule.getName()));
//
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}

	//	@Test
	//	public void testDroolsImpl() throws Exception{
	//		String	 drlRule = "\n rule \"RuleInterface Message_Hello\""
	//				+ "\n when"
	//				+ "\n \t Message( status: Message.HELLO, myMessage : message )"
	//				+ "\n \t eval (status == 0)"
	//				+ "\n then"
	//				+ "\n \t System.out.println( \"RuleInterface Message_Hello :: output :: \" + myMessage  );"
	//				+ "\n end" ;
	//
	//		String rule2 =  "\nrule \"RuleInterface Event_container\" \n "
	//				+ "when \n"
	//				+ "\tEvents( containerId : origin,  val : value)\n"
	//				+ "\t eval (val == 4.0) \n"
	//				+ "then \n"
	//				+ "\t System.out.println( \"RuleInterface Event_container :: output ::\" + val ); \n"
	//				+ "end \n";
	//
	//		assertEquals(true, drlImpl.writeRule(drlRule));
	//		DoubleEvent event = new DoubleEvent(4.0);
	//		DeviceId id = new DeviceId("devId", "wrapperId");
	//		String client = "client";
	//
	//		Rule rule = new Rule();
	//		rule.setCommand(true);
	//		rule.setName("RuleInterface Message_Hello");
	//		rule.setRuleCondition("Events( containerId : origin,  val : value)");
	//		rule.setConsequence(" System.out.println( \"RuleInterface Event_container :: output ::\" + val );");
	//		
	//		droolsImpl.onDoubleEventReceived(event, id, client);
	//		assertEquals(true, droolsImpl.addRule(rule));
	//
	//	}
	
	
//	@Test
//	public void testOnDoubleEventReceived() throws Exception{
//		try{
////			Rule rule = new Rule();
////			rule.setName("Rule2");
////			rule.setCron(3);
////			rule.setRuleCondition("Events( origin == \"y\" , val : value > 0)");
////			rule.setUserId(1);
////			rule.setContainerId("testContainerID");
////			rule.setRuleType("command");
////			rule.setConsequence("System.out.println( \"RULESYSTEM:: Rule2 Triggered\")");
////			rule.setCommand(true);
////			rule.setAgendaGroup("active");
////
////			assertEquals(true,impl.createRule(rule));
//			
//			DoubleEvent event = new DoubleEvent(130);
//			DeviceId id = new DeviceId("0001860B-151", "");
//			String client = "client";
//
//			droolsImpl.onDoubleEventReceived(event, id, client);
////			assert(droolsImpl.onDoubleEventReceived(event, new DeviceId("devID",  "wrapperId"), "client");
//		}catch(Exception e){
//			logger.debug("Exception "+e);
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void testSendCommand() throws Exception{
//		try{
//			DoubleCommand command = new DoubleCommand(1);
//			System.out.println ( "command: "+ command.getValue());
//
//			DeviceId id = new DeviceId("office1030light", "enoceanUSB.wrapper");
//			System.out.println ("wrapperID: "+ id.getWrapperId()+ "  devID: "+ id.getDevid());
//
//			DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
//					ContainerManagerInterface.class,
//					ContainerManagerQueueNames
//					.getContainerManagerInterfaceQueryQueue(), 50000);
//			container = clientInfo.init();
//			container.sendCommand(new DoubleCommand (command.getValue()), new DeviceId (id.getDevid(), id.getWrapperId()));
//			System.out.println("COMMAND:: issued to Device :: value " + command.getValue() + " sent to " + id.getDevid());
//
//			// assert(droolsImpl.onDoubleEventReceived(event, new DeviceId("devID",  "wrapperId"), "client");
//		}catch(Exception e){
//			logger.debug("Exception "+e);
//			e.printStackTrace();
//		}
//	}
	
	@Test
	public void testWorkflow() throws Exception{
		try{
			RuleParserImpl parserImpl = new RuleParserImpl();
			List<Rule> ruleList = parserImpl.parseXML("/opt/felix/rules.xml");
			RuleSystemDBImpl dbImpl = new RuleSystemDBImpl(db);
			for (Rule rule : ruleList) {
				dbImpl.insertIntoRuleTable(rule);
			}
			List<Rule> dbRuleList = dbImpl.getAllRulesFromDB();
			//creating drl file
			RuleSystemImpl ruleSystemImpl = new RuleSystemImpl();
			ruleSystemImpl.createDrlFile(dbRuleList.get(0));
			RuleSystemDroolsImpl executeRule = new RuleSystemDroolsImpl(db);
		}catch(Exception e){
			logger.debug("Exception "+e);
			e.printStackTrace();
		}

	}
/*
	@Test
	public void testSendCommand() throws Exception{
		try{
			
			
			DoubleCommand command = new DoubleCommand(130);
			System.out.println ( "command: "+ command.getValue());
						
			DeviceId id = new DeviceId("30office1030light", "enocean.wrapper");
			System.out.println ("wrapperID: "+ id.getWrapperId()+ "  devID: "+ id.getDevid());

			DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
					ContainerManagerInterface.class,
					ContainerManagerQueueNames
							.getContainerManagerInterfaceQueryQueue(), 50000);
			container = clientInfo.init();
			container.sendCommand(new DoubleCommand (command.getValue()), new DeviceId (id.getDevid(), id.getWrapperId()));
			
			System.out.println("COMMAND:: issued to Device :: value " + command.getValue() + " sent to " + id.getDevid());
			clientInfo.destroy();
			
//			assert(droolsImpl.onDoubleEventReceived(event, new DeviceId("devID",  "wrapperId"), "client");
		}catch(Exception e){
			logger.debug("Exception "+e);
			e.printStackTrace();
		}
	}
	*/
}
