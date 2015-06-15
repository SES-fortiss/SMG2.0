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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;

import org.apache.commons.math3.util.Pair;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerEdge;
import org.fortiss.smg.containermanager.api.devices.ContainerFunction;
import org.fortiss.smg.containermanager.api.devices.ContainerType;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceContainerSpec;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.EdgeType;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.containermanager.impl.ContainerManagerImpl;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.fortiss.smg.sqltools.lib.serialize.SimpleSerializer;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.fortiss.smg.testing.MockOtherBundles;
import org.fortiss.smg.containermanager.test.DummySensorCodes;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestContainerManagerImpl {

	private static org.fortiss.smg.testing.MockOtherBundles mocker;

	@BeforeClass
	public static void setUpDataBase() throws SQLException,
			ClassNotFoundException {
		ArrayList<String> bundles = new ArrayList<String>();

		bundles.add("Ambulance");
		bundles.add("InformationBroker");
		bundles.add("ActuatorMaster");
		
		
		mocker = new org.fortiss.smg.testing.MockOtherBundles(bundles);
	}

	private ContainerManagerImpl impl;
	private TestingDBUtil db;
	@Before
	public void setUp() throws IOException, TimeoutException, ClassNotFoundException {
		
		Class.forName("com.mysql.jdbc.Driver");
		db = new TestingDBUtil();
		
		System.out.println("Statement created...");
		String sql = "TRUNCATE ContainerManager_Devices";
		db.executeQuery(sql);
		sql = "TRUNCATE ContainerManager_Containers";
		db.executeQuery(sql);
		sql = "TRUNCATE ContainerManager_ContainerEdges";
		System.out.println(db.executeQuery(sql));
		System.out.println("Query executed...");
		
		System.out.println("searching for informationbroker");
		DefaultProxy<InformationBrokerInterface> clientInfo = new DefaultProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(), 300);

		InformationBrokerInterface broker = clientInfo.init();
		System.out.println("found informationbroker");
		impl = new ContainerManagerImpl();//broker);
		System.out.println("init containermgr");
		
		//broker.executeQuery("INSERT INTO Room (id, roomRoleId,user_id) VALUES (1,1,1)");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", 1);
		map.put("roomRoleId", 1);
		map.put("user_id", 2);
		SimpleSerializer.saveToDB(map, broker, "Room");
	}



	public void testOnDeviceEventReceivedDeviceContainerString() {
	
		
		DeviceContainerSpec dSpec = new DeviceContainerSpec();
		dSpec.setDeviceType(SIDeviceType.Powerplug);
		dSpec.setCommandMinRange(0);
		dSpec.setCommandMaxRange(100);
		dSpec.setAcceptsCommands(false);
		DeviceContainer devCon = new DeviceContainer(new DeviceId("dev", "dummy"), "foo",
				dSpec);
		impl.onDeviceEventReceived(new DeviceEvent(devCon), "foo");
		DeviceContainer con = impl.getDeviceContainer(new DeviceId("dev", "dummy"));
		assertEquals(0.0, con.getCommandMinRange(), 0.1);
		assertEquals(100.0, con.getCommandMaxRange(), 0.1);
		assertEquals(false, con.getAcceptsCommands());
		assertEquals(SIDeviceType.Powerplug, con.getDeviceType());
	}

	@Test
	public void testGetEdgeType() {
		impl.addRealContainerEdge("1", "2");
		assertEquals(EdgeType.REAL,(impl.getEdgeType("1", "2")));
		impl.addVirtualContainerEdge("3", "4");
		assertEquals(EdgeType.VIRTUAL,(impl.getEdgeType("3", "4")));
	}

	@Test
	public void testGetChildrenWithEdgeTypes() {
		
		Container c1 =  new Container("dummy1", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		Container c2 = new Container("dummy2", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		
		impl.addContainer(c1);
		impl.addContainer(c2);
		
		impl.addRealContainerEdge("1", "dummy1");
		impl.addVirtualContainerEdge("1", "dummy2");
		
		List<Entry<Container, EdgeType>> foo = impl.getChildrenWithEdgeTypes("1");
		assertEquals(2, foo.size());
	}

	@Test
	public void testGetDevContainer() {
		//impl.addContainer("dummy.dev", "dummyHR", ContainerType.DEVICE, ContainerFunction.None, 0);
		
		HashMap<String, Object> deviceSpec = new HashMap<String, Object>();
		deviceSpec.put("smgdevicetype", SIDeviceType.Accelerometer);
		impl.addDevContainer(new DeviceContainer(new DeviceId("dev", "dummy"), "foo",deviceSpec));
		
		DeviceContainer container = impl.getDeviceContainer(new DeviceId("dev", "dummy"));
		assertEquals(container.getContainerId(), "dummy.dev");
		assertEquals(container.getContainerFunction(), ContainerFunction.NONE);
		assertEquals(container.isVirtualContainer(), false);
		assertEquals(container.getContainerType(), ContainerType.DEVICE);
	}

	@Test
	public void testGetContainerId() {
		Container c1 =  new Container("dummy.dev", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		String id = impl.getContainerId(new DeviceId("dev", "dummy"));
		assertEquals("dummy.dev",id);
	}

	@Test
	public void testGetContainer() {
		Container c1 =  new Container("dummy.dev", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		Container container = impl.getContainer("dummy.dev");
		assertEquals(container.getContainerId(), "dummy.dev");
		assertEquals(container.getContainerFunction(), ContainerFunction.KITCHEN);
		assertEquals(container.isVirtualContainer(), false);
		assertEquals(container.getContainerType(), ContainerType.BUILDING);
	}

	@Test
	public void testGetParentContainer() {
		
		Container c1 =  new Container("dummy.dev", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		Container c2 =  new Container("dummy.dev.parent", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		Container c3 =  new Container("dummy.dev.parent.virtual", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		impl.addContainer(c2);
		impl.addContainer(c3);
		
		impl.addRealContainerEdge("dummy.dev.parent", "dummy.dev");
		impl.addRealContainerEdge("dummy.dev.parent.virtual", "dummy.dev");
		
		ArrayList<String> container = impl.getParentContainer("dummy.dev");
		assertEquals(2, container.size());
		assertTrue(container.contains("dummy.dev.parent"));
		assertTrue(container.contains("dummy.dev.parent.virtual"));
	}

	@Test
	public void testGetRealParentContainer() {
		Container c1 =  new Container("dummy.dev", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		Container c2 =  new Container("dummy.dev.parent", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		Container c3 =  new Container("dummy.dev.parent.virtual", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		impl.addContainer(c2);
		impl.addContainer(c3);
		
		impl.addRealContainerEdge("dummy.dev.parent", "dummy.dev");
		impl.addContainerEdge("dummy.dev.parent.virtual", "dummy.dev",1);
		
		String container = impl.getRealParentContainer("dummy.dev");
		assertEquals("dummy.dev.parent", container);
	}

	@Test
	public void testGetRoomMap() {
		Container c1 =  new Container("dummy.dev", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		Container c2 =  new Container("dummy.dev.parent", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		Container c3 =  new Container("dummy.dev.parent.virtual", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		impl.addContainer(c2);
		impl.addContainer(c3);
		
		impl.addRealContainerEdge("dummy.dev.parent", "dummy.dev");
		impl.addVirtualContainerEdge("dummy.dev.parent.virtual", "dummy.dev");
		
		Pair<List<Container>, List<ContainerEdge>> roomMap = impl.getRoomMap("dummy.dev.parent");
		assertEquals(roomMap.getKey().size(), 2);
		assertEquals(roomMap.getKey().get(0).getContainerId(), "dummy.dev.parent");
		assertEquals(roomMap.getKey().get(1).getContainerId(), "dummy.dev");
		//assertEquals(roomMap.getKey().get(0), "dummy.dev.parent");
		//assertEquals(roomMap.getKey().get(1), "dummy.dev");
		
		assertEquals(roomMap.getValue().size(), 1);
		assertEquals(roomMap.getValue().get(0).getType(), EdgeType.REAL);
		assertEquals(roomMap.getValue().get(0).getParent(), "dummy.dev.parent");
		assertEquals(roomMap.getValue().get(0).getChild(), "dummy.dev");
		//assertEquals(roomMap.getValue().get(0).getKey(), "dummy.dev.parent");
		//assertEquals(roomMap.getValue().get(0).getValue(), "dummy.dev");
				
		//Pair<List<Container>, List<ContainerEdge>> roomMapnothere = impl.getRoomMap("not_here");
		Pair<List<Container>, List<ContainerEdge>> roomMapnothere = impl.getRoomMap("not_here");
		
		assertEquals(roomMapnothere.getKey().size(),0);
		assertEquals(roomMapnothere.getValue().size(),0);

	}

	@Test
	public void testRemoveContainer() {
		Container c1 =  new Container("dummy.dev", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		Container container = impl.getContainer("dummy.dev");
		assertEquals(container.getContainerId(), "dummy.dev");
		impl.removeContainer("dummy.dev");
		container = impl.getContainer("dummy.dev");
		assertEquals(null, container);
	}

	@Test
	public void testAddRealContainerEdge() {
		impl.addRealContainerEdge("1", "2");
		EdgeType type = impl.getEdgeType("1", "2");
		assertEquals(type, EdgeType.REAL);
	}

	@Test
	public void testAddVirtualContainerEdge() {
		impl.addVirtualContainerEdge("1", "2");
		EdgeType type = impl.getEdgeType("1", "2");
		assertEquals(type, EdgeType.VIRTUAL);
	}
	
	
	@Test
	public void testUpdateRealContainerEdgeFixedParent() {
		impl.addRealContainerEdge("1", "2");
		impl.updateRealContainerEdgeFixedParent("1", "2", "3");
		EdgeType type = impl.getEdgeType("1", "3");
		assertEquals(EdgeType.REAL,type);
	}

	@Test
	public void testUpdateVirtualContainerEdgeFixedParent() {
		impl.addVirtualContainerEdge("1", "2");
		impl.updateVirtualContainerEdgeFixedParent("1", "3", "2");
		EdgeType type = impl.getEdgeType("1", "3");
		assertEquals(EdgeType.VIRTUAL, type);
	}
	
	@Test
	public void testUpdateVirtualContainerEdgeFixedChild() {
			impl.addVirtualContainerEdge("1", "2");
			impl.updateVirtualContainerEdgeFixedChild("1", "3", "2");
			EdgeType type = impl.getEdgeType("3", "2");
			assertEquals(type, EdgeType.VIRTUAL);
	}
	
	@Test
	public void testUpdateRealContainerEdgeFixedChild() {
			impl.addRealContainerEdge("1", "2");
			impl.updateRealContainerEdgeFixedChild("3", "2");
			EdgeType type = impl.getEdgeType("3", "2");
			assertEquals(EdgeType.REAL, type);
	}		
			
	@Test
	public void testUpdateRealContainerEdgeFixedChildWithVirtuals() {
			impl.addRealContainerEdge("5", "6");
			impl.addVirtualContainerEdge("4", "6");
			impl.updateRealContainerEdgeFixedChild("7", "6");
			EdgeType type = impl.getEdgeType("7", "6");
			assertEquals(EdgeType.REAL, type);
			assertEquals(EdgeType.VIRTUAL, impl.getEdgeType("4", "6"));
	}



	@Test
	public void testUpdateContainerEdgeType() {
		impl.addRealContainerEdge("1", "2");
		impl.updateContainerEdgeType("1", "2", 1);
		EdgeType type = impl.getEdgeType("1", "2");
		assertEquals(EdgeType.VIRTUAL,type);
	}

	@Test
	public void testRemoveRealContainerEdge() {
		impl.addRealContainerEdge("1", "2");
		EdgeType type = impl.getEdgeType("1", "2");
		assertEquals(type, EdgeType.REAL);
		impl.removeContainerEdge("1", "3");
		type = impl.getEdgeType("3", "2");
		assertEquals(null, type);
	}

	@Test
	public void testRemoveContainerEdge() {
		impl.addContainerEdge("1", "2", 1);
		EdgeType type = impl.getEdgeType("1", "2");
		assertEquals(type, EdgeType.VIRTUAL);
		impl.removeContainerEdge("1", "2");
		type = impl.getEdgeType("1", "2");
		assertEquals(type, null);
	}


	@Test
	public void testGetRoomName() {
		Container c1 =  new Container("dummy1", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		Container c2 =  new Container("dummy.dev.parent", "dummyParentHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c2);
		impl.addRealContainerEdge("dummy.dev.parent", "dummy.dev");
		String container = impl.getRoomName("dummy.dev");
		assertEquals("dummyParentHR", container);
	}


	@Test
	public void testGetMinMaxByType() {
		Container c1 =  new Container("dummy1", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		
		DeviceContainerSpec dSpec = new DeviceContainerSpec();
		dSpec.setDeviceType(SIDeviceType.Battery);
		dSpec.setCommandMinRange(0);
		dSpec.setCommandMaxRange(100);
		dSpec.setAcceptsCommands(false);
		
		DeviceContainer devCon = new DeviceContainer(new DeviceId("dev", "dummy"), "foo",
				dSpec);
		
		DeviceContainerSpec dSpec2 = new DeviceContainerSpec();
		dSpec2.setDeviceType(SIDeviceType.Battery);
		dSpec2.setCommandMinRange(0);
		dSpec2.setCommandMaxRange(100);
		dSpec2.setAcceptsCommands(false);
		
		DeviceContainer devCon2 = new DeviceContainer(new DeviceId("dev", "dummy2"), "foo",
				dSpec2);
		
		impl.onDeviceEventReceived(new DeviceEvent(devCon), "foo");
		impl.onDeviceEventReceived(new DeviceEvent(devCon2), "foo2");
		
		impl.addRealContainerEdge("dummy1", new DeviceId("dev", "dummy").toContainterId());
		impl.addRealContainerEdge("dummy1", new DeviceId("dev", "dummy2").toContainterId());
		
		
		impl.onDoubleEventReceived(new DoubleEvent(4.0), new DeviceId("dev","dummy2"), "foo");
		impl.onDoubleEventReceived(new DoubleEvent(2.0), new DeviceId("dev","dummy"), "foo");
		impl.onDoubleEventReceived(new DoubleEvent(8.0), new DeviceId("dev","dummy2"), "foo");
		
		
		double min = impl.getMinByType("dummy1", SIDeviceType.Battery);
		double max = impl.getMaxByType("dummy1", SIDeviceType.Battery);
		assertEquals(2.0, min, 0.01);
		assertEquals(8.0, max, 0.01);
		
		impl.onDoubleEventReceived(new DoubleEvent(6.0), new DeviceId("dev","dummy"), "foo");
		
		min = impl.getMinByType("dummy1", SIDeviceType.Battery);
		assertEquals(6.0, min, 0.01);
		
		
		//double sum = impl.getSumByType("dummy1", SIDeviceType.Battery);
		//assertEquals(16.0, sum, 0.01);
		
		double mean = impl.getMeanByType("dummy1", SIDeviceType.Battery);
		assertEquals(7.0, mean, 0.01);

		ArrayList<Double> stats = impl.getStatisticsByType("dummy1", SIDeviceType.Battery);
		assertEquals(-1.0, stats.get(0), 0.01);
		assertEquals(7.0, stats.get(1), 0.01);
		assertEquals(6.0, stats.get(2), 0.01);
		assertEquals(8.0, stats.get(3), 0.01);
	}

	@Test
	public void testGetDeviceSpecData() {
		
		db.executeQuery(DummySensorCodes.getString());
		
		Container c1 =  new Container("dummy1", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		HashMap<String, Object> spec = impl.getDeviceSpecData(1);
		assertEquals(17, spec.size());
		assertEquals("ACCELEROMETER", spec.get("devicetype"));
		assertEquals(3600000.0, spec.get("maxupdaterate"));
	}

	@Test
	public void testGetDeviceSpec() {

		Container c1 =  new Container("root", "1", ContainerType.BUILDING, ContainerFunction.NONE, false);
		impl.addContainer(c1);
		
		DeviceContainerSpec dSpec = new DeviceContainerSpec();
		dSpec.setDeviceType(SIDeviceType.Powerplug);
		dSpec.setCommandMinRange(0);
		dSpec.setCommandMaxRange(100);
		dSpec.setAcceptsCommands(false);
		DeviceContainer devCon = new DeviceContainer(new DeviceId("dev", "dummy"), "foo",
				dSpec);
		
		String sql = DummySensorCodes.getString();
		db.executeQuery(sql);
		
		
		int powerplugDeviceCode = 138;
		
		
		impl.onDeviceEventReceived(new DeviceEvent(devCon), "foo");
		DeviceContainer spec = impl.getDeviceSpec(new DeviceId("dev", "dummy"), powerplugDeviceCode);
		assertEquals(ContainerType.DEVICE, spec.getContainerType());
		assertEquals(SIDeviceType.Powerplug, spec.getDeviceType());
	}
	

	@Test
	public void testGetSupportedSensorDeviceCodes() {
		
		String sql = DummySensorCodes.getString();
		db.executeQuery(sql);
		
		HashMap<Integer, String> codes = impl.getSupportedSensorDeviceCodes();
		assertEquals(6, codes.size());
		String ambiente = codes.get(13);
		//TODO
		assertEquals("Temperature", ambiente);
	}

	@Test
	public void testOnDeviceEventReceivedDeviceEventDeviceIdString() {
		Container c1 =  new Container("dummy1", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		
		DeviceContainerSpec dSpec = new DeviceContainerSpec();
		dSpec.setDeviceType(SIDeviceType.Powerplug);
		dSpec.setCommandMinRange(0);
		dSpec.setCommandMaxRange(100);
		dSpec.setAcceptsCommands(false);
		DeviceContainer devCon = new DeviceContainer(new DeviceId("dev", "dummy"), "foo",
				dSpec);
		impl.onDeviceEventReceived(new DeviceEvent(devCon),"foo");
		impl.onDoubleEventReceived(new DoubleEvent(4.0), new DeviceId("dev","dummy"), "foo");
		impl.onDoubleEventReceived(new DoubleEvent(2.0), new DeviceId("dev","dummy"), "foo");
		impl.onDoubleEventReceived(new DoubleEvent(8.0), new DeviceId("dev","dummy"), "foo");
		double min = impl.getMinByType("dummy.dev", SIDeviceType.Battery);
	}
	
	@Test
	public void testAddContainerStringStringContainerTypeContainerFunctionInt() {
		DeviceContainerSpec dSpec = new DeviceContainerSpec();
		dSpec.setDeviceType(SIDeviceType.Powerplug);
		dSpec.setCommandMinRange(0);
		dSpec.setCommandMaxRange(100);
		dSpec.setAcceptsCommands(false);
		DeviceContainer devCon = new DeviceContainer(new DeviceId("dev", "dummy"), "foo",
				dSpec);
		devCon.setContainerType(ContainerType.BUILDING);
		devCon.setContainerFunction(ContainerFunction.KITCHEN);
		impl.onDeviceEventReceived(new DeviceEvent(devCon),"foo");
		
		DeviceContainer container = impl.getDeviceContainer(new DeviceId("dev", "dummy"));
		assertEquals(container.getContainerId(), "dummy.dev");
		assertEquals(container.getContainerFunction(), ContainerFunction.KITCHEN);
		assertEquals(container.isVirtualContainer(), false);
		assertEquals(container.getContainerType(), ContainerType.BUILDING);
	}

	@Test
	public void testUpdateContainerStringStringContainerTypeContainerFunctionInt() {
		Container c1 =  new Container("dummy1", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		
		Container c = new Container("dummy.dev", "dummy.de.new",ContainerType.DEVICE,ContainerFunction.CONFERENCE, true);
		
		impl.updateContainer(c);
		Container container = impl.getContainer("dummy.dev");
		assertEquals(container.getContainerId(), "dummy.dev");
		assertEquals(container.getContainerFunction(), ContainerFunction.CONFERENCE);
		assertEquals(container.isVirtualContainer(), true);
		assertEquals(container.getContainerType(), ContainerType.DEVICE);
	}

	@Test
	public void testUpdateContainerStringStringStringContainerTypeContainerFunctionInt() {
		Container c1 =  new Container("dummy1", "dummyHR", ContainerType.BUILDING, ContainerFunction.KITCHEN, false);
		impl.addContainer(c1);
		
		Container c = new Container("dummy.dev.new", "dummy.dev.new",ContainerType.DEVICE,ContainerFunction.CONFERENCE, true);
		
		impl.updateContainer(c);
		Container container = impl.getContainer("dummy.dev.new");
		assertEquals(container.getContainerId(), "dummy.dev.new");
		assertEquals(container.getContainerFunction(), ContainerFunction.CONFERENCE);
		assertEquals(container.isVirtualContainer(), true);
		assertEquals(container.getContainerType(), ContainerType.DEVICE);
	}

	@Test
	public void testSendCommand() {
	
		DeviceContainerSpec dSpec = new DeviceContainerSpec();
		dSpec.setDeviceType(SIDeviceType.Powerplug);
		dSpec.setCommandMinRange(0);
		dSpec.setCommandMaxRange(100);
		dSpec.setAcceptsCommands(false);
		DeviceContainer devCon = new DeviceContainer(new DeviceId("dev", "dummy"), "foo",
				dSpec);
		
		String id = new DeviceId("dev", "dummy").toContainterId();
		
		impl.addDevContainer(devCon);
		
		//TODO
		impl.sendCommand(new DoubleCommand(2.0), new DeviceId("dev", "dummy"));
		
		double max = impl.getMaxByType(id, SIDeviceType.Powerplug);
		//Assert.assertEquals(2.0, max, 0.1);
	}
	
	@Test
	public void testGetActorsInContainer() {
		Container c1 =  new Container("fortiss", "fortissHQ", ContainerType.BUILDING, ContainerFunction.NONE, false);
		impl.addContainer(c1);
		
		DeviceContainerSpec sensoractor = new DeviceContainerSpec();
		sensoractor.setDeviceType(SIDeviceType.Powerplug);
		sensoractor.setCommandMinRange(0);
		sensoractor.setCommandMaxRange(100);
		sensoractor.setAcceptsCommands(true);
		sensoractor.setHasValue(false);
		
		DeviceContainer devConSA = new DeviceContainer(new DeviceId("dev", "sensoractor"), "fooSensorActor", sensoractor);
		devConSA.setHrName("sensoractor." + SIDeviceType.Powerplug);
		
		impl.onDeviceEventReceived(new DeviceEvent(devConSA), "foo");
		impl.addRealContainerEdge("fortiss", devConSA.getContainerId());
		
		DeviceContainerSpec actor = new DeviceContainerSpec();
		actor.setDeviceType(SIDeviceType.Switch);
		actor.setCommandMinRange(0);
		actor.setCommandMaxRange(100);
		actor.setAcceptsCommands(true);
		actor.setHasValue(true);
		DeviceContainer devConA = new DeviceContainer(new DeviceId("dev", "actor"), "fooActor", actor);
		
		devConA.setHrName("actor." + SIDeviceType.Switch);
		impl.onDeviceEventReceived(new DeviceEvent(devConA), "foo");
		impl.addRealContainerEdge("fortiss", devConA.getContainerId());
		
		DeviceContainerSpec sensor = new DeviceContainerSpec();
		sensor.setDeviceType(SIDeviceType.Temperature);
		sensor.setCommandMinRange(0);
		sensor.setCommandMaxRange(100);
		sensor.setAcceptsCommands(false);
		sensor.setHasValue(false);
		DeviceContainer devConS = new DeviceContainer(new DeviceId("dev", "sensor"), "fooSensor", sensor);
		devConS.setHrName("sensor." + SIDeviceType.Temperature);
	
		impl.onDeviceEventReceived(new DeviceEvent(devConS), "foo");
		impl.addRealContainerEdge("fortiss", devConS.getContainerId());
		
		assertEquals(true,  impl.getActuatorsInContainer("root").containsKey(devConA.getContainerId()));
		System.out.println(impl.getActuatorsInContainer("root"));
		assertEquals(false, impl.getActuatorsInContainer("root").containsKey(devConS.getContainerId()));
		assertEquals(false, impl.getActuatorsInContainer("root").containsKey(devConSA.getContainerId()));
		
		assertEquals(true,  impl.getActuatorSensorsInContainer("root").containsKey(devConSA.getContainerId()));
		assertEquals(false, impl.getActuatorSensorsInContainer("root").containsKey(devConS.getContainerId()));
		assertEquals(false, impl.getActuatorSensorsInContainer("root").containsKey(devConA.getContainerId()));
			
		assertEquals(true,  impl.getSensorsInContainer("root").containsKey(devConS.getContainerId()));
		assertEquals(false, impl.getSensorsInContainer("root").containsKey(devConSA.getContainerId()));
		assertEquals(false, impl.getSensorsInContainer("root").containsKey(devConA.getContainerId()));	
		
		System.out.println(impl.getActuatorSensorsInContainerByType("root"));
		List<String> test = impl.getActuatorSensorsInContainerByType("root");
		assertEquals(1, test.size());
		assertEquals(true, impl.getActuatorsInContainerByType("root").contains("Switch"));
		assertEquals(true, impl.getSensorsInContainerByType("root").contains("Temperature"));
		
		
	}
	
}