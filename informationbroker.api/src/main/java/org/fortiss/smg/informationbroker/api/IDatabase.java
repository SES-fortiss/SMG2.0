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
