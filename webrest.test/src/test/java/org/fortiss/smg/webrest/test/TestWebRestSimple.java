package org.fortiss.smg.webrest.test;


import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.fortiss.smg.config.lib.Ops4JTestTime;
import org.fortiss.smg.webrest.api.WebRestInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
/*
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)*/
public class TestWebRestSimple {

	private WebRestInterface impl;

	@Configuration
	public Option[] config() {
            // this is used to build up a default OSGi Container and inject the SMG scope
            // add here all API-libraries of the smg project on which your api & impl depend on
            Option[] defaultSpace = Ops4JTestTime.getOptions();
            Option[] currentSpace = options(
                            mavenBundle("org.fortiss.smartmicrogrid", "webrest.api",
                                            "1.0-SNAPSHOT"),
                            mavenBundle("org.fortiss.smartmicrogrid", "webrest.impl",
                                            "1.0-SNAPSHOT"));

            return OptionUtils.combine(defaultSpace, currentSpace);
	}

	@Before
	public void setUp() {
		//impl = new WebRestImpl();
        }

	@After
	public void tearDown(){
            // TODO do some cleanup
        }

	@Test(timeout=5000)
	public void testYourMethod(){
		//assertEquals("Hello smg",impl.doSomething("hi"));
	}
}
