package org.fortiss.smg.actuatorclient.hexabus.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.slf4j.LoggerFactory;

public class HexabusLooper implements Runnable {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(HexabusLooper.class);

	private ActuatorClientImpl impl;
	private DoubleEvent ev;
	private JSONReaderHexabus jsonReader;
	private ArrayList<String[]> values;

	HexabusLooper(ActuatorClientImpl impl) {
		this.impl = impl;
		jsonReader = new JSONReaderHexabus();
	}

	@Override
	public void run() {
		logger.info("Read values at: " + (new Date()));
		readDevice();
	}

	private void readDevice() {
		impl.getDevices();
		values = (ArrayList<String[]>) jsonReader.readJson_Hexabus(impl
				.getHost());
		if (values != null) {
			if (values.size() > impl.getDeviceCounter()) {
				impl.loadStaticDevs(impl.getWrapperTag());
				logger.info("############### HexabusWrapperImpl: read Hexabus devices: ###############");
			}
			logger.debug("############### Devices: "
					+ impl.getDevices().toString() + " ###############");
			createNewEvent();
		}
	}

	/**
	 * The Order of the devices is important when the Events are created. The
	 * Devices are initialised dynamically by measuring how many they are at the
	 * moment.Devices If the new device number is the same as the old one, then
	 * it will not reinitialise anything. Othewise we make a new device
	 * initialisation.
	 */
	private void createNewEvent() {

		if (values.size() != impl.getDeviceCounter())
			impl.loadStaticDevs(impl.getWrapperTag());
		int i = 0;
		for (DeviceContainer dev : impl.getDevices()) {
			ev = new DoubleEvent(Double.parseDouble(values.get(i)[1]));
			try {
				impl.getMaster().sendDoubleEvent(ev, dev.getDeviceId(),
						impl.getClientId());
				logger.info(dev.getDeviceId() + " send new event: "
						+ ev.getValue() + " " + dev.getDeviceType().getType());
				logger.debug(dev.getDeviceId() + " send new event: "
						+ ev.getValue() + " " + dev.getDeviceType().getType());
			} catch (TimeoutException e) {
				logger.debug("Event can't be sent to the master");
				e.printStackTrace();
			}
			i++;
		}

	}

}
