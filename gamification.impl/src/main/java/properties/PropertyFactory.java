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
