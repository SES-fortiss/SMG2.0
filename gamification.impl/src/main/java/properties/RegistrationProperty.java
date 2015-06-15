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
public class RegistrationProperty extends Property {
	public RegistrationProperty() {
		super(1, "Registration Property",
				"This property shows that the user is registered as gamification participant");
	}
}
