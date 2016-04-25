/**
 * 
 */
package properties;

import org.fortiss.smg.gamification.api.Property;

/**
 * @author Pahlke
 *
 */
public class OnePointProperty extends Property {
	public OnePointProperty() {
		super(2, "One Point Property",
				"This property shows that the user currently has a positive value of points");
	}
}
