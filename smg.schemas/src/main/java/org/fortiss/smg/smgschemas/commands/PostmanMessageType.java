package org.fortiss.smg.smgschemas.commands;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PostmanMessageType {
	Email, IRCMessage, Twitter;

}
