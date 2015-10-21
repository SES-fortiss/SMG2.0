package org.fortiss.smg.actuatorclient.enocean.impl;

import org.fortiss.smg.actuatorclient.enocean.impl.telegrams.UniversalTelegram;

public interface USBTelegramHandler {
	// FIXME: this interface is temporary and should be merged with (probably) EnOcenaTelegramHandler
	void handleTelegram(char[] telegramBytes);
	
}
