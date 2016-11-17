package org.fortiss.smg.actuatorclient.enocean.impl;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.ESP2DevIDTelegram;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.ESP3DevIDTelegram;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.ESP3Telegram;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.EnOceanOrigin;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.StringByteUtils;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class USBStickCommunicator  extends Thread implements EnOceanCommunicator {
	private static final int BAUD_RATE = 57600;

	private static final Logger logger = LoggerFactory
			.getLogger(USBStickCommunicator.class);

	private EnOceanTelegramHandler telegramHandler;
	private String senderdeviceId;
	private boolean active;
	private String portIdentifier;
	private InputStream in;

	private OutputStream out;

	public USBStickCommunicator(EnOceanTelegramHandler telegramHandler,
			String portIdentifier) {
		this.telegramHandler = telegramHandler;
		this.senderdeviceId = "";
		this.portIdentifier = portIdentifier;
		logger.debug("Creating new USB-EnOcean communicator at "
				+ portIdentifier);
	}

	@Override
	public void run() {

		active = true;
		while (!connect()) {
			try {
				logger.debug("not connected");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		TelegramBuffer buffer = new TelegramBuffer(new USBTelegramHandler() {
	
	@Override
	public void handleTelegram(char[] telegramBytes) {
					
				if (StringByteUtils.byteCharToHexString(telegramBytes[4]).equals("01")) {
					logger.debug("found telegram" + Arrays.toString(telegramBytes));
					ESP3Telegram telegram = new ESP3Telegram(telegramBytes, true);
					if (telegram.getOrg().equals(EnOceanOrigin.ID_RESP)) {
						senderdeviceId = telegram.getIdString();
						logger.debug("received device-id: "+senderdeviceId+" telegram: "+telegram.getTelegramString());
					}else {
						telegramHandler.handleIncomingTelegram(telegram);
					}
				}else {
					logger.debug("discarding unknown telegram "+Arrays.toString(telegramBytes));
				}
			}
			

		});
		int i;
		
			while (active) {
				
				try {
					i = in.read();
					
					if ( i != -1) {
						buffer.receive((char) i);
					}
				}
				catch (IOException e) {
					logger.error("IO-Exception while reading from EnOcean-USB", e);
				}
		} 
	}

	private boolean connect() {

		CommPortIdentifier id;
		try {
			
			HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
	        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
	        while (thePorts.hasMoreElements()) {
	            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
	            switch (com.getPortType()) {
	            case CommPortIdentifier.PORT_SERIAL:
	                try {
	                    CommPort thePort = com.open("CommUtil", 50);
	                    thePort.close();
	                    h.add(com);
	                    
	                } catch (PortInUseException e) {
	                    logger.debug("Port, "  + com.getName() + ", is in use.");
	                } catch (Exception e) {
	                	logger.debug("Failed to open port " +  com.getName());
	                    e.printStackTrace();
	                }
	            }
	        }
			
			
			logger.info("portIdentifier: " + portIdentifier);
			id = CommPortIdentifier.getPortIdentifier(portIdentifier);
			CommPort commPort = id.open(this.getClass().getName(), 5000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(BAUD_RATE,
						SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				logger.debug("fetching enocean-id");
				sendTelegram(new ESP3DevIDTelegram());
				return true;
			} else {
				logger.error("Could not initialize Port (not instanceof SerialPort): " + portIdentifier);				
			}
			
		} catch (NoSuchPortException e) {
			logger.error("No such port: " + portIdentifier, e);
		} catch (PortInUseException e) {
			logger.error("Port in use: " + portIdentifier, e);
		} catch (UnsupportedCommOperationException e) {
			logger.error("Error while connecting to EnOcean USB", e);
		} catch (IOException e) {
			logger.error("Error while connecting to EnOcean USB", e);
		}

		return false;
	}

	@Override
	public void sendTelegram(UniversalTelegram telegram) {
		ESP3Telegram telegramToSend;
		
		
		if (telegram instanceof ESP3Telegram) {
			telegramToSend = (ESP3Telegram) telegram;
		} else {
			telegramToSend = new ESP3Telegram(telegram);
		}
			logger.debug(telegramToSend.toString());
		logger.info("Sending {}",StringByteUtils.byteCharsToHexString(telegramToSend.getTelegramBytes()));

		try 
		{
			for (char b : telegramToSend.getTelegramBytes()) {
				logger.info("out: "+StringByteUtils.byteCharToHexString(b));
				out.write(b);
			}
			out.flush();
			/*
			for (char b : StringByteUtils.readBytes("55000707017AF630001B10963003FFFFFFFFFF00F1")) {
				logger.info("out: "+StringByteUtils.byteCharToHexString(b));
				out.write(b);
			}*/

		} catch (IOException e) {
			logger.error("Could not end Telegram " + telegram, e);
		}
	}

	@Override
	public String getSenderdeviceId() {
		return senderdeviceId;
	}

	@Override
	public void disconnect() {
		this.active = false;
	}

	
	@Override
	public boolean isActive() {
		return active;
	}

}
