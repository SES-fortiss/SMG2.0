package org.fortiss.smg.webrest.impl.openhab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerEdge;
import org.neo4j.shell.util.json.JSONException;
import org.neo4j.shell.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Noha Khater
 *
 */
public class OpenhabHelper {

	private static Logger logger = LoggerFactory
			.getLogger(OpenhabGateway.class);
	private static ArrayList<JSONObject> deviceJSONs = new ArrayList<JSONObject>();
	private static JSONObject JSONObjectSoFar = new JSONObject();
	private static JSONObject parentJSONObject = new JSONObject();

	/**
	 * Creates the JSON File needed by the iOS app
	 * 
	 * @param map
	 *            : A Pair containing the list of all available containers and
	 *            the list of all available container edges in the system
	 * @return a string representing the JSON file
	 */
	public static String createJSONFile(
			Pair<List<Container>, List<ContainerEdge>> map) {

		HashMap<String, String> childParentMap = getChildParentMapping(map
				.getSecond());
		HashMap<String, Container> containerIDMap = getAllContainerIdMapping(map
				.getFirst());
		removeRedundantEdges(map.getSecond(), childParentMap, containerIDMap);
		removeRedundantContainers(map.getFirst());
		List<Container> deviceList = getDeviceList(map.getFirst());
		List<String> deviceListString = getDeviceListString(map.getFirst());
		List<String> nonDeviceListNames = getNonDeviceListNames(map.getFirst());

		deviceJSONs.clear();

		createDeviceJSONObjects(deviceList);
		if (deviceJSONs.isEmpty()) {
			return null;
		}
		addDeviceToJSON(childParentMap, deviceList, deviceListString,
				containerIDMap, nonDeviceListNames);
		if (JSONObjectSoFar == null) {
			return null;
		}
		return JSONObjectSoFar.toString();

	}

	/**
	 * Returns a list of the human readable names of all the containers who are
	 * not devices
	 * 
	 * @param allContainers
	 *            : the list of all available containers
	 * @return
	 */
	private static List<String> getNonDeviceListNames(
			List<Container> allContainers) {
		List<String> deviceContainers = new ArrayList<String>();

		for (Container c : allContainers) {
			if (!c.getContainerType().toString().equals("DEVICE")) {
				deviceContainers.add(c.getHrName());
			}
		}

		return deviceContainers;
	}

	/**
	 * removes the edges and containers who are not a building, room or device
	 * 
	 * @param allEdges
	 *            : the list of all available container edges
	 * @param childParentMap
	 *            : mapping of each child container ID to its parent container
	 *            ID
	 * @param containerIDMap
	 *            : mapping of each container ID to its corresponding container
	 */
	private static void removeRedundantEdges(List<ContainerEdge> allEdges,
			HashMap<String, String> childParentMap,
			HashMap<String, Container> containerIDMap) {
		for (Iterator<ContainerEdge> itr = allEdges.iterator(); itr.hasNext();) {
			ContainerEdge e = itr.next();
			if (containerIDMap.get(e.getChild()).getContainerType().toString()
					.equalsIgnoreCase("UNKNOWN")
					|| containerIDMap.get(e.getChild()).getContainerType()
							.toString().equalsIgnoreCase("DEVICEGATEWAY")) {
				itr.remove();
			} else if (containerIDMap.get(e.getParent()).getContainerType()
					.toString().equalsIgnoreCase("UNKNOWN")
					|| containerIDMap.get(e.getParent()).getContainerType()
							.toString().equalsIgnoreCase("DEVICEGATEWAY")) {
				String pID = childParentMap.get(e.getParent());
				while (pID != null) {
					if (!containerIDMap.get(pID).getContainerType().toString()
							.equalsIgnoreCase("UNKNOWN")
							&& !containerIDMap.get(pID).getContainerType()
									.toString()
									.equalsIgnoreCase("DEVICEGATEWAY")) {
						e.setParent(pID);
						break;
					} else {
						pID = childParentMap.get(pID);
					}
				}
			}
		}

		for (ContainerEdge e : allEdges) {
			childParentMap.put(e.getChild(), e.getParent());
		}

	}

	/**
	 * removes the containers who are not a building, room or device
	 * 
	 * @param allContainers
	 *            : the list of all available containers
	 */
	private static void removeRedundantContainers(List<Container> allContainers) {
		for (Iterator<Container> itr = allContainers.iterator(); itr.hasNext();) {
			Container c = itr.next();
			if (c.getContainerType().toString().equalsIgnoreCase("UNKNOWN")
					|| c.getContainerType().toString()
							.equalsIgnoreCase("DEVICEGATEWAY")) {
				itr.remove();
			}
		}

	}

	/**
	 * Returns a list of containers of type "DEVICE" from the list of all
	 * available containers
	 * 
	 * @param allContainers
	 *            : the list of all available containers
	 * @return List of DEVICE containers
	 */
	private static List<Container> getDeviceList(List<Container> allContainers) {
		List<Container> deviceContainers = new ArrayList<Container>();

		for (Container c : allContainers) {
			if (c.getContainerType().toString().equals("DEVICE")) {
				deviceContainers.add(c);
			}
		}

		return deviceContainers;
	}

	/**
	 * Returns a list of container IDs of all the containers of type "DEVICE"
	 * from the list of all available containers
	 * 
	 * @param allContainers
	 *            : the list of all available containers
	 * @return List of DEVICE container IDs
	 */
	private static List<String> getDeviceListString(
			List<Container> allContainers) {
		List<String> deviceContainers = new ArrayList<String>();

		for (Container c : allContainers) {
			if (c.getContainerType().toString().equals("DEVICE")) {
				deviceContainers.add(c.getContainerId());
			}
		}

		return deviceContainers;
	}

	/**
	 * Creates a mapping using container IDs of each child container to its
	 * parent container
	 * 
	 * @param allEdges
	 *            : the list of all available container edges
	 * @return Mapping of containers to their parents
	 */
	private static HashMap<String, String> getChildParentMapping(
			List<ContainerEdge> allEdges) {
		HashMap<String, String> map = new HashMap<String, String>();

		for (ContainerEdge edge : allEdges) {
			map.put(edge.getChild(), edge.getParent());
		}

		return map;
	}

	/**
	 * Creates a mapping between all the container IDs and their corresponding
	 * containers
	 * 
	 * @param allContainers
	 *            : the list of all available containers
	 * @return Mapping of container IDs to their containers
	 */
	private static HashMap<String, Container> getAllContainerIdMapping(
			List<Container> allContainers) {
		HashMap<String, Container> map = new HashMap<String, Container>();

		for (Container c : allContainers) {
			map.put(c.getContainerId(), c);
		}

		return map;
	}

	/**
	 * Creates a JSONObject for each DEVICE container and adds them to the list
	 * of deviceJsons
	 * 
	 * @param deviceList
	 *            : list of all available DEVCIE containers
	 */
	private static void createDeviceJSONObjects(List<Container> deviceList) {
		try {
			// each JSONObject consists of name, type and human readable name
			// fields. The "name" key is used for the container ID because in
			// OpenHAB, the name of the device is unique and is used as the ID.
			for (Container device : deviceList) {
				JSONObject object = new JSONObject();
				object.put("containerID", device.getContainerId());
				// object.put("type", device.getContainerType().toString());
				object.put("readableName", device.getHrName());
				JSONObject devObj = new JSONObject();
				devObj.put(device.getContainerId(), object);
				deviceJSONs.add(devObj);
			}
		} catch (JSONException e) {
			deviceJSONs.clear();
			logger.error("JSONException: Error in the json file generator. Device JSONObjects cannot be created.");
		}
	}

	/**
	 * Creates the JSON file by looping on the JSONObjects of the devices and
	 * adding them to the main/final JSONObject
	 * 
	 * @param childParentMap
	 *            : mapping of each child container ID to its parent container
	 *            ID
	 * @param deviceList
	 *            : list of all available DEVICE containers
	 * @param deviceListString
	 *            : list of all available DEVICE container IDs
	 * @param containerIDMapping
	 *            : mapping of each container ID to its corresponding container
	 */
	private static void addDeviceToJSON(HashMap<String, String> childParentMap,
			List<Container> deviceList, List<String> deviceListString,
			HashMap<String, Container> containerIDMapping,
			List<String> nonDeviceListNames) {
		try {
			for (int i = 0; i < deviceList.size(); i++) {

				addDeviceToJSONHelper(deviceList.get(i).getContainerId(),
						deviceJSONs.get(i), childParentMap, deviceListString,
						containerIDMapping, nonDeviceListNames);

			}
		} catch (JSONException e) {
			JSONObjectSoFar = null;
			logger.error("JSONException: Error in the json file generator. Cannot add the Device JSONObjects to the json file.");
		}
	}

	/**
	 * Helper method for creating the JSON file recursively starting from the
	 * device JSONObject
	 * 
	 * @param containerID
	 *            : the id of the container that is being added to the JSON file
	 * @param currentJSONObject
	 *            : the current JSONObject
	 * @param childParentMap
	 *            : mapping of each child container ID to its parent container
	 *            ID
	 * @param deviceListString
	 *            : list of all available DEVICE container IDs
	 * @param containerIDMapping
	 *            : mapping of each container ID to its corresponding container
	 * @throws JSONException
	 */
	private static void addDeviceToJSONHelper(String containerID,
			JSONObject currentJSONObject,
			HashMap<String, String> childParentMap,
			List<String> deviceListString,
			HashMap<String, Container> containerIDMapping,
			List<String> nonDeviceListNames) throws JSONException {
		/*
		 * If the container has no parent, then we've reached the top of the
		 * hierarchy and we return
		 */
		String parent = childParentMap.get(containerID);

		if (parent == null) {
			JSONObjectSoFar = currentJSONObject;
			return;
		}
		/*
		 * If the container's parent already exists in the JSONObjectSoFar, then
		 * we put the currentJSONObject into the parent's JSONObject
		 */
		else if (containerExistsInJSON(parent, JSONObjectSoFar,
				containerIDMapping, nonDeviceListNames)) {
			if (deviceListString.contains(containerID)) {
				try {
					JSONObject obj = parentJSONObject.getJSONObject("devices");
					obj.put(containerID,
							currentJSONObject.getJSONObject(containerID));
				} catch (JSONException e) {
					JSONObject obj = new JSONObject();
					obj.put(containerID,
							currentJSONObject.getJSONObject(containerID));
					parentJSONObject.put("devices", obj);
				}
			} else {
				parentJSONObject.put(containerIDMapping.get(containerID)
						.getHrName(), currentJSONObject
						.getJSONObject(containerIDMapping.get(containerID)
								.getHrName()));
			}
			return;
		}
		/*
		 * Otherwise, create a new JSONObject with the parent container ID as a
		 * key and the currentJSONObject as an object value. And repeat
		 * recursively.
		 */
		else {
			JSONObject newObject = new JSONObject();
			if (deviceListString.contains(containerID)) {
				JSONObject temp = new JSONObject();
				temp.put("devices", currentJSONObject);
				JSONObject temp2 = new JSONObject();
				temp2.put(childParentMap.get(containerID), temp);
				newObject.put(
						containerIDMapping.get(childParentMap.get(containerID))
								.getHrName(), temp2);
			} else {
				JSONObject temp = new JSONObject();
				temp.put(childParentMap.get(containerID), currentJSONObject);
				newObject.put(
						containerIDMapping.get(childParentMap.get(containerID))
								.getHrName(), temp);
			}
			addDeviceToJSONHelper(childParentMap.get(containerID), newObject,
					childParentMap, deviceListString, containerIDMapping,
					nonDeviceListNames);
		}
	}

	/**
	 * 
	 * @param containerID
	 *            : the id of the container whose existence in the JSONObject is
	 *            being checked
	 * @param object
	 *            : the JSONObject that is being checked
	 * @param containerIDMapping
	 *            : mapping of each container ID to its corresponding container
	 * @return true if the container exists in the JSONObject as a key, and
	 *         false otherwise
	 * @throws JSONException
	 */
	private static boolean containerExistsInJSON(String containerID,
			JSONObject object, HashMap<String, Container> containerIDMapping,
			List<String> nonDeviceListNames) throws JSONException {
		if (object == null || JSONObject.getNames(object) == null) {
			return false;
		}
		String[] names = JSONObject.getNames(object);
		for (String name : names) {
			if (name.equalsIgnoreCase(containerID)) {
				parentJSONObject = object.getJSONObject(name);
				return true;
			} else if (containerIDMapping.containsKey(name)
					|| nonDeviceListNames.contains(name)) {
				boolean flag = containerExistsInJSON(containerID,
						object.getJSONObject(name), containerIDMapping,
						nonDeviceListNames);
				if (flag) {
					return true;
				}
			}
		}
		return false;
	}

}
