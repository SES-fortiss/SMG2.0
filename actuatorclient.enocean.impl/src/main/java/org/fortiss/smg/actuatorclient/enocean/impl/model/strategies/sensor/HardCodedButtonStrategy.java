package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor;

import java.util.Map;


import java.util.concurrent.TimeoutException;

//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
import org.fortiss.smg.websocket.api.shared.schema.DeviceCategory;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HardCodedButtonStrategy extends SensorStrategy {
	private static final Logger logger = LoggerFactory.getLogger(HardCodedButtonStrategy.class);
	
	public HardCodedButtonStrategy() {
		super();
		
	}

	@Override
	public void handleTelegram(UniversalTelegram telegram) {
		if (telegram == null) {
			return;
		}
		
		try{
			
		Double value =  0.0;
//		DeviceId origin = impl.getDeviceSpecs().get(37).getDeviceId();
		DeviceId origin = sensor.getDeviceId();
		logger.debug(sensor.getDeviceId().toString()+ " In Sensor HardcoudedButtonStrategy class");
		logger.debug("Received telegram: {}", telegram);
		if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x50) {
			DoubleEvent ev = new DoubleEvent(value);
			impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
		    logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//			getEventHandler().booleanEvent("enoceanQ?btn1", false);
		} else if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x70) {
			value = 1.0;
			DoubleEvent ev = new DoubleEvent(value);
			impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
		    logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//			getEventHandler().booleanEvent("enoceanQ?btn1", true);
		}
		if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x10) {
			DoubleEvent ev = new DoubleEvent(value);
//			origin = impl.getDeviceSpecs().get(36).getDeviceId();
			impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
		    logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//			getEventHandler().booleanEvent("enoceanQ?btn2", false);
		} else if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x30) {
			DoubleEvent ev = new DoubleEvent(value);
//			origin = impl.getDeviceSpecs().get(36).getDeviceId();
			impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
		    logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//			getEventHandler().booleanEvent("enoceanQ?btn2", true);
		}
		} catch (TimeoutException e) {
       	 logger.error("timeout sending to master", e);
		 }
	}

//	@Override
//	public Map<String, DeviceSpec> getSpecs() {
//		return asHashMap(new DeviceSpecBuilder("EnOcean.Fortiss.HardCodedButton", DeviceCategory.SWITCH).addBoolean().asEvent().create());
//	}

}