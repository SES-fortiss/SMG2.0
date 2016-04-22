package org.fortiss.smg.websocket.api.shared.communication;

public final class Status {

	public static final int LOGGED_IN = 100;
	
	public static final int OK = 200;
	
	public static final int UNSUPPORTED = 400;
	public static final int WRONG_LOGIN_DATA = 401;
	/*no implemented */
	public static final int CLIENT_DISCONNECTED = 403;
	/*no implemented */
	public static final int CONTENT_ERROR = 404;

	public static final int UNSUPPORTED_REQUEST_TYPE = 406;

	public static final int OUT_OF_ORDER = 500;

	public static final int NOT_IMPLEMENTED = 600;
	
	public static final int DEVICE_UPDATE = 700;
	public static final int ROOM_UPDATE = 710;

	public static final int DEPRECATED = 800;
	public static final int WRONG_INPUT = 801;
	


}
