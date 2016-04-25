package org.fortiss.smg.actuatorclient.froeschl.impl;

import java.util.Date;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.slf4j.LoggerFactory;

public class FroeschlLooper implements Runnable {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(FroeschlLooper.class);

	private ActuatorClientImpl impl;
	private DoubleEvent ev;
 	//private JSONReaderFroeschl jsonReader;
	FroeschlLooper(ActuatorClientImpl impl) {
		this.impl = impl;
		//jsonReader= new JSONReaderFroeschl();	
	}

	@Override
	public void run() {
		logger.info("Read values at: " + (new Date()));
		readDevice();
	}
	
	
	private void readDevice() {
		impl.getDevices();
		logger.info("Froeschl start: " + (new Date()));
		/*
		 * The reader provides 2 times more values (value and unit) for each device
		 */
		String[] val = impl.getReader().readJsonFroeschl(impl.getHost());
		if (val != null && val.length==impl.getDeviceCounter()*2) {
			logger.info("############### FroeschlWrapperImpl: read Froeschl devices: ###############");
			createNewEvent(val);
		}
		else if (val != null && val.length!=impl.getDeviceCounter()*2) {
			logger.info("Wrong number of values/units re-read available devices ");
			impl.loadStaticDevs(impl.getWrapperTag(), val);
		}
		else {
			logger.debug("FroeschlbusLooper could not get any Data");
		}
	}
	/*
	 *      Creating a new Event with this String
	 *  	[0] = Actual Consumption
	 *  	[1] = Actual Consumption Unit
	 *  	[2] = aktueller Zaehlerstand
	 *  	[3] = aktueller Zaehlerstand Unit
	 *  	[4] = Tagesverbrauch
	 */
	 
	private void createNewEvent(String[] values) {
		if ((values.length/2) != impl.getDeviceCounter()) {
			impl.loadStaticDevs(impl.getWrapperTag(), values);
		}
		int counter = 0;
		int loggercount = 0;
		for(DeviceContainer dev : impl.getDevices()) {
			ev = new DoubleEvent(Double.parseDouble(values[counter]));
			
			logger.debug("got " + ev.getValue() + " for " + impl.getDevices().get(loggercount).getHrName());
			counter = counter +2;
			loggercount++;
			try {
				logger.info(dev.getDeviceId() + " send new event: " + ev.getValue() + " " + dev.getDeviceType().getType());
				impl.getMaster().sendDoubleEvent(ev, dev.getDeviceId(), impl.getClientId());
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
