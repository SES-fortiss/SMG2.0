/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatormaster.test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.AbstractDeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.actuatormaster.impl.ActuatorMasterImpl;
import org.fortiss.smg.config.lib.Ops4JTestTime;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.remoteframework.lib.DefaultServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.Bundle;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.util.Annotations;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class TETTActuatorMasterSimple {

	private IActuatorMaster impl;

	@Inject
	private Bundle bundle;

	@Configuration
	public Option[] config() {
		// this is used to build up a default OSGi Container and inject the SMG
		// scope
		// add here all API-libraries of the smg project on which your api &
		// impl depend on
		Option[] defaultSpace = Ops4JTestTime.getOptions();
		Option[] currentSpace = CoreOptions.options(
				CoreOptions.mavenBundle("org.fortiss.smartmicrogrid", "actuatormaster.api",
						"1.0-SNAPSHOT"),
						CoreOptions.mavenBundle("org.fortiss.smartmicrogrid",
						"actuatormaster.impl", "1.0-SNAPSHOT"));
		
		return OptionUtils.combine(defaultSpace, currentSpace);
	}

	@Before
	public void setUp() {
		impl = new ActuatorMasterImpl();
	}

	@After
	public void tearDown() {
		// TODO do some cleanup
	}

	@Test
	public void testPoly() throws IOException, ClassNotFoundException{
		ObjectMapper mapper = new ObjectMapper(); 
		
		//bundle.loadClass(DoubleEvent.class.getCanonicalName());
		
		//mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		DoubleEvent ev = new DoubleEvent(2.0);
		AnnotationIntrospector po = mapper.getSerializationConfig().getAnnotationIntrospector();
		AnnotatedClass construct = AnnotatedClass.construct(ev.getClass(), po, null);
		System.out.println("Size:" + construct.getAnnotations().size());
		Annotations annotations = construct.getAnnotations();
		System.out.println("Ani:"+annotations.get(JsonTypeInfo.class));
		System.out.println("Hashcode:"+annotations.get(JsonTypeInfo.class).hashCode());
		/*for( Annotation x :){
			System.out.println("Annotation: "+ x);
		}
		*/
		System.out.println("Annotations:"+ ev.getClass().getDeclaredAnnotations().length);
		System.out.println("Annotations:"+ DoubleEvent.class.getAnnotations().length);
		System.out.println("Annotations:"+ ev.getClass().getComponentType());
		System.out.println("Annotations:"+ ev.getClass().getClassLoader());
		System.out.println("Classloader:"+ Thread.currentThread().getContextClassLoader());
		System.out.println("class:"+ ev.getClass());
		
		
		ClassLoader tccl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
		try {
			DoubleEvent ev2 = new DoubleEvent(2.0);
			String json1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ev2);  
	        System.out.println("+++>>>" + json1); 
		} finally {
		  Thread.currentThread().setContextClassLoader(tccl);
		}
		
		
		
		JsonTypeInfo type = ev.getClass().getAnnotation(JsonTypeInfo.class);
		
		Thread.currentThread().setContextClassLoader(ev.getClass().getClassLoader());
		
		System.out.println("JSONType:"+ type);
        String json1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ev);  
        System.out.println("+++>>>" + json1); 
        String json = "{ \"type\": \"org.fortiss.smg.actuatormaster.api.events.DoubleEvent\",  \"foo\" : null, \"value\" : 2.0	}";
        AbstractDeviceEvent zoo = mapper.readValue(json1, AbstractDeviceEvent.class); 
        System.out.println(zoo.toString());
	}
	
	@Test(timeout = 5000)
	public void testPolymorphismJackson() throws TimeoutException, IOException, InterruptedException {

		//IActuatorMaster implTest = Mockito.mock(IActuatorMaster.class);
		
		String queue = "foo1234";
		// publish master
		DefaultServer<IActuatorMaster> server = new DefaultServer<IActuatorMaster>(
				IActuatorMaster.class, impl, queue );
		server.init();

		Thread.sleep(300);
		
		DefaultProxy<IActuatorMaster> proxy = new DefaultProxy<IActuatorMaster>(
				IActuatorMaster.class, queue, 200);

		IActuatorMaster init = proxy.init();
		DeviceId dev = new DeviceId("dev1", "wrapper4");
		DoubleEvent ev = new DoubleEvent(12);
		//init.sendEvent(ev, dev, "abc");
		init.isComponentAlive();
		
		//Mockito.verify(implTest,Mockito.atLeastOnce());
		
		proxy.destroy();

		server.destroy();

	}
}
