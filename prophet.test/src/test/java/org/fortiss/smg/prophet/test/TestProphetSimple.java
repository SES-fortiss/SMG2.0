/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.prophet.test;


import org.fortiss.smg.prophet.api.ProphetInterface;
import org.fortiss.smg.prophet.impl.ProphetImpl;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.fortiss.smg.config.lib.Ops4JTestTime;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

public class TestProphetSimple {

	private ProphetInterface impl;


	@Before
	public void setUp() {
		impl = new ProphetImpl();
        }

	@After
	public void tearDown(){
            // TODO do some cleanup
        }

	@Test(timeout=5000)
	public void testYourMethod() throws TimeoutException{
		HashMap<Long,Double> test = impl.getConsumptionForecast(0l, 24, 15, null);
		assertEquals(96, test.size());
		HashMap<Long,Double> test2 = impl.getConsumptionForecast(0l, 24, 30, null);
		assertEquals(48, test2.size());
		assertEquals(true,test.containsKey(0l) );
		
		assertEquals("Hello smg",impl.doSomething("hi"));
	}
}
