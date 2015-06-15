/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.api;

import org.fortiss.smg.ambulance.api.HealthCheck;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public interface RuleSystemDBInterface extends HealthCheck {
    String doSomething(String arg) throws TimeoutException;
    
    public boolean insertIntoRuleTable(RuleInterface rule) throws Exception;
    public boolean deleteFromRuleTable (String ruleName) throws Exception;
    public boolean updateRule(RuleInterface rule) throws Exception;
    public List<Map<String, Object>> getAllCmdRules() throws Exception;
    public List<Map<String, Object>> getAllNotifyRules() throws Exception;
    public List<Map<String, Object>> getAllRulesFromDB() throws Exception;
    
    
}
