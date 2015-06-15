/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
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