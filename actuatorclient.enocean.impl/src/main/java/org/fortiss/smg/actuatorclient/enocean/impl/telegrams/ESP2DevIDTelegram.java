package org.fortiss.smg.actuatorclient.enocean.impl.telegrams;

public class ESP2DevIDTelegram extends ESP2Telegram {

	public ESP2DevIDTelegram() {
		super(false);
		setPrefix();
		setOrg(EnOceanOrigin.ID);
	}

	private void setPrefix() {
		prefix = new char[3];
		prefix[0] = 0xA5;
		prefix[1] = 0x5A;
		prefix[2] = 0xAB;
	}
}
