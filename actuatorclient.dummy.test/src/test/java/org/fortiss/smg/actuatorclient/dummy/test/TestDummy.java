package org.fortiss.smg.actuatorclient.dummy.test;

import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.dummy.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatormaster.api.AbstractClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;


public class TestDummy extends AbstractClient {

	private ActuatorClientImpl implClient;



	@Before
	public void setUp() {
		
        }

	@After
	public void tearDown(){
            // TODO do some cleanup
        }

	@Test(timeout=5000)
	public void testYourMethod() throws TimeoutException{
		
	}
}
