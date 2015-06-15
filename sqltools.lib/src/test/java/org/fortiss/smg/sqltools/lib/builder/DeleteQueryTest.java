/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.sqltools.lib.builder;

import static org.junit.Assert.assertEquals;

import org.fortiss.smg.sqltools.lib.builder.DeleteQuery;
import org.junit.Test;


public class DeleteQueryTest {

	@Test
	public void shouldCreateQuery() throws Exception {
		// Given
		DeleteQuery query = new DeleteQuery("persons p");

		// When
		String actual = query.toString();

		// Then
		String expected = "DELETE FROM persons p";
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCreateQueryUsingWhere() throws Exception {
		// Given
		DeleteQuery query = new DeleteQuery("account a").addWhere("a.id > 666")
														.addWhere("a.creation_date > '2013-01-01'");

		// When
		String actual = query.toString();

		// Then
		String expected = "DELETE FROM account a WHERE a.id > 666 AND a.creation_date > '2013-01-01'";
		assertEquals(expected, actual);
	}

}
