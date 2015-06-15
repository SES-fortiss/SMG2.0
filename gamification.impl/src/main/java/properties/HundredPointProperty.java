/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
/**
 * 
 */
package properties;

import org.fortiss.smg.gamification.api.Property;

/**
 * @author Pahlke
 *
 */
public class HundredPointProperty extends Property {
	public HundredPointProperty() {
		super(3, "Hundred Point Property",
				"This property is fulfilled, when the user has at least 100 points");
	}
}
