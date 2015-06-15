/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.rulesystem.api.RuleInterface;
import org.fortiss.smg.rulesystem.api.RuleSystemDBInterface;
import org.fortiss.smg.sqltools.lib.builder.DeleteQuery;
import org.fortiss.smg.sqltools.lib.builder.InsertQuery;
import org.fortiss.smg.sqltools.lib.builder.SelectQuery;
import org.fortiss.smg.actuatormaster.api.IActuatorListener;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.IDatabase;
import org.slf4j.Logger;


/**
 * @author pragyagupta
 *
 */
public class RuleSystemDBImpl implements RuleSystemDBInterface, IActuatorListener{

		private static final String DB_NAME_RS = "Rule";
		private static final String DB_NAME_RS_NOTIFY = "Rule_Notification";
		private static final String DB_NAME_RS_CMD = "Rule_Command";
		private static Logger logger = org.slf4j.LoggerFactory
				.getLogger(RuleSystemDBImpl.class);
		
		IDatabase database;
		
		/**
		 * Constructor of the rulesystem dealing with information broker
		 * @param infoBroker
		 */
		public RuleSystemDBImpl(IDatabase database) {
			this.database = database;
		}

		
		@Override
		public boolean insertIntoRuleTable(RuleInterface rule) throws Exception, SQLException {
			boolean result = false; int ruleIdGen ;
			try {
				logger.debug("Insert the rule in DB");
				InsertQuery query = new InsertQuery(DB_NAME_RS);
				query.columns("RuleName", "Cron", "RuleCondition", 
						"UserID", "ContainerID", "RuleType");
			
				query.values(rule.getName(),rule.getCron(),rule.getRuleCondition(), 
						rule.getUserId(), rule.getContainerId(),rule.getRuleType());
			
				logger.debug(query.toString());
				
				result = database.executeQuery(query.toString());
//				System.out.println ("Query Executed in Rule Table " + query.toString());
				
				String ruleType = rule.getRuleType();
				
				if ((result == true) && (rule.getRuleType()!= null)){
//					logger.debug("Query succesfully inserted in the table");
					
					ruleIdGen= getRuleIDWithType(ruleType);
//					System.out.println("ruleIdGen: "+ ruleIdGen);
					boolean queryResult ;
					if (ruleType.equals("notification"))
						queryResult	= insertIntoNotificationTable(ruleIdGen, rule);
					else
						queryResult= insertIntoCommandTable(ruleIdGen, rule);
					
					return queryResult;
				}
				else 
					return false;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		private boolean insertIntoNotificationTable(int ruleIdGen, RuleInterface rule) {
			boolean result= false;
			try {
				logger.debug("Insert the notification rule in DB");
			
				if (rule.getMailNotifyContent()!= null){
					InsertQuery mailQuery = new InsertQuery(DB_NAME_RS_NOTIFY);
					
					mailQuery.columns("RuleID", "NotifyType", "Content");
					mailQuery.values(ruleIdGen,"Mail",rule.getMailNotifyContent());
					
					result = database.executeQuery(mailQuery.toString());
					
					logger.debug(mailQuery.toString());
				}
				if (rule.getSMSNotifyContent()!= null){
					InsertQuery SMSQuery = new InsertQuery(DB_NAME_RS_NOTIFY);
					
					SMSQuery.columns("RuleID", "NotifyType", "Content");
					SMSQuery.values(ruleIdGen,"SMS",rule.getSMSNotifyContent());
					
					result = database.executeQuery(SMSQuery.toString());
					
					logger.debug(SMSQuery.toString());
				}
				if (rule.getTweetNotifyContent()!= null){
					
					InsertQuery TweetQuery = new InsertQuery(DB_NAME_RS_NOTIFY);
					
					TweetQuery.columns("RuleID", "NotifyType", "Content");
					TweetQuery.values(ruleIdGen,"Tweet",rule.getTweetNotifyContent());

					result = database.executeQuery(TweetQuery.toString());
					
					logger.debug(TweetQuery.toString());
				}
				
				if (result == false)
						System.out.println("this rule cannot be added: check sql query");
				else return true;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result;
		}
		
		private boolean insertIntoCommandTable(int ruleIdGen, RuleInterface rule) {
			boolean result= false;
			try {
				logger.debug("Insert the command rule in DB");
			
				if (rule.getCommand()!= null){
					InsertQuery cmdQuery = new InsertQuery(DB_NAME_RS_CMD);
					
					cmdQuery.columns("RuleID", "Content");
					cmdQuery.values(ruleIdGen,rule.getCommand());
					
//					System.out.println(cmdQuery.toString());
					result = database.executeQuery(cmdQuery.toString());
					
					logger.debug(cmdQuery.toString());
				}
						
				if (result == false)
						System.out.println("this rule cannot be added: check sql query");
				else return true;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result;
		}

		@Override
		public boolean deleteFromRuleTable(String ruleName) throws Exception {
			logger.debug("Deleting rule from DB");
			boolean deleted = false; 
			logger.debug("Delete Rule with rule name: " + ruleName );
			
			int ruleId= getRuleIDWithName(ruleName);
//			System.out.println("ruleId: " +ruleId);
			try {
				DeleteQuery deleteFromCmdTable = new DeleteQuery( DB_NAME_RS_CMD );
				deleteFromCmdTable.addWhere("RuleID= " + ruleId);
				deleted = database.executeQuery(deleteFromCmdTable.toString());
//				System.out.println("result of Cmd Table: "  +deleted);
				
				DeleteQuery deleteFromNotifyTable = new DeleteQuery( DB_NAME_RS_NOTIFY );
				deleteFromNotifyTable.addWhere("RuleID= " + ruleId);
				deleted = database.executeQuery(deleteFromNotifyTable.toString());
//				System.out.println("result of Notify Table: "  +deleted);
				
				DeleteQuery deleteFromRuleTable = new DeleteQuery( DB_NAME_RS );
				deleteFromRuleTable.addWhere("RuleID = " + ruleId);
//				System.out.println(deleteFromRuleTable.toString());
				deleted = database.executeQuery(deleteFromRuleTable.toString());
//				System.out.println("result of Rule Table: "+ deleted);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return deleted;
		}
		
		private int getRuleIDWithType(String type) {
			int ruleIdGen = 0;
			SelectQuery select= new SelectQuery();
			
			select.addFrom(DB_NAME_RS);
			select.addColumn("RuleID");
			select.addWhere("RuleType = \"" +type+ "\"");
			
			List <Map<String, Object>>resultSet = null;
			try {
				resultSet = database.getSQLResults(select.toString());
//				logger.debug( resultSet);
				for (Map<String, Object> set : resultSet) {
					
//					if(set.get("ruleid")!=null)
//						logger.debug("Rule Id generated:: " + set.get("ruleid").toString());
						ruleIdGen = Integer.parseInt(set.get("ruleid").toString());
//					else
//						ruleIdGen =365;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ruleIdGen;
		}
		
		private int getRuleIDWithName(String ruleName) {
			int ruleIdGen= 0;
			SelectQuery select= new SelectQuery();
			
			select.addFrom(DB_NAME_RS);
			select.addColumn("RuleID");
			select.addWhere("RuleName = " + ruleName  );
			
			List <Map<String, Object>>resultSet = null;
			try {
				resultSet = database.getSQLResults(select.toString());
				for (Map<String, Object> set : resultSet) {
					
					if(set.get("ruleid")!=null)
//						logger.debug("Rule Id generated:: " + set.get("ruleid").toString());
						ruleIdGen = Integer.parseInt(set.get("ruleid").toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ruleIdGen;
		}
		
		@Override
		public boolean updateRule(RuleInterface rule) throws Exception {
			logger.debug("updating rule for " +rule.getId());
			boolean result = false;
//			if (dbUtil.checkOrOpenDBConnection()) {
//				try {
//					PreparedStatement ps = dbUtil.getCon().prepareStatement("UPDATE RuleInterface SET RuleName=?,Cron=?, RuleCondition=?,"
//							+ "Consequence=?, UserID=?, ContainerID=?, Type=? WHERE ruleID = ?");
//					ps.setString(1, rule.getName());
//					ps.setInt(2, rule.getCron());
//					ps.setString(3, rule.getRuleCondition());
//					ps.setString(4, rule.getConsequence());
//					ps.setLong(5, rule.getUserId());
//					ps.setString(6, rule.getContainerId());
//					ps.setString(7,  rule.getRuleType());
//					ps.setInt(8,  rule.getId());
//					if( ps.executeUpdate() > 0){
//						result = true;
//						return result;
//					}
//				}catch (IllegalArgumentException e) {
//					dbUtil.getCon().rollback();
//					throw e;
//				} 
//			}else {
//				logger.debug("DB connection is closed");
//				logger.error("could not update rule " + rule.getId() );
//			}

			return result;
		}


		@Override
		public String doSomething(String arg) throws TimeoutException {
			return "Hello smg";
		}
		
		@Override
		public boolean isComponentAlive() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public List<Map<String, Object>> getAllCmdRules() throws Exception {
			List<Map<String, Object>> resultSet = null;
			try {
				SelectQuery select= new SelectQuery();
				select.addFrom(DB_NAME_RS_CMD + " rule_cmd");
				select.join(DB_NAME_RS + " rule", "rule.RuleID= " + "rule_cmd.RuleID");
			
				resultSet = database.getSQLResults(select.toString());
				
//			 	System.out.println (resultSet.size() + " \n " +select.toString());
			}catch (Exception e) {
				e.printStackTrace();
			}
			return resultSet;
		}

		@Override
		public List<Map<String, Object>> getAllNotifyRules() throws Exception {
			List<Map<String, Object>> resultSet = null;
			try {
				SelectQuery select= new SelectQuery();
				select.addFrom(DB_NAME_RS_NOTIFY + " rule_notify");
				select.join(DB_NAME_RS + " rule", "rule.RuleID= " + "rule_notify.RuleID");
			
				resultSet = database.getSQLResults(select.toString());
//				System.out.println (resultSet + " \n " +select.toString());
			}catch (Exception e) {
				e.printStackTrace();
			}
			return resultSet;
		}

		@Override
		public List<Map<String, Object>> getAllRulesFromDB() throws Exception {
			List<Map<String, Object>> resultSet = null;
			try {
				resultSet = getAllCmdRules() ;
				resultSet.addAll(getAllNotifyRules());
			}catch (Exception e) {
				e.printStackTrace();
			}
			return resultSet;
		}
	/*	private RuleInterface getResultFromQuery(ResultSet results) throws SQLException {
			int id = results.getInt("Id");
			String name = results.getString("name");
			int cron = Integer.parseInt(results.getString("cron"));
			String condition = results.getString("condition");
			int userId= results.getInt("userId");
			String containerId = results.getString("containerId");
			String ruleType= results.getString("ruleType");
			String consequence= results.getString("consequence");
			
			Rule rule = new Rule();
			rule.setId(id);
			rule.setName(name);
			rule.setCron(cron);
			rule.setRuleCondition(condition);
			rule.setUserId(userId);
			rule.setContainerId(containerId);
			rule.setRuleType(ruleType);
			rule.setConsequence(consequence);
			return rule;
		}*/

		@Override
		public void onDoubleEventReceived(DoubleEvent ev, DeviceId dev, String client) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDeviceEventReceived(DeviceEvent ev, String client) {
			// TODO Auto-generated method stub
			
		}

}
