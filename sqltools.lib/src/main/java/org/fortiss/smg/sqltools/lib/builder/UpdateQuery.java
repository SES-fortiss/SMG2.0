package org.fortiss.smg.sqltools.lib.builder;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class UpdateQuery {

	private String table;

	private Map<String, String> sets;

	private Collection<String> wheres;


	public UpdateQuery(String table) {
		this.table = table;

		this.sets = new LinkedHashMap<String, String>();
		this.wheres = new LinkedList<String>();
	}

	public UpdateQuery set(String column, String value) {
		sets.put(column, value);
		return this;
	}

	public UpdateQuery addWhere(String where) {
		wheres.add(where);
		return this;
	}
	
	public UpdateQuery and(String where) {
		return addWhere("AND " + where);
	}

	public UpdateQuery or(String where) {
		return addWhere("OR " + where);
	}

	@Override
	public String toString() {
		if (sets.isEmpty()) throw new IllegalQueryException("Not contains SET statements!");

		StringBuilder result = new StringBuilder();

		result.append("UPDATE ")
			  .append(table)
			  .append(" SET ");
		int length = 1;
		for (Entry<String, String> entry : sets.entrySet()) {
			result.append(entry.getKey())
				  .append(" = ")
				  .append("'")
				  .append(entry.getValue())
				  .append("'")
				  ;
				
				 if (length < sets.size()) {
					 result.append(",");
				 }
				 length++;
		}

		if (!wheres.isEmpty()) {
			result.append(" WHERE ");
			result.append(StringUtils.join(wheres, " "));
		}
		
		return result.toString();
	}
}
