package org.fortiss.smg.websocket.impl.communication.statemachine.states;


import org.fortiss.smg.websocket.api.shared.communication.Request;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.api.shared.communication.ResponseType;
import org.fortiss.smg.websocket.api.shared.communication.Status;
import org.fortiss.smg.websocket.impl.communication.statemachine.StateMachine;


public class Error extends AbstractState {
	public Error(StateMachine stateMachine) {
		super(stateMachine);
	}



	@Override
	public Response<String> process(Request request) {
		Response<String> r = new Response<String>("OUT_OF_ORDER",
				Status.OUT_OF_ORDER, ResponseType.ERROR);
		return r;
	}




}
