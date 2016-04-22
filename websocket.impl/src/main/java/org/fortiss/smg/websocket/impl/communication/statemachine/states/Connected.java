package org.fortiss.smg.websocket.impl.communication.statemachine.states;

import org.fortiss.smg.websocket.api.shared.communication.Request;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.api.shared.communication.ResponseType;
import org.fortiss.smg.websocket.api.shared.communication.Status;
import org.fortiss.smg.websocket.impl.WebSocketImpl;
import org.fortiss.smg.websocket.impl.communication.statemachine.StateMachine;
import org.fortiss.smg.websocket.impl.usermanagement.User;

public class Connected extends AbstractState {

    private LoggedIn loggedInState;
    private Error userError;
    private WebSocketImpl webSocket;

    public Connected(StateMachine stateMachine, LoggedIn loggedInState,
            Error userError) {
        super(stateMachine);
        this.loggedInState = loggedInState;
        this.userError = userError;
    }

    @Override
    public Response<String> process(Request request) {
        Response<String> r = new Response<String>("UNSUPPORTED_REQUEST_TYPE",
                Status.UNSUPPORTED_REQUEST_TYPE, ResponseType.UNSUPPORTED);
        if (webSocket == null)
            webSocket = new WebSocketImpl().getInstance();
        switch (request.getType()) {
            case LOGIN_MAC:
                User user = (User) request.getData();
                // also you can try authenticate user by device address using
                // the line below
                r = webSocket.authenticateUser(user.getMac());
                // change state connected->loggedin
                if (r.getStatus() == Status.LOGGED_IN) {
                    this.stateMachine.setUserID(user.getUserName());
                    this.stateMachine.changeState(this.loggedInState);
                }
                return r;
            case LOGIN:
                user = (User) request.getData();
                r = webSocket.authenticateUser(user.getUserName(),
                        user.getPassword());

                // also you can try authenticate user by device address using
                // the line below
                // r = webSocket.authenticateUser(user.getMac());
                // change state connected->loggedin
                if (r.getStatus() == Status.LOGGED_IN) {
                    this.stateMachine.setUserID(user.getUserName());
                    this.stateMachine.changeState(this.loggedInState);
                }
                return r;
            default:
        }
        return r;
    }

}
