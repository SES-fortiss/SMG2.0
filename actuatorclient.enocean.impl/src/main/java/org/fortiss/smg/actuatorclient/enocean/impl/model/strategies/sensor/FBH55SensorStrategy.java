package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor;

import java.util.Map;
import java.util.concurrent.TimeoutException;


//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
import org.fortiss.smg.websocket.api.shared.schema.DeviceCategory;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FBH55SensorStrategy extends SensorStrategy{
	private static final Logger logger = LoggerFactory.getLogger(FBH55SensorStrategy.class);
	
	public FBH55SensorStrategy() {
		super();
	}
	/**
	 * See wireless_system_high_res-1.pdf page219
	 */
	@Override
	public void handleTelegram(UniversalTelegram telegram) {
		try{if (telegram.getOrg().equals(EnOceanOrigin.EEP_4BS) && !isTeachIn(telegram)) {
			int brightnessByte = telegram.getDataByte(2);
			// brightness is [0-2048]
			int realBrightness = brightnessByte*8;
			
//			DeviceId origin = impl.getDeviceSpecs().get(10).getDeviceId();
			DeviceId origin = sensor.getDeviceId();
			
			
		
			
			double value = (double) realBrightness;
			DoubleEvent ev = new DoubleEvent(value);
			System.out.println("++++ DeviceCode : " + sensor.getDeviceCode() + " ++++");
			if (sensor.getDeviceCode() != 153) {
	        impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//			getEventHandler().doubleEvent(sensor.getId(), realBrightness, SIUnitType.LUX, 4D);
	        
	        //TODO: motion is another device !
			}
			else if (sensor.getDeviceCode() == 153) {
			char motionByte = telegram.getDataByte(0);
			boolean boolMotion = (motionByte==0x0d);
//			origin = impl.getDeviceSpecs().get(25).getDeviceId();
			if(boolMotion)
				value = 1.0;
			else 
				value = 0.0;
			
			ev = new DoubleEvent(value);
	        impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//			getEventHandler().booleanEvent(sensor.getId(), boolMotion);
			}

		}else {
			logger.warn("Received invalid telegram:"+telegram.getTelegramString());
		}
		} catch (TimeoutException e) {
       	 logger.error("timeout sending to master", e);
		 }
	}

	private boolean isTeachIn(UniversalTelegram telegram) {
		return  telegram.getDataByte(3) == 0x20 &&
				telegram.getDataByte(2) == 0x08 &&
				telegram.getDataByte(1) == 0x0D &&
				telegram.getDataByte(0) == 0x85;
	}

//	@Override
//	public Map<String, DeviceSpec> getSpecs() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Eltako.FBH55", DeviceCategory.SENSOR);
//		builder.addCategory(DeviceCategory.MOTION_DETECTOR);
//		builder.addDouble(SIUnitType.LUX).withRange(0d,2048d).withDescription("Indoor brightness").asEvent();
//		builder.addBoolean().withDescription("Occupancy").withFalseSynonym("unoccupied").withTrueSynonym("occupied").asEvent();
//		return asHashMap(builder.create());
//	}

}
