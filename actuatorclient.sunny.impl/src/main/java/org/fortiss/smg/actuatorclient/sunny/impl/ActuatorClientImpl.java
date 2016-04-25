package org.fortiss.smg.actuatorclient.sunny.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.client.ClientProtocolException;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.Device;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.GetDevicesRequest;
import org.fortiss.smg.actuatorclient.sunny.impl.dtos.GetDevicesResponse;
import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.slf4j.LoggerFactory;

public class ActuatorClientImpl implements IActuatorClient{

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(ActuatorClientImpl.class);
	private IActuatorMaster master;
	private String clientId;
	private ScheduledExecutorService executor;
	private int pollFrequency;
	private String host;
	private SunnyCommunicator communicator;
	private String wrapperTag;
	ArrayList<String> sunnyDeviceIds;

	public ArrayList<String> getSunnyDeviceIds() {
		return sunnyDeviceIds;
	}

	ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();
	private DeviceContainer batterySpecPerc;
	private DeviceContainer batterySpecVoltage;
	private DeviceContainer chargerAccPower;
	private DeviceContainer batterySpecTemp;
	private DeviceContainer chargerAccBattery;
	private DeviceContainer inverterBattery;

	public ActuatorClientImpl(String host, String port, String wrapperTag,
			int pollFreq, String username, String password) {
		this.host = host;
		this.wrapperTag = wrapperTag;
		loadStaticDevs(wrapperTag);
		communicator = new SunnyCommunicator(this.host);
		pollFrequency = pollFreq;

	}

	public synchronized void activate() {
		try {
			sunnyDeviceIds = requestDeviceIDs();
		} catch (ClientProtocolException e) {
			logger.debug("failed",e);
		} catch (IOException e) {
			logger.debug("failed",e);
		}
		sendNewDeviceEvents();

		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new SunnyLooper(this), 0,
				getPollFrequency(), TimeUnit.SECONDS);
	}

	public synchronized void deactivate() {
		if (!executor.isTerminated() || !executor.isShutdown()) {
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		}
	}

	public String getClientId() {
		return clientId;
	}

	public SunnyCommunicator getCommunicator() {
		return communicator;
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
		return wrapperTag;
	}

	@Override
	public boolean isComponentAlive() {
		return true;
	}

	private void loadStaticDevs(String wraperTagN) {

		 batterySpecPerc = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"battery_percentage", wraperTagN), wraperTagN + ".battery");
		 batterySpecPerc.setRangeMin(0);
		 batterySpecPerc.setRangeMin(100);
		 batterySpecPerc.setRangeStep(0.1);
		 batterySpecPerc.setHrName( "Battery [%]");
		 batterySpecPerc.setDeviceType(SIDeviceType.Battery);
		

		 batterySpecVoltage = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"battery_voltage", wraperTagN),wraperTagN + ".battery");

		 batterySpecVoltage.setRangeMin(-50);
		 batterySpecVoltage.setRangeMin(250);
		 batterySpecVoltage.setRangeStep(0.001);
		 batterySpecVoltage.setHrName( "Battery [V]");
		 batterySpecVoltage.setDeviceType(SIDeviceType.FeedVoltmeter);
		 
		 batterySpecTemp = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"battery_temperature", wraperTagN), wraperTagN
						+ ".battery");

		 batterySpecTemp.setRangeMin(-50);
		 batterySpecTemp.setRangeMin(150);
		 batterySpecTemp.setRangeStep(0.01);
		 batterySpecTemp.setHrName( "Battery [C]");
		 batterySpecTemp.setDeviceType(SIDeviceType.Temperature);
		 
		 chargerAccPower = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"charger_acc_power", wraperTagN), wraperTagN
						+ ".charger" );

		 chargerAccPower.setRangeMin(0);
		 chargerAccPower.setRangeMin(1.0E6);
		 chargerAccPower.setRangeStep(0.001);
		 chargerAccPower.setHrName("Charger Accumulated Power");
		 chargerAccPower.setDeviceType(SIDeviceType.ConsumptionPowermeter);
		 
		 chargerAccBattery = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"charger_acc_battery", wraperTagN), wraperTagN
						+ ".charger");

		 chargerAccBattery.setRangeMin(0);
		 chargerAccBattery.setRangeMin(1.0E6);
		 chargerAccBattery.setRangeStep(0.091);
		 chargerAccBattery.setHrName("Charger Accumulated Battery");
		 chargerAccBattery.setDeviceType(SIDeviceType.ConsumptionAmperemeter);
		 
		 
		 inverterBattery = new DeviceContainer(
				new org.fortiss.smg.containermanager.api.devices.DeviceId(
						"inverter_power", wraperTagN),
				wrapperTag + ".inverter");
		 
		 inverterBattery.setRangeMin(0);
		 inverterBattery.setRangeMin(1.0E6);
		 inverterBattery.setRangeStep(0.01);
		 inverterBattery.setHrName( "Power at inverter");
		 inverterBattery.setDeviceType(SIDeviceType.FeedPowerMeter);
		 

		devices.add(batterySpecPerc);
		devices.add(batterySpecTemp);
		devices.add(batterySpecVoltage);
		devices.add(chargerAccBattery);
		devices.add(chargerAccPower);
		devices.add(inverterBattery);

	}

	public ArrayList<DeviceContainer> getDevices() {
		return devices;
	}

	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		logger.debug("Received Doublecommand for " + dev.getDevid());

	}
	

	ArrayList<String> requestDeviceIDs() throws ClientProtocolException,
			IOException {
		GetDevicesResponse resp = communicator.execute(new GetDevicesRequest(),
				GetDevicesResponse.class);
		ArrayList<String> result = new ArrayList<String>();
		for (Device device : resp.getDevices()) {
			result.add(device.getKey());
		}
		return result;
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

}
