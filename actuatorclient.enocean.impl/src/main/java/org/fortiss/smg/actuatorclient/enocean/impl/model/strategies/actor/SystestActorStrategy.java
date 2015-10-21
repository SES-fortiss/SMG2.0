package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor;

import java.util.Collection;



import java.util.concurrent.TimeoutException;


//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
import org.fortiss.smg.websocket.api.shared.schema.DeviceCategory;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
//import org.fortiss.smartmicrogrid.shared.schema.TeachInTelegramInfo;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teachintelegraminfo.TeachInTelegramInfo;
/**
 * This Actor-Strategy is only for use in tests.
 * It is written to react in a hardcoded fashion to a single hardcoded command and will throw  an exception in any other case.<p>
 * Please refer to the code for details.
 */
public class SystestActorStrategy extends ActorStrategy {
	private static final Logger logger = LoggerFactory.getLogger(SystestActorStrategy.class);
	
	@Override
	public void setDouble(double valueDbl, SIUnitType unit, String internalID, int delay, String tag, boolean execute, String valueIdentifier) {
		
		try{
		// we only want the setDouble that was intended for the system test
		if (unit.equals(SIUnitType.CELSIUS) && valueDbl == 42 && internalID.equals("0") && delay == 23 && tag.equals("testtag")) {
			UniversalTelegram telegram = new UniversalTelegram();

			//TODO fuuu invalid Origins not possible
//			telegram.setType((char) 0x11);
			//replaced 11 by UNKNOWN
			telegram.setOrg(EnOceanOrigin.UNKNOWN);
			
			telegram.setDataByte((char) 0x42, 0);
			telegram.setDataByte((char) 0x13, 1);
			telegram.setDataByte((char) 0x37, 2);
			telegram.setDataByte((char) 0xBB, 3);

			telegram.setIdByte((char) 0x18, 0);
			telegram.setIdByte((char) 0xBA, 1);
			telegram.setIdByte((char) 0xF3, 2);
			telegram.setIdByte((char) 0xCC, 3);
			
			telegram.setIdHexString(communicator.getSenderdeviceId(), 0); 
			
			if (execute)
				communicator.sendTelegram(telegram);

//			DeviceId origin = impl.getDeviceSpecs().get(0).getDeviceId();
			DeviceId origin = actor.getDeviceId();
			logger.debug(actor.getDeviceId().toString()+ " In Actor SystemActor class");
			DoubleEvent ev = new DoubleEvent(valueDbl);
            impl.getMaster().sendDoubleEvent(ev, origin,impl.getClientId() );
            logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + valueDbl );
//			getEventHandler().doubleEvent(actor.getId(), valueDbl, unit, 1D);
            
		} else {
			logger.error("received invalid values");
			throw new IllegalArgumentException("SystestActorStrategy received invalid values");
		}
		} catch (TimeoutException e) {
	       	 logger.error("timeout sending to master", e);
		}
	}


//	@Override
//	public DeviceSpec getSpec() {
//		DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Fortiss.SystestActorStrategy", DeviceCategory.BLINDS);
//		builder.addDouble(SIUnitType.PERCENT).withRange(42d,42d).asCommand();
//		return builder.create();
//	}

	@Override
	public Collection<TeachInTelegramInfo> getSupportedTeachInTelegrams() {
		return null;
	}

}
