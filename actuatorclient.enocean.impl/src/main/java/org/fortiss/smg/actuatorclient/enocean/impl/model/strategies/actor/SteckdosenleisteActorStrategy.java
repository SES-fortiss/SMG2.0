package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor;

import java.util.ArrayList;
import java.util.Collection;

import java.util.concurrent.TimeoutException;
//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
import org.fortiss.smg.websocket.api.shared.schema.DeviceCategory;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
//import org.fortiss.smartmicrogrid.shared.schema.TeachInTelegramInfo;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teachintelegraminfo.TeachInTelegramInfo;

public class SteckdosenleisteActorStrategy extends ActorStrategy {

	private static final Logger logger = LoggerFactory.getLogger(SteckdosenleisteActorStrategy.class);

	private boolean lightState = false;

	
	
	public SteckdosenleisteActorStrategy() {
		 
	}
//	@Override
//	public DeviceSpec getSpec() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Fortiss.Steckdosenleiste", DeviceCategory.SWITCH);
//		builder.addBoolean().withFalseSynonym("OFF").withTrueSynonym("ON").asCommand();
//		builder.addBoolean().withFalseSynonym("OFF").withTrueSynonym("ON").asEvent();
//
//		builder.addToggle().withToggleSynonym("Toggle ON/OFF").asCommand();
//
//
//		return builder.create();
//	}

	@Override
	public void setBoolean(boolean valueBool, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		try{
		logger.debug("received setBoolean({},{})", valueBool, internalID);
		lightState = valueBool;

		UniversalTelegram telegram = new UniversalTelegram();
		telegram.setOrg(EnOceanOrigin.EEP_RPS);
		telegram.setDataByte((char) (valueBool ? 0x10 : 0x30), 3);
		telegram.setStatus((char) 0x30);
		telegram.setIdHexString(communicator.getSenderdeviceId(), actor.getChannel()); 
		
		if (execute) {
			communicator.sendTelegram(telegram);
		} //otherwise change state without executing (external connection)
//		DeviceId origin = impl.getDeviceSpecs().get(6).getDeviceId();
		DeviceId origin = actor.getDeviceId();
		logger.debug(actor.getDeviceId().toString()+ " In Actor Steckdosenleiste class");
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
		telegram.setDataByte((char) 0x10, 3);
		telegram.setStatus((char) 0x30);

		TeachInTelegramInfo ti = new TeachInTelegramInfo();
		ti.setTeachInTelegramString(telegram.getTelegramString());
		ti.setTeachInTelegramDescription("Schalter 10/30 Logik einlernen 0x10 (bitte ebenfalls 0x30 einlernen)");
		ts.add(ti);

		ti = new TeachInTelegramInfo();
		telegram.setDataByte((char) 0x30, 3);
		telegram.setStatus((char) 0x30);
		ti.setTeachInTelegramString(telegram.getTelegramString());
		ti.setTeachInTelegramDescription("Schalter 10/30 Logik einlernen 0x30 (bitte ebenfalls 0x10 einlernen)");
		ts.add(ti);

		return ts;
	}
}