package org.fortiss.smg.usermanager.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class User implements Serializable {

	private static final long serialVersionUID = 9186973163897574132L;

	protected boolean loggedin;
	protected long id;
	protected String name;
	protected String email;
	protected long roleId;
	protected long roomRoleId;
	protected String userName;
	protected String phone;
	protected Set profileDetails;
	protected String activeProfile;
	protected String primaryContactInfo;
	
	public User() {
	}

	public User(boolean loggedin, long id, String userName, String name,
			String email, long roleId, long roomRoleId, String phone,
			Set profileDetails, String activeProfile, String primaryContactInfo) {
		super();
		this.id = id;
		this.userName = userName;
		this.name = name;
		this.email = email;
		this.roleId = roleId;
		this.roomRoleId = roomRoleId;
		this.loggedin = loggedin;
		this.phone = phone;
		this.profileDetails = profileDetails;
		this.activeProfile = activeProfile;
		this.primaryContactInfo = primaryContactInfo;
	
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getRoomRoleId() {
		return roomRoleId;
	}
	
	public void setRoomRoleId(long roomRoleId) {
		this.roomRoleId = roomRoleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getLoggedin() {
		return loggedin;
	}

	public void setLoggedin(boolean loggedin) {
		this.loggedin = loggedin;
	}

	public void setProfileDetails(Set profileDetails) {
		this.profileDetails = profileDetails;
	}
	
	public Set getProfileDetails() {
		return profileDetails;
	}

	public Double getValueOfDeviceID(Integer deviceID) {
		Iterator i = profileDetails.iterator();
		boolean found = false;
		Double result = 0.0;
		while (i.hasNext() && found != true) {
			Map.Entry entry = (Map.Entry) i.next();
			if (entry.getKey() == deviceID) {
				result = ((Double) entry.getValue());
				found = true;
			}

		}
		return result;
	}

	public void setActiveProfile(String activeProfile) {
		this.activeProfile = activeProfile;
	}

	public String getActiveProfile() {
		if (activeProfile == null) {
			return "";
		}
		return activeProfile;
	}

	public void setPrimaryContactInfo(String primaryContactInfo) {
		this.primaryContactInfo = primaryContactInfo;
	}

	public String getPrimaryContactInfo() {
		if (primaryContactInfo == null) {
			return "";
		}
		return primaryContactInfo;
	}

}
