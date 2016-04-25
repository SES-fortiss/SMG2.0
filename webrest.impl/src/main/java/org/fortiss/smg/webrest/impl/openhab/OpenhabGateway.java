package org.fortiss.smg.webrest.impl.openhab;

import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.fortiss.smg.webrest.impl.BundleFactory;
import org.fortiss.smg.webrest.impl.ParametersNotValid;
import org.apache.commons.math3.util.Pair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerEdge;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.fortiss.smg.webrest.impl.BundleFactory;
import org.fortiss.smg.webrest.impl.front.ContainerManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 
 * @author Noha Khater
 * @editor Sajjad Taheri
 * @editor Markus Duchon
 *
*/
@Path("/openhab")
public class OpenhabGateway {

	private static Logger logger = LoggerFactory
			.getLogger(OpenhabGateway.class);

	/**
	 * Retrieves the current state of a device
	 * 
	 * @param deviceContainerID
	 *            : the the device container ID (wrapperID.deviceID)
	 * @return
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
	@Path("/items/{itemname}/type/{type}")		
	public static double getState(
			@PathParam("itemname") String ContainerID, @PathParam("type") SIDeviceType type) {
		ContainerManagement containerManager = new ContainerManagement();
		System.out.println("openHAB is getting a state and a container ID with: " + ContainerID);
		
		// getting the wrapper & device ID
		String[] split = ContainerID.split("\\.");
		if (split.length > 1) {
		logger.debug("openHAB is getting a state and a container DEVID with: " + split[0]);
		String devID = split[2];
		logger.debug("openHAB is getting a state and a container DEVID with: " + devID);
		String wrapperID = split[0] + "." + split[1];
		logger.debug("openHAB is getting a state and a container WRAPPERID with: " + wrapperID);
		DeviceId deviceID = new DeviceId(devID, wrapperID);
		logger.debug("openHAB is getting a state and a container DEVICEID with: " + deviceID);
		}
		// Else: probably a container   
//		List<DoublePoint> doubleDataPt;
//		try {
//			return doubleDataPt = BundleFactory.getInformationBroker()
//					.getDoubleValue(deviceID, 1, 1);
//		} catch (TimeoutException e) {
//			logger .info("No connection?", e.fillInStackTrace());
//			throw new ParametersNotValid("Unable to connect to InformationBroker");
//		}
		
		double doubleDataPt2;
		try {
			return containerManager.getMean(ContainerID, type);
		
		} catch (TimeoutException e) {
			logger.info("No connection?", e.fillInStackTrace());
			throw new ParametersNotValid("Unable to connect to ContainerManager");
		}
		
	}

	
	/**
	 * Retrieves the current state of a device
	 * 
	 * @param deviceContainerID
	 *            : the the device container ID (wrapperID.deviceID)
	 * @return
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN  })
	@Path("/items/{itemname}/{type}")		
	public static String getCurrentState(
			@PathParam("itemname") String ContainerID, @PathParam("type") SIDeviceType type) {
		ContainerManagement containerManager = new ContainerManagement();
		logger.debug("openHAB is getting a state and a container ID with: " + ContainerID);
		

		String[] split = ContainerID.split("\\.");
		String devID = "key";
		if (split.length > 1) {
			devID = split[2];
		}
		
		
		JSONObject result = new JSONObject();
		
		Double value = 0.0;
		
		try {
			value = containerManager.getCurrentValueByType(ContainerID, type);
			
			if (Double.isNaN(value)) {
				result.put(devID, 0.0);
			}
			else {
				result.put(devID, value);
			}
		} catch (TimeoutException e) {
			logger.info("No connection?", e.fillInStackTrace());
			throw new ParametersNotValid("Unable to connect to ContainerManager");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return result.toString();
		
	}
	
	
	/**
	 * Retrieves the current state of a device
	 * 
	 * @param deviceContainerID
	 *            : the the device container ID (wrapperID.deviceID)
	 * @return
	 */
	@GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	@Path("/items/{itemname}")
	public static String getAllStates(@PathParam("itemname") String ContainerID) {
		ContainerManagement containerManager = new ContainerManagement();
		logger.debug("openHAB is getting a state and a container ID with: " + ContainerID);

		
//		String[] types = { "Brightness", "Temperature", "Occupancy", "ConsumptionPowermeter", "Window", "ProductionPowermeter", "Battery"};

		String[] types = { "ConsumptionPowermeter", "ProductionPowermeter", "Battery"};

		
		JSONObject result = new JSONObject();
		
		
		try {
			
			for (String type : types) {
				SIDeviceType t = SIDeviceType.fromString(type);
				
				double value = containerManager.getMean(ContainerID, t);
				if (Double.isNaN(value)) {
					result.put(type.toLowerCase(), 0.0);
				}
				else {
					result.put(type.toLowerCase(), value);
				}
			}
		
		} catch (TimeoutException e) {
			logger.info("No connection?", e.fillInStackTrace());
			throw new ParametersNotValid("Unable to connect to ContainerManager");
		} catch (JSONException e	) {		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.toString();
		//return "Hello world";
		
	}
	
	
	
	/**
	 * Changes the state of a device to the input desired value. Returns true if
	 * the command was a success, false otherwise.
	 * 
	 * @param deviceContainerID
	 *            : the device container ID (wrapperID.deviceID)
	 * @param value
	 *            : the value we want to set the device to
	 */
	@POST
	@Path("/{itemname}/{value}")
	public static boolean setState(
			@PathParam("itemname") String deviceContainerID, @PathParam("value") double newValue) {

		// getting the wrapper & device ID
				String[] split = deviceContainerID.split("\\.");
				logger.debug("openHAB is getting a state and a container DEVID with: " + split[0]);
				String devID = split[2];
				logger.debug("openHAB is getting a state and a container DEVID with: " + devID);
				String wrapperID = split[0] + "." + split[1];
				logger.debug("openHAB is getting a state and a container WRAPPERID with: " + wrapperID);
				DeviceId deviceID = new DeviceId(devID, wrapperID);
				logger.debug("openHAB is getting a state and a container DEVICEID with: " + deviceID);

		Double currentValue = -1.0;
		
		try {
			// getting the device container
			DeviceContainer devCont = BundleFactory.getContainerManager()
					.getDeviceContainer(deviceID);

			// sending the commands
			BundleFactory.getContainerManager().sendCommand(
					new DoubleCommand(newValue), deviceID);

			currentValue = devCont.getValue();
			if (newValue == currentValue) {
				logger.info("Changed the state of device (" + deviceContainerID
						+ ") to " + newValue);
				return true;
			} else {
				logger.warn("State of the device (" + deviceContainerID
						+ ") was not changed!");
				return false;
			}
		} catch (NullPointerException e) {
			logger.error("OpenHAB Gateway: error from DeviceID. GetDeviceContainer() returns null");
			return false;
		} catch (TimeoutException e) {
			logger.error("OpenHAB Gateway: Unable to connect to the container manager to send command");
			return false;
		}

	}

	/**
	 * Returns the map/layout of the system (rooms hierarchy) in the form of a
	 * JSON file
	 * 
	 * @return
	 * @throws TimeoutException
	 */
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getJSONFile")
	public static String getJSONFile() {

		try {
			
			Pair<List<Container>, List<ContainerEdge>> map = BundleFactory
					.getContainerManager().getRoomMap("1");
			System.out.println("OPENHABHELPER STARTS HERE" + map);
			if (map == null) {
				logger.warn("OpenHAB Gateway: Null map retrieved from ContainerManager");
				return null;
			} else {
				
				String result = OpenhabHelper.createJSONFile(map);
				if(result == null || result.isEmpty()) {
					logger.warn("OpenHAB Gateway: Json File creation failed!");
					return null;
				} else {
					logger.info("OpenHAB Gateway: Json File creation succeeded!");
					return result;
				}				 
			}

		} catch (TimeoutException e) {
			logger.error("OpenHAB Gateway: Unable to connect to the container manager");
			return null;
		}
	}
	

}
