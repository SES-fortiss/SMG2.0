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
import org.fortiss.smg.gamification.api.PropertyAchievementIDs;
import org.fortiss.smg.gamification.impl.GamificationImpl;

/**
 * @author Pahlke
 *
 */
public class AchievementFactory {
	
	public Achievement createAchievement(int achievementID) {
		if(achievementID == PropertyAchievementIDs.hundredPointsAchievement) {
			return new HundredPointsAchievement();
		}
		else if(achievementID == PropertyAchievementIDs.onePointAchievement) {
			return new OnePointAchievement();
		}
		else if(achievementID == PropertyAchievementIDs.registerAchievement) {
			return new RegistrationAchievement();
		}
		else {
			return null;
		}
	}

}
