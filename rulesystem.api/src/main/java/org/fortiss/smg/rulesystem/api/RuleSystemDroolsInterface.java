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

public interface RuleSystemDroolsInterface extends HealthCheck{

	public void modifyRule(String rulename, String ruleString);
	public void removeRule(String rule);
	public boolean addRule(String rule);
	public boolean loadRulesFromDB();
}
