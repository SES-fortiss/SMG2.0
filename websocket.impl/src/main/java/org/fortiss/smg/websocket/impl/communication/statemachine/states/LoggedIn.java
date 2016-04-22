package org.fortiss.smg.websocket.impl.communication.statemachine.states;

import java.util.List;

import org.fortiss.smg.websocket.api.WebSocketInterface;
import org.fortiss.smg.websocket.api.shared.RoomMap;
import org.fortiss.smg.websocket.api.shared.communication.Request;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.api.shared.communication.ResponseType;
import org.fortiss.smg.websocket.api.shared.communication.Status;
import org.fortiss.smg.websocket.api.shared.schema.Device;
import org.fortiss.smg.websocket.api.shared.subscribe.SubscribeData;
import org.fortiss.smg.websocket.api.shared.subscribe.UpdateDeviceData;
import org.fortiss.smg.websocket.impl.WebSocketImpl;
import org.fortiss.smg.websocket.impl.communication.statemachine.StateMachine;

public class LoggedIn extends AbstractState {

	private Error error;
	WebSocketInterface webSocket;

	public LoggedIn(StateMachine stateMachine, Error error) {
		super(stateMachine);
		this.error = error;
	}

	@Override
	public Response process(Request request) {
		Response<String> r = new Response<String>("UNSUPPORTED_REQUEST_TYPE",
				Status.UNSUPPORTED_REQUEST_TYPE, ResponseType.UNSUPPORTED);

		System.out.println(request.getType());

		if (webSocket == null) 
			webSocket = new WebSocketImpl().getInstance();
		switch (request.getType()) {
		case SUBSCRIBE_DEVICE:
			if (webSocket != null) {
				SubscribeData s = (SubscribeData) request.getData();
				Response resp = webSocket.subscribe(s.getId(),
						this.stateMachine.getSessionID());
				return resp;
			} else 
				return new Response<String>("CONTENT_ERROR", Status.CONTENT_ERROR,
						ResponseType.ERROR);

		case UNSUBSCRIBE_DEVICE:
			if (webSocket != null) {
				SubscribeData s = (SubscribeData) request.getData();
				Response resp = webSocket.unsubscribe(s.getId(),
						this.stateMachine.getSessionID());
				return resp;
			} else 
				return new Response<String>("CONTENT_ERROR", Status.CONTENT_ERROR,
						ResponseType.ERROR);
		case SUBSCRIBE_ROOM:
			if (webSocket != null) {
				SubscribeData s = (SubscribeData) request.getData();
				Response resp = webSocket.subscribeAll(s.getId(),
						this.stateMachine.getSessionID());
				return resp;
			} else 
				return new Response<String>("CONTENT_ERROR", Status.CONTENT_ERROR,
						ResponseType.ERROR);

		case UNSUBSCRIBE_ROOM:
			if (webSocket != null) {
				SubscribeData s = (SubscribeData) request.getData();
				Response resp = webSocket.unsubscribeAll(this.stateMachine
						.getSessionID());
				return resp;
			} else 
				return new Response<String>("CONTENT_ERROR", Status.CONTENT_ERROR,
						ResponseType.ERROR);

			// maybe
		/*case GET_CHILDREN_IDS_OF:
			if (webSocket != null) {
				SubscribeData s = (SubscribeData) request.getData();
				Response<List<Device>> resp = null;TODO webSocket.getChildrenList(s
						.getId());
				return resp;
			} else {
				// ERROR
				r = new Response<String>("CONTENT_ERROR", Status.CONTENT_ERROR,
						ResponseType.ERROR);
				return r;
			}
		case GET_ALL_CHILDREN_IDS_OF:
			if (webSocket != null) {
				SubscribeData s = (SubscribeData) request.getData();
				Response<RoomMap> resp = null;//TODO webSocket.getRoomMap(s.getId());
				return resp;
			} else {
				// ERROR
				r = new Response<String>("CONTENT_ERROR", Status.CONTENT_ERROR,
						ResponseType.ERROR);
				return r;
			}*/
		case GET_ASSIGNED_DEVICES:// TODO
		case SEND_COMMAND: //control device
			if (webSocket != null) {
				UpdateDeviceData s = (UpdateDeviceData) request.getData();
				Response resp = webSocket.sendCommand(s.getConId(),
						s.getType(), s.getCommand());
				return resp;
			} else 
				return new Response<String>("CONTENT_ERROR", Status.CONTENT_ERROR,
						ResponseType.ERROR);
		default:
		}
		return r;
	}

}
