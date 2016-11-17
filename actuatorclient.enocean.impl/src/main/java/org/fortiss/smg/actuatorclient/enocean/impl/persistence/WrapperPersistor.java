package org.fortiss.smg.actuatorclient.enocean.impl.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.enocean.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.EnOceanCommunicator;
import org.fortiss.smg.actuatorclient.enocean.impl.EnOceanLooper;
import org.fortiss.smg.actuatorclient.enocean.impl.ThermokonCommunicator;
import org.fortiss.smg.actuatorclient.enocean.impl.USBStickCommunicator;
import org.fortiss.smg.actuatorclient.enocean.impl.model.Actor;
import org.fortiss.smg.actuatorclient.enocean.impl.model.Sensor;
import org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.ActorStrategy;
import org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.AbstractImpl;
import org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.SensorStrategy;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Right now those MySQL Tables are in use:
 * 
 * TABLE Actors queue varchar(50) COMINDED KEY channel int(11) COMIBNED KEY
 * strategy varchar(256)
 * 
 * Table Sensors queue varchar(50) COMBINED KEY id varchar(100) COMBINED KEY
 * strategy varchar(256)
 * 
 **/

public class WrapperPersistor {
    private static final Logger logger = LoggerFactory.getLogger(WrapperPersistor.class);
    private static final Marker teachinMarker = MarkerFactory.getMarker("teachIn");
    private ActuatorClientImpl impl;
    private InformationBrokerInterface broker;

    public WrapperPersistor(ActuatorClientImpl impl) {
        super();
        this.impl = impl;
        
    	// say hello to ambulance
		DefaultProxy<InformationBrokerInterface> brokerClient = new DefaultProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(), 5000);
		 try {
			broker = brokerClient.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ambulanceClient.destroy(); 
    }

    private ActorStrategy createActorStrategy(String className, ActuatorClientImpl impl) {
        ActorStrategy strategy = (ActorStrategy) createStrategyObject(className, impl);
        strategy.startStrategy();
        return strategy;
    }

    private SensorStrategy createSensorStrategy(String className, ActuatorClientImpl impl) {
        return (SensorStrategy) createStrategyObject(className, impl);
    }

    private Object createStrategyObject(String className, ActuatorClientImpl impl) {
        Object object = null;
        try {
            object = Class.forName(className).newInstance();
            (( AbstractImpl ) (object)).setImpl(impl);
        
        } catch (InstantiationException e) {
            WrapperPersistor.logger.error("Invalid strategy: " + className, e);
            
        } catch (IllegalAccessException e) {
            WrapperPersistor.logger.error("Invalid strategy: " + className, e);
        
        } catch (ClassNotFoundException e) {
            WrapperPersistor.logger.error("Invalid strategy: " + className, e);
        
        } catch (IllegalArgumentException e) {
            WrapperPersistor.logger.error("Invalid strategy: " + className, e);
        
        } catch (SecurityException e) {
            WrapperPersistor.logger.error("Invalid strategy: " + className, e);
        
        }
        return object;
    }

    
    
    public Map<String, EnOceanLooper> createWrappersFromPersistency() {

        HashMap<String, EnOceanLooper> wrappers = new HashMap<String, EnOceanLooper>();
        try {
            // Code for creating EnOcean Wrapper
                 
            	 String wrapperHost = impl.getHost();
                 int wrapperPort = impl.getPort();
                 WrapperPersistor.logger.debug("Create EnOceanLooper '" +"enoceanQ"+ "'");
                 EnOceanLooper wrapper = impl.getLooperForTest(); 
                 EnOceanCommunicator communicator;
                 if (wrapperHost.equals("USB")) {
//                     String portIdentifier = "/dev/ttyACM0"; //wrapperHost.substring("usb:".length());
                	 //String portIdentifier = "/dev/tty.usbserial-FTXMILC1";
                	 String portIdentifier = "/dev/ttyUSB0";
                	 //String portIdentifier = "COM1";
                	 
                     communicator = new USBStickCommunicator(wrapper, portIdentifier);
                 } else {
                     communicator = new ThermokonCommunicator(wrapperHost,wrapperPort, wrapper);
                 }
                 
                 ((Thread) communicator).start();
                 wrapper.setCommunicator(communicator);
                            
                 // CODE FOR ACTORS AND SENSORS
                 List<Map<String, Object>> actorsSensorsFromDB = new ArrayList<Map<String, Object>>();
                 actorsSensorsFromDB = broker.getSQLResults("SELECT * FROM EnOcean_Devices ");
                 for(Map<String,Object> entity : actorsSensorsFromDB){        
//                	 needed to be read from DataBase
                	 int deviceCode = (Integer)entity.get("devicecode");
                	 String hrName = (String)entity.get("hrname");
                     int channel = (Integer)entity.get("channel");
                     String id = (String)entity.get("enoceanid");
                     String strategyClassName = (String)entity.get("strategy");
                     WrapperPersistor.logger.debug(" + [Actor/Sensor]\t " + hrName + "\t " + strategyClassName);
                     DeviceId deviceId = new DeviceId(id+"-"+deviceCode, impl.getWrapperTag());
//                     impl.setEnoceanDeviceIds(deviceId); //I add it
                     if (channel == -1)
                    	 wrapper.addSensor(id+"-"+deviceCode, new Sensor(id+"-"+deviceCode, createSensorStrategy(strategyClassName, impl),deviceId, deviceCode), deviceCode);
                     else{
                    	deviceId = new DeviceId(hrName, impl.getWrapperTag());
                    	Actor actor = new Actor(channel,createActorStrategy(strategyClassName, impl),deviceId);
//                    	wrapper.addActor(id+"-"+deviceCode, actor, deviceCode);
                    	wrapper.addActor(hrName, actor, deviceCode);
                    	 }
                 }
                               
                // wrapper.activate();
                
                 wrappers.put("enoceanQ",wrapper);
                        
                 
        }catch (TimeoutException e) {
			WrapperPersistor.logger.error("Could not read EnOcean WrapperConfig from db: ", e);
            
        } return wrappers;

    }

    public void insertActor( int channel, String hrName, String strategy , int deviceCode) {
       try {
    	     broker.getSQLResults("INSERT INTO enocean_devices VALUES ('" + "',"+ channel + ",'" + strategy + "','" + hrName + "'',"+ deviceCode +"')");
    	    
    	     WrapperPersistor.logger.info("Teach-In for Actor " + "?"+ hrName + " on channel " + channel + ", strategy " 
            + strategy + "complete");
        
       } catch (TimeoutException e) {
            WrapperPersistor.logger.error("Could not save EnOcean WrapperConfig to db: ", e);
       }
    }

    public void insertSensor(String hrName, String id, String strategy, int deviceCode) {
     
    	try {
        	
    	   broker.getSQLResults("INSERT INTO enocean_devices VALUES ('-1 ',"+ strategy + "','" + hrName +"','"+ id + "','"+ deviceCode +"')");
        	 
        } catch (TimeoutException e) {
           
        	WrapperPersistor.logger.error("Could not save EnOcean WrapperConfig to db: ", e);
        }
    }

    public void removeActor( String actorid) {
       
    	try {
        	
        	broker.getSQLResults("DELETE FROM enocean_devices WHERE HRName = '" + actorid + "'");
                 
            WrapperPersistor.logger.info("Removed actor " + "?" + actorid);
       
        } catch (TimeoutException e) {
           
        	WrapperPersistor.logger.error("Could not delete EnOcean actor from db: ", e);
   
        }
    }

    public void removeSensor(String sensorid) {
       
        try {
        	
        	broker.getSQLResults("DELETE FROM enocean_devices WHERE ID = '" + sensorid + "'");

        } catch (TimeoutException e) {
            
        	WrapperPersistor.logger.error("Could not delete EnOcean sensor from db: ", e);
        }

    }

}

