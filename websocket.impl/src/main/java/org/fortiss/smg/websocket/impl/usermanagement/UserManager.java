package org.fortiss.smg.websocket.impl.usermanagement;

import org.fortiss.smg.usermanager.api.UserManagerInterface;
import org.fortiss.smg.websocket.impl.communication.statemachine.ServerStateMachine;

/**
 * 
 * @author Chrysa Papadaki - papadaki.chr@gmail.com
 * 
 */
public class UserManager {

	private static UserManager instance;
	protected UserManagerInterface userManager;
	private ServerStateMachine smachine;

	public ServerStateMachine getSmachine() {
		return smachine;
	}

	public void setSmachine(ServerStateMachine smachine) {
		this.smachine = smachine;
	}

	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}

	public static boolean validLogin(String username, String password) {
		if ((password.length() > 0 && username.length() > 0)) 
			if ((username.equals("dev")// TODO make use of UserManager i.e.
										// invoke webSocketImpl.login
			&& password.equals("dev")))
				return true;
		return false;
	}

	/**
	 * Initializes a new session and adds session to <code>sessions</code>
	 * 
	 * @param sessionId
	 */
	public void initSession(String sessionId) {
		this.smachine = new ServerStateMachine(sessionId);
	}

}
