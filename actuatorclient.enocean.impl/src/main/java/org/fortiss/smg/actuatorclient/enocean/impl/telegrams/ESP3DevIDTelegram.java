package org.fortiss.smg.actuatorclient.enocean.impl.telegrams;


public class ESP3DevIDTelegram extends ESP3Telegram {

	public ESP3DevIDTelegram() {
		setOrg(EnOceanOrigin.ID);
	}
	
	@Override
	public String getTelegramString() {
		return "5500010005700838"; // TODO: veeerrrryyy dirty hack
	}

	@Override
	public char[] getTelegramBytes() {
		return StringByteUtils.readBytes(getTelegramString()); // TODO: dirty
																// hack..
	}
	
}