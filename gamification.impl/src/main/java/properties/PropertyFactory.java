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
import org.fortiss.smg.gamification.api.PropertyAchievementIDs;

/**
 * @author Pahlke
 *
 */
public class PropertyFactory {

	public Property createProperty(int propertyID) {
		if(propertyID == PropertyAchievementIDs.registerProperty) {
			return new RegistrationProperty();
		}
		else if(propertyID == PropertyAchievementIDs.onePointProperty) {
			return new OnePointProperty();
		}
		else if(propertyID == PropertyAchievementIDs.hundredPointsProperty) {
			return new HundredPointProperty();
		}
		else {
			return null;
		}
	}
}
