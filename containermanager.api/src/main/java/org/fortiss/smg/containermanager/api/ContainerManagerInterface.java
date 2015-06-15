/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.containermanager.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import org.apache.commons.math3.util.Pair;
import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerEdge;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.EdgeType;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.containermanager.api.devices.SingleContainerEdge;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;

public interface ContainerManagerInterface extends HealthCheck {

	/**
	 * This method returns the edgetype (virtual or real) between a "parent" and "child" (containerIDs). Here a virtual edge receives updates 
	 * but does not forward information/data to possibly connected parents. A real edge represents 
	 * a real world physical connection. 
	 * @param parent
	 * @param child
	 * @return EdgeType or null
	 */
	public EdgeType getEdgeType(String parent, String child);

	/**
	 * This method returns the children (containers) and the edgetype (virtual or real) they are connected
	 * to the parent with "id" (containerID)
	 * @param id
	 * @return List<Entry<Container,EdgeType>> or an empty List
	 */
	public List<Entry<Container, EdgeType>> getChildrenWithEdgeTypes(String id);
	
	/**
	 * This method returns a DeviceContainer for a given DeviceId Object or null
	 * @param id
	 * @return DeviceContainer or null
	 */
	public DeviceContainer getDeviceContainer(DeviceId id);
	
	/**
	 * Returns a list of Device Spec Data (by a Google Device Code)
	 * As the received information only contains Strings they must be parsed accordingly 
	 * As keys the table row names will be used: 
	 * devicecode (googleCode), devicetype (google name e.g. temperature), smgdevicetype (internal name),
	 * alloweduserprofile (is the value allowed for profile), minupdaterate, maxupdaterate (samplerates),
	 * acceptscommands (is it an actuator?), hasvalue (e.g. a switch has a state value), rangemin, rangemax, 
	 * rangestep (min max ganularity according to sensor specification), commandminrange, commandmaxrange, 
	 * commandrangestep, (same for actuator), commandrangetype (could be linear, exponential, etc. usually linear),
	 * humanreadablename (human readable name - usually devicetype and wrapper/number), description (more detailed inforamtion)
	 * @param Google deviceCode (int)
	 * @return HashMap<String,Object> or an empty HashMap
	 */
	public HashMap<String,Object> getDeviceSpecData(int deviceCode) throws TimeoutException;
	
	/**
	 * like the getDeviceSpecData but returns a complete DeviceContainer (which might be configured/adapted later on)
	 * commandrangesteptype is Linear even if the device is a sensor and other command* are 0
	 * @param id
	 * @param deviceCode
	 * @return DeviceContainer 
	 * @throws TimeoutException
	 */
	public DeviceContainer getDeviceSpec(DeviceId id, int deviceCode) throws TimeoutException;
	
	/**
	 * this method return the unique containerId for a given deviceId object
	 * if the container currently exists in the system its containerId is provided
	 * else null is returned if the container has not registered or is not known to 
	 * the system 
	 * @param id 
	 * @return containerId (String) or null
	 * @throws TimeoutException
	 */
	public String getContainerId(DeviceId id) throws TimeoutException;

	/**
	 * this method return the container according to the unique containerId
	 * if it exists or is known to the system, else null. The returned container 
	 * could be a DeviceContainer too.
	 * @param id
	 * @return Container or null
	 * @throws Exception
	 */
	public Container getContainer(String id) throws Exception;
	
	/**
	 * this method returns the one ContainerId (parent) which is connected to the given containerId (child)
	 * with a real edge (EdgeType.REAL). This is used for the raspberry bluetooth location service
	 * (smartphonegateway) to get e.g. the roomname/id. Also it is used to get the depth of the 
	 * physical world container model. Returns null if no real parent container exits (e.g. the root node)
	 * @param id
	 * @return unique ContainerId (String) or null
	 * @throws TimeoutException
	 */
	public String getRealParentContainer(String id) throws TimeoutException;
	
	/**
	 * this method returns all (real and virtual) connected parents for a given containerId.
	 * or and empty list
	 * @param id 
	 * @return parent containerId list ArrayList<String>
	 * @throws TimeoutException
	 */
	public ArrayList<String> getParentContainer(String id) throws TimeoutException;
	
	/**
	 * this method returns the physical blueprint of the current container setting as a Pair of 
	 * Containers (List) and Edges (EdgeType.REAL). If the requested ContainerId is not known
	 * or it does not have any edges null is returned. According to this Pair the current 
	 * structure could be determined - used e.g. for the Rest and WebSocket APIs
	 * @param ContainerId
	 * @return Pair<List<Container>>,List<ContainerEdge>> (ContainerEdge: ParentId, ChildId, EdgeType) or null
	 * @throws TimeoutException
	 */
	public Pair<List<Container>, List<ContainerEdge>> getRoomMap(String ContainerId) throws TimeoutException;
	
	/**
	 * this method returns the unique containerId of the real parent container
	 * REFACTOR: use getRealParentContainer instead!
	 * 
	 * Called when the server starts and needs to know its room number.
	 * Alternative: server never uses/sends room number, but always its own mac address or hardware id. 
	 * The usermanager then resolves the mac address on its own each time.
	 * Alternative: this call returns the room id.
	 * 
	 * @param macAdress 
	 * @return unique containerId or null
	 * @throws TimeoutException
	 */

	//public String serverStarted(String macAdress) throws TimeoutException; //: int roomNumber
	
	/**
	 * this methods provides the human readable name for a given containerId
	 * or null if the according container is not known to the system.
	 * @param macAdress
	 * @return unique containerId or null
	 * @throws TimeoutException
	 */
	public String getRoomName(String macAdress) throws TimeoutException; // String roomNumber
	/** 
	 * Gets the human readable name (eg. "1001, Dykstra Konferenzraum") of the room where the raspberry with the given mac adress is located.
	 */

	//public getSensors(): List<SensorEntity> sensors
	//* Returns all sensors currently supported by the app/smg2
	/**
	 * this method returns the available (in DB) GoogleDeviceCodes and the DeviceType (e.g. temperature)
	 * or an empty HashMap
	 * @return HashMap<String,String> DeviceCode,DeviceType
	 * @throws TimeoutException
	 */
	public HashMap<Integer, String> getSupportedSensorDeviceCodes() throws TimeoutException;
	
	/**
	 * provides the actuators for a given ContainerID 
	 * @param containerId
	 * @return HashMap<String,String> ContainerID,SIDeviceType
	 * @throws TimeoutException
	 */
	public HashMap<String,String> getActuatorsInContainer(String containerId) throws TimeoutException;
	
	/**
	 * provides the SIDeviceTypes of actuators for a given ContainerID (
	 * @param containerId
	 * @return List<String> SIDeviceType
	 * @throws TimeoutException
	 */
	public List<String> getActuatorsInContainerByType(String containerId) throws TimeoutException;
	
	/**
	 * provides the sensors for a given ContainerID 
	 * @param containerId
	 * @return HashMap<String,String> ContainerID,SIDeviceType
	 * @throws TimeoutException
	 */
	public HashMap<String,String> getSensorsInContainer(String containerId) throws TimeoutException;
	
	/**
	 * provides the SIDeviceTypes of sensors for a given ContainerID (
	 * @param containerId
	 * @return List<String> SIDeviceType
	 * @throws TimeoutException
	 */
	public List<String> getSensorsInContainerByType(String containerId) throws TimeoutException;
	
	/**
	 * provides the actuatorsensors for a given ContainerID 
	 * @param containerId
	 * @return HashMap<String,String> ContainerID,SIDeviceType
	 * @throws TimeoutException
	 */
	public HashMap<String,String> getActuatorSensorsInContainer(String containerId) throws TimeoutException;
	
	/**
	 * provides the SIDeviceTypes of sensoractuators for a given ContainerID (
	 * @param containerId
	 * @return List<String> SIDeviceType
	 * @throws TimeoutException
	 */
	public List<String> getActuatorSensorsInContainerByType(String containerId) throws TimeoutException;
	
	/**
	 * Add containers to DB (like Raspberry Pis)
	 * returns true on success - else false
	 */
	public boolean addContainer(Container con) throws TimeoutException;
	public boolean addDevContainer(DeviceContainer con);
	
	/**
	 * containerID (unique must exist)
	 */

	/**
	 * remove Container (remember to remove/update edges)
	 * @param containerID
	 * @return true if container was removed, else false 
	 */
	public boolean removeContainer(String containerID) throws TimeoutException;
	
	
	public boolean updateContainer(String containerIDold, Container newContainer) throws TimeoutException;
	public boolean updateContainer(Container con) throws TimeoutException;
	
	
	public boolean addContainerEdge(String parentID, String childID, int virtual) throws TimeoutException;
	public boolean addRealContainerEdge(String parentID, String childID) throws TimeoutException;
	public boolean addVirtualContainerEdge(String parentID, String childID) throws TimeoutException;
	
	public boolean updateRealContainerEdgeFixedChild(String newParentID, String childID) throws TimeoutException;
	public boolean updateRealContainerEdgeFixedParent(String parentID, String oldChildID, String newChildID) throws TimeoutException;
	public boolean updateVirtualContainerEdgeFixedChild(String oldParentID, String newParentID, String childID) throws TimeoutException;
	public boolean updateVirtualContainerEdgeFixedParent(String parentID, String newChildID, String oldChildID) throws TimeoutException;
	
	public boolean updateContainerEdgeType(String parentID, String childID, int virtual) throws TimeoutException;

	public boolean removeRealContainerEdge(String childID) throws TimeoutException;
	public boolean removeContainerEdge(String parentID, String childID) throws TimeoutException;
	

	//TODO Check if this is neccessary ? duchon 01.2015
	public ArrayList<SingleContainerEdge> getChilds(DeviceId devId) throws TimeoutException;;
	/*
	 * List of Device Types
	 */
	
	/**
	 * 
	 * Send a command (double) to the Device conId with type (type) 
	 * 
	 * @param command
	 * @param id
	 * @throws TimeoutException
	 */
	//TODO probably instead of the object also the uniquie containerId could be used
	public void sendCommand(DoubleCommand command, DeviceId id) throws TimeoutException;
	
	
	public void sendCommand(DoubleCommand command, String containerId, SIDeviceType type) throws TimeoutException;
	
	
	/**
	 * Request which DeviceTypes are considered reasonable for providing a Sum
	 * @return List of of DeviceTypes (as String)
	 */
	public List<String> getReasonableTypeForSum();
	/*
	 * get Statistical Information
	 * is it better to provide single Values or a list 
	 */
	public double getCurrentValueByType(String ContainerId, SIDeviceType type) throws TimeoutException;
	public double getSumByType(String ContainerId, SIDeviceType type) throws TimeoutException;
	public double getMeanByType(String ContainerId, SIDeviceType type) throws TimeoutException;
	public double getMinByType(String ContainerId, SIDeviceType type) throws TimeoutException;
	public double getMaxByType(String ContainerId, SIDeviceType type) throws TimeoutException;
	
	/*
	 *  StatisticsOrder  sum, mean, min, max
	 */
	public ArrayList<Double> getStatisticsByType(String ContainerId, SIDeviceType type) throws TimeoutException;

	//public boolean updateContainer(String containerIDold, String containerIDnew, String HRName, ContainerType containerType, ContainerFunction containerFunction, int virtual) throws TimeoutException;
	//public boolean updateContainer(String containerID, String HRName, ContainerType containerType, ContainerFunction containerFunction, int virtual) throws TimeoutException;

	//TODO check is it is necessary 
	//public DeviceContainer getDeviceSpec(DeviceId id) throws TimeoutException;
	
	
}
