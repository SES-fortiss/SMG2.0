package org.fortiss.smg.containermanager.api.devices;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@XmlType(name = "CommandRangeStepType")
@XmlEnum
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommandRangeStepType {

	@XmlEnumValue("Linear")
	LINEAR("Linear"), 
	@XmlEnumValue("Expo")
	EXPO("Expo"), 
	@XmlEnumValue("Log")
	LOG("Log"), 
	@XmlEnumValue("Unknown")
	UNKNOWN("Unknown");
	
	
	private final String value;

	CommandRangeStepType(String v) {
		value = v;
	}
	
	public String value() {
		return value;
	}
	
	/*public String toString() {
		switch (this) {
			case LINEAR: return "Linear";
			case EXPO: return "Expo";
			case LOG: return "Log";
			default: return name();
		}
	};*/
	
	public static CommandRangeStepType fromValue(String v) {
		for (CommandRangeStepType c: CommandRangeStepType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
	/*
	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	} */
	
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
