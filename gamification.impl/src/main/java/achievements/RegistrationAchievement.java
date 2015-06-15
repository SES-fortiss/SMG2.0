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
package achievements;

import org.fortiss.smg.gamification.api.Achievement;

/**
 * @author Pahlke
 *
 */
public class RegistrationAchievement extends Achievement {
	public RegistrationAchievement() {
		super(1, "Registration Achievement",
				"This achievement is earned, when the user registers for the gamification feature",
				"registration.jpg");

	}
}
