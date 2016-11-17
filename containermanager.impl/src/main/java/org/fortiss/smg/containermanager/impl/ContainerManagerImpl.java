package org.fortiss.smg.containermanager.impl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import org.apache.commons.math3.util.Pair;
import org.fortiss.smg.actuatormaster.api.ActuatorMasterQueueNames;
import org.fortiss.smg.actuatormaster.api.IActuatorListener;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.CommandRangeStepType;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerEdge;
import org.fortiss.smg.containermanager.api.devices.ContainerFunction;
import org.fortiss.smg.containermanager.api.devices.ContainerType;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.EdgeType;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.containermanager.api.devices.SingleContainerEdge;
import org.fortiss.smg.informationbroker.api.IDatabase;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.fortiss.smg.sqltools.lib.serialize.SimpleSerializer;
import org.slf4j.LoggerFactory;

public class ContainerManagerImpl implements ContainerManagerInterface,
		IActuatorListener {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(ContainerManagerImpl.class);

	public Map<String, Container> cons;
	public Map<String, ArrayList<SingleContainerEdge>> edges;

	IDatabase database;
	private DefaultProxy<IActuatorMaster> proxyMaster;
	private IActuatorMaster master;
	
	private DefaultProxy<IActuatorMaster> proxyMasterPermanent;
	private IActuatorMaster masterPermanent;
	

	public static final String DB_NAME_CONTAINERS = "ContainerManager_Containers";
	public static final String DB_NAME_CONTAINEREDGES = "ContainerManager_ContainerEdges";
	public static final String DB_NAME_DEVICES = "ContainerManager_Devices";

	private static final int TIMEOUTLONG = 5000;

	private static final String DB_NAME_SPECS = "ContainerManager_Specs";

	private ArrayList<SIDeviceType> reasonableforSum = new ArrayList<SIDeviceType>(
			Arrays.asList(SIDeviceType.ConsumptionAmperemeter,
					SIDeviceType.ConsumptionPowermeter,
					SIDeviceType.ConsumptionPowermeterAggregated,
					SIDeviceType.FeedAmperemeter, SIDeviceType.FeedPowerMeter,
					SIDeviceType.FeedPowerMeterAggregated,
					SIDeviceType.ProductionAmperemeter,
					SIDeviceType.ProductionPowermeter,
					SIDeviceType.ProductionPowermeterAggregated));

	private ArrayList<SIDeviceType> reasonableforBool = new ArrayList<SIDeviceType>(
			Arrays.asList(SIDeviceType.Door,
					SIDeviceType.Window,
					SIDeviceType.LightSimple,
					SIDeviceType.Occupancy));
	
	
	
	private EdgeHandler edgeHandler;

	public ContainerManagerImpl() {
		
		proxyMasterPermanent = new DefaultProxy<IActuatorMaster>(
				IActuatorMaster.class,
				ActuatorMasterQueueNames.getActuatorMasterInterfaceQueue(),
				TIMEOUTLONG);
		try {
			masterPermanent = proxyMasterPermanent.init();
			
		} catch (IOException e) {
			logger.error("ContainerManager: Unable to connect to master");
			waitOrIsKilled();
		} catch (TimeoutException e) {
			logger.error("ContainerManager: Unable to connect to master (Timeout).");
			waitOrIsKilled();
		}
		
		

		DefaultProxy<InformationBrokerInterface> clientInfo = new DefaultProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(), TIMEOUTLONG);

		edgeHandler = new EdgeHandler(this);

		try {
			InformationBrokerInterface broker = clientInfo.init();

			this.cons = new HashMap<String, Container>();
			this.edges = new HashMap<String, ArrayList<SingleContainerEdge>>();
			this.database = broker;

			/*
			 * Request Structure from Database (if exists)
			 */
			loadfromDB();

			/*
			 * in case some devices have already registered at the master
			 */

			getCurrentlyRegisteredDevices();

			String registeredContainers = "";
			for (Container container : this.cons.values()) {
				registeredContainers = registeredContainers
						+ container.getContainerId() + ", ";
			}
			logger.info("Registered Containers/Devices: "
					+ registeredContainers);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadfromDB() {

		try {
			/*
			 * Filter out Devices, we want to consider only active devices
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINERS
					+ " WHERE ContainerType != 'DEVICE';";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				ContainerManagerImpl.logger
						.warn("No Containers found - adding initial Container");
				Container root = new Container("root", "Initial Container",
						ContainerType.COMPLEX, ContainerFunction.ROOT, false);
				SimpleSerializer.saveToDB(root.serialize(), database,
						ContainerManagerImpl.DB_NAME_CONTAINERS);
				this.cons.put(root.getContainerId(), root);
			}
			/*
			 * go through results and create containers
			 */
			else {
				for (Map<String, Object> result : resultSet) {
					Boolean virtual = false;
					if ((Integer) result.get("virtualcontainer") == 1) {
						virtual = true;
					}

					Container container = new Container(result.get(
							"containerid").toString(), result.get(
							"containerhrname").toString(),
							ContainerType.fromSting(result.get("containertype")
									.toString()),
							ContainerFunction.fromString(result.get(
									"containerfunction").toString()), virtual);
					ContainerManagerImpl.logger.info("Container added: "
							+ result.get("containerhrname").toString() + "("
							+ container.getContainerId() + ")");
					this.cons.put(container.getContainerId(), container);
					logger.info("Container added from DB: "
							+ container.getHrName());

				}
			}

		} catch (TimeoutException e) {
			ContainerManagerImpl.logger.warn("SQL Statement error", e);
		}

		/*
		 * Possibly assign devices to containers they belong to
		 */

		edgeHandler.loadEdges();
	}

	private void getCurrentlyRegisteredDevices() {
		// try to connect to master ...
		for (int i = 0; i < 100; i++) {
			proxyMaster = new DefaultProxy<IActuatorMaster>(
					IActuatorMaster.class,
					ActuatorMasterQueueNames.getActuatorMasterInterfaceQueue(),
					2000);
			try {
				master = proxyMaster.init();
				break;
			} catch (IOException e) {
				logger.error("ContainerManager: Unable to connect to master");
				waitOrIsKilled();
			} catch (TimeoutException e) {
				logger.error("ContainerManager: Unable to connect to master (Timeout).");
				waitOrIsKilled();
			}
		}

		if (this.master != null) {

			try {
				this.master.resendRegisteredDevices();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		} else {
			logger.debug("No Connection to Master");
		}
		try {
			proxyMaster.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void waitOrIsKilled() {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.debug("Could not sleep");
			e.printStackTrace();
		}

	}

	public String doSomething(String s) {
		return "Hello smg";
	}

	@Override
	public boolean isComponentAlive() {
		/**
		 * perform quick self check
		 */
		boolean alive = false;
		if (!(this.cons.size() > 0)) {
			return alive;
		}
		/*
		 * more to add
		 */
		
		return true;
	}

	@Override
	public void onDoubleEventReceived(DoubleEvent ev, DeviceId dev,
			String client) throws TimeoutException {
		
	
		logger.info("Event from " + dev + " Value " + ev.getValue());

		if (dev != null) {

			DeviceContainer device = getDeviceContainer(dev);

			if (device != null) {
				logger.info("received Event from: " + device.getHrName() + " ("
						+ ev.getValue() + " "
						+ device.getDeviceType().getType() + ")");
				device.setValue(ev.getValue());

				TreeMap<Integer, Set<String>> listParents = new TreeMap<Integer, Set<String>>(
						Collections.reverseOrder());
				this.getSortedParentContainers(device.getContainerId(),
						listParents);

				String LoggingString = device.getHrName() + "("
						+ device.getContainerId() + ") \n";

				for (Entry<Integer, Set<String>> parent : listParents
						.entrySet()) {

					for (String entry : parent.getValue()) {
						logger.debug("update statistics for Parent "
								+ this.cons.get(entry).getHrName() + " "
								+ entry);
						logger.debug("update statistics for Parent "
								+ ev.getValue() + " " + device.getDeviceType());

						// if (this.getContainer(entry))
						this.getContainer(entry).onUpdateStatistics(
								device.getDeviceType(), this);

						if (entry.equals("1")) {

							LoggingString = "Entry "
									+ entry
									+ "\n"
									+ "Current Consumption  ("+this.getContainer(entry).getHrName()+"): "
									+ this.cons.get(entry).getSum(
											SIDeviceType.ConsumptionPowermeter)
									+ "\n"
									+ "Current Production   ("+this.getContainer(entry).getHrName()+"): "
									+ this.cons.get(entry).getSum(
											SIDeviceType.ProductionPowermeter)
									+ "\n"
									+ "Current Feed In      ("+this.getContainer(entry).getHrName()+"): "
									+ this.cons.get(entry).getSum(
											SIDeviceType.FeedPowerMeter)
									+ "\n"
									+ "Current Brightness   ("+this.getContainer(entry).getHrName()+"): "
									+ this.cons.get(entry).getSum(
											SIDeviceType.Brightness)
									+ "\n"
									+ "Current Battery Cap. ("+this.getContainer(entry).getHrName()+"): "
									+ this.cons.get(entry).getSum(
											SIDeviceType.Battery);
						}

					}

				}
				logger.debug(LoggingString);
			} else {
				logger.debug("Device not found " + dev.toContainterId());
			}
		} else {
			logger.debug("DeviceId " + dev);
		}
	}

	private void getSortedParentContainers(String containerId,
			TreeMap<Integer, Set<String>> sortedParents) {

		ArrayList<String> parents = this.getParentContainer(containerId);
		for (String parent : parents) {
			if (this.getContainer(parent) != null) {
				if (!sortedParents.containsKey(this.getContainer(parent)
						.getDepth(this))) {
					sortedParents.put(this.getContainer(parent).getDepth(this),
							new TreeSet<String>());
				}

				sortedParents.get(this.getContainer(parent).getDepth(this))
						.add(parent);
			}
			this.getSortedParentContainers(parent, sortedParents);
		}
	}

	@Override
	public void onDeviceEventReceived(DeviceEvent devEvent, String client) {

		DeviceContainer ev = devEvent.getValue();
		addDevContainer(ev);

		
		
		// TODO: check for all raspberry types

		// add an edge for the new mobile Device automatically (Parent=:
		// getContainer(wrapperId).getParent() CHild:=
		// ev.getDeviceContainer.getDeviceID()
		if (ev.getDeviceType() == null) {
			logger.debug("invalid device type" + ev.getContainerId());
			return;
		}

		if (ev.getDeviceId().getWrapperId().startsWith("rasp.")) {
			String raspberryParentContainerId = this.getRealParentContainer(ev
					.getContainerId());
			if (this.removeRealContainerEdge(ev.getContainerId())) {
				logger.debug("Removed old real Edge from Raspberry child (MobileDevice): "
						+ ev.getContainerId());
			}
			this.addContainerEdge(raspberryParentContainerId,
					ev.getContainerId(), 0);
			logger.info("Automatically added/updated Edge Parent "); /*
																	 * +
																	 * this.cons
																	 * .get(
																	 * raspberryParentContainerId
																	 * )
																	 * .getHrName
																	 * () +
																	 * " and Child "
																	 * + ev.
																	 * getContainerId
																	 * ());
																	 */
		}

		DeviceId devID = ev.getDeviceId();

		// ensure there is a root
		String root = getRoot();
		
		if (!edges.containsKey(root)) {
			this.edges.put(root, new ArrayList<SingleContainerEdge>());
		}

		// connect non-tree childs with the root
		if (!childIsInTree(devID)) {
			this.addRealContainerEdge(getRoot(), devID.toContainterId());
		}


	}
	
	public String getRoot() {
		String root = "";
		for (Entry<String, Container> entry : this.cons.entrySet()) {
			if (entry.getValue().getContainerFunction().equals(ContainerFunction.ROOT)) {
				return entry.getKey().toString();
				
			}
			
		}
		return root;
	}

	public boolean childIsInTree(DeviceId devID) {
		boolean existingEdge = false;
		for (Entry<String, ArrayList<SingleContainerEdge>> parent : edges
				.entrySet()) {
			// look for existing edge
			for (SingleContainerEdge edge : parent.getValue()) {
				if (edge.getChild().equals(devID.toContainterId())) {
					existingEdge = true;
				}
			}
		}
		return existingEdge;
	}

	public ArrayList<SingleContainerEdge> getChilds(DeviceId devId) {
		return this.edges.get(devId);
	}

	private void checkDevice(DeviceContainer ev) {
		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINERS
					+ " WHERE ContainerID = '" + ev.getContainerId() + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet != null && resultSet.size() == 1) {
				/**
				 * exactly one container
				 */
				ev.setHrName(resultSet.get(0).get("containerhrname").toString());
				ev.setVirtualContainer(Double.parseDouble(resultSet.get(0)
						.get("virtualcontainer").toString()) == 1);
				logger.debug("Container found in DB HRName and virtual-tag replaced ("
						+ ev.getHrName()
						+ ", is virtual: "
						+ ev.isVirtualContainer() + ")");
			}
		} catch (TimeoutException e) {
			ContainerManagerImpl.logger.warn("SQL Statement error", e);
		}
	}

	@Override
	public EdgeType getEdgeType(String parent, String child) {
		return edgeHandler.getEdgeType(parent, child);
	}

	@Override
	public List<Entry<Container, EdgeType>> getChildrenWithEdgeTypes(String id) {
		return edgeHandler.getChildrenWithEdgeTypes(id);
	}

	/**
	 * will pick only dev containers
	 */
	@Override
	public DeviceContainer getDeviceContainer(DeviceId dev) {
		if (dev != null) {
			String id = dev.toContainterId();
			if (cons.containsKey(id)) {
				Container con = cons.get(id);
				if (con instanceof DeviceContainer) {
					return (DeviceContainer) con;
				}
			}
		}
		logger.debug("no container found for " + dev);
		return null;
	}

	/**
	 * will return the Container for an Dev ID
	 */
	@Override
	public String getContainerId(DeviceId id) {
		logger.debug("try to get containerId by deviceID " + id);
		String conId = id.toContainterId();
		if (this.cons.containsKey(conId)) {
			return this.cons.get(conId).getContainerId();
		}
		logger.debug("no container found for " + id);
		return null;
	}

	/**
	 * will return the Container for an String ID
	 */
	@Override
	public Container getContainer(String dev) {
		logger.debug("try to get container by containerID " + dev);
		if (this.cons.containsKey(dev)) {
			return this.cons.get(dev);
		}
		return null;
	}

	
	@Override
	public void sendCommand(DoubleCommand command, DeviceId id) {
		DefaultProxy<IActuatorMaster> proxyMasterCommand = new DefaultProxy<IActuatorMaster>(
				IActuatorMaster.class,
				ActuatorMasterQueueNames.getActuatorMasterInterfaceQueue(),
				TIMEOUTLONG);
		
		try {
			IActuatorMaster proxyCommand = proxyMasterCommand.init();
			if (proxyCommand != null) {
				Container con = this.getContainer(id.toContainterId());
				if (con instanceof DeviceContainer) {
					((DeviceContainer) con).setValue(command.getValue());
				}
				proxyCommand.sendDoubleCommand(command, id);
			}
		} catch (TimeoutException e) {
			logger.debug("sending command failed", e.fillInStackTrace());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			proxyMasterCommand.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void sendCommandToContainer(DoubleCommand command, String containerId,
			SIDeviceType type) {
		// first get all children
		for (Entry<Container, EdgeType> entry : this
				.getChildrenWithEdgeTypes(containerId)) {
			Container con = entry.getKey();
			if (con instanceof DeviceContainer) {
				DeviceContainer deviceContainer = (DeviceContainer) con;
				if (deviceContainer.getDeviceType().equals(type)
						&& deviceContainer.getCommandMaxRange() > 0) {
					deviceContainer.setValue(command.getValue());
					sendCommand(command, deviceContainer.getDeviceId());
				}
			}

			sendCommandToContainer(command, con.getContainerId(), type);

		}
	}
	
	
	public void sendCommand(DoubleCommand command, String containerId, SIDeviceType type, long userId) {
		//TODO Connect to the Usermanager and verify userId has access to containerId
	}
	

	@Override
	public ArrayList<String> getParentContainer(String id) {

		ArrayList<String> parents = new ArrayList<String>();

		SingleContainerEdge edge = new SingleContainerEdge(id, null);

		for (Entry<String, ArrayList<SingleContainerEdge>> entry : edges
				.entrySet()) {
			// if we see a child == the one we look for -> we found a parent
			if (entry.getValue().contains(edge)) {
				parents.add(entry.getKey());
			}

		}
		return parents;
	}

	@Override
	public String getRealParentContainer(String id) {

		SingleContainerEdge edge = new SingleContainerEdge(id, EdgeType.REAL);
		for (Entry<String, ArrayList<SingleContainerEdge>> entry : this.edges
				.entrySet()) {

			// if we see a child == the one we look for -> we found a parent
			if (entry.getValue().contains(edge)) {
				for (SingleContainerEdge childs : entry.getValue()) {
					if (childs.getType().equals(edge.getType())) {
						return entry.getKey();
					}
				}
			}

		}
		return null;
	}

	@Override
	public Pair<List<Container>, List<ContainerEdge>> getRoomMap(
			String ContainerId) {
		List<Container> roomcontainers = new ArrayList<Container>();
		List<ContainerEdge> roomedges = new ArrayList<ContainerEdge>();

		// if the requested container is null return an empty Pair
		Container requestedContainer = this.getContainer(ContainerId);
		if (requestedContainer != null) {
			roomcontainers.add(requestedContainer);
		} else {
			
			return new Pair<List<Container>, List<ContainerEdge>>(
					roomcontainers, roomedges);
		}

		// if edges are empty there is no tree information (yet)
		if (this.edges.isEmpty()) {
			return new Pair<List<Container>, List<ContainerEdge>>(
					roomcontainers, roomedges);
		}

		if (this.edges.get(ContainerId) != null) {
			for (SingleContainerEdge singleContainerEdge : this.edges
					.get(ContainerId)) {
				if (singleContainerEdge.getType().equals(EdgeType.REAL)) {
					roomedges.add(new ContainerEdge(ContainerId, singleContainerEdge.getChild(),singleContainerEdge.getType()));
					
					
					Pair<List<Container>, List<ContainerEdge>> tmpPair = getRoomMap(singleContainerEdge
							.getChild());
					roomcontainers.addAll(tmpPair.getKey());
					roomedges.addAll(tmpPair.getValue());
				}
			}
		}

		return new Pair<List<Container>, List<ContainerEdge>>(roomcontainers,
				roomedges);
	}

	@Override
	public double getCurrentValueByType(String ContainerId, SIDeviceType type) {
		/*if (reasonableforBool.contains(type)) {
			Container con =  this.cons.get(ContainerId);
			if (con instanceof DeviceContainer) {
				DeviceContainer deviceContainer = (DeviceContainer) con;
				return deviceContainer.getValue();
			}
			return -1;
		}
		else */ 
		if (reasonableforSum.contains(type)) {
			//TODO it is the other way round
			return this.getSumByType(ContainerId, type);
		}
		else {
			return this.getMeanByType(ContainerId, type);
		}
	}
	
	@Override
	public double getSumByType(String ContainerId, SIDeviceType type) {
		double result = -1.0;
		if (this.cons.containsKey(ContainerId)) {
		if (typeIsReasonable("SUM", type)) {
			
			result = this.cons.get(ContainerId).getSum(type);
		}
		else {
			result = this.cons.get(ContainerId).getMean(type);
		}
		}
		return result;
	}

	@Override
	public double getMeanByType(String ContainerId, SIDeviceType type) {
		double result = -1.0;
		if (this.cons.containsKey(ContainerId)) {
		if (typeIsReasonable("MEAN", type)) {
			result = this.cons.get(ContainerId).getMean(type);
		}
		else {
			result = this.cons.get(ContainerId).getSum(type);
		}
		}
		return result;
	}

	@Override
	public double getMinByType(String ContainerId, SIDeviceType type) {
		if (this.cons.containsKey(ContainerId)) {
		return this.cons.get(ContainerId).getMin(type);
		}
		else {
			return -1.0;
		}
	}

	@Override
	public double getMaxByType(String ContainerId, SIDeviceType type) {
		if (this.cons.containsKey(ContainerId)) {
		return this.cons.get(ContainerId).getMax(type);
	}
	else {
		return -1.0;
	}
	}

	@Override
	public ArrayList<Double> getStatisticsByType(String ContainerId,
			SIDeviceType type) {
		ArrayList<Double> results = new ArrayList<Double>();

		results.add(getSumByType(ContainerId, type));
		results.add(getMeanByType(ContainerId, type));
		results.add(getMinByType(ContainerId, type));
		results.add(getMaxByType(ContainerId, type));

		return results;
	}

	private boolean typeIsReasonable(String method, SIDeviceType type) {
		/*
		 * Min and Max are always reasonable
		 */

		if (method.equals("SUM")) {
			if (reasonableforSum.contains(type)) {
				return true;
			}
			return false;
		} else if (method.equals("MEAN")) {
			/*
			 * Due to simplicity invert sum
			 */
			if (!reasonableforSum.contains(type)) {
				return true;
			}
			return false;
		} else {
			return false;
		}

	}

	// TODO refactor, possibly not neccessary ! see smartphonegateway code
	// for what purpose the ContainerId is required
	/*
	 * @Override public String serverStarted(String macAdress) { return
	 * getRealParentContainer(macAdress); //return
	 * getContainer(getRealParentContainer(macAdress)).getContainerId(); }
	 */
	@Override
	public String getRoomName(String macAdress) {
		String parent = getRealParentContainer(macAdress);
		if (parent != null) {
			Container con = getContainer(parent);
			if (con != null) {
				return con.getHrName();
			}
		}
		logger.debug("No container found for containerId " + macAdress);
		return null;
	}

	/**
	 * returns HR device name as value (useful for user menus)
	 */
	@Override
	public HashMap<Integer, String> getSupportedSensorDeviceCodes() {
		HashMap<Integer, String> supported = new HashMap<Integer, String>();
		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT DeviceCode, SMGDeviceType FROM "
					+ ContainerManagerImpl.DB_NAME_DEVICES + " WHERE 1;";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet != null) {
				for (Map<String, Object> deviceCodeEntry : resultSet) {
					try {
						supported
								.put((Integer.parseInt(deviceCodeEntry.get(
										"devicecode").toString())),
										deviceCodeEntry.get("smgdevicetype")
												.toString());
					} catch (NumberFormatException e) {
						logger.error("Could not parse Google Device Code: "
								+ deviceCodeEntry.get("devicecode").toString()
								+ "using SMG type: "
								+ deviceCodeEntry.get("smgdevicetype")
										.toString());
					}
				}

			}
		} catch (TimeoutException e) {
			ContainerManagerImpl.logger.warn("SQL Statement error", e);
		}
		return supported;
	}

	public boolean addContainer(Container dev) {
		return saveContainer(dev);
	}

	private boolean saveContainer(Container dev) {
		System.out.println("saving " + dev.getContainerId());
		if (!cons.containsKey(dev.getContainerId())) {
			cons.put(dev.getContainerId(), dev);
		}
		return SimpleSerializer.saveToDB(dev.serialize(), database,
				ContainerManagerImpl.DB_NAME_CONTAINERS);
	}

	private boolean saveSpec(DeviceContainer dev) {
		return SimpleSerializer.saveToDB(dev.serializeDev(), database,
				ContainerManagerImpl.DB_NAME_SPECS);
	}

	@Override
	public boolean addDevContainer(DeviceContainer ev) {
		checkDevice(ev);

		// serialize first
		if (saveContainer(ev)) {
			// saveSpec(ev);
			return true;
		}
		return false;
	}

	/*
	 * Update anything but the ContainerId
	 * 
	 * @see org.fortiss.smg.containermanager.api.ContainerManagerInterface#
	 * updateContainer(java.lang.String, java.lang.String,
	 * org.fortiss.smg.containermanager.api.devices.ContainerType,
	 * org.fortiss.smg.containermanager.api.devices.ContainerFunction, int)
	 */
	@Override
	public boolean updateContainer(Container con) {
		return saveContainer(con);
	}

	@Override
	public boolean updateContainer(String containerIDold, Container newContainer) {
		if (containerIDold.equals(newContainer.getContainerId())) {
			return updateContainer(newContainer);
		} else {
			cons.remove(containerIDold);
			return saveContainer(newContainer);
		}
	}

	@Override
	public boolean removeContainer(String containerID) {
		/*
		 * Update the Edges for all children and set them to this.parent
		 * TODO: All devices to parent node delete "Bulding" structure
		 * TODO: consider structure of building: Building must have floors, floors may have wings and rooms
		 * TODO: when a floor is removed the substructure should be removed too
		 * TODO: when a logical structure e.g. Wing is removed the substrcture should be kept 
		 * 
		 */
		List<Entry<Container, EdgeType>> children = new ArrayList<Entry<Container, EdgeType>>();
		children = this.getChildrenWithEdgeTypes(containerID);
		Array[] movables = {"WING","DEVICE","DEVICEGATEWAY","UNKNOWN"};
		
		if (getRealParentContainer(containerID) != null) {
			for (Entry<Container, EdgeType> entry : children) {
				if (entry.getValue().equals(EdgeType.REAL)) {
					String containerType = entry.getKey().getContainerType().toString();
					if (movables.contains(entry.getKey().getContainerType().toString())) {
						logger.debug("updating (real) edge from children to parent of "
							+ containerID);
						this.updateRealContainerEdgeFixedChild(
							getRealParentContainer(containerID), entry.getKey()
									.getContainerId());
					}
					else {
						//TODO: Remove all children 
					}
				} else {
					String containerType = entry.getKey().getContainerType().toString();
					if (containerType.equals("WING") || containerType.equals("DEVICE") || containerType.equals("DEVICEGATEWAY") || containerType.equals("UNKNOWN") ) {
						logger.debug("updating (virtual) edge from children to parent of "
							+ containerID);
						this.updateVirtualContainerEdgeFixedChild(containerID,
							getRealParentContainer(containerID), entry.getKey()
									.getContainerId());
					}
					else {
						//TODO: Remove all children 
					}
				}
			}
		}

		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINERS
					+ " WHERE ContainerID = '" + containerID + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Container to delete return 0
				 */
				return false;
			}
			sql = "DELETE FROM " + ContainerManagerImpl.DB_NAME_CONTAINERS
					+ " WHERE `ContainerID` =  '" + containerID + "';";
			if (this.database.executeQuery(sql)) {

				this.cons.remove(containerID);

				return true;
			} else {
				return false;
			}
		} catch (TimeoutException e) {
			ContainerManagerImpl.logger.warn("SQL Statement error", e);
		}

		return false;
	}

	@Override
	public boolean addContainerEdge(String parentID, String childID, int virtual) {
		return edgeHandler.addContainerEdge(parentID, childID, virtual);
	}

	@Override
	public boolean addRealContainerEdge(String parentID, String childID) {
		return addContainerEdge(parentID, childID, 0);
	}

	@Override
	public boolean addVirtualContainerEdge(String parentID, String childID) {
		return addContainerEdge(parentID, childID, 1);
	}

	@Override
	public boolean updateRealContainerEdgeFixedChild(String newParentID,
			String childID) {
		return edgeHandler.updateRealContainerEdgeFixedChild(newParentID,
				childID);
	}

	@Override
	public boolean updateRealContainerEdgeFixedParent(String parentID,
			String oldChildID, String newChildID) {
		return edgeHandler.updateRealContainerEdgeFixedParent(parentID,
				oldChildID, newChildID);
	}

	@Override
	public boolean updateVirtualContainerEdgeFixedChild(String oldParentID,
			String newParentID, String childID) {
		return edgeHandler.updateVirtualContainerEdgeFixedChild(oldParentID,
				newParentID, childID);
	}

	@Override
	public boolean updateVirtualContainerEdgeFixedParent(String parentID,
			String newChildID, String oldChildID) {
		return edgeHandler.updateVirtualContainerEdgeFixedParent(parentID,
				newChildID, oldChildID);
	}

	@Override
	public boolean updateContainerEdgeType(String parentID, String childID,
			int virtual) {
		return edgeHandler.updateContainerEdgeType(parentID, childID, virtual);
	}

	@Override
	public boolean removeRealContainerEdge(String childID) {
		return edgeHandler.removeRealContainerEdge(childID);
	}

	@Override
	public boolean removeContainerEdge(String parentID, String childID) {
		return edgeHandler.removeContainerEdge(parentID, childID);
	}

	@Override
	public HashMap<String, Object> getDeviceSpecData(int deviceID) {

		// TODO: Fix null return deviceSpec was set to null before;
		HashMap<String, Object> deviceSpec = new HashMap<String, Object>();
		try {
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_DEVICES
					+ " WHERE DeviceCode = " + deviceID + ";";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() != 1) {
				ContainerManagerImpl.logger
						.warn("No or More than one DeviceSpecData found");
			}
			/*
			 * go through results and create containers
			 */
			else {
				// deviceSpec = new HashMap<String, Object>();
				for (Map<String, Object> result : resultSet) {
					deviceSpec.put("devicecode", result.get("devicecode")); // int
					deviceSpec.put("devicetype", result.get("devicetype")); // String
																			// googletype
					deviceSpec
							.put("smgdevicetype", result.get("smgdevicetype")); // String
																				// smg
																				// type
					deviceSpec.put("alloweduserprofile",
							result.get("alloweduserprofile")); // "Boolean" as
																// Double
					deviceSpec
							.put("minupdaterate", result.get("minupdaterate")); // Double
					deviceSpec
							.put("maxupdaterate", result.get("maxupdaterate")); // Double
					deviceSpec.put("acceptscommands",
							result.get("acceptscommands")); // "Boolean" as
															// Double
					deviceSpec.put("hasvalue", result.get("hasvalue")); // "Boolean"
																		// as
																		// Double
					deviceSpec.put("rangemin", result.get("rangemin")); // Double
					deviceSpec.put("rangemax", result.get("rangemax")); // Double
					deviceSpec.put("rangestep", result.get("rangestep")); // Double
					deviceSpec.put("commandminrange",
							result.get("commandminrange")); // Double
					deviceSpec.put("commandmaxrange",
							result.get("commandmaxrange")); // Double
					deviceSpec.put("commandrangestep",
							result.get("commandrangestep")); // Double
					deviceSpec.put("commandrangesteptype",
							result.get("commandrangesteptype")); // String
					deviceSpec.put("humanreadablename",
							result.get("humanreadablename")); // String
					deviceSpec.put("description", result.get("description")); // String
				}
			}
		} catch (TimeoutException e) {
			ContainerManagerImpl.logger.warn("SQL Statement error", e);
		}
		return deviceSpec;
	}

	/*
	 * information is only stored in memory
	 */
	/*
	 * @Override public DeviceContainer getDeviceSpec(DeviceId id) { return
	 * getDeviceContainer(id); }
	 */

	@Override
	public DeviceContainer getDeviceSpec(DeviceId id, int deviceCode) {
		/*
		 * select (if available) the devicecontainer (HR Name) from DB
		 */
		String hrName = null;

		try {
			String sql = "SELECT ContainerHRName FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINERS
					+ " WHERE ContainerID = " + id.toContainterId()
					+ " AND ContainerType = DEVICE;";

			List<Map<String, Object>> resultSet;

			resultSet = this.database.getSQLResults(sql);

			if (resultSet != null) {
				if (resultSet.size() == 1) {
					hrName = resultSet.get(0).toString();
				}
			}
			/*
			 * go through results and create containers
			 */

		} catch (TimeoutException e) {
			ContainerManagerImpl.logger.warn("SQL Statement error", e);
		}

		/*
		 * get the Rest from DeviceSpec
		 */

		HashMap<String, Object> deviceSpec = getDeviceSpecData(deviceCode);

		if (hrName == null && deviceSpec.size() > 0) {
			hrName = deviceSpec.get("humanreadablename").toString();
		}

		DeviceContainer dev = new DeviceContainer(id, id.getWrapperId());
		if (deviceSpec.size() > 0) {
			dev.setCommandRange(
					(Double.parseDouble(deviceSpec.get("rangemin").toString())),
					(Double.parseDouble(deviceSpec.get("rangemax").toString())),
					(Double.parseDouble(deviceSpec.get("rangestep").toString())));
			dev.setDeviceType(SIDeviceType.fromString(deviceSpec.get(
					"smgdevicetype").toString()));
			dev.setCommandRange((Double.parseDouble(deviceSpec.get(
					"commandminrange").toString())),
					(Double.parseDouble(deviceSpec.get("commandmaxrange")
							.toString())), (Double.parseDouble(deviceSpec.get(
							"commandrangestep").toString())));

			dev.setCommandRangeStepType(CommandRangeStepType
					.fromString("LINEAR"));

			dev.setHrName(hrName);

			// boolean!!
			dev.setHasValue((Double.parseDouble(deviceSpec.get("hasvalue")
					.toString()) != 0));
			dev.setAcceptsCommands((Double.parseDouble(deviceSpec.get(
					"acceptscommands").toString()) != 0));
		}
		return dev;

	}

	@Override
	public HashMap<String, String> getActuatorsInContainer(String containerId) {
		HashMap<String, String> actuators = new HashMap<String, String>();

		List<Entry<Container, EdgeType>> children = getChildrenWithEdgeTypes(containerId);

		for (Entry<Container, EdgeType> child : children) {
			Container con = child.getKey();

			if (con instanceof DeviceContainer) {
				DeviceContainer deviceContainer = (DeviceContainer) con;
				/*
				 * Only those DeviceContainers are SensorActuators which do have
				 * a state value e.g. light switch (no sensor but actuator with
				 * state)
				 */
				if (deviceContainer.getAcceptsCommands()
						&& deviceContainer.isHasValue()) {
					actuators.put(deviceContainer.getContainerId(),
							deviceContainer.getDeviceType().toString());
				}
			}
			else { 
				if (con != null) {
					actuators.putAll(getActuatorsInContainer(con.getContainerId()));
				}
					
			}
		}

		return actuators;
	}
	
	@Override
	public List<String> getActuatorsInContainerByType(String containerId) {
		
		HashSet<String> actuatortypes = new HashSet<String>();
		HashMap<String, String> actuators = getActuatorsInContainer(containerId);
		
		for (Entry<String, String> entry : actuators.entrySet()) {
			actuatortypes.add(entry.getValue());
		}
		
		return new ArrayList<String>(actuatortypes);
	}

	@Override
	public HashMap<String, String> getSensorsInContainer(String containerId) {
		HashMap<String, String> sensors = new HashMap<String, String>();

		List<Entry<Container, EdgeType>> children = getChildrenWithEdgeTypes(containerId);

		for (Entry<Container, EdgeType> child : children) {
			Container con = child.getKey();

			if (con instanceof DeviceContainer) {
				DeviceContainer deviceContainer = (DeviceContainer) con;
				if (!deviceContainer.getAcceptsCommands() ) {
						//&& !deviceContainer.isHasValue()) {
					sensors.put(deviceContainer.getContainerId(),
								deviceContainer.getDeviceType().toString());
				}
			}
			else { 
				if (con != null) {
					sensors.putAll(getSensorsInContainer(con.getContainerId()));
				}
					
			}
		}

		return sensors;
	}
	
	@Override
	public List<String> getSensorsInContainerByType(String containerId) {
		
		HashSet<String> sensortypes = new HashSet<String>();
		HashMap<String, String> sensors = getSensorsInContainer(containerId);
		
		for (Entry<String, String> entry : sensors.entrySet()) {
			sensortypes.add(entry.getValue());
		}
		
		return new ArrayList<String>(sensortypes);
	}
	

	@Override
	public HashMap<String, String> getActuatorSensorsInContainer(String containerId) {
		HashMap<String, String> sensorsactors = new HashMap<String, String>();

		List<Entry<Container, EdgeType>> children = getChildrenWithEdgeTypes(containerId);

		for (Entry<Container, EdgeType> child : children) {
			Container con = child.getKey();

			if (con instanceof DeviceContainer) {
				DeviceContainer deviceContainer = (DeviceContainer) con;
				/*
				 * Only those DeviceContainers are SensorActuators which do not
				 * have a state value
				 */
				if (deviceContainer.getAcceptsCommands()
						&& !deviceContainer.isHasValue()) {
					sensorsactors.put(deviceContainer.getContainerId(),
							deviceContainer.getDeviceType().toString());
				}
			}
			else { 
				if (con != null) {
					sensorsactors.putAll(getActuatorSensorsInContainer(con.getContainerId()));
				}
					
			}
		}

		return sensorsactors;
	}

	@Override
	public List<String> getActuatorSensorsInContainerByType(String containerId) {
		
		HashSet<String> actuatorsensortypes = new HashSet<String>();
		HashMap<String, String> actuatorsensors = getActuatorSensorsInContainer(containerId);
		
		for (Entry<String, String> entry : actuatorsensors.entrySet()) {
			actuatorsensortypes.add(entry.getValue());
		}
		
		return new ArrayList<String>(actuatorsensortypes);
	}
	
	@Override
	public List<String> getReasonableTypeForSum()  {
		List<String> reasonable = new ArrayList<String>();
		for (SIDeviceType type : reasonableforSum) {
			reasonable.add(type.toString());
			
		}
		return reasonable;
	}
}
