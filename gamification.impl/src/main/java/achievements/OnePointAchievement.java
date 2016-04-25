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
