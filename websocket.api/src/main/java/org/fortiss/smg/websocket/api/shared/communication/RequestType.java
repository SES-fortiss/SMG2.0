package org.fortiss.smg.websocket.api.shared.communication;

public enum RequestType {
	
	
	LOGIN, //use a valid user name, for the start: dev/dev
	LOGIN_MAC,
	SEND_COMMAND, //control device
	SUBSCRIBE_DEVICE, //use a valid device id to subscribe to a node
	UNSUBSCRIBE_DEVICE,// unsubscribe
	SUBSCRIBE_ROOM,//use a valid device id to subscribe to all nodes
	UNSUBSCRIBE_ROOM,
	//new in SGM2
	GET_ASSIGNED_DEVICES, // returns the assigned rooms - a collection of containers which user is allowed to control
	GET_ROOM_MAP;// all children of parent container
	
}
