/**
 * 
 */
package achievements;

import org.fortiss.smg.gamification.api.Achievement;

/**
 * @author Pahlke
 *
 */
public class HundredPointsAchievement extends Achievement {
	public HundredPointsAchievement() {
		super(3, "Hundred Points Achievement",
				"This achievement is earned, when the user reaches 100 points the first time",
				"hundredPoints.jpg");
	}
}
