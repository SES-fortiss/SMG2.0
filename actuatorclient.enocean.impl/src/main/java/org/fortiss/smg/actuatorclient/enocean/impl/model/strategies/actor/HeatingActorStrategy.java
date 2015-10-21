package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor;

import java.util.ArrayList;
import java.util.Collection;
//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
//import org.fortiss.smartmicrogrid.shared.schema.TeachInTelegramInfo;


import java.util.concurrent.TimeoutException;

import org.fortiss.smg.websocket.api.shared.schema.DeviceCategory;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teachintelegraminfo.TeachInTelegramInfo;

public class HeatingActorStrategy extends ActorStrategy {

	private static final Logger logger = LoggerFactory.getLogger(HeatingActorStrategy.class);
	private static final double REF_TEMP = 22.0;
	private static final double HYSTERESIS = 4.0;

	private static final String CELSIUS_REFERENCE_IDENTIFIER = "Reference";
	private static final String CELSIUS_ACTUAL_IDENTIFIER = "Actual";
	
	private double act;
	private double ref;
	private double nig;
	private long act_time;
	private long ref_time;
	private long nig_time;
	
	public HeatingActorStrategy() {
		
	}
	
	@Override
	public void setBoolean(boolean valueBool, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.debug("received setBoolean({},{})", valueBool, internalID);
		if (valueBool) {
			setDouble(100.0, SIUnitType.PERCENT, internalID, delay, tag, execute, valueIdentifier);
		} else {
			setDouble(0.0, SIUnitType.PERCENT, internalID, delay, tag, execute, valueIdentifier);
		}
	}

	@Override
	public void setDouble(final double valueDbl, SIUnitType unit, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		
		try{
		if (SIUnitType.PERCENT.equals(unit) && execute) {
		
			// night reduction in DataByte 3
			// 0x00 = 0Â°K, 0x06 = 1Â°K, 0x0C = 2Â°K, 0x13 = 3Â°K, 0x19 = 4Â°K, 0x1F
			// = 5Â°K
			// reference temperature in DataByte 2
			// 0 = 40Â°C, linear 0x00 - 0xFF adjustable range: 8Â°C - 40Â°C
			// actual temperature in DataByte 1
			// 0 = 40Â°C, linear 0xFFss - 0x00
			// learn button
			// DB0_Bit3 = LRN Button (0 = teach-in telegram, 1 = data telegram)
			// for data telegram: 0x0F, for teach-in telegram: 0x87

			sendTelegram((char) 0x00, getTempChar0x00(REF_TEMP),getTempChar40x00(getActTemp(valueDbl, REF_TEMP, HYSTERESIS)), (char) 0x0F, (char) 0x30);
			
//			DeviceId origin = impl.getDeviceSpecs().get(0).getDeviceId();
			DeviceId origin = actor.getDeviceId();
			System.out.println(actor.getDeviceId().toString()+"In Actor Heating class");
			DoubleEvent ev = new DoubleEvent(valueDbl);
            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId() );
            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + valueDbl );
						
            //getEventHandler().doubleEvent(actor.getId(), valueDbl, SIUnitType.PERCENT, 5D);

		} else if (CELSIUS_ACTUAL_IDENTIFIER.equals(valueIdentifier) && SIUnitType.CELSIUS.equals(unit)) {
			act = valueDbl;
			act_time = System.currentTimeMillis();
			triggerEventFromValues(execute);
		} else if (CELSIUS_REFERENCE_IDENTIFIER.equals(valueIdentifier) && SIUnitType.CELSIUS.equals(unit)) {
			ref = valueDbl;
			ref_time = System.currentTimeMillis();
			triggerEventFromValues(execute);
		} else if (SIUnitType.K.equals(unit)) {
			nig = valueDbl;
			nig_time = System.currentTimeMillis();
			triggerEventFromValues(execute);
		} else {
			logger.warn("HeatingActorStrategy can only be used with setDouble(double, \"%\", String unit, int delay, String tag, boolean execute)");
		}
		} catch (TimeoutException e) {
       	 logger.error("timeout sending to master", e);
       	 }
	}

	private void triggerEventFromValues(boolean execute) {
		try{
		if (Math.abs(act_time - ref_time) < 5000 && Math.abs(ref_time - nig_time) < 5000) {

			if (execute) {
				sendTelegram((char) nig, (char) ref, (char) act, (char) 0x0F, (char) 0x30);
			}

			double value = 100.0 * ((ref - act) / HYSTERESIS);
			// difference = hysteresis -> 100% | difference 0 -> 0%
			value = Math.max(0.0, Math.min(100.0, value));
			
//			DeviceId origin = impl.getDeviceSpecs().get(0).getDeviceId();
			DeviceId origin = actor.getDeviceId();
			logger.debug(actor.getDeviceId().toString()+"In Actor Heating Class");
			DoubleEvent ev = new DoubleEvent(value);
            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId() );
            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//			getEventHandler().doubleEvent(actor.getId(), value, SIUnitType.PERCENT, 5D);
		}
		}catch (TimeoutException e) {
	       	 logger.error("timeout sending to master", e);
		}
	}

	private void sendTelegram(char db3, char db2, char db1, char db0, char status) {

		UniversalTelegram telegram = new UniversalTelegram();
		telegram.setOrg(EnOceanOrigin.EEP_4BS);
		telegram.setIdHexString(communicator.getSenderdeviceId(), actor.getChannel());



		// night reduction in DataByte 3
		// 0x00 = 0Â°K, 0x06 = 1Â°K, 0x0C = 2Â°K, 0x13 = 3Â°K, 0x19 = 4Â°K, 0x1F
		// = 5Â°K
		telegram.setDataByte(db3, 3);

		// reference temperature in DataByte 2
		// 0 = 40Â°C, linear 0x00 - 0xFF adjustable range: 8Â°C - 40Â°C
		telegram.setDataByte(db2, 2);

		// actual temperature in DataByte 1
		// 0 = 40Â°C, linear 0xFFss - 0x00
		telegram.setDataByte(db1, 1);

		// learn button
		// DB0_Bit3 = LRN Button (0 = teach-in telegram, 1 = data telegram)
		// for data telegram: 0x0F, for teach-in telegram: 0x87
		telegram.setDataByte(db0, 0);

		//TODO why did we do that??!
		//telegram.setPrefixPostfix((char) 0x6B);

		telegram.setStatus(status);

		communicator.sendTelegram(telegram);
	}

	public char getTempChar0x00(double tempC) {
		// 0 -> 0x00
		// 40 -> 0xFF
		int scaledTemp = (int) (tempC * (255.0 / 40.0));
		return (char) scaledTemp;
	}

	public char getTempChar40x00(double tempC) {
		// 0 -> 0xFF
		// 40 -> 0x00
		int scaledTemp = (int) (255.0 - (tempC * (255.0 / 40.0)));
		return (char) scaledTemp;
	}

	public double getActTemp(double percentage, double refTemp, double hysteresis) {

		if (percentage == 100.0) {
			return refTemp - hysteresis - 5;
			// -5 to make sure we are below ref-hys and do not need the
			// LEQ-equals-condition -> 100% for sure!
		} else if (percentage == 0.0) {
			return refTemp + 5;
			// +5 to make sure we are above refTemp and do not need the
			// GEQ-equals-condition -> 0% for sure!
		} else {
			return refTemp - percentage * (hysteresis / 100.0);
			// default (should work for 100 and 0 too) calculates an actTemp
			// based on the percentage that shall be set.
		}
	}

//	@Override
//	public DeviceSpec getSpec() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("Enocean.Fortiss.Heating", DeviceCategory.HEATING);
//
//		builder.addDouble(SIUnitType.PERCENT).withRange(0, 100).withDescription("ON-Percentage").asCommand();
//		builder.addDouble(SIUnitType.PERCENT).withRange(0, 100).withDescription("ON-Percentage").asEvent();
//
//		builder.addBoolean().withFalseSynonym("MIN/OFF").withTrueSynonym("MAX").asCommand();
//
//		return builder.create();
//	}

	@Override
	public Collection<TeachInTelegramInfo> getSupportedTeachInTelegrams() {
		UniversalTelegram telegram = new UniversalTelegram();
		telegram.setOrg(EnOceanOrigin.EEP_4BS);
		telegram.setDataByte((char) 0x40, 3);
		telegram.setDataByte((char) 0x30, 2);
		telegram.setDataByte((char) 0x0D, 1);
		telegram.setDataByte((char) 0x87, 0);
		telegram.setStatus((char) 0x30);
		TeachInTelegramInfo ti = new TeachInTelegramInfo();
		ti.setTeachInTelegramString(telegram.getTelegramString());
		ti.setTeachInTelegramDescription("FTR55D Logik einlernen (Temperature controller/sensors) supports setDouble(%)");

		ArrayList<TeachInTelegramInfo> ts = new ArrayList<TeachInTelegramInfo>();
		ts.add(ti);

		return ts;
	}


}

