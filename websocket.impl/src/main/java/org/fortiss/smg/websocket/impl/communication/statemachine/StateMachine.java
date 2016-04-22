package org.fortiss.smg.websocket.impl.communication.statemachine;


import org.fortiss.smg.websocket.api.shared.communication.Request;
import org.fortiss.smg.websocket.api.shared.communication.Response;

public interface StateMachine {
	public void changeState(State state);
	public State currentState();
	public Response process(Request request);
	public String getSessionID();
	public String getUserID();
	public void setUserID(String user);
}
