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
