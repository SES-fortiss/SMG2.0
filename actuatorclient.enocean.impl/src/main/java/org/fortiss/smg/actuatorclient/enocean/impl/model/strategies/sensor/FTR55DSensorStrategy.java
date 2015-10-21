package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import java.util.concurrent.TimeoutException;



//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
import org.fortiss.smg.websocket.api.shared.schema.DeviceCategory;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FTR55DSensorStrategy extends SensorStrategy {

	private static final String KELVIN_NR = "Kelvin NR";
	private static final String CELSIUS_REFERENCE_POSTFIX = "_Reference";
	private static final String CELSIUS_ACTUAL_POSTFIX = "_Actual";
	private static final Logger logger = LoggerFactory.getLogger(FTR55DSensorStrategy.class);
	
	
	public FTR55DSensorStrategy() {
	
	}
	@Override
	public Collection<String> getPossibleOrigins() {
		return Arrays.asList(sensor.getId()+CELSIUS_ACTUAL_POSTFIX, sensor.getId()+CELSIUS_REFERENCE_POSTFIX, sensor.getId()+KELVIN_NR);
	}

	/**
	 * See wireless_system_high_res-1.pdf page220
	 */
	@Override
	public void handleTelegram(UniversalTelegram telegram) {
		try{
		if (telegram.getOrg().equals(EnOceanOrigin.EEP_4BS) && !isTeachIn(telegram)) {
			
			int count = 0;
			String id = telegram.getIdString();
			// remove leading zeros
			for(int i=0; i < 7; i++){
		    	if(id.charAt(0) == '0'){
		    		id = id.substring(1);
		    	}else{
		    		break;
		    	}
		    }
//			List<DeviceId> devices = new ArrayList<DeviceId>();
//		    
//			devices.add(impl.getDeviceSpecs().get(28).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(30).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(35).getDeviceId());
//			
//			for (DeviceId deviceId : devices) {
//				if(deviceId.toString().contains(id))
//					break;
//				count++;
//			}
//			DeviceId origin = devices.get(count);
			DeviceId origin = sensor.getDeviceId();
			logger.debug(sensor.getDeviceId().toString()+ " In Sensor FTR55D class");
			//Actual Temperature
			int actTempByte = telegram.getDataByte(1);
			double actualTemp =  (40 - actTempByte * 40/256.0);
			DoubleEvent ev = new DoubleEvent(actualTemp);
            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + actualTemp );
//			getEventHandler().doubleEvent(sensor.getId()+CELSIUS_ACTUAL_POSTFIX, actualTemp, SIUnitType.CELSIUS, 0.5);

			//Reference Temperature , now it's not needed
//			int refTempByte = telegram.getDataByte(2);
//			double referenceTemp =  (refTempByte * 40/256.0);
//			origin = impl.getDeviceSpecs().get(30).getDeviceId();
//			ev = new DoubleEvent(referenceTemp);
//            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
//            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + referenceTemp );
//			getEventHandler().doubleEvent(sensor.getId()+CELSIUS_REFERENCE_POSTFIX, referenceTemp, SIUnitType.CELSIUS, 0.078125);

			//Night reduction , now it's not needed
//			int nightReducByte = telegram.getDataByte(3);
//			int nightReductionKelvin = nightReducByte/6;
//			Double value = (double) nightReductionKelvin;
//			origin = impl.getDeviceSpecs().get(35).getDeviceId();
//			ev = new DoubleEvent(value);
//            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
//            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
			//0x00 = 0ï¿½K, 0x06 = 1ï¿½K, 0x0C = 2ï¿½K, 0x13 = 3ï¿½K, 0x19 = 4ï¿½K, 0x1F = 5ï¿½K
//			getEventHandler().doubleEvent(sensor.getId(), nightReductionKelvin, SIUnitType.K, 0D);
		}} catch (TimeoutException e) {
       	 logger.error("timeout sending to master", e);
		 }
	}


	private boolean isTeachIn(UniversalTelegram telegram) {
		return  telegram.getDataByte(3) == 0x40 &&
				telegram.getDataByte(2) == 0x30 &&
				telegram.getDataByte(1) == 0x0D &&
				telegram.getDataByte(0) == 0x87;
	}

//	@Override
//	public Map<String, DeviceSpec> getSpecs() {
//		HashMap<String, DeviceSpec> result = new HashMap<String, DeviceSpec>();
//
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Eltako.FTR55.Actual", DeviceCategory.SENSOR);
//		builder.addDouble(SIUnitType.CELSIUS).withDescription("Actual Temperature in Room").withRange(0,40).asEvent();
//		result.put(sensor.getId()+CELSIUS_ACTUAL_POSTFIX,builder.create());
//
//		builder = new DeviceSpecBuilder("EnOcean.Eltako.FTR55.Reference", DeviceCategory.SENSOR);
//		builder.addDouble(SIUnitType.CELSIUS).withRange(8d,40d).withDescription("Reference temperature set on temperature controller").asEvent();
//		result.put(sensor.getId()+CELSIUS_REFERENCE_POSTFIX,builder.create());
//
//		builder = new DeviceSpecBuilder("EnOcean.Eltako.FTR55.NightReduction", DeviceCategory.SENSOR);
//		builder.addDouble(SIUnitType.K).withDescription("Night reduction set on temperature controller").withRange(0, 40).asEvent();
//		result.put(sensor.getId()+KELVIN_NR, builder.create());
//
//		return result;
//	}



}

