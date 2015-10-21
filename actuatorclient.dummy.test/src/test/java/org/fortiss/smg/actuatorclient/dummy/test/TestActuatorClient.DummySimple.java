package org.fortiss.smg.actuatorclient.dummy.test;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.fortiss.smg.config.lib.Ops4JTestTime;
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

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class TestActuatorClient.DummySimple {

	private ActuatorClient.DummyInterface impl;

	@Configuration
	public Option[] config() {
            // this is used to build up a default OSGi Container and inject the SMG scope
            // add here all API-libraries of the smg project on which your api & impl depend on
            Option[] defaultSpace = Ops4JTestTime.getOptions();
            Option[] currentSpace = options(
                            mavenBundle("org.fortiss.smartmicrogrid", "actuatorclient.dummy.api",
                                            "1.0-SNAPSHOT"),
                            mavenBundle("org.fortiss.smartmicrogrid", "actuatorclient.dummy.impl",
                                            "1.0-SNAPSHOT"));

            return OptionUtils.combine(defaultSpace, currentSpace);
	}

	@Before
	public void setUp() {
		impl = new ActuatorClient.DummyImpl();
        }

	@After
	public void tearDown(){
            // TODO do some cleanup
        }

	@Test(timeout=5000)
	public void testYourMethod() throws TimeoutException{
		assertEquals("Hello smg",impl.doSomething("hi"));
	}
}
