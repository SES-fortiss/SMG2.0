/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.data;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.fortiss.smg.optimizer.utils.Tools.capacity;
import static org.fortiss.smg.optimizer.utils.Tools.format;
import static org.fortiss.smg.optimizer.utils.Tools.power;
import static org.fortiss.smg.optimizer.utils.Tools.range;

/***
 * 
 * @author Cheng Zhang
 * @version 1.0
 * 
 */
public class Specification {

	private static Specification specification = null;

	// Range from 0 to 1
	private double chargeEfficiency = 1.0;

	// Range from 0 to 1
	private double dischargeEfficiency = 1.0;

	// Unit Wh
	private double maximumCapacity;

	// Unit Wh
	private double initialCapacity;

	// Unit Watt
	private double maximumLineLoad = 20000.0;

	// Unit Watt
	private double maximumPowerOutput = 20000.0;

	// Unit Watt
	private double maximumPowerInput = 12000.0;

	// Possible export energy form battery
	private boolean possibleFromBattery = false;

	// Possible export energy from generation
	private boolean possibleFromGeneration = false;

	public static Specification getSpecification() {
		if (specification == null) {
			specification = new Specification();
		}
		return specification;
	}

	/** Constructor */
	public void setSpecification(double chargeEfficiency,
			double dischargeEfficiency, double maximumCapacity,
			double initialCapacity, double maximumLineLoad,
			double maximumPowerOutput, double maximumPowerInput,
			boolean possibleFromBattery, boolean possibleFromGeneration) {
		this.chargeEfficiency = range(0, 1, chargeEfficiency);
		this.dischargeEfficiency = range(0, 1, dischargeEfficiency);
		this.maximumCapacity = max(0, maximumCapacity);
		this.initialCapacity = max(0, initialCapacity);
		this.maximumLineLoad = max(0, maximumLineLoad);
		this.maximumPowerOutput = max(0, maximumPowerOutput);
		this.maximumPowerInput = max(0, maximumPowerInput);
		this.possibleFromBattery = possibleFromBattery;
		this.possibleFromGeneration = possibleFromGeneration;
	}

	public double getChargeEfficiency() {
		return chargeEfficiency;
	}

	public double getDischargeEfficiency() {
		return dischargeEfficiency;
	}

	public double getMaximumCapacity() {
		return maximumCapacity;
	}

	public double getInitialCapacity() {
		return initialCapacity;
	}

	public double getMaximumLineLoad() {
		return maximumLineLoad;
	}

	public double getMaximumPowerOutput() {
		return maximumPowerOutput;
	}

	public double getMaximumPowerInput() {
		return maximumPowerInput;
	}

	public boolean isPossibleFromBattery() {
		return possibleFromBattery;
	}

	public boolean isPossibleFromGeneration() {
		return possibleFromGeneration;
	}

	public void setMaximumCapacity(double maximumCapacity) {
		this.maximumCapacity = maximumCapacity;
	}

	/** Get demand energy */
	public double getDemand(double consumption, double generation,
			double duration) {
		double batteryPower = 0.0;
		if (possibleFromBattery && possibleFromGeneration) {
			batteryPower = -maximumLineLoad - generation - consumption;
		} else if (!possibleFromBattery && possibleFromGeneration) {
			batteryPower = max(0, -maximumLineLoad - generation) - consumption;
		} else if (possibleFromBattery && !possibleFromGeneration) {
			batteryPower = generation + consumption < 0 ? -generation
					- consumption : -maximumLineLoad - generation - consumption;
		} else {
			batteryPower = -consumption - generation;
		}
		return format(capacity(
				range(-maximumPowerOutput, maximumPowerInput, batteryPower),
				duration, chargeEfficiency, dischargeEfficiency));
	}

	/** Get supply energy */
	public double getSupply(double consumption, double duration) {
		return format(capacity(
				min(maximumPowerInput, maximumLineLoad - consumption),
				duration, chargeEfficiency, dischargeEfficiency));
	}

	/** Get battery exchange power */
	public double getBatteryExchange(double exchange, double duration) {
		return format(power(exchange, duration, chargeEfficiency,
				dischargeEfficiency));
	}

	/** Get grid exchange power */
	public double getGridExchange(double consumption, double generation,
			double charge, double discharge) {
		if (possibleFromBattery && possibleFromGeneration) {
			return -consumption - generation - charge - discharge;
		} else if (!possibleFromBattery && possibleFromGeneration) {
			return min(-generation - consumption - charge - discharge,
					-generation);
		} else if (possibleFromBattery && !possibleFromGeneration) {
			return min(-generation - consumption - charge - discharge,
					-discharge);
		} else {
			return min(-consumption - generation - charge - discharge, 0);
		}
	}

	/** Get battery exchange energy for the dummy algorithm */
	public double getDummyBatteryExchange(double consumption,
			double generation, double capacity, double elapsedTime) {
		if (consumption + generation > 0 && capacity > 0) {
			return max(
					capacity(
							max(-consumption - generation, -maximumPowerOutput),
							elapsedTime, chargeEfficiency, dischargeEfficiency),
					-capacity);
		} else if (consumption + generation < 0 && capacity < maximumCapacity
				&& !possibleFromGeneration) {
			return min(
					capacity(min(-consumption - generation, maximumPowerInput),
							elapsedTime, chargeEfficiency, dischargeEfficiency),
					maximumCapacity - capacity);
		}
		return 0;
	}

	@Override
	public String toString() {
		return "[Specification: chargeEfficiency" + chargeEfficiency
				+ ", dischargeEfficiency " + dischargeEfficiency
				+ ", maximumCapacity:" + maximumCapacity + ", initialCapacity:"
				+ initialCapacity + ", maximumLineLoad:" + maximumLineLoad
				+ ", maximumPowerOutput:" + maximumPowerOutput
				+ ", maximumPowerInput:" + maximumPowerInput
				+ ", possibleFromBattery:" + possibleFromBattery
				+ ", possibleFromGeneration:" + possibleFromGeneration + "]";
	}
}
