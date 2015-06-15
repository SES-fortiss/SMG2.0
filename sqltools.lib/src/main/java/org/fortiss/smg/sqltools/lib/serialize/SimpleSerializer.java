/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.sqltools.lib.serialize;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.informationbroker.api.IDatabase;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;



public class SimpleSerializer {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(SimpleSerializer.class);
	
	public static boolean  saveToDB(HashMap<String, Object> map, IDatabase db, String table){

		Iterable<String> transformedSet = Iterables.transform(map.entrySet(),new Function<Entry<String,Object>, String>() {
			@Override
			public String apply(Entry<String, Object> entry) {
				return entry.getKey() + "=" + "VALUES(" + (String) entry.getKey().toString() + ")";
			}
		});
		Iterable<String> transformedEntries = Iterables.transform(map.values(), new Function<Object, String>() {
			@Override
			public String apply(Object arg0) {
					if(arg0 instanceof Boolean){
						return arg0.toString();
					}
					if(arg0 == null){
						return "''";
					}
					return "'" + arg0.toString() + "'";
			}	
		});
		
		
		String values = "(" + Joiner.on(",").join(transformedEntries) + ")";
		String keys = "(" + Joiner.on(",").join(map.keySet()) + ")";
		String set =  Joiner.on(",").join(transformedSet);
		
		// uses https://dev.mysql.com/doc/refman/5.0/en/insert-on-duplicate.html
		String sql = "INSERT INTO " + table + " "
				+ keys + " VALUES " + values + " ON DUPLICATE KEY "
				+ "UPDATE " + set;

		try {
			if (db.executeQuery(sql)) {
				SimpleSerializer.logger.trace("SQL success");
				return true;
			} else {
				SimpleSerializer.logger.warn("SQL insert error " + sql);
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			SimpleSerializer.logger.warn("SQL insert error " + sql, e.fillInStackTrace());
		}
		return false;
	}
	
	public static boolean  updateDB(HashMap<String, Object> map, IDatabase db, String table, HashMap<String,Object> conditionMap){

		Iterable<String> transformedSet = Iterables.transform(map.entrySet(),new Function<Entry<String,Object>, String>() {
			@Override
			public String apply(Entry<String, Object> entry) {
				return entry.getKey() + " = "  + (String) entry.getKey().toString();
			}
		});
		
		Iterable<String> transformedConditionSet = Iterables.transform(conditionMap.entrySet(),new Function<Entry<String,Object>, String>() {
			@Override
			public String apply(Entry<String, Object> entry) {
				return entry.getKey() + " = "  + (String) entry.getKey().toString();
			}
		});
		String set =  Joiner.on(",").join(transformedSet);
		String condSet = Joiner.on(",").join(transformedConditionSet);
		// uses https://dev.mysql.com/doc/refman/5.0/en/update.html
		
		String sql = "UPDATE " + table + " SET"
				+ set +" WHERE " + condSet;

		try {
			if (db.executeQuery(sql)) {
				SimpleSerializer.logger.trace("SQL success");
				return true;
			} else {
				SimpleSerializer.logger.warn("SQL update error " + sql);
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			SimpleSerializer.logger.warn("SQL update error " + sql, e.fillInStackTrace());
		}
		return false;
	}

	public static boolean  deleteFromDB(IDatabase db, String table, HashMap<String,Object> conditionMap){
		
		Iterable<String> transformedConditionSet = Iterables.transform(conditionMap.entrySet(),new Function<Entry<String,Object>, String>() {
			@Override
			public String apply(Entry<String, Object> entry) {
				return entry.getKey() + " = "  + (String) entry.getKey().toString();
			}
		});
		
		String condSet = Joiner.on(",").join(transformedConditionSet);
		// uses https://dev.mysql.com/doc/refman/5.0/en/delete.html
		
		String sql = "DELETE FROM " + table + " WHERE " + condSet;

		try {
			if (db.executeQuery(sql)) {
				SimpleSerializer.logger.trace("SQL success");
				return true;
			} else {
				SimpleSerializer.logger.warn("SQL delete error " + sql);
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			SimpleSerializer.logger.warn("SQL delete error " + sql, e.fillInStackTrace());
		}
		return false;
	}
	
}

	




