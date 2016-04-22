package org.fortiss.smg.websocket.api.shared.communication;

public enum ResponseType {

    Hello, 
    LOGIN, 
    SEND_COMMAND, 
    SUBSCRIBE_DEVICE, 
    SUBSCRIBE_ROOM, 
    UNSUBSCRIBE,
    ROOM_MAP,
    ERROR, 
    UNSUPPORTED, 
    DEVICE_NOT_FOUND,
}
