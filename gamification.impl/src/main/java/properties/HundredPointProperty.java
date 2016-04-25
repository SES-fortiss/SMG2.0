/**
 * 
 */
package properties;

import org.fortiss.smg.gamification.api.Property;

/**
 * @author Pahlke
 *
 */
public class HundredPointProperty extends Property {
	public HundredPointProperty() {
		super(3, "Hundred Point Property",
				"This property is fulfilled, when the user has at least 100 points");
	}
}
