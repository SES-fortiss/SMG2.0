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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

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

public class TestContainerMgrRoommap {

	String wrapperTag = "fooWrapper";

	DeviceContainer solarSpecGenerator;

	Container root;
	Container eastwing;
	Container westwing;
	Container conferenceroom;

	@Before
	public void load() {

		DeviceContainerSpec solarPower = new DeviceContainerSpec();
		solarPower.setRange(0, Integer.MAX_VALUE);
		solarPower.setDeviceType(SIDeviceType.ProductionPowermeter);

		solarSpecGenerator = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_generator_watt", wrapperTag), wrapperTag
						+ ".solar", solarPower);

		root = new Container("fortissRoot", "fortiss", ContainerType.BUILDING,
				ContainerFunction.ROOT, false);
		eastwing = new Container("fortissEastWing", "fortissEast",
				ContainerType.ROOM, ContainerFunction.WING, false);
		westwing = new Container("fortissWestWing", "fortissWest",
				ContainerType.ROOM, ContainerFunction.WING, false);
		conferenceroom = new Container("fortissConferenceRoom", "fortissConf",
				ContainerType.ROOM, ContainerFunction.CONFERENCE, false);

	}

	public ContainerManagerImpl simpleTree() {

		ArrayList<ContainerEdge> edges = new ArrayList<ContainerEdge>();
		ArrayList<Container> cons = new ArrayList<Container>();

		edges.add(new ContainerEdge("fortissRoot", "fortissEastWing",
				EdgeType.REAL));
		edges.add(new ContainerEdge("fortissRoot", "fortissWestWing",
				EdgeType.REAL));
		edges.add(new ContainerEdge("fortissEastWing", "fortissConferenceRoom",
				EdgeType.REAL));

		edges.add(new ContainerEdge("fortissRoot", solarSpecGenerator
				.getContainerId(), EdgeType.VIRTUAL));
		edges.add(new ContainerEdge("fortissConferenceRoom", solarSpecGenerator
				.getContainerId(), EdgeType.REAL));

		cons.add(root);
		cons.add(eastwing);
		cons.add(westwing);
		cons.add(conferenceroom);
		cons.add(solarSpecGenerator);

		ContainerManagerImpl impl = new ContainerManagerImpl();
		impl.cons = new HashMap<String, Container>();
		impl.edges = new HashMap<String, ArrayList<SingleContainerEdge>>();

		for (Container con : cons) {
			impl.cons.put(con.getContainerId(), con);
		}

		for (ContainerEdge edge : edges) {
			if (!impl.edges.containsKey(edge.getParent())) {
				impl.edges.put(edge.getParent(),
						new ArrayList<SingleContainerEdge>());
			}
			impl.edges.get(edge.getParent()).add(
					new SingleContainerEdge(edge.getChild(), edge.getType()));
		}
		return impl;

	}

	@Test
	public void test() {
		ContainerManagerImpl impl = simpleTree();
		assertNotNull(impl.getContainer("fortissRoot"));

		assertEquals(5.0, impl.getRoomMap("fortissRoot").getFirst().size(),
				0.01);
		assertEquals(4.0, impl.getRoomMap("fortissRoot").getSecond().size(),
				0.01);

		assertEquals(root, impl.getRoomMap("fortissRoot").getFirst().get(0));
		assertTrue(impl.getRoomMap("fortissRoot").getFirst()
				.contains(conferenceroom));

		assertEquals(3.0, impl.getRoomMap("fortissEastWing").getFirst().size(),
				0.01);
		assertEquals(2.0,
				impl.getRoomMap("fortissEastWing").getSecond().size(), 0.01);

		assertEquals(1.0, impl.getRoomMap("fortissWestWing").getFirst().size(),
				0.01);
		assertEquals(0.0,
				impl.getRoomMap("fortissWestWing").getSecond().size(), 0.01);
		assertFalse(impl.getRoomMap("fortissWestWing").getFirst()
				.contains(conferenceroom));

	}

}
