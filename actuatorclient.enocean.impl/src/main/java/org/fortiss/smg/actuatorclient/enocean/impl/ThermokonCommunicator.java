/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl;

	import java.io.BufferedInputStream;
	import java.io.IOException;
	import java.io.PrintStream;
	import java.net.ConnectException;
	import java.net.Socket;
	import java.net.UnknownHostException;
	import java.util.concurrent.Semaphore;

	import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.ESP2DevIDTelegram;
	import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.ESP2Telegram;
	import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.IllegalTelegramException;
	import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;

	public class ThermokonCommunicator extends Thread implements
	        EnOceanCommunicator {
	    // TODO refactor this inner class
	    /**
	     * The Watchdog tries to reach the Thermokon STC periodically.
	     */
	    private class Watchdog extends Thread {
	        @Override
	        public void run() {
	            ThermokonCommunicator.logger.debug("Watchdog startetd");
	            while (isActive()) {
	                try {
	                    ThermokonCommunicator.logger.debug("Checking Thermokon availabiltity");
	                    sendTelegram(new ESP2DevIDTelegram());
	                    Thread.sleep(12000);
	                } catch (InterruptedException e) {
	                    ThermokonCommunicator.logger.warn("Interrupted availability-check", e);
	                }
	            }
	        }
	    }

	    private static final Logger logger = LoggerFactory.getLogger(ThermokonCommunicator.class);
	    private EnOceanTelegramHandler telegramHandler;
	    private int port;

	    private String host;
	    private Socket sock;
	    private PrintStream printStream;

	    private BufferedInputStream binput;

	    private boolean active = false;

	    private String thermokonId;

	    //private SysalSoapPortType alarm;

	    private Semaphore connectionSemaphore = new Semaphore(0);

	    /**
	     * Constructor of ThermokonCommunicator establishing a connection to an
	     * Thermokon STC
	     * 
	     * @param host
	     *            The host Address of the device
	     * @param port
	     *            The port of the device
	     * @param telegramHandler
	     *            Object of type EnOceanTelegramHandler to redirect incoming
	     *            telegrams to.
	     */
//	    public ThermokonCommunicator(String host, int port, EnOceanTelegramHandler telegramHandler, SysalSoapPortType alarm) {
	    public ThermokonCommunicator(String host, int port, EnOceanTelegramHandler telegramHandler) {
	        this.host = host;
	        this.port = port;
	        this.telegramHandler = telegramHandler;
//	        this.alarm = alarm;
	        thermokonId = "";
	    }

	    private String charArrayToString(char[] msg) {
	        StringBuffer buf = new StringBuffer();
	        for (Character ch : msg) {
	            buf.append(ch);
	        }
	        return buf.toString();
	    }

	    /**
	     * Disconnects from the Thermokon STC
	     */
	    @Override
	    public void disconnect() {
	        active = false;
	        try {
	            if (sock != null) {
	                sock.close();
	                binput.close();
	            }
	        } catch (IOException e) {
	            ThermokonCommunicator.logger.warn("could not close gateway-socket",e);
	        }
	    }

	    /**
	     * Establishes the connection with the Thermokon STC Retries until
	     * connection is established.
	     */
	    private void establishConnection() {
	        boolean connected = false;
	        connectionSemaphore.drainPermits();
	        do {
	            try {
	                ThermokonCommunicator.logger.info("Thermokon communicator trying to establish connection to {}:{}",host, port);
	                if (sock != null && !sock.isClosed()) {
	                    sock.close();
	                    sock = null;
	                    printStream = null;
	                    binput = null;
	                }
	                sock = new Socket(host, port);
	                printStream = new PrintStream(sock.getOutputStream());
	                binput = new BufferedInputStream(sock.getInputStream());
	                ThermokonCommunicator.logger.info("Thermokon communicator established connection to {}:{}",host, port);

	                // Now Ask Thermokon for ID:
	                ESP2DevIDTelegram tel = new ESP2DevIDTelegram();
	                sendTelegram(tel);
	                String msg = readNextMessage();
	                if (isGatewayIDTelegram(msg)) {
	                    thermokonId = msg.substring(8, 16);
	                    ThermokonCommunicator.logger.debug("Received Thermokon ID {}", thermokonId);
	                    connected = true;
	                    connectionSemaphore.release();
//	                    alarm.processAlarm(telegramHandler.getQueueName(),"Thermokon Connection failed",SysalSoapPortType.ALARMING_SRV_REST);
	                }
	            } catch (UnknownHostException e) {
	                ThermokonCommunicator.logger.warn("UnknownHost: Socket could not be created!", e);
//	                alarm.processAlarm(telegramHandler.getQueueName(),"Thermokon Connection failed",SysalSoapPortType.ALARMING_SRV_AFF);
	            } catch (ConnectException e) {
	            	ThermokonCommunicator.logger.warn("ConnectException: Already established ? - only one is allowed", e);
//	            	alarm.processAlarm(telegramHandler.getQueueName(),"Thermokon Connection failed",SysalSoapPortType.ALARMING_SRV_AFF);
	            } catch (IOException e) {
	                ThermokonCommunicator.logger.warn("Socket/getStream() could not establish connection", e);
//	                alarm.processAlarm(telegramHandler.getQueueName(),"Thermokon Connection failed", SysalSoapPortType.ALARMING_SRV_AFF);
	            }
	            if (!connected) {
	                try {
	                    Thread.sleep(2000);
	                } catch (InterruptedException e) {
	                    ThermokonCommunicator.logger.warn("you interrupted my sleep!!!", e);
	                }
	            }
	        } while (!connected && isActive());
	    }

	    public String getGatewayHost() {
	        return host;
	    }

	    public int getGatewayPort() {
	        return port;
	    }

	    /**
	     * @return String value containing the 8 HEXdigits of the ThermokonID
	     */
	    @Override
	    public String getSenderdeviceId() {
	        return thermokonId;
	    }

	    /**
	     * Determines whether the component is active
	     */
	    @Override
	    public boolean isActive() {
	        return active;
	    }

	    /**
	     * Checks if a messages string
	     * 
	     * @param msg
	     * @return
	     */
	    private boolean isGatewayIDTelegram(String msg) {
	        return msg.startsWith("A55A8B98");
	    }

	    /**
	     * Reads one telegram from the input-Stream
	     * 
	     * @return
	     * @throws IOException
	     */
	    private String readNextMessage() throws IOException {
	        int i = 0;
	        char[] msg = new char[28];
	        while (true) {
	            int tmp = binput.read();
	            if (tmp == -1) {
	                throw new IOException("readNextMessage - EOF");
	            }
	            msg[i] = (char) tmp;
	            if (i < 27) {
	                i++;
	            } else {
	                i = 0;
	                return charArrayToString(msg);
	            }
	        }
	    }

	    @Override
	    public void run() {
	        // alarm = this
	        active = true;
	        super.run();
	        establishConnection();
	        new Watchdog().start();
	        ThermokonCommunicator.logger.debug("EnOceanLooper receiver-loop active");

	        while (isActive()) {
	            String msg = null;
	            try {
	                msg = readNextMessage();
	                ESP2Telegram telegram = new ESP2Telegram(msg, true);
	                ThermokonCommunicator.logger.debug("received telegram {}", msg);
	                if (!isGatewayIDTelegram(msg)) {
	                    telegramHandler.handleIncomingTelegram(telegram);
	                }
	            } catch (IllegalTelegramException e) {
	                ThermokonCommunicator.logger.error("Received invalid telegram: " + msg+ " (reestablishing connection)", e);
	                establishConnection();
	            } catch (IOException e) {
	                ThermokonCommunicator.logger.error("Error while reading next message (reestablishing connection)",e);
	                establishConnection();
	            }
	        }
	        ThermokonCommunicator.logger.debug("EnOceanLooper receiver-loop ended");
	    }

	    @Override
	    public void sendTelegram(UniversalTelegram telegram) {

	        ESP2Telegram esptelegram;
	        // recreate only if it is not already an Telegram of Type ESP2
	        // cast otherwise in order to keep ESP2-custom settings (e.g. ASK
	        // Thermokon for ID!)
	        if (!(telegram instanceof ESP2DevIDTelegram) && !(telegram instanceof ESP2Telegram)) {
	            esptelegram = new ESP2Telegram(telegram, false);
	        } else {
	            esptelegram = (ESP2Telegram) telegram;
	        }

	        if (printStream != null) {
	            ThermokonCommunicator.logger.debug("Sending telegram {}",esptelegram.getTelegramString());
	            printStream.print(esptelegram.getTelegramString());
	            printStream.flush();
	        } else {
	            ThermokonCommunicator.logger.warn("Aborted sending telegram {}. PrintStream not available!",esptelegram.getTelegramString());
	        }
	    }

	    /**
	     * This method blocks until the Communicator is connected to a Gateway
	     * 
	     * @throws InterruptedException
	     */
	    public void waitForConnection() throws InterruptedException {
	        connectionSemaphore.acquire();
	    }

	}


