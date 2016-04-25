package org.fortiss.smg.rulesystem.test;


import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.rulesystem.api.Rule;
import org.fortiss.smg.rulesystem.impl.executor.RuleSystemDroolsDrl;
import org.fortiss.smg.rulesystem.impl.executor.RuleSystemDroolsImpl;
import org.fortiss.smg.rulesystem.impl.executor.SendEmail;
import org.fortiss.smg.rulesystem.impl.persistor.RuleSystemDBImpl;
import org.fortiss.smg.rulesystem.impl.ruleManager.RuleParserImpl;
import org.fortiss.smg.rulesystem.impl.ruleManager.RuleSystemImpl;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.fortiss.smg.sqltools.lib.TestingDatabase;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
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

	@Before
	public void setUp() throws IOException, TimeoutException,ClassNotFoundException{

		db= new TestingDBUtil();
		String sql = "TRUNCATE Rule";
		db.executeQuery(sql);
		sql = "TRUNCATE Rule_Notification";
		db.executeQuery(sql);
		sql = "TRUNCATE Rule_Command";
		db.executeQuery(sql);

		db = new TestingDBUtil();
		dbImpl = new RuleSystemDBImpl(db);
		droolsImpl = new RuleSystemDroolsImpl(db);
		impl = new RuleSystemImpl();
		drlImpl = new RuleSystemDroolsDrl();

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
//	@Test
//	public void testDBMethods() throws Exception{
//
//		RuleParserImpl parser = new RuleParserImpl();
//
//		String fileName = ("/Users/sajjad/Documents/workspace/smg2/rulesystem.impl/src/main/resources/rules.xml");
//		//		String fileName = (System.getProperty("user.home"+"/smg2/rulesystem.impl/src/main/resources/rules.xml");
//		List<Rule> ruleElements = parser.parseXML(fileName);
//
//		for(Rule rule : ruleElements){
//			assertEquals(true, dbImpl.insertIntoRuleTable(rule));
//		}
//
//		List<Rule>cmdRules = dbImpl.getAllCmdRules();
//		assertEquals(1,cmdRules.size());
//
//		List<Rule>notifyRules = dbImpl.getAllNotifyRules();
//		assertEquals(1,notifyRules.size());
//
//		List<Rule>allDBRules = dbImpl.getAllRulesFromDB();
//		assertEquals(2,allDBRules.size());
//
//
//		assertEquals(true, dbImpl.deleteFromRuleTable("TestRule1"));
//
//	}

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
	//
	//	
	@Test
	public void testOnDoubleEventReceived() throws Exception{
		try{
//			Rule rule = new Rule();
//			rule.setName("Rule2");
//			rule.setCron(3);
//			rule.setRuleCondition("Events( origin == \"y\" , val : value > 0)");
//			rule.setUserId(1);
//			rule.setContainerId("testContainerID");
//			rule.setRuleType("command");
//			rule.setConsequence("System.out.println( \"RULESYSTEM:: Rule2 Triggered\")");
//			rule.setCommand(true);
//			rule.setAgendaGroup("active");
//
//			assertEquals(true,impl.createRule(rule));
			
			DoubleEvent event = new DoubleEvent(130);
			DeviceId id = new DeviceId("0001860B-151", "");
			String client = "client";

			droolsImpl.onDoubleEventReceived(event, id, client);
//			assert(droolsImpl.onDoubleEventReceived(event, new DeviceId("devID",  "wrapperId"), "client");
		}catch(Exception e){
			logger.debug("Exception "+e);
			e.printStackTrace();
		}
	}

}
