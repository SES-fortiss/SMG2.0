package org.fortiss.smg.rulesystem.impl.persistor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.rulesystem.api.Rule;
import org.fortiss.smg.rulesystem.api.RuleSystemDBInterface;
import org.fortiss.smg.rulesystem.impl.executor.RuleSystemDroolsImpl;
import org.fortiss.smg.sqltools.lib.builder.DeleteQuery;
import org.fortiss.smg.sqltools.lib.builder.InsertQuery;
import org.fortiss.smg.sqltools.lib.builder.SelectQuery;
import org.fortiss.smg.informationbroker.api.IDatabase;
import org.slf4j.Logger;


/**
 * @author pragyagupta
 *
 */
public class RuleSystemDBImpl  implements RuleSystemDBInterface {

	private static final String DB_NAME_RS = "Rule";
	private static final String DB_NAME_RS_NOTIFY = "Rule_Notification";
	private static final String DB_NAME_RS_CMD = "Rule_Command";
	private static Logger logger = org.slf4j.LoggerFactory
			.getLogger(RuleSystemDBImpl.class);

	static IDatabase database;

	/**
	 * Constructor of the rulesystem dealing with information broker
	 * @param infoBroker
	 */
	public RuleSystemDBImpl(IDatabase database) {
		this.database = database;
	}

	
	
	public static boolean insertIntoRuleTable(Rule rule) throws Exception, SQLException {
		boolean result = false; 
		try {
			logger.debug("Insert the rule in DB");
			InsertQuery query = new InsertQuery(DB_NAME_RS);
			query.columns("RuleName", "Cron", "RuleCondition", 
					"UserID", "ContainerID", "RuleType");

			query.values(rule.getName(),rule.getCron(),rule.getRuleCondition(), 
					rule.getUserId(), rule.getContainerId(),rule.getRuleType());

			logger.debug(query.toString());

			result = database.executeQuery(query.toString());

			String ruleType = rule.getRuleType();

			if ((result == true) && (rule.getRuleType()!= null) && (rule.getConsequence()!= null)){
				rule.setRuleId( getRuleIDWithType(ruleType));
				boolean queryResult ;
				if (ruleType.equals("notification"))
					queryResult	= insertIntoNotificationTable(rule);
				else
					queryResult= insertIntoCommandTable( rule);
				return queryResult;
			}
			else 
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static boolean insertIntoNotificationTable( Rule rule) {
		boolean result= false;
		try {
			logger.debug("Insert the notification rule in DB");

			InsertQuery mailQuery = new InsertQuery(DB_NAME_RS_NOTIFY);

			mailQuery.columns("RuleID", "NotifyType", "Content");
			mailQuery.values(rule.getRuleId(),rule.getNotificationType(),rule.getConsequence());

			result = database.executeQuery(mailQuery.toString());

			logger.debug(rule.getConsequence());

			if (result == false)
				System.out.println("this rule cannot be added: check sql query");
			else return true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private static boolean insertIntoCommandTable(Rule rule) {
		boolean result= false;
		try {
			logger.debug("Insert the command rule in DB");
			InsertQuery cmdQuery = new InsertQuery(DB_NAME_RS_CMD);
			cmdQuery.columns("RuleID", "Content");
			cmdQuery.values(rule.getRuleId(),rule.getConsequence());

			result = database.executeQuery(cmdQuery.toString());

			logger.debug(cmdQuery.toString());

			if (result == false)
				System.out.println("this rule cannot be added: check sql query");
			else return true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}


	public static boolean deleteFromRuleTable(String ruleName) throws Exception {
		logger.debug("Deleting rule from DB");
		boolean deleted = false; 
		logger.debug("Delete Rule with rule name: " + ruleName );

		int ruleId= getRuleIDWithName(ruleName);
		try {
			DeleteQuery deleteFromCmdTable = new DeleteQuery( DB_NAME_RS_CMD );
			deleteFromCmdTable.addWhere("RuleID= " + ruleId);
			deleted = database.executeQuery(deleteFromCmdTable.toString());

			DeleteQuery deleteFromNotifyTable = new DeleteQuery( DB_NAME_RS_NOTIFY );
			deleteFromNotifyTable.addWhere("RuleID= " + ruleId);
			deleted = database.executeQuery(deleteFromNotifyTable.toString());

			DeleteQuery deleteFromRuleTable = new DeleteQuery( DB_NAME_RS );
			deleteFromRuleTable.addWhere("RuleID = " + ruleId);
			deleted = database.executeQuery(deleteFromRuleTable.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleted;
	}

	private static int getRuleIDWithType(String type) {
		int ruleIdGen = 0;
		SelectQuery select= new SelectQuery();

		select.addFrom(DB_NAME_RS);
		select.addColumn("RuleID");
		select.addWhere("RuleType = \"" +type+ "\"");

		List <Map<String, Object>>resultSet = null;
		try {
			resultSet = database.getSQLResults(select.toString());
			for (Map<String, Object> set : resultSet) {
				ruleIdGen = Integer.parseInt(set.get("ruleid").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ruleIdGen;
	}

	private static int getRuleIDWithName(String ruleName) {
		int ruleIdGen= 0;
		SelectQuery select= new SelectQuery();			
		select.addFrom(DB_NAME_RS);
		select.addColumn("RuleID");
		select.addWhere("RuleName = '" + ruleName +"'");

		List <Map<String, Object>>resultSet = null;
		try {
			resultSet = database.getSQLResults(select.toString());

			for (Map<String, Object> set : resultSet) {

				if(set.get("ruleid")!=null)
					ruleIdGen = Integer.parseInt(set.get("ruleid").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ruleIdGen;
	}


	public static boolean updateRule(Rule rule) throws Exception {
		logger.debug("updating rule for " + rule.getRuleId());
		boolean result = false;
		deleteFromRuleTable(rule.getName());
		insertIntoRuleTable(rule);

		return result;
	}

	public List<Rule> getAllCmdRules() throws Exception {
		
		List<Rule> cmRules = new ArrayList<Rule>();
		try {
			SelectQuery select= new SelectQuery();
			select.addFrom(DB_NAME_RS_CMD + " rule_cmd");
			select.join(DB_NAME_RS + " rule", "rule.RuleID= " + "rule_cmd.RuleID");

			List<Map<String, Object>> resultSet = database.getSQLResults(select.toString());
			
			for (Map<String, Object> set : resultSet) {
				Rule dbRule = new Rule();
				dbRule.setName((set.get("rulename").toString()));
				dbRule.setCron(Integer.parseInt(set.get("cron").toString()));
				dbRule.setRuleId(Integer.parseInt(set.get("ruleid").toString()));
				dbRule.setUserId(Integer.parseInt(set.get("userid").toString()));
				dbRule.setRuleType((set.get("ruletype").toString()));
				dbRule.setRuleCondition((set.get("rulecondition").toString()));
				dbRule.setContainerId((set.get("containerid").toString()));
				dbRule.setConsequence((set.get("content").toString()));
				dbRule.setCommand(true);
				cmRules.add(dbRule);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return cmRules;
	}


	public List<Rule> getAllNotifyRules() throws Exception {
		
		List<Rule> ntRules = new ArrayList<Rule>();
		try {
			SelectQuery select= new SelectQuery();
			select.addFrom(DB_NAME_RS_NOTIFY + " rule_notify");
			select.join(DB_NAME_RS + " rule", "rule.RuleID= " + "rule_notify.RuleID");

			List<Map<String, Object>> resultSet = database.getSQLResults(select.toString());
			
			for (Map<String, Object> set : resultSet) {
				Rule dbRule = new Rule();
				dbRule.setName((set.get("rulename").toString()));
				dbRule.setCron(Integer.parseInt(set.get("cron").toString()));
				dbRule.setRuleId(Integer.parseInt(set.get("ruleid").toString()));
				dbRule.setUserId(Integer.parseInt(set.get("userid").toString()));
				dbRule.setRuleType((set.get("ruletype").toString()));
				dbRule.setRuleCondition((set.get("rulecondition").toString()));
				dbRule.setContainerId((set.get("containerid").toString()));
				dbRule.setConsequence((set.get("content").toString()));
				dbRule.setNotificationType((set.get("notifytype").toString()));
				dbRule.setNotification(true);
				ntRules.add(dbRule);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ntRules;
	}


	public List<Rule> getAllRulesFromDB() throws Exception {
		List<Rule> rules = new ArrayList<Rule>();
		try {
			rules = getAllCmdRules() ;
			rules.addAll(getAllNotifyRules());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return rules;
	}



	@Override
	public boolean isComponentAlive() throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

}
