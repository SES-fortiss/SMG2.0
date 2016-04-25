package org.fortiss.smg.remoteframework.test.rabbit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import org.apache.commons.math3.util.Pair;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerEdge;
import org.fortiss.smg.containermanager.api.devices.ContainerFunction;
import org.fortiss.smg.containermanager.api.devices.ContainerType;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.EdgeType;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.containermanager.api.devices.SingleContainerEdge;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;

public class EnumTestImpl implements ContainerManagerInterface {

	@Override
	public boolean isComponentAlive() throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EdgeType getEdgeType(String parent, String child) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entry<Container, EdgeType>> getChildrenWithEdgeTypes(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceContainer getDeviceContainer(DeviceId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> getDeviceSpecData(int deviceCode)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceContainer getDeviceSpec(DeviceId id, int deviceCode)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContainerId(DeviceId id) throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container getContainer(String id) throws TimeoutException {
		Container test = new Container("test", "test", ContainerType.ROOM, ContainerFunction.OFFICE, false);
		return test;
	}

	@Override
	public String getRealParentContainer(String id) throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getParentContainer(String id)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<List<Container>, List<ContainerEdge>> getRoomMap(
			String ContainerId) throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	@Override
	public String serverStarted(String macAdress) throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public String getRoomName(String macAdress) throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Integer, String> getSupportedSensorDeviceCodes()
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public int addContainer(String containerID, String HRName,
			ContainerType containerType, ContainerFunction containerFunction,
			int virtual) throws TimeoutException {
		// TODO Auto-generated method stub
		return 0;
	}

	//@Override
	public int updateContainer(String containerIDold, String containerIDnew,
			String HRName, ContainerType containerType,
			ContainerFunction containerFunction, int virtual)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return 0;
	}

	//@Override
	public boolean updateContainer(String containerID, String HRName,
			ContainerType containerType, ContainerFunction containerFunction,
			int virtual) throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeContainer(String containerID) throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addContainerEdge(String parentID, String childID, int virtual)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addRealContainerEdge(String parentID, String childID)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addVirtualContainerEdge(String parentID, String childID)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateRealContainerEdgeFixedChild(String newParentID,
			String childID) throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateRealContainerEdgeFixedParent(String parentID,
			String oldChildID, String newChildID) throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateVirtualContainerEdgeFixedChild(String oldParentID,
			String newParentID, String childID) throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateVirtualContainerEdgeFixedParent(String parentID,
			String newChildID, String oldChildID) throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateContainerEdgeType(String parentID, String childID,
			int virtual) throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRealContainerEdge(String childID) throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeContainerEdge(String parentID, String childID)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public void sendCommand(String conId, SIDeviceType type, double command) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getSumByType(String ContainerId, SIDeviceType type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMeanByType(String ContainerId, SIDeviceType type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMinByType(String ContainerId, SIDeviceType type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMaxByType(String ContainerId, SIDeviceType type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Double> getStatisticsByType(String ContainerId,
			SIDeviceType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addContainer(Container con) throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addDevContainer(DeviceContainer con) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<SingleContainerEdge> getChilds(DeviceId devId)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendCommand(DoubleCommand command, DeviceId id)
			throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateContainer(String containerIDold, Container newContainer)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateContainer(Container con) throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}
/*
	@Override
	public DeviceContainer getDeviceSpec(DeviceId id) throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}
*/

	
	public void sendCommand(DoubleCommand command, String containerId,
			SIDeviceType type) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<String, String> getActuatorsInContainer(String containerId)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getActuatorsInContainerByType(String containerId)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getSensorsInContainer(String containerId)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSensorsInContainerByType(String containerId)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getActuatorSensorsInContainer(
			String containerId) throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getActuatorSensorsInContainerByType(String containerId)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getReasonableTypeForSum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCurrentValueByType(String ContainerId, SIDeviceType type)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void sendCommandToContainer(DoubleCommand command,
			String containerId, SIDeviceType type) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}
}
