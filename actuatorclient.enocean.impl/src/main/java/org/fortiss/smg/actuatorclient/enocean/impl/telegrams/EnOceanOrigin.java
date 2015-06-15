/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.telegrams;

public enum EnOceanOrigin {

	/*V2 | V3*/ /** ESP Versions */
EEP_RPS,	/*05 | F6*/ /** used for Rocker/MechanicalHandle */
EEP_1BS,	/*06 | D5*/ /** used for Contact/Switch */
EEP_4BS,	/*07 | A5*/ /** used for Temperature/Light/Occupancy/Gas/Meter/HVAC/DigitalMeasuring/Universal */
EEP_VLD,	/*xx | D2*/	/** not used yet Room - Room Control Panel*/
ID,			/*58 | 08*/	/** used for getting the ID of a device */
UNKNOWN,	/*xx | xx*/	/** used for unknown non-error State (like empty instantiation) */
ERROR,		/*xx | xx*/	/** used for any error State */
ID_RESP // TODO: description

}
