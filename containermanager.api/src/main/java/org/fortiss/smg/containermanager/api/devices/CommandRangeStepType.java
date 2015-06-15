/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.containermanager.api.devices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommandRangeStepType {

	LINEAR, 
	EXPO, 
	LOG, 
	UNKNOWN;
	
	
	public String toString() {
		switch (this) {
			case LINEAR: return "Linear";
			case EXPO: return "Expo";
			case LOG: return "Log";
			default: return name();
		}
	};
	
	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}
	
	@JsonCreator
	public static CommandRangeStepType fromJson(String text) {
		return valueOf(text.toUpperCase());
	}
	
	  public static CommandRangeStepType fromString(String text) {
		    if (text != null) {
		      for (CommandRangeStepType b : CommandRangeStepType.values()) {
		        if (text.equals(b.name())) {
		          return b;
		        }
		      }
		    }
		    return null;
		  }
	
}
