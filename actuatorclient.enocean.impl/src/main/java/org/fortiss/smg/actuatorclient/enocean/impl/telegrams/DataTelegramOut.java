
/**
 * THIS CODE HAS BEEN IN USE BEFORE IMPLEMENTING TWO DIFFERENT VERSIONS OF THE PROTOCOL
 * CODE IS KEPT FOR REFERENCE OF THE ENOCEAN PROTOCO
 */
//package org.fortiss.smg.actuatorclient.enocean.impl.telegrams;
//
//public class DataTelegramOut extends UniversalTelegram {
//
//	private static final int CHECKSUM_OFFSET = 13;
//	private static final int STATUS_OFFSET = 12;
//	private static final int TYPE_OFFSET = 3;
//
//	public DataTelegramOut() {
//		super();
//	}
//
//	// Created for test only
////	public DataTelegramOut(String completeMsg) throws IllegalTelegramException {
////		super(completeMsg);
////	}
//
////	public void setType(char type) {
////		telegramBytes[TYPE_OFFSET] = (char) (type & 0xFF);
////	}
//
//	public void setIdHexString(String id, int channel) throws IllegalArgumentException {
//		
//		if (channel > 127){
//			throw new IllegalArgumentException("Valid channels for EnOcean-Devices are [0, 127]. Input was " + channel);
//		}
//		
//		if (id.length() == 8) {
//			setIdByte((char) (Integer.valueOf(id.substring(0, 2), 16)
//					.intValue() & 0xFF), 3);
//			setIdByte((char) (Integer.valueOf(id.substring(2, 4), 16)
//					.intValue() & 0xFF), 2);
//			setIdByte((char) (Integer.valueOf(id.substring(4, 6), 16)
//					.intValue() & 0xFF), 1);
//			setIdByte((char) ((Integer.valueOf(id.substring(6, 8), 16)
//					.intValue() + channel) & 0xFF), 0);
//		}
//	}
//
////	public void setStatus(char status, char tc, char rpc) {
////		setStatus((char) (((status << 4) + (tc << 2) + (rpc)) & 0xFF));
////	}
//
////	public void setStatus(char statusTcRpc) {
////		telegramBytes[STATUS_OFFSET] = statusTcRpc;
////	}
//
////	@OVERRIDE
////	PROTECTED STRING GETPREFIX() {
////		RETURN "A55A6B";
////	}
//
////	public void setPrefixPostfix(char val){
////		telegramBytes[2] = val;
////	}
////	
////	public char getPrefixPostfix(){
////		return telegramBytes[2];
////	}
//	
////	public char getType() {
////		return telegramBytes[TYPE_OFFSET];
////	}
//
////	public char getStatusTcRpc() {
////		return telegramBytes[STATUS_OFFSET];
////	}
//
////	private char telegramChecksum() {
////		char sum = (char) (getPrefixPostfix() + getType() + getStatusTcRpc());
////		for (int i = 0; i < 4; i++) {
////			sum += getDataByte(i);
////			sum += getSensorId(i);
////		}
////		return (char) (sum & 0xFF);
////	}
////
////	@Override
////	public String getTelegramString() {
////		telegramBytes[CHECKSUM_OFFSET] = telegramChecksum();
////		return super.getTelegramString();
////	}
//
//}
