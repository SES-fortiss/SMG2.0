package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor;

import java.util.Collection;





import java.util.concurrent.TimeoutException;

import javax.swing.text.html.HTMLEditorKit.Parser;





//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
import org.fortiss.smg.websocket.api.shared.schema.DeviceCategory;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
//import org.fortiss.smartmicrogrid.shared.schema.TeachInTelegramInfo;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teachintelegraminfo.TeachInTelegramInfo;

public class OurHardcodedActorStrategy extends ActorStrategy {
	private static final Logger logger = LoggerFactory.getLogger(OurHardcodedActorStrategy.class);

	private boolean light1state = false;
	private boolean light2state = false;
	
	
	public OurHardcodedActorStrategy() {
		
	}

	@Override
	public void setBoolean(boolean valueBool, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		try{
		logger.debug("received setBoolean({},{})", valueBool, internalID);
		UniversalTelegram telegram = new UniversalTelegram();
		telegram.setOrg(EnOceanOrigin.EEP_RPS);
		int base;
		if (internalID.equals("0")) {
			light1state = !light1state;
			base = 0x00;
		} else {
			light2state = !light2state;
			base = 0x40;
		}
		telegram.setDataByte((char) (valueBool ? base + 0x30 : base + 0x10), 3);
		telegram.setIdHexString(communicator.getSenderdeviceId(), 1); 
		
		if (execute)
			communicator.sendTelegram(telegram);
//		DeviceId origin = impl.getDeviceSpecs().get(4).getDeviceId();
		DeviceId origin = actor.getDeviceId();
		logger.debug(actor.getDeviceId().toString()+ " In Actor OurHardcoded Devices class");
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
	public void setDouble(double valueDbl, SIUnitType unit, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.debug("received setDouble({},{})", valueDbl, internalID);
		try{
		UniversalTelegram telegram = new UniversalTelegram();
		telegram.setOrg(EnOceanOrigin.EEP_RPS);

		telegram.setDataByte((char) (valueDbl % 255.0), 3);
		telegram.setIdHexString("FF9C6E80", Integer.parseInt(internalID));
		if (execute)
			communicator.sendTelegram(telegram);
		
//		DeviceId origin = impl.getDeviceSpecs().get(4).getDeviceId();
		DeviceId origin = actor.getDeviceId();
		DoubleEvent ev = new DoubleEvent(valueDbl);
		impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
		logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + valueDbl );
//		getEventHandler().doubleEvent(actor.getId(), valueDbl, unit, 1D);
		}catch(TimeoutException e){
			 logger.error("timeout sending to master", e);
		}
	}

	@Override
	public void setString(String valueStr, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.debug("received setStr({},{})", valueStr, internalID);
		try{
		UniversalTelegram telegram = new UniversalTelegram();
		telegram.setOrg(EnOceanOrigin.EEP_RPS);

		telegram.setDataByte(valueStr.charAt(0), 3);
		telegram.setIdHexString("FF9C6E80", Integer.parseInt(internalID));
		if (execute)
			communicator.sendTelegram(telegram);
		
//		DeviceId origin = impl.getDeviceSpecs().get(4).getDeviceId();
		DeviceId origin = actor.getDeviceId();
		logger.debug(actor.getDeviceId().toString()+ " In Actor OurHardcoded Devices class");
		DoubleEvent ev = new DoubleEvent(Integer.parseInt(valueStr));
		impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
		logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + valueStr );
//		getEventHandler().stringEvent(actor.getId(), valueStr);
		}catch(TimeoutException e){
			 logger.error("timeout sending to master", e);
		}
	};

	@Override
	public void toggle(String internalIDToggle, int delay, String tag, boolean execute, String valueIdentifier) {
		logger.debug("received toggle for {}", internalIDToggle);
		if (internalIDToggle.equals("0")) {
			setBoolean(!light1state, internalIDToggle, delay, tag, execute, valueIdentifier);
		} else {
			setBoolean(!light2state, internalIDToggle, delay, tag, execute, valueIdentifier);
		}

//		getEventHandler().toggleEvent(actor.getId());
	}

//	@Override
//	public DeviceSpec getSpec() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Fortiss.OurHardcodedActor", DeviceCategory.AC);
//		builder.addDouble(SIUnitType.PERCENT).withRange(0, 255).asCommand();
//		builder.addBoolean().asCommand();
//		builder.addString().asCommand();
//		builder.addToggle().asCommand();
//
//		return builder.create();
//	}

	@Override
	public Collection<TeachInTelegramInfo> getSupportedTeachInTelegrams() {
		return null;
	}

}




