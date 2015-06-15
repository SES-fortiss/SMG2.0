/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
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
