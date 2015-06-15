/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.api;


public interface RuleSystemInterface {
		boolean createRule(String rule) throws Exception;
	    boolean deleteRule (String ruleName) throws Exception;
	    boolean updateRule(String rule) throws Exception;
		boolean writeRuleInFile(String rule) throws Exception;
}
