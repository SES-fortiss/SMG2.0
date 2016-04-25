package org.fortiss.smg.containermanager.api.devices;

public class DeviceId {

	// has to be unique for each wrapper
	String devid;
	String wrapperId;

	protected DeviceId() {

	}

	public DeviceId(String devid, String wrapperId) {
		super();
		this.devid = devid;
		this.wrapperId = wrapperId;
	}

	@Override
	public String toString() {
		return "DeviceId [devid=" + devid + ", wrapperId=" + wrapperId + "]";
	}

	public String toContainterId() {
		return wrapperId + "." + devid;
	}

	public String getDevid() {
		return devid;
	}

	public void setDevid(String devid) {
		this.devid = devid;
	}

	public String getWrapperId() {
		return wrapperId;
	}

	public void setWrapperId(String wrapperId) {
		this.wrapperId = wrapperId;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (getClass() != other.getClass()) {
			return false;
		}
		DeviceId otherDev = (DeviceId) other;
		if (!devid.equals(otherDev.devid)) {
			return false;
		}
		if (!wrapperId.equals(otherDev.wrapperId)) {
			return false;
		}
		return true;
	}
}
