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
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FWZ12SensorStrategy extends SensorStrategy {
	// TODO: look into history and check whether refactoring made any sense!!!
	private static final String OFF_PEAK_POSTFIX = "_offPeak";
	private static final String PEAK_POSTFIX = "_peak";
	private static final Logger logger = LoggerFactory.getLogger(FWZ12SensorStrategy.class);
	
	public FWZ12SensorStrategy() {
		super();
	}
	@Override
	public Collection<String> getPossibleOrigins() {
		return Arrays.asList(sensor.getId()+OFF_PEAK_POSTFIX, sensor.getId()+PEAK_POSTFIX);
	}

	/**
	 * See wireless_system_high_res-1.pdf page219
	 */
	@Override
	public void handleTelegram(UniversalTelegram telegram) {
		try{
		if (telegram.getOrg().equals(EnOceanOrigin.EEP_4BS)
				&& !isTeachIn(telegram)) {
			int value = telegram.getDataByte(3);
			value <<= 8;
			value += telegram.getDataByte(2);
			value <<= 8;
			value += telegram.getDataByte(1);
			double realValue = value;
			if (isEnergyReading(telegram)) {
				realValue *= 100d;
			}
//			int count = 0;
//			String id = telegram.getIdString();
//			List<DeviceId> devices = new ArrayList<DeviceId>();
//			devices.add(impl.getDeviceSpecs().get(31).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(32).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(33).getDeviceId());
//			devices.add(impl.getDeviceSpecs().get(34).getDeviceId());
//			
//			for (DeviceId deviceId : devices) {
//				if(deviceId.toString().contains(id))
//					break;
//				count++;
//			}
//			DeviceId origin = devices.get(count);			
//			
			DeviceId origin = sensor.getDeviceId();
			logger.debug(sensor.getDeviceId().toString()+ " In Sensor FWZ12 class");
			DoubleEvent ev = new DoubleEvent(realValue);
            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId());
            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + realValue );
			// TASK Class B Accuracy of 1%
//			getEventHandler().doubleEvent(sensor.getId() + getPostfix(telegram), realValue,isEnergyReading(telegram) ? SIUnitType.WH : SIUnitType.W, Double.NaN);
		}
		} catch (TimeoutException e) {
       	 logger.error("timeout sending to master", e);
		 }
	};

	private boolean isTeachIn(UniversalTelegram telegram) {
		return telegram.getDataByte(3) == 0x48
				&& telegram.getDataByte(2) == 0x08
				&& telegram.getDataByte(1) == 0x0D
				&& telegram.getDataByte(0) == 0x80;
	}

//	@Override
//	public Map<String, DeviceSpec> getSpecs() {
//		HashMap<String, DeviceSpec> result = new HashMap<String, DeviceSpec>();
//
//		DeviceSpecBuilder peakBuilder = new DeviceSpecBuilder("EnOcean.Eltako.FWZ12.peak", DeviceCategory.SENSOR);
//		peakBuilder.addDouble(SIUnitType.W).withDescription("Current Power Consumption (peak time)").withRange(0d, Double.MAX_VALUE).asEvent();
//		peakBuilder.addDouble(SIUnitType.WH).withDescription("Energy Meter reading (peak time)").withRange(0d, Double.MAX_VALUE).asEvent();
//
//		result.put(sensor.getId()+PEAK_POSTFIX,peakBuilder.create());
//
//		DeviceSpecBuilder offPeakBuilder = new DeviceSpecBuilder("EnOcean.Eltako.FWZ12.offpeak", DeviceCategory.SENSOR);
//		offPeakBuilder.addDouble(SIUnitType.W).withDescription("Current Power Consumption (peak time)").withRange(0d, Double.MAX_VALUE).asEvent();
//		offPeakBuilder.addDouble(SIUnitType.WH).withDescription("Energy Meter reading (peak time)").withRange(0d, Double.MAX_VALUE).asEvent();
//
//		result.put(sensor.getId()+OFF_PEAK_POSTFIX, offPeakBuilder.create());
//
//		return result;
//	}

	private boolean isEnergyReading(UniversalTelegram telegram) {
		char b = telegram.getDataByte(0);
		return b == 0x19 || b == 0x09;

	}


	private String getPostfix(UniversalTelegram telegram) {
		switch (telegram.getDataByte(0)) {
		case 0x09:
			return PEAK_POSTFIX;
		case 0x19:
			return OFF_PEAK_POSTFIX;
		case 0x0C:
			return PEAK_POSTFIX;
		case 0x1C:
			return OFF_PEAK_POSTFIX;
		default:
			logger.warn("Unknown FWZ12-Telegram: " + telegram);
			return "unknown";
		}
	}

}
