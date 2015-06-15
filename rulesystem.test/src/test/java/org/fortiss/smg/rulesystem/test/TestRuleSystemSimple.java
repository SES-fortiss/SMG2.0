/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.test;


import org.fortiss.smg.rulesystem.api.RuleSystemInterface;
import org.fortiss.smg.rulesystem.impl.Rule;
import org.fortiss.smg.rulesystem.impl.RuleParserImpl;
import org.fortiss.smg.rulesystem.impl.RuleSystemDBImpl;
import org.fortiss.smg.rulesystem.impl.RuleSystemDroolsImpl;
import org.fortiss.smg.sqltools.lib.TestingDatabase;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.slf4j.LoggerFactory;

public class TestRuleSystemSimple {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(TestRuleSystemSimple.class);

	public RuleSystemDroolsImpl droolsImpl;
	public RuleSystemDBImpl impl;
	
	private TestingDBUtil db;

	@Before
	public void setUp() throws IOException, TimeoutException,ClassNotFoundException{
		String dburl = "jdbc:"+ TestingDatabase.getDBUrl();
		String dbuser = TestingDatabase.getDBUser();
		String dbpassword = TestingDatabase.getDBPassword();
		
		db= new TestingDBUtil();
		String sql = "TRUNCATE Rule";
		db.executeQuery(sql);
		sql = "TRUNCATE Rule_Notification";
		db.executeQuery(sql);
		sql = "TRUNCATE Rule_Command";
		db.executeQuery(sql);

		db = new TestingDBUtil();
		impl = new RuleSystemDBImpl(db);
		droolsImpl = new RuleSystemDroolsImpl(db);
		
	}


	@After
	public void tearDown(){
            // TODO do some cleanup
        }
	
	@Test
	public void testXMLParser() throws Exception{
		try{
			RuleParserImpl parser = new RuleParserImpl();
		}
		catch ( Exception e){
			e.getMessage();
		}
	}
 
	/**
	 * Start: Testing Database related methods
	 * Testing the methods of RuleSystemDBImpl class
	 * @throws Exception	
	 */
	@Test
	public void testDBMethods() throws Exception{

		RuleParserImpl parser = new RuleParserImpl();
		String fileName = "/Users/pragyagupta/Projects/SMG_GIT/smg2/rulesystem.impl/src/main/resources/rules.xml";
		List<Rule> ruleElements = parser.parseXML(fileName);
		
		for(Rule rule : ruleElements){
            assertEquals(true, impl.insertIntoRuleTable(rule));
        }
//		Rule ruleObj = new Rule();
//		ruleObj.setName("DummyRule");
//		ruleObj.setCron(3);
//		ruleObj.setRuleCondition("some boolean expression");
//		ruleObj.setUserId(123);
//		ruleObj.setContainerId("abcdef");
//		ruleObj.setRuleType("command");
//		Assert.assertEquals(true, impl.insertRule(ruleImpl));
	
		List<Map<String, Object>>cmdRules = impl.getAllCmdRules();
		assertEquals(1,cmdRules.size());
		
		List<Map<String, Object>>notifyRules = impl.getAllNotifyRules();
		assertEquals(3,notifyRules.size());
		
		
		List<Map<String, Object>>allDBRules = impl.getAllRulesFromDB();
		assertEquals(4,allDBRules.size());
		
//		Assert.assertEquals(true, impl.deleteFromRuleTable("\"TestRule1\""));
//		allDBRules = impl.getAllRulesFromDB();
//		assertEquals(1,allDBRules.size());
		
		
	}
	/**
	 * End: Testing Database related methods finished
	 */
	
	/**
	 * Start: Testing Database related methods	
	 * Testing the methods of RuleSystemDroolsImpl class
	 * @throws Exception
	 */
	@Test
	public void testLoadRulesFromDB() throws Exception{
		try{
			Assert.assertEquals(true, droolsImpl.loadRulesFromDB());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test(timeout=500000)
	public void testAddRule() throws Exception{
		try{
			
		String rule = null;
		logger.info("before addRule call");
		assertEquals(true, droolsImpl.addRule(rule));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWriteRuleInFile() throws Exception{
//			String	 rule = "\n rule \"RuleInterface Message_Hello\""
//						+ "\n when"
//						+ "\n \t Message( status: Message.HELLO, myMessage : message )"
//						+ "\n \t eval (status == 0)"
//						+ "\n then"
//						+ "\n \t System.out.println( \"RuleInterface Message_Hello :: output :: \" + myMessage  );"
//						+ "\n end" ;
//		
//		String rule =  "\nrule \"RuleInterface Event_container\" \n "
//				+ "when \n"
//				+ "\tEvents( containerId : origin,  val : value)\n"
//				+ "\t eval (val == 4.0) \n"
//				+ "then \n"
//				+ "\t System.out.println( \"RuleInterface Event_container :: output ::\" + val ); \n"
//				+ "end \n";
//		
//		assertEquals(true, rsImpl.writeRuleInFile(rule));
//		DoubleEvent event = new DoubleEvent(4.0);
//		DeviceId id = new DeviceId("devId", "wrapperId");
//		String client = "client";
//	
//		droolsImpl.onDoubleEventReceived(event, id, client);
//		assertEquals(true, droolsImpl.addRule(rule));
		
	}

	
	@Test(timeout=500000)
	public void testOnDoubleEventReceived() throws Exception{
		try{
					
		DoubleEvent event = new DoubleEvent(130);
		DeviceId id = new DeviceId("solarlog.wrapper.solar_generator_watt", "solarlog.wrapper");
		String client = "client";
	
		droolsImpl.onDoubleEventReceived(event, id, client);
		
//		assert(droolsImpl.onDoubleEventReceived(event, new DeviceId("devID",  "wrapperId"), "client");
		}catch(Exception e){
			logger.debug("Exception "+e);
		}
	}
	
}
