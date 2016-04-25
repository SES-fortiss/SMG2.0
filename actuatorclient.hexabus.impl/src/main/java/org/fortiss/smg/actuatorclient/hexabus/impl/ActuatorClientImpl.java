package org.fortiss.smg.actuatorclient.hexabus.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.ContainerManagerQueueNames;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.slf4j.LoggerFactory;

public class ActuatorClientImpl implements IActuatorClient {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(ActuatorClientImpl.class);
	private IActuatorMaster master;
	private String clientId;
	private ScheduledExecutorService executor;
	private int pollFrequency;
	private String host;
	private int deviceCounter;
	private JSONReaderHexabus reader;
	private String wrapperTag;
	private ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();

	public ActuatorClientImpl(String host, String port, String wrapperTag,
			int pollFreq, String username, String password) {
		this.host = host;
		// this.port = port;
		this.wrapperTag = wrapperTag;
		this.deviceCounter = 0;
		reader = new JSONReaderHexabus();
		loadStaticDevs(this.wrapperTag);
		pollFrequency = pollFreq;
	}

	/**
	 * Activates the HexabusLooper to run on a Scheduled basis
	 */
	public synchronized void activate() {
		sendNewDeviceEvents();
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new HexabusLooper(this), 0,
				getPollFrequency(), TimeUnit.SECONDS);
	}

	/**
	 * Deactivates the HexabusLooper
	 */
	public synchronized void deactivate() {
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Registers the devices with their specific characteristics
	 * 
	 * @param wrapperTagN
	 *            is the wrapper of the device
	 */
	public void loadStaticDevs(String wrapperTagN) {
		deviceCounter = 0;
		List<String[]> parsedDevices = reader.readJson_Hexabus(host);

		if (parsedDevices != null) {
			DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
					ContainerManagerInterface.class,
					ContainerManagerQueueNames
							.getContainerManagerInterfaceQueryQueue(), 1000000);

			logger.info("try to init CM interface");
			ContainerManagerInterface containerManagerClient = null;
			try {
				containerManagerClient = clientInfo.init();
				// Check what type of device it is through parsing
				for (String[] dev : parsedDevices) {
					int deviceCode = 0;
					boolean virtual = false;
					// See what kind of device it is
					if (dev[6].contains("Temperatur"))
						deviceCode = 13;
					if (dev[6].contains("Humidity"))
						deviceCode = 12;
					if (dev[6].contains("power meter")) {
						deviceCode = 101;
						virtual = true;
					}
					if (deviceCode != 0) {
						HashMap<String, Object> deviceSpec = containerManagerClient
								.getDeviceSpecData(deviceCode);
						if (deviceSpec != null) {

							DeviceContainer device = new DeviceContainer(
									new org.fortiss.smg.containermanager.api.devices.DeviceId(
											dev[0], wrapperTagN), wrapperTagN,
									deviceSpec);
							device.setVirtualContainer(virtual);
							device.setHrName(dev[5] + " " + dev[6]);
							devices.add(device);
						}
						deviceCounter++;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}

			try {
				clientInfo.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<DeviceContainer> getDevices() {
		return devices;
	}

	/**
	 * sends a command to the Device
	 * 
	 * @param command
	 *            1 to turn on 0 to turn off This is being realised by calling a
	 *            php script that runs a bash script The bash script runs on the
	 *            server the proper command for turning the device on/off
	 *            
	 *            
	 */
	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		logger.debug("Received Doublecommand " + command.getValue() + " for "
				+ dev.getDevid());

		// Create proper URL for activateing/deactivateing the device
		String requestUrlStr = "http://192.168.21.217:8080/schalter.php?";
		String devId = dev.getDevid();
		for (int i = 0; i < devId.length(); i++)
			if (devId.charAt(i) == '.') {
				requestUrlStr += "ip=" + devId.substring(0, i);
				break;
			}
		if (command.getValue() == 1)
			requestUrlStr += "&status=on";
		if (command.getValue() == 0)
			requestUrlStr += "&status=off";
		try {
			// Call the URL to run the php script
			URL requestUrl = new URL(requestUrlStr);
			Scanner scanner = new Scanner(requestUrl.openStream());
			String response = scanner.useDelimiter("\\Z").next();
			logger.debug("Device " + dev.getDevid() + " URL requested:"
					+ requestUrlStr);
		} catch (MalformedURLException e) {
			logger.debug("Hexabus failed to send Double command for the Device: "
					+ dev.getDevid());
			e.printStackTrace();
		} catch (IOException e) {
			logger.debug("Hexabus failed to send Double command for the Device: "
					+ dev.getDevid());
			e.printStackTrace();
		}
	}

	private void sendNewDeviceEvents() {
		for (DeviceContainer dev : devices) {
			try {
				logger.debug("Dev:" + dev.getDeviceId().getDevid() + " -> "
						+ clientId);
				master.sendDeviceEvent(new DeviceEvent(dev), this.clientId);
			} catch (TimeoutException e) {
				logger.debug("Failed to send " + dev.getDeviceId()
						+ " to master");
			}
		}
	}

	public void setMaster(IActuatorMaster master) {
		this.master = master;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setPollFrequency(int pollFrequency) {
		this.pollFrequency = pollFrequency;
	}

	public void setWrapperTag(String wrapperTag) {
		this.wrapperTag = wrapperTag;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getClientId() {
		return clientId;
	}

	public List<DeviceContainer> getDeviceSpecs() {
		return devices;
	}

	public IActuatorMaster getMaster() {
		return master;
	}

	public int getPollFrequency() {
		return pollFrequency;
	}

	public String getWrapperTag() {
		return this.wrapperTag;
	}

	@Override
	public boolean isComponentAlive() {
		return true;
	}

	public int getDeviceCounter() {
		return deviceCounter;
	}

	public void setDeviceCounter(int deviceCounter) {
		this.deviceCounter = deviceCounter;
	}
}
