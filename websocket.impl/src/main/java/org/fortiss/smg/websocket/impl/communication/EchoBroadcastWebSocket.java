package org.fortiss.smg.websocket.impl.communication;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.WebSocket;
import org.fortiss.smg.websocket.api.WebSocketInterface;
import org.fortiss.smg.websocket.api.shared.communication.Request;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.api.shared.communication.ResponseType;
import org.fortiss.smg.websocket.api.shared.communication.Status;
import org.fortiss.smg.websocket.impl.WebSocketImpl;
import org.fortiss.smg.websocket.impl.communication.statemachine.ServerStateMachine;
import org.fortiss.smg.websocket.impl.utils.RequestDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * 
 * There is an onOpen, onClose and onMessage callback. The onOpen callback is
 * passed in a Connection instance that sends messages. The implementation of
 * onOpen adds the WebSocket to a collection of all known WebSockets, and
 * onClose removes the WebSocket.
 * <p>
 * The implementation of onMessage is to simply iterate through that collection
 * and to send the received message to each WebSocket
 * 
 */
public class EchoBroadcastWebSocket implements WebSocket,
		WebSocket.OnTextMessage {

	private Connection _connection;
	private ConcurrentLinkedQueue<EchoBroadcastWebSocket> _broadcast;

	/**
	 * handles user session
	 */
	private ServerStateMachine serverState;

	private static final Logger logger = LoggerFactory
			.getLogger(EchoBroadcastWebSocket.class);

	public EchoBroadcastWebSocket(
			ConcurrentLinkedQueue<EchoBroadcastWebSocket> _broadcast2) {
		this._broadcast = _broadcast2;
	}

	public void set_connection(Connection _connection) {
		this._connection = _connection;
	}

	/**
     * 
     */
	public void onOpen(Connection connection) {
		_connection = connection;
		_broadcast.add(this);

		logger.info("New device connected to websocket");
		String t_hash_id = "" + Math.random() * 6987 + new Date().getTime()
				+ Math.random() * 25962;
		// generate md5
		MessageDigest md;
		String sessionID = "error_creating_id";
		try {
			byte[] bytesOfMessage = t_hash_id.getBytes("UTF-8");
			md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			BigInteger bigInt = new BigInteger(1, thedigest);
			sessionID = bigInt.toString(16);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		serverState = new ServerStateMachine(sessionID);
		logger.info("New session created: " + sessionID);
		try {
			// send hello Messsage
			sendResponseMessage(new Response<String>("HELLO_CLIENT", Status.OK,
					ResponseType.Hello));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			serverState.process(null);
		}
	}

	/**
     * 
     */
	@Override
	public void onClose(int code, String message) {
	 WebSocketInterface instance = new WebSocketImpl()
				.getInstance(this._broadcast);
		instance.closeSession(this.serverState.getSessionID());
		_broadcast.remove(this);
	}

	/**
	 * 
	 */
	@Override
	public void onMessage(final String data) {

		GsonBuilder gson_builder = new GsonBuilder();
		gson_builder.registerTypeAdapter(Request.class,
				new RequestDeserializer());
		Gson gson = gson_builder.create();
		Request request = null;
		try {
			request = gson.fromJson(data, Request.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (request != null) {
			System.out.println("Request-Type:" + request.getType());
			System.out.println("Request-Data:" + request.getData());
		}

		Response response = new Response<String>("UNSUPPORTED",
				Status.UNSUPPORTED, ResponseType.UNSUPPORTED);

		if (request != null) {
			if (request.getType() != null) {
				response = serverState.process(request);
			} else {
				response = new Response<String>("UNSUPPORTED_REQUEST_TYPE",
						Status.UNSUPPORTED_REQUEST_TYPE);
			}
		}

		// send the response to the client
		try {
			if (request != null) {
				if (request.getmId().length() > 0) {
					response.setId(request.getmId());
				}
			}
			sendResponseMessage(response);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// for (TestEchoBroadcastWebSocket ws : _broadcast) {
		// try {
		// if (request.getmId().length() > 0) {
		// response.setId(request.getmId());
		// }
		// ws.sendResponseMessage(response);
		// } catch (IOException e) {
		// _broadcast.remove(ws);
		// e.printStackTrace();
		// }
	}

	/**
	 * 
	 * @param response
	 * @throws IOException
	 */
	public void sendResponseMessage(Response response) throws IOException {
		// to json

		// easy way without nan support
		// Gson gson = new Gson();

		GsonBuilder gsonBuilder = new GsonBuilder()
				.serializeSpecialFloatingPointValues();
		gsonBuilder.registerTypeAdapter(Double.class,
				new JsonSerializer<Double>() {
					public JsonElement serialize(Double src, Type typeOfSrc,
							JsonSerializationContext context) {
						if (src.isNaN() || src.isInfinite()
								|| src > 1.7976931348623157E300
								|| src < -1.7976931348623157E300) {
							// this line would return the string value of NaN
							// return new JsonPrimitive(src.toString());
							return null;
						} else
							return new JsonPrimitive(src);
					}
				});
		Gson gson = gsonBuilder.create();

		String jsonresult = gson.toJson(response);
		_connection.sendMessage(jsonresult);
	}

	private void sendMessage(Object o, int status) throws IOException {
		sendMessage(o, status, "-1");
	}

	private void sendMessage(Object o) throws IOException {
		sendMessage(o, Status.OK, "-1");
	}

	/**
	 * Encodes all messages
	 * @param o
	 * @param status
	 * @param messageID
	 * @throws IOException
	 */
	private void sendMessage(Object o, int status, String messageID)
			throws IOException {
		Response m = new Response(o, status, messageID);
		sendResponseMessage(m);
	}

	public ServerStateMachine getServerState() {
		return serverState;
	}

	public void setServerState(ServerStateMachine serverState) {
		this.serverState = serverState;
	}
}