package org.fortiss.smg.containermanager.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

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

public class TestContainerMgr {

	String wrapperTag = "fooWrapper";

	DeviceContainer solarSpecGenerator;
	DeviceContainer solarSpecGeneratorTemp;
	DeviceContainer solarSpecGeneratorVoltage;

	Container root ;
	Container fakewing;
	Container conferenceroom;
	Container eastwing;
	Container northwing;

	@Before
	public void load() {
		
		DeviceContainerSpec solarPower = new DeviceContainerSpec();
		solarPower.setRange(0, Integer.MAX_VALUE);
		solarPower.setDeviceType(SIDeviceType.ProductionPowermeter);
		
		solarSpecGenerator = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_generator_watt", wrapperTag), wrapperTag
						+ ".solar", solarPower);
		
		DeviceContainerSpec solarTemp = new DeviceContainerSpec();
		solarTemp.setRange(-100, 200);
		solarTemp.setDeviceType(SIDeviceType.Temperature);
		
		solarSpecGeneratorTemp = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_generator_temperature", wrapperTag), wrapperTag
						+ ".solar", solarTemp);
		
		DeviceContainerSpec solarVolt = new DeviceContainerSpec();
		solarVolt.setRange(-10000, 10000);
		solarVolt.setDeviceType(SIDeviceType.ProductionVoltmeter);
		
		solarSpecGeneratorVoltage = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"solar_generator_voltage", wrapperTag), wrapperTag
						+ ".solar", solarVolt);
		
		root = new Container("fortissRoot", "fortiss", ContainerType.BUILDING,
				ContainerFunction.ROOT, false);
		fakewing = new Container("fortissFakeWing", "fortissFake",
				ContainerType.ROOM, ContainerFunction.WING, true);
		conferenceroom = new Container("fortissConferenceRoom", "fortissConf",
				ContainerType.ROOM, ContainerFunction.CONFERENCE, false);
		eastwing = new Container("fortissEastWing", "fortissEast",
				ContainerType.ROOM, ContainerFunction.WING, true);
		northwing = new Container("fortissNorthWing", "fortissNorth",
				ContainerType.ROOM, ContainerFunction.WING, true);

	}

	public ContainerManagerImpl simpleTree() {

		ArrayList<ContainerEdge> edges = new ArrayList<ContainerEdge>();
		ArrayList<Container> cons = new ArrayList<Container>();

		edges.add(new ContainerEdge("fortissRoot", "fortissEastWing",
				EdgeType.REAL));
		edges.add(new ContainerEdge("fortissEastWing", "fortissConferenceRoom",
				EdgeType.REAL));
		edges.add(new ContainerEdge("fortissNorthWing",
				"fortissConferenceRoom", EdgeType.VIRTUAL));
		edges.add(new ContainerEdge("fortissRoot", "fortissFakeWing",
				EdgeType.REAL));
		edges.add(new ContainerEdge("fortissFakeWing", "fortissNorthWing",
				EdgeType.REAL));

		edges.add(new ContainerEdge("fortissRoot", solarSpecGenerator
				.getContainerId(), EdgeType.VIRTUAL));
		edges.add(new ContainerEdge("fortissConferenceRoom", solarSpecGenerator
				.getContainerId(), EdgeType.REAL));

		edges.add(new ContainerEdge("fortissEastWing", solarSpecGeneratorTemp
				.getContainerId(), EdgeType.REAL));
		edges.add(new ContainerEdge("fortissEastWing",
				solarSpecGeneratorVoltage.getContainerId(), EdgeType.REAL));

		cons.add(root);
		cons.add(eastwing);
		cons.add(fakewing);
		cons.add(northwing);
		cons.add(conferenceroom);
		cons.add(solarSpecGeneratorVoltage);
		cons.add(solarSpecGeneratorTemp);
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
	public void test() throws TimeoutException {
		ContainerManagerImpl impl = simpleTree();
		assertNotNull(impl.getContainer("fortissRoot"));
		assertEquals(Double.NaN,
				root.getMean(solarSpecGenerator.getDeviceType()), 0.01);

		impl.onDoubleEventReceived(new DoubleEvent(1.0),
				solarSpecGenerator.getDeviceId(), "foo");

		assertEquals(1.0,
				conferenceroom.getSum(solarSpecGenerator.getDeviceType()), 0.01);
		assertEquals(1.0, eastwing.getSum(solarSpecGenerator.getDeviceType()),
				0.01);
		assertEquals(1.0, root.getSum(solarSpecGenerator.getDeviceType()), 0.01);
		assertEquals(1.0,
				solarSpecGenerator.getSum(solarSpecGenerator.getDeviceType()),
				0.01);
		assertEquals(0.0, fakewing.getSum(solarSpecGenerator.getDeviceType()),
				0.01);

		impl.onDoubleEventReceived(new DoubleEvent(3.0),
				solarSpecGenerator.getDeviceId(), "foo");
		assertEquals(3.0, root.getMax(solarSpecGenerator.getDeviceType()), 0.01);
		assertEquals(3.0,
				solarSpecGenerator.getMax(solarSpecGenerator.getDeviceType()),
				0.01);
		assertEquals(3.0, eastwing.getMax(solarSpecGenerator.getDeviceType()),
				0.01);
	}

}
