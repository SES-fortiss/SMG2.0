/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.config;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
////import org.fortiss.smartmicrogrid.shared.EnOceanConfigInterface;
////import org.fortiss.smartmicrogrid.shared.JMSHelpers;
////import org.fortiss.smartmicrogrid.shared.LocationMgr;
////import org.fortiss.smartmicrogrid.shared.schema.DeviceSpec;
//import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
//import  org.fortiss.smg.containermanager.api.devices.DeviceId;
////import org.fortiss.smartmicrogrid.shared.schema.TeachInTelegramInfo;
////import org.fortiss.smartmicrogrid.shared.wssecurity.ComponentAuthentication;
//import org.fortiss.smg.websocket.api.shared.wssecurity.CheckPermission;
////import org.fortiss.smartmicrogrid.shared.wssecurity.ComponentLogin;
//import org.fortiss.smg.actuatorclient.enocean.impl.EnOceanLooper;
//import org.fortiss.smg.actuatorclient.enocean.impl.model.Actor;
//import org.fortiss.smg.actuatorclient.enocean.impl.model.Sensor;
//import org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor.ActorStrategy;
//import org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.sensor.SensorStrategy;
//import org.fortiss.smg.actuatorclient.enocean.impl.persistence.WrapperPersistor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class EnOceanConfigurator { //implements EnOceanConfigInterface, ComponentLogin {
//
//    /**
//     * A list of all enOcean wrappers running - this bundle can control multiple
//     * instances of enOcean-Wrappers talking to different Gateways
//     */
//    private Map<String, EnOceanLooper> wrappers;
//
//    /**
//     * DatabaseUtil to store new Device/Wrapper-Information
//     */
//    private WrapperPersistor persistor;
//
//    private static final Logger logger = LoggerFactory
//            .getLogger(EnOceanConfigurator.class);
//
//    /**
//     * Constructor
//     * 
//     * @param persistor
//     *            a Database-Util to work with
//     * @param wrappers
//     *            all Wrappers available
//     */
//    public EnOceanConfigurator(WrapperPersistor persistor,
//            Map<String, EnOceanLooper> wrappers, ComponentAuthentication auth) {
//        this.persistor = persistor;
//        this.wrappers = wrappers;
//        auth.loginComponent(this);
//    }
//
//    /**
//     * This returns the available teach-in-telegrams in order to provide the
//     * user with the different telegrams and let him issue them to learn in new
//     * Actuators (if those telegrams are necessary)
//     */
////    @Override
//    public List<TeachInTelegramInfo> getActorStrategyTeachInTelegrams(String actorStrategyName) {
//        ArrayList<TeachInTelegramInfo> availableTITs = new ArrayList<TeachInTelegramInfo>();
//        try {
//            ActorStrategy as = (ActorStrategy) Class.forName(actorStrategyName).newInstance();
//            availableTITs.addAll(as.getSupportedTeachInTelegrams());
//        } catch (InstantiationException e) {
//            EnOceanConfigurator.logger.warn("Instantiation in getActorStrategyTeachInTelegrams", e);
//        } catch (IllegalAccessException e) {
//            EnOceanConfigurator.logger.warn("Illegal Access", e);
//        } catch (ClassNotFoundException e) {
//            EnOceanConfigurator.logger.warn("Class missing", e);
//        }
//        return availableTITs;
//    }
//
//    /**
//     * All available-Actuator-Strategies are available in every Wrapper - pick
//     * any!
//     */
////    @Override
//    public List<String> getAvailableActorStrategies() {
//        return wrappers.values().iterator().next().getAvailableActorStrategies();
//    }
//
//    /**
//     * Returns a list of channels that are available for usage enocean-actuators
//     * are learned into the gateway each on it's own channel
//     */
////    @Override
//    public List<String> getAvailableChannels(String getAvailChannelsForQueue) {
//        return wrappers.get(getAvailChannelsForQueue).getAvailableChannels();
//    }
//
//    /**
//     * All available-Sensor-Strategies are available in every Wrapper - pick
//     * any!
//     */
////    @Override
//    public List<String> getAvailableSensorStrategies() {
//        return wrappers.values().iterator().next().getAvailableSensorStrategies();
//    }
//
//    /**
//     * Return the Names of all qNames like "enoceanQ", maybe others like
//     * "enoceanUSBQ"
//     */
////    @Override
//    public List<String> getAvailableWrapperQueueNames() {
//        List<String> queueNames = new ArrayList<String>();
//        queueNames.addAll(wrappers.keySet());
//        return queueNames;
//    }
//
//    /**
//     * security
//     */
////    @Override
//    public String getLogin() {
//        return "EOC";
//    }
//
//    /**
//     * when in listen-mode wrappers store telegrams in a list used to query this
//     * list in order to let the user choose a telegram and use it for learning
//     * in a new sensor
//     */
////    @Override
//    public List<String> getReceivedTelegrams(String getReceivedTelegramsQueue,int timeout) {
//        EnOceanLooper wrapper = wrappers.get(getReceivedTelegramsQueue);
//        if (timeout > 0) {
//            try {
//            	System.out.println("wrapper: " + wrapper.getQueueName());
//                Object monitor = wrapper.getWaitForListenTelegramMonitor();
//                synchronized (monitor) {
//                    monitor.wait(timeout);
//                }
//            } catch (InterruptedException e) {
//                //
//            }
//        }
//        return wrapper.getListenedTelegrams();
//    }
//
//    /**
//     * redirects to the wrapper in order to send a teach-in-telegram
//     */
////    @Override
//    public void learn(String learnRequestQueue, int channel,String telegramString) {
//        wrappers.get(learnRequestQueue).sendLearnTelegram(channel,telegramString);
//
//    }
//
//    /**
//     * Store a newly created Actuator in db and add it to wrapper
//     */
////    @Override
//    public void putActor(String queue, int channel, String id, String strategy) {
//        try {
//            ActorStrategy strategyObj = (ActorStrategy) Class.forName(strategy).newInstance();
//            strategyObj.startStrategy();
//            wrappers.get(queue).addActor(id, new Actor(channel, strategyObj));
//            persistor.insertActor(queue, channel, id, strategy);
//
//            LocationMgr lm = JMSHelpers.getProxy(LocationMgr.QUEUE_ADDRESS,
//                    LocationMgr.class, ComponentAuthentication.get().getSession(this));
//            lm.createNode(queue + "?" + id);
//
//        } catch (InstantiationException e) {
//            EnOceanConfigurator.logger.warn("Instantiation in putSensor", e);
//        } catch (IllegalAccessException e) {
//            EnOceanConfigurator.logger.warn("Illegal Access", e);
//        } catch (ClassNotFoundException e) {
//            EnOceanConfigurator.logger.warn("Class missing", e);
//        }
//    }
//
//    /**
//     * Add a new sensor to the the wrapper and database
//     */
////    @Override
//    public void putSensor(String queue, String id, String strategy) {
//        try {
//            SensorStrategy strat = (SensorStrategy) Class.forName(strategy).newInstance();
//            wrappers.get(queue).addSensor(id,new Sensor(queue + "?" + id, strat));
//
//            persistor.insertSensor(queue, id, strategy);
//            for (Entry<String, DeviceSpec> entry : strat.getSpecs().entrySet()) {
//                wrappers.get(queue).getEventHandler().newDeviceEvent(entry.getKey(), entry.getValue());
//            }
//
//        } catch (InstantiationException e) {
//            EnOceanConfigurator.logger.warn("Instantiation in putSensor", e);
//        } catch (IllegalAccessException e) {
//            EnOceanConfigurator.logger.warn("Illegal Access", e);
//        } catch (ClassNotFoundException e) {
//            EnOceanConfigurator.logger.warn("Class missing", e);
//        }
//    }
//
//    /**
//     * remove a device from the wrapper and db
//     */
////    @Override
//    public void removeDevice(String removeDeviceName) {
//        String[] split = removeDeviceName.split("\\?");
//        if (split.length == 2
//                && getAvailableWrapperQueueNames().contains(split[0])) {
//            if (wrappers.get(split[0]).removeActor(split[1])) {
//                persistor.removeActor(split[0], split[1]);
//                EnOceanConfigurator.logger.info("Enocean config-interface removed actor: {}",removeDeviceName);
//            } else if (wrappers.get(split[0]).removeSensor(split[1])) {
//                persistor.removeSensor(split[0], split[1]);
//                EnOceanConfigurator.logger.info("Enocean config-interface removed sensor: {}",removeDeviceName);
//            } else {
//                EnOceanConfigurator.logger.warn("Enocean config-interface could not remove unknown device: {}",removeDeviceName);
//            }
//        } else {
//            EnOceanConfigurator.logger.warn("Enocean config-interface could not remove device {} (not satisyfying naming convention)",
//                            removeDeviceName);
//        }
//    }
//
//    /**
//     * Set the Wrapper to LISTEN-Mode incoming telegrams will not be processed
//     * but displayed only (in order to be able to learn in new sensors)
//     */
////    @Override
//    public void wrapperSetListenMode(String queue, boolean listening) {
//        wrappers.get(queue).setListening(listening);
//    }
//
//}
