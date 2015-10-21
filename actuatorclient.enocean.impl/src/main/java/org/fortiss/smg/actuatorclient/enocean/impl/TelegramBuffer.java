package org.fortiss.smg.actuatorclient.enocean.impl;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.StringByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TelegramBuffer {
	/** Size of msg after both sizes are read. */ 
	private static final int AFTER_SIZES_OFFSET = 4;
	private USBTelegramHandler handler;
	private List<Character> msg = new ArrayList<Character>();
	private boolean inMsg;
	private int expectedMsgLength;
	
	private static final Logger logger = LoggerFactory
			.getLogger(TelegramBuffer.class);
	
	public TelegramBuffer(USBTelegramHandler usbTelegramHandler) {
		this.handler = usbTelegramHandler;
	}

	public void receive(char b) {
		//logger.debug("in: "+ StringByteUtils.byteCharToHexString(b) 
		//		+ " --- " + StringByteUtils.byteCharToHexString(StringByteUtils.hexStringToByteChar(StringByteUtils.byteCharToHexString(b))) + " ---- " + msg);
		
		if (!inMsg && b == 0x55) {
			msg.clear();
		}
		msg.add(b); //   (char) b);
		if (msg.size()==AFTER_SIZES_OFFSET) {
			int dataLength1 = msg.get(1);
			int dataLength2 = msg.get(2);
			
			int dataLength =(dataLength1<<1)+dataLength2;
			
			int optDataLength = msg.get(3);
			
			expectedMsgLength = dataLength+optDataLength+7;
		}
		if (msg.size()==expectedMsgLength) {
			handler.handleTelegram(convert());
			inMsg=false;
		}
	}

	private char[] convert() {
		char[] result = new char[msg.size()];
		int i =0;
		for (Character c : msg) {
			result[i++] = c;
			//System.out.println(c + " vs. " + msg.get(i-1));
		}
		return result;
	}
	
}



