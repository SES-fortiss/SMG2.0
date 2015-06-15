/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.test.openhab;

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
import org.fortiss.smg.containermanager.api.devices.EdgeType;
import org.fortiss.smg.containermanager.api.devices.SingleContainerEdge;
import org.fortiss.smg.containermanager.impl.ContainerManagerImpl;
import org.fortiss.smg.webrest.test.server.MockWrapperServerControl;
import org.fortiss.smg.webrest.test.util.ClientHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestOpenhabJSON {

	private static MockWrapperServerControl server;
	private static int port;

	private Container root;
	private Container eastwing;
	private Container westwing;
	private Container conferenceroom;

	@BeforeClass
	public static void startServer() {
		TestOpenhabJSON.server = new MockWrapperServerControl();
		port = server.start();
	}

	@AfterClass
	public static void stopServer() {
		TestOpenhabJSON.server.stop();
	}

	public ContainerManagerImpl buildMap() {
		ArrayList<ContainerEdge> edges = new ArrayList<ContainerEdge>();
		ArrayList<Container> cons = new ArrayList<Container>();
		root = new Container("1", "Fortiss", ContainerType.BUILDING,
				ContainerFunction.ROOT, false);
		eastwing = new Container("8", "Eastwing", ContainerType.UNKNOWN,
				ContainerFunction.OFFICE, false);
		westwing = new Container("9", "Westwing", ContainerType.UNKNOWN,
				ContainerFunction.OFFICE, false);
		conferenceroom = new Container("17", "224", ContainerType.ROOM,
				ContainerFunction.CONFERENCE, false);
		cons.add(root);
		cons.add(new Container("2", "Floor -1", ContainerType.FLOOR,
				ContainerFunction.UTILITY, false));
		cons.add(new Container("3", "Floor 0", ContainerType.FLOOR,
				ContainerFunction.OFFICE, false));
		cons.add(new Container("4", "Floor 2", ContainerType.FLOOR,
				ContainerFunction.OFFICE, false));
		cons.add(new Container("5", "Roof", ContainerType.FLOOR,
				ContainerFunction.UTILITY, false));
		cons.add(new Container("6", "Utilityroom", ContainerType.ROOM,
				ContainerFunction.UTILITY, false));
		cons.add(new Container("7", "Serverroom", ContainerType.ROOM,
				ContainerFunction.UTILITY, false));
		cons.add(eastwing);
		cons.add(westwing);
		cons.add(new Container("fooWrapper.10", "SiemensPacSen",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.11", "Solarlogger",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.12", "Sunny", ContainerType.DEVICE,
				ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.13", "Aircondition",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.14", "SiemensPacSen",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.16", "Enocean Blinds",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(conferenceroom);
		cons.add(new Container("18", "EastKitchen", ContainerType.ROOM,
				ContainerFunction.KITCHEN, false));
		cons.add(new Container("19", "206", ContainerType.ROOM,
				ContainerFunction.OFFICE, false));
		cons.add(new Container("20", "225", ContainerType.ROOM,
				ContainerFunction.OFFICE, false));
		cons.add(new Container("fooWrapper.21", "Enocean Window",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.22", "Enocean Light F",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.23", "Enocean Light H",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.24", "CoffeeMaker",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.25", "Enocean Temp",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.26", "Enocean Light W",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));
		cons.add(new Container("fooWrapper.27", "Enocean Light W",
				ContainerType.DEVICE, ContainerFunction.UTILITY, false));

		edges.add(new ContainerEdge("1", "2", EdgeType.REAL));
		edges.add(new ContainerEdge("1", "3", EdgeType.REAL));
		edges.add(new ContainerEdge("1", "4", EdgeType.REAL));
		edges.add(new ContainerEdge("1", "5", EdgeType.REAL));
		edges.add(new ContainerEdge("2", "6", EdgeType.REAL));
		edges.add(new ContainerEdge("2", "7", EdgeType.REAL));
		edges.add(new ContainerEdge("4", "8", EdgeType.REAL));
		edges.add(new ContainerEdge("4", "9", EdgeType.REAL));
		edges.add(new ContainerEdge("4", "fooWrapper.10", EdgeType.REAL));
		edges.add(new ContainerEdge("5", "fooWrapper.11", EdgeType.REAL));
		edges.add(new ContainerEdge("6", "fooWrapper.12", EdgeType.REAL));
		edges.add(new ContainerEdge("7", "fooWrapper.13", EdgeType.REAL));
		edges.add(new ContainerEdge("7", "fooWrapper.14", EdgeType.REAL));
		edges.add(new ContainerEdge("8", "17", EdgeType.REAL));
		edges.add(new ContainerEdge("8", "18", EdgeType.REAL));
		edges.add(new ContainerEdge("8", "19", EdgeType.REAL));
		edges.add(new ContainerEdge("8", "20", EdgeType.REAL));
		edges.add(new ContainerEdge("17", "fooWrapper.16", EdgeType.REAL));
		edges.add(new ContainerEdge("17", "fooWrapper.21", EdgeType.REAL));
		edges.add(new ContainerEdge("17", "fooWrapper.22", EdgeType.REAL));
		edges.add(new ContainerEdge("17", "fooWrapper.23", EdgeType.REAL));
		edges.add(new ContainerEdge("18", "fooWrapper.24", EdgeType.REAL));
		edges.add(new ContainerEdge("19", "fooWrapper.25", EdgeType.REAL));
		edges.add(new ContainerEdge("20", "fooWrapper.26", EdgeType.REAL));
		edges.add(new ContainerEdge("20", "fooWrapper.27", EdgeType.REAL));

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
	public void testMap() {
		ContainerManagerImpl impl = buildMap();
		assertNotNull(impl.getContainer("1"));
		assertNotNull(impl.getRoomMap("1"));
		assertEquals(root, impl.getRoomMap("1").getFirst().get(0));
		assertTrue(impl.getRoomMap("1").getFirst().contains(conferenceroom));
		assertFalse(impl.getRoomMap("9").getFirst().contains(conferenceroom));
		assertTrue(impl.getRoomMap("8").getFirst().contains(conferenceroom));
	}
	
	@Test
	public void testJSON() {
		String requestURL = "openhab/getJSONFile";
		Assert.assertTrue(ClientHelper.checkResponse(requestURL, port));
		String response = ClientHelper.checkEquality(requestURL, String.class,
				port);
		assertNotNull(response);
	}

}
