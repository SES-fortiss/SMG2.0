package org.fortiss.smg.websocket.impl.communication.statemachine;


import org.fortiss.smg.websocket.api.shared.communication.Request;
import org.fortiss.smg.websocket.api.shared.communication.Response;


public interface State {
	public Response process(Request request);
}
