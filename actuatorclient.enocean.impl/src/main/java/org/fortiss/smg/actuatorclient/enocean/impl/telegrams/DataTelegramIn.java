/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */

/**
 * THIS CODE HAS BEEN IN USE BEFORE IMPLEMENTING TWO DIFFERENT VERSIONS OF THE PROTOCOL
 * CODE IS KEPT FOR REFERENCE OF THE ENOCEAN PROTOCO
 */
//package org.fortiss.smg.actuatorclient.enocean.impl.telegrams;
//
///**
// * This class represents an incoming Telegram at the basic level. It gives you
// * easier access to the telegrams contents.
// **/
//public class DataTelegramIn extends UniversalTelegram {
//	private static final int LENGTH_OFFSET = 2;
//	private static final int ORG_OFFSET = 3;
//	private static final int CHANNEL_OFFSET = 13;
//	private static final int STATUS_OFFSET = 12;
//
//	/**
//	 * @param byteString
//	 *            a String what encodes 13 bytes in hex.
//	 * @throws IllegalTelegramException
//	 */
////	public DataTelegramIn(String byteString) throws IllegalTelegramException {
////		super(byteString);
////		telegramBytes[LENGTH_OFFSET] = 0x0B;
////	}
//
//	public DataTelegramIn() {
//		super();
////		telegramBytes[LENGTH_OFFSET] = 0x0B;
//	}
//
////	public char getOrg() {
////		return telegramBytes[ORG_OFFSET];
////	}
//
////	public char getStatus() {
////		return telegramBytes[STATUS_OFFSET];
////	}
//
//	public char getChannel() {
//		return telegramBytes[CHANNEL_OFFSET];
//	}
//
//	@Override
//	public String toString() {
//		return "DataTelegramIn [org=" + getOrg().toString() + ", dataByte=" + getDataString() + ", sensorId="
//				+ getIdInt() + "(" + getIdString() + "), status=" + Integer.toHexString(getStatus())
//				+ ", channel=" + Integer.toHexString(getChannel()) + "]";
//	}
//
////	@Override
////	protected String getPrefix() {
////		return "A55A";
////	}
//
////	public void setOrg(char b) {
////		telegramBytes[ORG_OFFSET] = b;
////	}
//
//	public void setChannel(char c) {
//		telegramBytes[CHANNEL_OFFSET] = c;
//	}
//
////	public void setStatus(char c) {
////		telegramBytes[STATUS_OFFSET] = c;
////	}
//
//	public int getDataInt() {
//		int value = 0;
//		for (int i = 0; i <=3; i++) {
//			value = value << 8;
//			value += getDataByte(i);
//		}
//		return value;
//	}
//
//}
