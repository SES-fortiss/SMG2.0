package org.fortiss.smg.actuatorclient.enocean.impl.model.strategies.actor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Semaphore;



import java.util.concurrent.TimeoutException;



//import org.fortiss.smartmicrogrid.shared.builders.devspecbuilder.DeviceSpecBuilder;
import org.fortiss.smg.websocket.api.shared.schema.DeviceSpec;
import org.fortiss.smg.websocket.api.shared.schema.DeviceCategory;
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

public class FortissBlindActorStrategy extends ActorStrategy {

    private static final int BUTTON_DOUBLECLICK_TIMEOUT = 500;
    private Semaphore sema = new Semaphore(0);

    // value to send to move them down
    private static final int DATA_DOWN = 0x10;

    // value to send to move them up
    private static final int DATA_UP = 0x30;

    /**
     * The number of seconds to completely close the blinds from complete open
     * state
     */
    private static final int SECONDS_COMPLETE_DOWN = 50;

    private static final Logger logger = LoggerFactory.getLogger(FortissBlindActorStrategy.class);

    private int currentPosition = 0;
    private int target = 0;

    private boolean stopped = true;

    private boolean execT;

    private Boolean lastSimulateBoolean = null;
    private long lastSimulateBooleanTimeStamp = -1;

    private static double shorterTimeStrategy = 1;
    
    public FortissBlindActorStrategy(){
    	
    }
   
//    @Override
//    public DeviceSpec getSpec() {
//        DeviceSpecBuilder builder = new DeviceSpecBuilder("EnOcean.Fortiss.Blinds", DeviceCategory.BLINDS);
//        builder.addDouble(SIUnitType.PERCENT).withRange(0, 100)
//                .withDescription("Position of blinds (0% is up, 100% is down)")
//                .asCommand();
//        builder.addDouble(SIUnitType.PERCENT).withRange(0, 100)
//                .withDescription("Position of blinds (0% is up, 100% is down)")
//                .asEvent();
//
//        builder.addBoolean().withTrueSynonym("UP").withFalseSynonym("DOWN")
//                .asCommand();
//        builder.addStopLastCMD().withDescription("Stop Blinds").asCommand();
//
//        return builder.create();
//    }

    @Override
    public Collection<TeachInTelegramInfo> getSupportedTeachInTelegrams() {

        ArrayList<TeachInTelegramInfo> ts = new ArrayList<TeachInTelegramInfo>();

        UniversalTelegram telegram = new UniversalTelegram();
        telegram.setOrg(EnOceanOrigin.EEP_RPS);
        telegram.setDataByte((char) 0x10, 3);

        TeachInTelegramInfo ti = new TeachInTelegramInfo();
        ti.setTeachInTelegramString(telegram.getTelegramString());
        ti.setTeachInTelegramDescription("Schalter 10/30 Logik einlernen 0x10 (bitte ebenfalls 0x30 einlernen)");
        ts.add(ti);

        ti = new TeachInTelegramInfo();
        telegram.setDataByte((char) 0x30, 3);
        ti.setTeachInTelegramString(telegram.getTelegramString());
        ti.setTeachInTelegramDescription("Schalter 10/30 Logik einlernen 0x30 (bitte ebenfalls 0x10 einlernen)");
        ts.add(ti);

        return ts;
    }

    private void sendPush(char direction, boolean execute) {
        if (!execute) {
            return;
        }

        // Default enocean Button pressing
        // Data-Byte 3 = upper/lower button?!
        // Status = press or release?
        // see enocean-Documentation

        UniversalTelegram telegram = new UniversalTelegram();
        telegram.setOrg(EnOceanOrigin.EEP_RPS);
        telegram.setDataByte(direction, 3);
        telegram.setStatus((char) 0x30);
        telegram.setIdHexString("FF9C6E80", actor.getChannel());
        communicator.sendTelegram(telegram);
    }

    private void sendRelease(boolean execute) {

        // Default enocean Button releasing
        // Data-Byte 3 = upper/lower button?!
        // Status = press or release?
        // see enocean-Documentation

        if (!execute) {
            return;
        }
        UniversalTelegram telegram = new UniversalTelegram();
        telegram.setOrg(EnOceanOrigin.EEP_RPS);
        telegram.setDataByte((char) 0x00, 3);
        telegram.setStatus((char) 0x20);
        telegram.setIdHexString("FF9C6E80", actor.getChannel());
        communicator.sendTelegram(telegram);
    };

    @Override
    public void setBoolean(final boolean valueBool, String internalID,
            int delay, String tag, boolean execute, String valueIdentifier) {
        if (execute) {
            setBooleanInternal(valueBool, internalID, delay, tag, execute,valueIdentifier);
        } else {
            if (lastSimulateBoolean == null && !stopped) {
                stopLastCmd(internalID, delay, tag, execute);
                System.out.println("stopping");
            }
            if (lastSimulateBoolean == null) {
                lastSimulateBoolean = valueBool;
                lastSimulateBooleanTimeStamp = System.currentTimeMillis();
                System.out.println("first boolean");
            } else {
                if (System.currentTimeMillis() - lastSimulateBooleanTimeStamp < FortissBlindActorStrategy.BUTTON_DOUBLECLICK_TIMEOUT) {
                    setBooleanInternal(valueBool, internalID, delay, tag, execute, valueIdentifier);
                    System.out.println("second boolean, starting");
                    lastSimulateBoolean = null;
                    lastSimulateBooleanTimeStamp = -1;
                } else {
                    lastSimulateBoolean = valueBool;
                    lastSimulateBooleanTimeStamp = System.currentTimeMillis();
                }
            }
        }
    }

    public void setBooleanInternal(final boolean valueBool, String internalID,
            int delay, String tag, boolean execute, String valueIdentifier) {
        int toPosition = valueBool ? 0 : 100;
        setDouble(toPosition, SIUnitType.PERCENT, internalID, delay, tag, execute, valueIdentifier);
    }

    /**
     * This sets the blinds current position. USE WITH CARE, this can introduce
     * inconsistent system states.
     * 
     * @param position
     */
    public void setCurrentPosition(int position) {
        currentPosition = position;
    }

    @Override
    public void setDouble(final double valueDbl, SIUnitType unit,
            String internalID, int delay, String tag, boolean execute,
            String valueIdentifier) {
        if (!unit.equals(SIUnitType.PERCENT)) {
            throw new IllegalArgumentException("setDouble does expect unit \"%\", not \"" + unit + "\"");
        }
        target = (int) Math.round(valueDbl);
        execT = execute;
        sema.release(1);
    }

    public void shortenTime(double d) {
        FortissBlindActorStrategy.shorterTimeStrategy = d;
    }

    private void startMotor(char direction, boolean execute) {
        // System.out.println("### Starting Motor "
        // + (direction == DATA_UP ? "UP" : "DOWN"));

        // Starting the blinds to move requires to send
        // TWO simulated button push and releases within ~200-300ms
        stopped = false;
        sendPush(direction, execute);
        wrappedSleep(50);
        sendRelease(execute);

        wrappedSleep(200);

        sendPush(direction, execute);
        wrappedSleep(50);
        sendRelease(execute);
    }

    @Override
    public void startStrategy() {
    		new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        sema.acquire();
                    } catch (InterruptedException e) {
                        FortissBlindActorStrategy.logger.warn("Interrupted sema-aquire", e);
                    }

                    // calculate how far to move the blinds
                    double delta = target - currentPosition;
                    // determine the direction to move them
                    char direction = (char) (delta < 0 ? FortissBlindActorStrategy.DATA_UP : FortissBlindActorStrategy.DATA_DOWN);

                    // System.out.println("### Moving "
                    // + (direction == DATA_UP ? "up" : "down"));
                    // System.out.println("### Position " + currentPosition);
                    // System.out.println("### Target " + target);
                    // System.out.println("### Delta " + delta);
                    // System.out.println("### Stopped " + stopped);

                    if (delta != 0) {
                        startMotor(direction, execT);
                    }
                    // wait until done
                    while (delta != 0 && !stopped) {
                        System.out.println("cur pos " + currentPosition);
                        /** The distance to drive in percent [-100..100] */
                        wrappedSleep((long) (FortissBlindActorStrategy.SECONDS_COMPLETE_DOWN / 100.0 * 1000));
                        currentPosition += delta < 0 ? -1 : +1;
                        delta = target - currentPosition;
                        char newDirection = direction;
                        if (delta < 0) {
                            newDirection = FortissBlindActorStrategy.DATA_UP;
                        } else if (delta > 0) {
                            newDirection = FortissBlindActorStrategy.DATA_DOWN;
                        }
                        if (newDirection != direction) {
                            direction = newDirection;
                            startMotor(direction, execT);
                        }

                        // System.out.println("###LOOP Moving "
                        // + (direction == DATA_UP ? "up" : "down"));
                        // System.out.println("###LOOP Position "
                        // + currentPosition);
                        // System.out.println("###LOOP Target " + target);
                        // System.out.println("###LOOP Delta " + delta);
                    }

                    // System.out.println("###END Position " + currentPosition);
                    // System.out.println("###END Target " + target);
                    // System.out.println("###END Delta " + delta);
                    // System.out.println("###END Stopped " + stopped);

                    // send stop message if not at top or bottom
                    if (currentPosition != 0 && currentPosition != 100 || stopped) {
                        stopMotor(execT);
                    }
                    stopped = false;
                    
//                    DeviceId origin = impl.getDeviceSpecs().get(5).getDeviceId();
                    DeviceId origin = actor.getDeviceId();
                    logger.debug(actor.getDeviceId().toString()+"In Actor Light 5070 class");
                    
            		DoubleEvent ev = new DoubleEvent(currentPosition);
                    try {
						impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
					} catch (TimeoutException e) {
						 logger.error("timeout sending to master", e);
					}
                    logger.info("EnOceanLooper: run(): getEventHandler - new Event from " +  origin + " value " + currentPosition );
//                    getEventHandler().doubleEvent(actor.getId(), currentPosition, SIUnitType.PERCENT, 10D);
                    sema.drainPermits();
                }
            }

        }.start();

    }

    @Override
    public void stopLastCmd(String internalIDStop, int delay, String tag, boolean execute) {
        execT = execute;
        System.out.println("CLickckclcikc");
        stopped = true;
    }

    private void stopMotor(boolean execute) {
        // System.out.println("### Stopping Motor");
        sendPush((char) FortissBlindActorStrategy.DATA_DOWN, execute);
        wrappedSleep(50);
        sendRelease(execute);
    }

    private void wrappedSleep(long l) {

        if (FortissBlindActorStrategy.shorterTimeStrategy != 1) {
            l = 1;
        }

        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            FortissBlindActorStrategy.logger.warn("Sleep interrupted", e);
        }

    }

}
