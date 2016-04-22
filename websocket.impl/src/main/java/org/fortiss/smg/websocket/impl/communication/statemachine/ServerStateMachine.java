package org.fortiss.smg.websocket.impl.communication.statemachine;


import org.fortiss.smg.websocket.api.shared.communication.Request;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.impl.communication.statemachine.states.Connected;
import org.fortiss.smg.websocket.impl.communication.statemachine.states.Error;
import org.fortiss.smg.websocket.impl.communication.statemachine.states.LoggedIn;


public class ServerStateMachine implements StateMachine {

    /**
     * current session id
     */
	private String sessionID;
	
	/**
	 * userName of {@link org.fortiss.smg.usermanager.api.User} 
	 * For the moment this is mainly used for using UserManager api 
	 */
	private String userID;
	
	public String getSessionID() {
		return sessionID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserID() {
		return userID;
	}

	/**
	 * new session
	 * @param sessionID
	 */
	public ServerStateMachine(String sessionID) {
		Error error = new Error(this);
		LoggedIn loggedIn = new LoggedIn(this, error);
		curState =  new Connected(this,loggedIn,error);
		this.sessionID = sessionID;
	}

	private State curState;

	@Override
	public void changeState(State state) {
		this.curState = state;
	}
	
	public State currentState(){
		return curState;
	}

	@Override
	public Response process(Request request) {
		Response output = curState.process(request);
		return output;
	}
}
