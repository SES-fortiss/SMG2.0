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
public class OnePointAchievement extends Achievement {
	public OnePointAchievement() {
		super(2, "One Point Achievement",
				"This achievement is earned, when the user scores a point the first time",
				"onePoint.jpg");
	}
}
