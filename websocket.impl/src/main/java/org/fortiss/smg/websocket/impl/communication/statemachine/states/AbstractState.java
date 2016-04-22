package org.fortiss.smg.websocket.impl.communication.statemachine.states;

import org.fortiss.smg.websocket.impl.communication.statemachine.State;
import org.fortiss.smg.websocket.impl.communication.statemachine.StateMachine;

public abstract class AbstractState implements State {
	protected StateMachine stateMachine;

	protected AbstractState(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}


}
