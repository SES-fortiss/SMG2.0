package org.fortiss.smg.rulesystem.impl.ruleManager;


import java.sql.SQLException;
import org.fortiss.smg.rulesystem.api.Rule;
import org.fortiss.smg.rulesystem.api.RuleSystemInterface;
import org.fortiss.smg.rulesystem.impl.executor.RuleSystemDroolsImpl;
import org.fortiss.smg.rulesystem.impl.persistor.RuleSystemDBImpl;
import org.fortiss.smg.rulesystem.impl.executor.RuleSystemDroolsDrl;

public class RuleSystemImpl implements RuleSystemInterface{

	@Override
	public boolean createRule(Rule rule){

		try {
			if(RuleSystemDBImpl.insertIntoRuleTable(rule))
				if(RuleSystemDroolsDrl.writeRule(rule.toDRL()))
					return true;
			RuleSystemDroolsImpl.addRule(rule);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean modifyRule(Rule rule){

		try {
			if(RuleSystemDBImpl.updateRule(rule))
				if(RuleSystemDroolsDrl.deleteRule(rule.getName())&& RuleSystemDroolsDrl.writeRule(rule.toDRL()))
					return true;
			RuleSystemDroolsImpl.modifyRule(rule);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeRule(String ruleName){

		try {
			if(RuleSystemDBImpl.deleteFromRuleTable(ruleName))
				if(RuleSystemDroolsDrl.deleteRule(ruleName))
					return true;

			RuleSystemDroolsImpl.removeRule(ruleName);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return false;
	}
}
