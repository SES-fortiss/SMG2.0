package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


//import org.fortiss.smartmicrogrid.shared.EventHandler;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.model.DummyEventHandler;
import org.fortiss.smg.actuatorclient.enocean.impl.model.Sensor;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;

public abstract class SensorStrategy extends AbstractImpl {
//	private EventHandler eventHandler;
	protected Sensor sensor;


//	public void setEventHandler(EventHandler eventHandler) {
//		this.eventHandler = eventHandler;
//	}
	
	
	
	/**
	 * Makes the strategy know the sensor it is used with
	 * @param sensor
	 */
	public void setSensor(Sensor sensor) {
		this.sensor=sensor;
		
	}
	
	/**
	 * This has to be implemented by each strategy
	 * A Universal Telegram is a telegram that carries all information available from the enocean-device
	 * parse it according to the specification 
	 * @param telegram
	 */
	public abstract void handleTelegram(UniversalTelegram telegram);

//	public EventHandler getEventHandler() {
//		if (eventHandler == null) {
//			return new DummyEventHandler();
//		} else {
//			return eventHandler;
//		}
//	}

	public String getDescription() {
		return "no description";
	}

	public Collection<String> getPossibleOrigins(){
		return Arrays.asList(sensor.getId()); // TODO: remove this? does it work?
	}

	protected Map<String,DeviceSpec> asHashMap(DeviceSpec spec) {
		Map<String,DeviceSpec> result = new HashMap<String,DeviceSpec>();
		if (spec!=null) {
			result.put(sensor.getId(),spec);
		}
		return result;

	}

//	public abstract Map<String,DeviceSpec> getSpecs();
}
