package org.fortiss.smg.rulesystem.api;


public interface RuleSystemInterface {

	boolean createRule(Rule rule) ;
	boolean removeRule (String ruleName) ;
	boolean modifyRule(Rule rule) ;

}
