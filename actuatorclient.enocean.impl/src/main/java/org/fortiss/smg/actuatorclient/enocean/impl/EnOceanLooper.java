/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.enocean.impl.model.Actor;
import org.fortiss.smg.actuatorclient.enocean.impl.model.Sensor;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.ESP2Telegram;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.IllegalTelegramException;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.ContainerManagerQueueNames;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EnOceanLooper implements EnOceanTelegramHandler , Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(EnOceanLooper.class);
	private EnOceanCommunicator communicator;
	private Map<String, Sensor> idSensorMap;
	private Map<String, Actor> internatlIdActorMap = new HashMap<String, Actor>();
	
	private ActuatorClientImpl impl;
	// Used for CONFIGURATION
	private boolean listenMode;
	private List<String> listenedTelegrams;
	private Object waitForListenTelegramMonitor = new Object();
	
	public EnOceanLooper(ActuatorClientImpl impl) {
		super();
		this.impl = impl;
		this.listenMode = false;
		this.listenedTelegrams = Collections.synchronizedList(new ArrayList<String>());
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 	Setter for the EnOceanCommunicator to talk with a Thermokon STC
	 * 
	 *  @param communicator
	 *  Object of type EnOceanCommunicator
	 */
	public void setCommunicator(EnOceanCommunicator communicator) {
		this.communicator = communicator;
	}

	/**
	 * 	Getter for the EnOceanCommuncator to talk with a Thermokon STC
	 * 
	 * @return object of type EnOceanCOmmunicator
	 */
	public EnOceanCommunicator getCommunicator() {
		return communicator;
	}

//@Override
//public String getQueueName() {
//    return MiscConstants.QUEUE_PREFIX + queueName;
//}

/**
 * EnOcean ListenMode. When active (true) all received telegrams are being
 * stored and not processed. The list of stored telegrams can be retrieved
 * using getListenedTelegrams(). This enables learn-in-procedure of sensors.
 * 
 * In a running system this has to be set to false. For Learn-In it may
 * temporarily be switched to true.
 * 
 * @param listen
 *            Boolean determining whether listen-mode is on or off (see
 *            Description)
 */
public void setListening(boolean listen) {
    this.listenMode = listen;
    if (listen) {
        this.listenedTelegrams.clear();
    }
}

/**
 * The list of collected Telegrams since listenMode was started.
 * 
 * @return A list containing the telegram Messages as String.
 */
public List<String> getListenedTelegrams() {
    return listenedTelegrams;
}

/**
 * Called to send a Learn-In-Telegram to a actor
 * 
 * @param channel
 *            The channel on a Thermokon STC used to communicate with the
 *            actor
 * @param telegramString
 *            The content of the telegram (complete Telegram-String with
 *            ThermokonID being overwritten by the first parameter of the
 *            call)
 */
public void sendLearnTelegram(int channel, String telegramString) {
    try {
        UniversalTelegram telegram;
        // TODO
        telegram = new ESP2Telegram(telegramString, false);
        telegram.setIdHexString(communicator.getSenderdeviceId(), channel);
        communicator.sendTelegram(telegram);
    } catch (IllegalTelegramException e) {
        logger.warn("Invalid teach-in-telegram");
    }
}

@Override
public void handleIncomingTelegram(UniversalTelegram telegram) {
    if (telegram == null) {
        return;
    }
    logger.debug("received telegram: " + telegram.getTelegramString() + " (" + telegram + ")");
    String id = telegram.getIdString();
    
    String idWithZeros = id;
    
    for(int i=0; i < 7; i++){
    	if(id.charAt(0) == '0'){
    		id = id.substring(1);
    	}else{
    		break;
    	}
    }
    
    
    List<DeviceContainer> devices = new ArrayList<DeviceContainer>();
    devices = impl.getDeviceSpecs();
    // Apparently we look here if the device id is already known
    for (DeviceContainer device : devices) {
    	
		if(device.getDeviceId().toString().contains(id)){
			if (getIdSensorMap().get(idWithZeros) != null) {
				getIdSensorMap().get(idWithZeros).handleIncomingTelegram(telegram);
			}
		}
		else if (listenMode) {
	        // handle unknown device
	        listenedTelegrams.add(telegram.getTelegramString());
	        synchronized (waitForListenTelegramMonitor) {
	            waitForListenTelegramMonitor.notifyAll();
	        }
		}
    }
}

public Object getWaitForListenTelegramMonitor() {
    return waitForListenTelegramMonitor;
}

//@Override
public void setBoolean(boolean valueBool, String internalIDWithPostfix, int delay, String tag, boolean execute) {

    String internalID = sensorIDFromIDWithPostfix(internalIDWithPostfix);
    String valueIdentifier = valueIdentifierFromIDWithPostfix(internalIDWithPostfix);

    if (internatlIdActorMap.containsKey(internalID)) {
        internatlIdActorMap.get(internalID).setBoolean(valueBool,
                internalID, delay, tag, execute, valueIdentifier);
    } else {
//        sendAlarm(getQueueName(), "Configuration: " + internalID + " missing", SysalSoapPortType.ALARMING_SRV_IDP);
    	logger.info("Configuration: " + internalID + " missing");
    }

}

//@Override
    public void setDouble(double valueDbl, SIUnitType unit, String internalIDWithPostfix, int delay, String tag, boolean execute) {

        String internalID = sensorIDFromIDWithPostfix(internalIDWithPostfix);
        String valueIdentifier = valueIdentifierFromIDWithPostfix(internalIDWithPostfix);

        logger.debug("enocean received doubleveent");
        if (internatlIdActorMap.containsKey(internalID)) {
            internatlIdActorMap.get(internalID).setDouble(valueDbl, unit, internalID, delay, tag, execute, valueIdentifier);
        } else {
//            sendAlarm(getQueueName(), "Configuration: " + internalID + " missing", SysalSoapPortType.ALARMING_SRV_IDP);
        	logger.info("Configuration: " + internalID + " missing");
        }
    }

//@Override
public void setString(String value, String internalIDWithPostfix, int delay, String tag, boolean execute) {

    String internalID = sensorIDFromIDWithPostfix(internalIDWithPostfix);
    String valueIdentifier = valueIdentifierFromIDWithPostfix(internalIDWithPostfix);

    if (internatlIdActorMap.containsKey((internalID))) {
        internatlIdActorMap.get((internalID)).setString(value, internalID, delay, tag, execute, valueIdentifier);
    } else {
//        sendAlarm(getQueueName(), "Configuration: " + internalID + " missing", SysalSoapPortType.ALARMING_SRV_IDP);
    	logger.info("Configuration: " + internalID + " missing");
    }
}

//@Override
public void toggle(String internalIDToggleWithPostfix, int delay, String tag, boolean execute) {

    String internalIDToggle = sensorIDFromIDWithPostfix(internalIDToggleWithPostfix);
    String valueIdentifier = valueIdentifierFromIDWithPostfix(internalIDToggleWithPostfix);

    if (internatlIdActorMap.containsKey((internalIDToggle))) {
        internatlIdActorMap.get((internalIDToggle)).toggle( internalIDToggle, delay, tag, execute, valueIdentifier);
    } else {
//        sendAlarm(getQueueName(), "Configuration: " + internalIDToggle + " missing", SysalSoapPortType.ALARMING_SRV_IDP);
    	logger.info("Configuration: " + internalIDToggle + " missing");
    }
}

//@Override
public void stopLastCmd(String internalIDStop, int delay, String tag, boolean execute) {
    if (internatlIdActorMap.containsKey((internalIDStop))) {
        internatlIdActorMap.get((internalIDStop)).stopLastCmd(internalIDStop, delay, tag, execute);
    } else {
//        sendAlarm(getQueueName(), "Configuration: " + internalIDStop + " missing", SysalSoapPortType.ALARMING_SRV_IDP);
    	logger.info("Configuration: " + internalIDStop + " missing");
    }
}

/**
 * Add a new Sensor to this component. The component will listen to this
 * Sensor and create Events.
 * 
 * @param key
 *            The uniqueID of the sensor. Key may contain ? - only the
 *            second part is stored in the Map
 * @param sensor
 *            Object of type Sensor to store along with the key.
 */

/**
 * Add a new Actor to this component.
 * 
 * @param internalId
 *            String used as Identifier for the actor. This may not contain
 *            a queueName.
 * @param actor
 *            Object of type Actor. Communicator and eventHandler are set
 *            and the ID is added automatically in the form "queueName?id".
 */
public void addActorOrSensor(String deviceId , String wrapperId , int deviceCode) {
   
	try{
		//Add by User
		ContainerManagerInterface containerManagerClient = null;
		DeviceId devId = new DeviceId(deviceId, wrapperId);
		DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
				ContainerManagerInterface.class, ContainerManagerQueueNames.getContainerManagerInterfaceQueryQueue(), 10000);
		containerManagerClient = clientInfo.init();
		DeviceContainer tempDevice = new DeviceContainer(devId, wrapperId , containerManagerClient.getDeviceSpecData(deviceCode));
    
		DeviceEvent ev = new DeviceEvent(tempDevice);
		impl.getMaster().sendDeviceEvent(ev,impl.getClientId());
	
	} catch(TimeoutException e){
		logger.error("timeout sending to master", e);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

public void addActor(String hrName, Actor actor, int deviceCode ) {
	
	internatlIdActorMap.put(hrName, actor);
    actor.getStrategy().setCommunicator(communicator);
    actor.setId(hrName);
	
	//Read from DB
	try{
		ContainerManagerInterface containerManagerClient = null;
		DeviceId devId = new DeviceId(actor.getId(), impl.getWrapperTag());
		DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
			ContainerManagerInterface.class, ContainerManagerQueueNames.getContainerManagerInterfaceQueryQueue(), 10000);
		containerManagerClient = clientInfo.init();
		DeviceContainer tempDevice = new DeviceContainer(devId, impl.getWrapperTag() , containerManagerClient.getDeviceSpecData(deviceCode));
		tempDevice.setHrName(hrName);
		DeviceEvent ev = new DeviceEvent(tempDevice);
		impl.getMaster().sendDeviceEvent(ev, impl.getClientId());
	
	} catch(TimeoutException e){
		logger.error("timeout sending to master", e);
	} catch (IOException e) {
	// 	TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

public void addSensor(String id, Sensor sensor, int deviceCode ) {
	
	getIdSensorMap().put(id, sensor);
    Collection<String> origins = sensor.getStrategy().getPossibleOrigins();
    for (String origin : origins) {
        String[] split = origin.split("\\?");
        getIdSensorMap().put(split[split.length - 1], sensor);
    }
	
	try{
		//Read form DB
		ContainerManagerInterface containerManagerClient = null;
		DeviceId devId = new DeviceId(sensor.getId(), impl.getWrapperTag());
		DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
			ContainerManagerInterface.class, ContainerManagerQueueNames.getContainerManagerInterfaceQueryQueue(), 10000);
		containerManagerClient = clientInfo.init();
		DeviceContainer tempDevice = new DeviceContainer(devId, impl.getWrapperTag() , containerManagerClient.getDeviceSpecData(deviceCode));
		DeviceEvent ev = new DeviceEvent(tempDevice);
		impl.getMaster().sendDeviceEvent(ev, impl.getClientId());
	}catch(TimeoutException e){
		logger.error("timeout sending to master", e);
	} catch (IOException e) {
	// 	TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}


/**
 * Getter for Sensor
 * 
 * @param id
 *            String ID without queueName
 * @return Object of type Sensor for the ID
 */
public Sensor getSensor(String id) {
    return getIdSensorMap().get(id);
}

/**
 * Getter for Actor
 * 
 * @param internalId
 *            String ID without queueName
 * @return Object of type Actor
 */
public Actor getActor(String internalId) {
    return internatlIdActorMap.get(internalId);
}

/**
 * @return a string for each channel
 */
public List<String> getAvailableChannels() {
    List<String> result = new ArrayList<String>();
    for (int i = 0; i < 128; i++) {
        Collection<Actor> actors = getActorForChannel(i);
        String s = "free";
        if (actors.size() > 0) {
            s = "";
            int j = 0;
            for (Actor actor : actors) {
                if (j++ > 0) {
                    s += ", ";
                }
                s += actor.getId();
            }
        }
        result.add(s);
    }
    return result;
}

public Collection<Actor> getActorForChannel(int channel) {
    Collection<Actor> result = new ArrayList<Actor>();

    for (Actor actor : internatlIdActorMap.values()) {
        if (actor.getChannel() == channel) {
            result.add(actor);
        }
    }
    return result;
}

/**
 * Removes an actor from the internal actor map if present
 * 
 * @param internalId
 *            The id of the actor to be deleted
 * @return true if the internal map contained the actor, false otherwise
 */
public boolean removeActor(String internalId) {
    return (internatlIdActorMap.remove(internalId) != null);
}

/**
 * Removes a sensor from the internal actor map if present
 * 
 * @param id
 *            The id of the sensor to be deleted
 * @return true if the internal map contained the sensor, false otherwise
 */
public boolean removeSensor(String id) {
    return (idSensorMap.remove(id) != null);
}

/**
 * Counts the number of sensors controlled by this component
 * 
 * @return Integer of the counted value
 */
public int getSensorCount() {
    return getIdSensorMap().size();
}

/**
 * Counts the number of actors controlled by this component
 * 
 * @return Integer of the counted value
 */
public int getActorCount() {
    return internatlIdActorMap.size();
}

/**
 * @return The Map containing the Sensors controlled by this component
 */
private synchronized Map<String, Sensor> getIdSensorMap() {
    if (idSensorMap == null) {
        idSensorMap = new HashMap<String, Sensor>();
    }
    // this is needed because sometimes super-constructor methods need the
    // map before it can be initialized.
    // example scenario: eventhandler service is already present ->
    // osgi-binder will call setEventHandler() directly from the super
    // constructor. in this situation the map cannot be initialized
    // beforehand.
    return idSensorMap;
}

//@Override
public synchronized void deactivate() {
   impl.deactivate();
   communicator.disconnect();
}

//@Override
//public String getSpecID(String specIdInternalId) {
//    if (idSensorMap.containsKey(specIdInternalId)) {
//    	return idSensorMap.get(specIdInternalId).getStrategy().getSpecs().get(this.queueName + "?" + specIdInternalId).getSpecId();
//    } else if (internatlIdActorMap.containsKey(specIdInternalId)) {
//        return internatlIdActorMap.get(specIdInternalId).getStrategy().getSpec().getSpecId();
//    } else {
//        return null;
//    }
//  impl.getMaster().send
//}


private String sensorIDFromIDWithPostfix(String internalIDWithPostfix) {
    if (internalIDWithPostfix.contains("_")) {
        return internalIDWithPostfix.split("_")[0];
    } else {
        return internalIDWithPostfix;
    }
}

private String valueIdentifierFromIDWithPostfix(String internalIDWithPostfix) {
    if (internalIDWithPostfix.contains("_")) {
        return internalIDWithPostfix.split("_")[1];
    } else {
        return "";
    }
}


}
