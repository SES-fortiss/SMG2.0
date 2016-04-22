package org.fortiss.smg.websocket.impl.communication;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
/**
 * looks for WebSocket handshake requests and handles them by calling the
 * doWebSocketConnect method, which we have extended to create a WebSocket
 * depending on the sub protocol passed:
 * 
 */
public class APIWebSocketHandler extends WebSocketHandler {

	private ConcurrentLinkedQueue<EchoBroadcastWebSocket> _broadcast;

	public APIWebSocketHandler(ConcurrentLinkedQueue<EchoBroadcastWebSocket> _broadcast) {
		this._broadcast = _broadcast;
	}
/**
 * TODO ...
 */
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
					System.out.println(protocol);
					WebSocket _websocket = null;
					if ("org.fortiss.smartmicrogrid.frontend.api_socket".equals(protocol)) {
						_websocket = new EchoBroadcastWebSocket(_broadcast); 
					} 
					return _websocket;
				}		
}

