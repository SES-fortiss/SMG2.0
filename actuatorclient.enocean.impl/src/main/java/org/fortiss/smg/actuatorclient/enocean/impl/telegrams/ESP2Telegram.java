/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.telegrams;

/**
 * VERSION 2 of the enOcean-Protocol
 */
public class ESP2Telegram extends UniversalTelegram {

	//Offsets for origin, data id and status in the bytestream
	private static final int ORG_OFFSET = 3;
	private static final int DATA_OFFSET = 4;
	private static final int ID_OFFSET = 8;
//	private static final int CHANNEL_OFFSET = 13;
	private static final int STATUS_OFFSET = 12;

	char[] prefix;

	public ESP2Telegram(String byteString, boolean incoming) throws IllegalTelegramException {
		setPrefix(incoming);

		char[] telegrambytes = StringByteUtils.readBytes(byteString);

		for (int i = 0;  i < 4; i++) {
			setDataByte(telegrambytes[DATA_OFFSET + i], 3-i);//DB 3.2.1.0
			setIdByte(telegrambytes[ID_OFFSET + i], 3-i);//ID 3.2.1.0
		}

		setStatus(telegrambytes[STATUS_OFFSET]);

		//Map the origin bytes to origin-enums
		switch (telegrambytes[ORG_OFFSET]) {
		case 0x58:
			setOrg(EnOceanOrigin.ID);
			break;
		case 0x05:
			setOrg(EnOceanOrigin.EEP_RPS);
			break;
		case 0x06:
			setOrg(EnOceanOrigin.EEP_1BS);
			break;
		case 0x07:
			setOrg(EnOceanOrigin.EEP_4BS);
			break;
		default:
			setOrg(EnOceanOrigin.UNKNOWN);
			break;
		}
	}

	public ESP2Telegram(boolean incoming) {
		setPrefix(incoming);
	}

	public ESP2Telegram(UniversalTelegram telegram, boolean incoming) {
		setPrefix(incoming);
		this.databytes = telegram.databytes;
		this.id = telegram.id;
		this.org = telegram.org;
		this.status = telegram.status;
	}

	/**
	 * Every Version-2-Telegram starts with the same prefix
	 * Prefix of received telegrams slightly different to the one for outgoing telegrams
	 * @param incoming
	 */
	private void setPrefix(boolean incoming) {
		prefix = new char[3];
		prefix[0] = 0xA5;
		prefix[1] = 0x5A;
		
		if (incoming) {
			prefix[2] = 0x0B;
		} else {
			prefix[2] = 0x6B;			
		}
	}

//	public char getChannel() {
//		// TODO
//		return 0x00;
//		// return telegramBytes[CHANNEL_OFFSET];
//	}

	@Override
	public String toString() {
		return "DataTelegramIn [org=" + getOrg().toString() + ", dataByte=" + getDataString() + ", sensorId="
				+ getIdInt() + "(" + getIdString() + "), status=" + Integer.toHexString(getStatus()) + /*", channel="
				+ Integer.toHexString(getChannel()) +*/ "]";
	}

	protected String getPrefix() {
		return StringByteUtils.byteCharsToHexString(prefix, 0, 2);
	}

	private char telegramChecksum() {
		char sum = (char) (prefix[2] + translateOrg() + getStatus());
		for (int i = 0; i < 4; i++) {
			sum += getDataByte(i);
			sum += getIdByte(i);
		}
		return (char) (sum & 0xFF);
	}

	/**
	 * Map the origin enum back to byte-values
	 * @return The byte-value for the origin set in the telegram
	 */
	protected char translateOrg() {
		switch (getOrg()) {
		case ID:
			return 0x58;
		case EEP_RPS:
			return 0x05;
		case EEP_1BS:
			return 0x06;
		case EEP_4BS:
			return 0x07;
		default:
			return 0x00;
		}
	}

	public String getTelegramString() {
		// TODO rewrite using StringBuilder
		return StringByteUtils.byteCharsToHexString(prefix) + // 3
				StringByteUtils.byteCharToHexString(translateOrg()) + // +1
				getDataString() + // +4
				getIdString() + // +4
				StringByteUtils.byteCharToHexString(getStatus()) + // +1
				StringByteUtils.byteCharToHexString(telegramChecksum());// +1 =
																		// 14
	}

}