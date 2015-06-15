/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.containermanager.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;

import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerEdge;
import org.fortiss.smg.containermanager.api.devices.ContainerFunction;
import org.fortiss.smg.containermanager.api.devices.ContainerType;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceContainerSpec;
import org.fortiss.smg.containermanager.api.devices.EdgeType;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.containermanager.api.devices.SingleContainerEdge;
import org.fortiss.smg.containermanager.impl.ContainerManagerImpl;
import org.junit.Before;
import org.junit.Test;

public class Test2ContainerMgr {
	
	String wrapperTag = "fooWrapper";
	DeviceContainerSpec spec =  new DeviceContainerSpec();
	DeviceContainer s3;
	Container root;
	Container eastwing;
	
	@Before
	public void load(){
		spec.setDeviceType(SIDeviceType.ProductionPowermeter);
		spec.setRangeMin(0);
		spec.setRangeMax(Integer.MAX_VALUE);
		s3 = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_generator_watt", wrapperTag), wrapperTag
						+ ".solar", spec);
		 root = new Container("1", "fortiss", ContainerType.BUILDING, ContainerFunction.ROOT, false);
		 eastwing = new Container("2", "fortissEast", ContainerType.ROOM, ContainerFunction.WING, true);
	}
	
	
	public ContainerManagerImpl simpleTree() {
		
		

		ArrayList<ContainerEdge> edges =new ArrayList<ContainerEdge>();
		ArrayList<Container> cons =new ArrayList<Container>();
		
		edges.add(new ContainerEdge("1", "2", EdgeType.REAL));
		edges.add(new ContainerEdge("2", s3.getContainerId(), EdgeType.REAL));
		edges.add(new ContainerEdge("1", s3.getContainerId(), EdgeType.VIRTUAL));
		
		cons.add(eastwing);
		cons.add(root);
		cons.add(s3);
		ContainerManagerImpl impl = new ContainerManagerImpl();
		
		
		
		impl.cons = new HashMap<String, Container>();
		impl.edges = new HashMap<String, ArrayList<SingleContainerEdge>>();

		for(Container con : cons ){
			impl.cons.put(con.getContainerId(), con);
		}
		
		for(ContainerEdge edge : edges ){
			if ( ! impl.edges.containsKey(edge.getParent()) ){
				impl.edges.put(edge.getParent(), new ArrayList<SingleContainerEdge>());
			}
			impl.edges.get(edge.getParent()).add(new SingleContainerEdge(edge.getChild(), edge.getType()));
		}
		return impl;
		
	}
	
	@Test
	public void test(){
		ContainerManagerImpl impl = simpleTree();
		assertNotNull(impl.getContainer("1"));
		assertEquals(Double.NaN, root.getMean(s3.getDeviceType()), 0.01);
		
		impl.onDoubleEventReceived(new DoubleEvent(1.0), s3.getDeviceId() , "foo");

		
		assertEquals(1.0, eastwing.getSum(s3.getDeviceType()), 0.01);
		//assertEquals(1.0, root.getSum(s3.getDeviceType()), 0.01);
		assertEquals(1.0, s3.getSum(s3.getDeviceType()), 0.01);
		
		
	}
	
	
}
