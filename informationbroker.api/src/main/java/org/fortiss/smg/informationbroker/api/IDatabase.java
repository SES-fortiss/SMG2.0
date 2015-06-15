/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.informationbroker.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public interface IDatabase {

	 List<Map<String,Object>>  getSQLResults(String sql) throws TimeoutException ;
	 boolean executeQuery(String sql) throws TimeoutException ;
	
	 List<Map<String,Object>>  getSQLResultsoldDB(String sql) throws TimeoutException ;
	 boolean executeQueryoldDB(String sql) throws TimeoutException ;
	 
	
}
