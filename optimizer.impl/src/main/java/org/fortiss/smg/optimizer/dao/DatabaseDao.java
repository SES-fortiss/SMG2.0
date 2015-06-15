/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.dao;

import static org.fortiss.smg.optimizer.utils.Tools.getAttribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.informationbroker.api.IDatabase;
import org.fortiss.smg.optimizer.data.Chosen;
import org.fortiss.smg.optimizer.data.Interval;
import org.fortiss.smg.optimizer.data.Period;
import org.fortiss.smg.optimizer.data.Specification;
import org.fortiss.smg.optimizer.utils.Tools;
import org.fortiss.smg.sqltools.lib.serialize.SimpleSerializer;

/**
 * Utilities of database access in the optimizer
 * 
 * @author Cheng Zhang
 * @version 1.0
 */
public class DatabaseDao {

	/** Singleton of database dao */
	private static DatabaseDao databaseDao = null;

	/** Database instance */
	private IDatabase database;

	/** Pool type */
	public static final int POOL = 0;
	public static final int EXCHANGE = 1;

	/** Steps of the advisor */
	public static final int INIT = 0;
	public static final int PRE = 1;
	public static final int POST = 2;
	public static final int FINAL = 3;

	/** Tables of the forecast */
	public static final String DB_NAME_PRICE = "PriceForecast";
	public static final String DB_NAME_CONSUMPTION = "DailyForecastConsumption";
	public static final String DB_NAME_GENERATION = "DailyForecastGeneration";
	/** Tables of the specification in the SMG */
	public static final String DB_NAME_SPECIFICATION = "Optimizer_Specification";
	public static final String DB_NAME_SPECIFICATION_LOG = "Optimizer_Suggestion";
	/** Tables of derived data in the advisor */
	public static final String DB_NAME_INTERVAL = "Optimizer_Intervals";
	public static final String DB_NAME_PERIOD = "Optimizer_Periods";
	public static final String DB_NAME_POOL = "Optimizer_Pools";
	public static final String DB_NAME_EXCHANGE = "Optimizer_Exchanges";
	public static final String DB_NAME_PATH = "Optimizer_Paths";

	/** Attributes of source data */
	private static final String[] attributes = { "timestamp", "duration",
			"price", "consumption", "generation" };

	/** Attributes of specification */
	private static final String[] specificationAttributes = {
			"chargeEfficiency", "dischargeEfficiency", "maximumCapacity",
			"initialCapacity", "maximumLineLoad", "maximumPowerOutput",
			"maximumPowerInput", "possibleFromBattery",
			"possibleFromGeneration" };

	/** Attributes of interval */
	private static final String[] intervalAttributes = { "date", "price",
			"duration", "consumption", "generation", "fromGrid", "toGrid",
			"charge", "discharge", "wastage", "demand", "supply", "capacity" };

	/** Attributes of period */
	private static final String[] periodAttributes = { "price",
			"possibleDemand", "leastSupply", "maximumSupply",
			"expectedStorage", "neededStorage", "exchange", "first", "last" };

	/** Attributes of pool */
	private static final String[] poolAttributes = { "value", "sequence",
			"type" };

	/** Attributes of decision path */
	private static final String[] pathAttributes = { "chosen", "exchange",
			"lastDemand", "lastSupply", "type" };

	/** Return singleton database dao instance */
	public static DatabaseDao getDatabaseDao() {
		if (databaseDao == null) {
			databaseDao = new DatabaseDao();
		}
		return databaseDao;
	}

	/** Set database broker instance */
	public void setDatabase(IDatabase database) {
		this.database = database;
	}

	/** Get Intervals start from a given date */
	private String getIntervalSQL(Date start) {
		String innerTable = "SELECT %1$s.timestamp %4$s, ROUND(%1$s.value, 4) %6$s, %2$s.value %7$s, %3$s.value %8$s FROM %1$s, %2$s, %3$s "
				+ "WHERE %1$s.timestamp = %2$s.timestamp AND %1$s.timestamp = %3$s.timestamp AND %1$s.forecasttime = (SELECT MAX(forecasttime) FROM %1$s WHERE forecasttime <= '%9$s') AND %1$s.timestamp BETWEEN '%9$s' AND '%9$s' + INTERVAL 1 DAY ORDER BY %4$s ASC";

		String duration = "SELECT TIME_TO_SEC(TIMEDIFF(min(B.%4$s), A.%4$s))/3600 FROM ("
				+ innerTable + ") B WHERE B.%4$s > A.%4$s";

		String format = "SELECT %4$s, %5$s, %6$s, %7$s, %8$s FROM (SELECT %4$s, ("
				+ duration
				+ ") %5$s, %6$s, %7$s, %8$s FROM ("
				+ innerTable
				+ ") A) C WHERE %5$s > 0";

		return String.format(format, DB_NAME_PRICE, DB_NAME_CONSUMPTION,
				DB_NAME_GENERATION, attributes[0], attributes[1],
				attributes[2], attributes[3], attributes[4],
				Tools.dateToString(start));
	}

	/** Get specification before a given date */
	private String getSpecificationSQL(Date start) {
		String attributes = Arrays.toString(specificationAttributes);
		return "SELECT " + attributes.substring(1, attributes.length() - 1)
				+ " FROM " + DB_NAME_SPECIFICATION + " WHERE timestamp <='"
				+ Tools.dateToString(start)
				+ "' ORDER BY timestamp DESC LIMIT 1";
	}

	/** Parse interval to HashMap<String, Object> for the serializer */
	private HashMap<String, Object> getInterval(Interval interval,
			String executetime) {
		HashMap<String, Object> intervalMap = new HashMap<String, Object>();
		Class<Interval> intervalClass = Interval.class;
		for (String key : intervalAttributes) {
			intervalMap.put(key, getAttribute(intervalClass, key, interval));
		}
		intervalMap.put("executetime", executetime);
		return intervalMap;
	}

	/** Parse period to HashMap<String, Object> for the serializer */
	private HashMap<String, Object> getPeriod(Period period, int sequence,
			String executetime) {
		HashMap<String, Object> periodMap = new HashMap<String, Object>();
		Class<Period> periodClass = Period.class;
		for (String key : periodAttributes) {
			periodMap.put(key, getAttribute(periodClass, key, period));
		}
		periodMap.put("sequence", sequence);
		periodMap.put("executetime", executetime);
		return periodMap;
	}

	/** Parse chosen to HashMap<String, Object> for the serializer */
	private HashMap<String, Object> getPath(Chosen chosen, int sequence,
			String executetime) {
		HashMap<String, Object> chosenMap = new HashMap<String, Object>();
		Class<Chosen> chosenClass = Chosen.class;
		for (String key : pathAttributes) {
			chosenMap.put(key, getAttribute(chosenClass, key, chosen));
		}
		chosenMap.put("sequence", sequence);
		chosenMap.put("executetime", executetime);
		return chosenMap;
	}

	/** Parse specification to HashMap<String, Object> for the serializer */
	private HashMap<String, Object> getSpecification(
			Specification specification, String executetime) {
		HashMap<String, Object> specificationMap = new HashMap<String, Object>();
		Class<Specification> specificationClass = Specification.class;
		for (String key : specificationAttributes) {
			specificationMap.put(key,
					getAttribute(specificationClass, key, specification));
		}
		specificationMap.put("executetime", executetime);
		return specificationMap;
	}

	/** Construct intervals from HashMap<String, Object> */
	private Interval[] getIntervals(List<Map<String, Object>> intervalList) {
		if (intervalList != null && intervalList.size() > 0) {
			List<Interval> intervals = new ArrayList<Interval>();
			for (Map<String, Object> intervalMap : intervalList) {
				int count = 0;
				intervals.add(new Interval(new Date((Long) intervalMap
						.get(attributes[count++])), ((BigDecimal) intervalMap
						.get(attributes[count++])).doubleValue(),
						(Double) intervalMap.get(attributes[count++]),
						(Double) intervalMap.get(attributes[count++]),
						(Double) intervalMap.get(attributes[count++])));
			}
			return intervals.toArray(new Interval[intervals.size()]);
		}
		return null;
	}

	/** Insert object to relevant table in the database */
	private boolean executeSQL(Object obj, int sequence, String executetime)
			throws TimeoutException {
		HashMap<String, Object> queryMap = null;
		String table;
		if (obj instanceof Interval) {
			queryMap = getInterval((Interval) obj, executetime);
			table = DB_NAME_INTERVAL;
		} else if (obj instanceof Period) {
			queryMap = getPeriod((Period) obj, sequence, executetime);
			table = DB_NAME_PERIOD;
		} else {
			queryMap = getPath((Chosen) obj, sequence, executetime);
			table = DB_NAME_PATH;
		}
		return SimpleSerializer.saveToDB(queryMap, database, table);
	}

	/** Insert value of pool to relevant table in the database */
	private boolean executeSQL(double value, int sequence, int type,
			int operationType, String executetime) {
		String table;
		if (operationType == POOL) {
			table = DB_NAME_POOL;
		} else {
			table = DB_NAME_EXCHANGE;
		}
		HashMap<String, Object> poolMap = new HashMap<String, Object>();
		poolMap.put(poolAttributes[0], value);
		poolMap.put(poolAttributes[1], sequence);
		poolMap.put(poolAttributes[2], type);
		poolMap.put("executetime", executetime);
		return SimpleSerializer.saveToDB(poolMap, database, table);
	}

	/** Load Intervals start from a given date */
	public Interval[] loadIntervals(Date start) throws TimeoutException {
		if (start == null)
			start = new Date();
		return getIntervals(database.getSQLResults(getIntervalSQL(start)));
	}

	/** Load specification config start from a given date */
	public void loadSpecification(Date start) throws TimeoutException {
		if (start == null)
			start = new Date();
		List<Map<String, Object>> resultSet = database
				.getSQLResults(getSpecificationSQL(start));
		if (resultSet != null && resultSet.size() == 1) {
			Map<String, Object> map = resultSet.get(0);
			Specification.getSpecification().setSpecification(
					(Double) map.get("chargeefficiency"),
					(Double) map.get("dischargeefficiency"),
					(Double) map.get("maximumcapacity"),
					(Double) map.get("initialcapacity"),
					(Double) map.get("maximumlineload"),
					(Double) map.get("maximumpoweroutput"),
					(Double) map.get("maximumpowerinput"),
					(Boolean) map.get("possiblefrombattery"),
					(Boolean) map.get("possiblefromgeneration"));
		}
	}

	/** Save specification to database */
	public void writeSpecification(Specification specification,
			String executetime) throws TimeoutException {
		if (specification == null)
			return;
		SimpleSerializer.saveToDB(getSpecification(specification, executetime),
				database, DB_NAME_SPECIFICATION_LOG);
	}

	/** Save intervals, periods, choices array to database */
	public void writeArray(Object objects, String executetime)
			throws TimeoutException {
		if (objects == null)
			return;
		if (objects instanceof Interval[]) {
			Interval[] intervals = (Interval[]) objects;
			for (int i = 0; i < intervals.length; i++) {
				executeSQL(intervals[i], i, executetime);
			}
		} else if (objects instanceof Period[]) {
			Period[] periods = (Period[]) objects;
			for (int i = 0; i < periods.length; i++) {
				executeSQL(periods[i], i, executetime);
			}
		} else if (objects instanceof Chosen[]) {
			Chosen[] chosens = (Chosen[]) objects;
			for (int i = 0; i < chosens.length; i++) {
				executeSQL(chosens[i], i, executetime);
			}
		}
	}

	/** Save pool and exchange array to database */
	public void writeDB(double[] pool, int type, int operationType,
			String executetime) {
		if (pool == null || pool.length == 0)
			return;
		for (int i = 0; i < pool.length; i++) {
			executeSQL(pool[i], i, type, operationType, executetime);
		}
	}
}
