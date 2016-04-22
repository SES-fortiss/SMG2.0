package org.fortiss.smg.websocket.api.shared.wssecurity;

import java.util.HashMap;
import java.util.Map;


/**
 * Enumeration for the different permissions available.
 * 
 */
public enum TypeOfPermission {
	USER_MANAGEMENT, RULES_MANAGEMENT, CONFIG_DEVICES, CONTROL_DEVICES, SUPER_USER_MANAGEMENT, LET_THROUGH, LOCATION_MANAGER, SUPER_LOCATION_MANAGER;

	/**
	 * This map holds the mapping between {@link TypeOfPermission} and the names
	 * used for each {@link Permission}. It is important that it remains
	 * consistent with the database names.
	 */
	private static final Map<String, TypeOfPermission> NAME_TO_PERM = new HashMap<String, TypeOfPermission>();
	static {
		NAME_TO_PERM.put("Super User Management",
				TypeOfPermission.SUPER_USER_MANAGEMENT);
		NAME_TO_PERM.put("User Management", TypeOfPermission.USER_MANAGEMENT);
		NAME_TO_PERM.put("Configure Devices", TypeOfPermission.CONFIG_DEVICES);
		NAME_TO_PERM.put("Control Devices", TypeOfPermission.CONTROL_DEVICES);
		NAME_TO_PERM.put("Rules Management", TypeOfPermission.RULES_MANAGEMENT);
		NAME_TO_PERM.put("Location Manager", TypeOfPermission.LOCATION_MANAGER);
		NAME_TO_PERM.put("Super Location Manager",
				TypeOfPermission.SUPER_LOCATION_MANAGER);
	}

	public static TypeOfPermission getTypeByName(String name) {
		TypeOfPermission top = NAME_TO_PERM.get(name);

		if (top == null) {
			throw new IllegalArgumentException(
					"No TypeOfPermission available for this name");
		}

		return top;
	}
}