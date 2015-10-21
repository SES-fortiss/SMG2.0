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

	

