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
