package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor;

import java.util.ArrayList;
import java.util.Collection;

import java.util.List;
import java.util.concurrent.TimeoutException;

//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
//import org.fortiss.smartmicrogrid.shared.schema.TeachInTelegramInfo;
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

import teachintelegraminfo.TeachInTelegramInfo;

public class Light5070ActorStrategy extends ActorStrategy {

	private static final Logger logger = LoggerFactory.getLogger(Light5070ActorStrategy.class);

	private boolean lightState = false;
	
	public Light5070ActorStrategy() {
	
	}
	
//	@Override
//	public DeviceSpec getSpec() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Fortiss.Light5070", DeviceCategory.LIGHT);
//		builder.addBoolean().withFalseSynonym("OFF").withTrueSynonym("ON").asCommand();
//		builder.addBoolean().withFalseSynonym("OFF").withTrueSynonym("ON").asEvent();
//
//		builder.addToggle().withToggleSynonym("Toggle Lights").asCommand();
//
//		return builder.create();
//	}

	@Override
	public void setBoolean(boolean valueBool, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.debug("received setBoolean({},{})", valueBool, internalID);
		lightState = valueBool;
		try{
		UniversalTelegram telegram = new UniversalTelegram();
		telegram.setOrg(EnOceanOrigin.EEP_RPS);
		telegram.setDataByte((char) (valueBool ? 0x70 : 0x50), 3); //send upper or lower button
		telegram.setIdHexString(communicator.getSenderdeviceId(), actor.getChannel());
		
		if (execute) {
			communicator.sendTelegram(telegram);
		}
		
//		List<DeviceContainer> spec = impl.getDeviceSpecs();
//		DeviceId origin = spec.get().getDeviceId();
		DeviceId origin = actor.getDeviceId();
		logger.debug(actor.getDeviceId().toString()+"In Actor Light 5070 class");
		Double value = 0.0;
		if (valueBool)
			value = 1.0;
		DoubleEvent ev = new DoubleEvent(value);
        impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
        logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + value );
//		getEventHandler().booleanEvent(actor.getId(), valueBool);
		} catch (TimeoutException e) {
	       	 logger.error("timeout sending to master", e);
			 }
	}

	@Override
	public void toggle(String internalIDToggle, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.debug("received toggle for {}", internalIDToggle);
		setBoolean(!lightState, internalIDToggle, delay, tag, execute, valueIdentifier);
	}

	@Override
	public Collection<TeachInTelegramInfo> getSupportedTeachInTelegrams() {

		ArrayList<TeachInTelegramInfo> ts = new ArrayList<TeachInTelegramInfo>();

		UniversalTelegram telegram = new UniversalTelegram();
		telegram.setOrg(EnOceanOrigin.EEP_RPS);
		telegram.setDataByte((char) 0x50, 3);

		TeachInTelegramInfo ti = new TeachInTelegramInfo();
		ti.setTeachInTelegramString(telegram.getTelegramString());
		ti.setTeachInTelegramDescription("Schalter 50/70 Logik einlernen 0x50 (bitte ebenfalls 0x50 einlernen)");
		ts.add(ti);

		ti = new TeachInTelegramInfo();
		telegram.setDataByte((char) 0x70, 3);
		ti.setTeachInTelegramString(telegram.getTelegramString());
		ti.setTeachInTelegramDescription("Schalter 50/70 Logik einlernen 0x70 (bitte ebenfalls 0x70 einlernen)");
		ts.add(ti);

		return ts;
	}
}