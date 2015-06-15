/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl;

import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;

public interface EnOceanTelegramHandler {

	/**
	 * This method is called when a new telegram was recognized
	 * @param telegram The received Telegram wrapped in an Object of type DataTelegramIn
	 */
	public void handleIncomingTelegram(UniversalTelegram telegram);
	
	/**
	 * @return String containing the queueName
	 */
//	public String getQueueName();
}

	

