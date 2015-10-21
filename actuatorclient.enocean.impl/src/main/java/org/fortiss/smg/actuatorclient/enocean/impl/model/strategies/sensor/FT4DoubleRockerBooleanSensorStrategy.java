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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FT4DoubleRockerBooleanSensorStrategy extends SensorStrategy {
	private static final Logger logger = LoggerFactory.getLogger(FT4DoubleRockerBooleanSensorStrategy.class);
	
	public FT4DoubleRockerBooleanSensorStrategy() {
		super();
	}

	/**
	 * See wireless_system_high_res-1.pdf page219
	 */
	@Override
	public void handleTelegram(UniversalTelegram telegram) {
		if (telegram == null) {
			return;
		}
		try{
			
			String id = telegram.getIdString();
			String type = telegram.getDataString();
//			boolean type5070 = false;
//			if(type.contains("70")|| type.contains("50"))
//				type5070 = true;
//			
			logger.debug("Received telegram: {}", telegram);
			
//			List<DeviceId> devices = new ArrayList<DeviceId>();
//			devices.add(impl.getDeviceSpecs().get(13).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(14).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(19).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(21).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(23).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(25).getDeviceId());
//			
			// remove leading zeros
			for(int i=0; i < 7; i++){
		    	if(id.charAt(0) == '0'){
		    		id = id.substring(1);
		    	}else{
		    		break;
		    	}
		    }
			
			
			// get the first matching device
//			int count = 0;
//			for (DeviceId deviceId : devices) {
//				if(deviceId.toString().contains(id))
//					break;
//				count++;
//			}
//		
//			if(count < devices.size()){
//				DeviceId origin5070 = devices.get(count);
			DeviceId origin = sensor.getDeviceId();
			logger.debug(sensor.getDeviceId().toString()+ " In Sensor FTDoubleRocker class");
			
			if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x50) {
				DoubleEvent ev = new DoubleEvent(0.0);
//		        impl.getMaster().sendDoubleEvent(ev, origin5070,impl.getClientId());
				impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
		        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + 0.0 );
//				getEventHandler().booleanEvent(sensor.getId()+"_5070", false);
		        
			} else if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x70) {
				DoubleEvent ev = new DoubleEvent(1.0);
		        impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
		        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + 1.0 );
//		        getEventHandler().booleanEvent(sensor.getId()+"_5070", true);
		        
			} 
//			}
			
//			if(!type5070){
//			count = 0;
//			devices = new ArrayList<DeviceId>() ;
//			devices.add(impl.getDeviceSpecs().get(12).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(15).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(16).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(17).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(18).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(20).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(22).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(24).getDeviceId());
//			
//			for (DeviceId deviceId : devices) {
//				if(deviceId.toString().contains(id))
//					break;
//				count++;
//			}
//			
//			
			
//			DeviceId origin1030 = devices.get(count);
			
		
		if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x10) {
			DoubleEvent ev = new DoubleEvent(0.0);
//	        impl.getMaster().sendDoubleEvent(ev, origin1030,impl.getClientId());
	        impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + 0.0 );
//			getEventHandler().booleanEvent(sensor.getId()+"_1030", false);
			
		} else if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x30) {
			DoubleEvent ev = new DoubleEvent(1.0);
	        impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
	        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + 1.0 );
//			getEventHandler().booleanEvent(sensor.getId()+"_1030", true);
	        
		} 
//			}
			
			
		
		// other stuff
		
		if (telegram.getOrg().equals(EnOceanOrigin.EEP_RPS) && telegram.getDataByte(3) == 0x00
				&& telegram.getStatus() == 0x20) {
			// release button
		} else {
			//invalid sensor-databyte(3) (10,30,50,70) or origin
			logger.warn(sensor.getId() + " uses FT4 - check correctness!");
		}
		} catch (TimeoutException e) {
       	 logger.error("timeout sending to master", e);
		 }
	}

	@Override
	public Collection<String> getPossibleOrigins(){
		return Arrays.asList(sensor.getId()+"_1030", sensor.getId()+"_5070");
	}

//	@Override
//	public Map<String, DeviceSpec> getSpecs() {
//		// TODO Auto-generated method stub
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Eltako.FT4DoubleRocker", DeviceCategory.SWITCH);
//		builder.addBoolean().asEvent();
//		DeviceSpec spec = builder.create();
//
//		HashMap<String, DeviceSpec> result = new HashMap<String, DeviceSpec>();
//		for (String origin : getPossibleOrigins()) {
//			result.put(origin, spec);
//		}
//		return result;
//	}

}
