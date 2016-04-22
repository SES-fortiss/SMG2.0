package org.fortiss.smg.websocket.impl.communication;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.api.shared.communication.ResponseType;
import org.fortiss.smg.websocket.api.shared.communication.Status;
import org.fortiss.smg.websocket.api.shared.subscribe.UpdateDeviceData;

import com.google.common.collect.Multimap;

/**
 * Provides functionality to populate events to clients
 * 
 * @author Chrysa Papadaki - papadaki.chr@gmail.com
 * 
 */
public class Dispatcher {

	private static Dispatcher instance;
	private Multimap<String, String> subscribedIDs;

	private ConcurrentLinkedQueue<EchoBroadcastWebSocket> _broadcast;

	public static Dispatcher getInstance() {
		if (instance == null) {
			instance = new Dispatcher();
		}
		return instance;
	}

	public <T> int sendEvent(T value, DeviceId dev, String client) {
		int counts = 0;
		synchronized (subscribedIDs) { // Synchronizing on m, not s!
			Collection<String> sessionIDs = subscribedIDs.get(client);
			Iterator<String> i = sessionIDs.iterator();
			while (i.hasNext()) {
				String session = i.next();
				for (Object element : _broadcast) {
					EchoBroadcastWebSocket socket = (EchoBroadcastWebSocket) element;
					if (socket.getServerState().getSessionID().equals(session)) {
						// this isn t that perfect
						// PROBLEM: Multiple double values

						UpdateDeviceData<T> update_resp = new UpdateDeviceData<T>();
/*TODO prepare response to client
						update_resp.setId(dev);
						update_resp.setType(type);
						update_resp.setValue(value);
						if (type == UpdateType.DOUBLE) {
							update_resp
									.setUnit(((DoubleEvent) value).getUnit());
						}
*/
						Response<UpdateDeviceData<T>> resp = new Response<UpdateDeviceData<T>>(
								update_resp);

						resp.setResponsetype(ResponseType.SUBSCRIBE_DEVICE);
						resp.setStatus(Status.DEVICE_UPDATE);

						try {
							socket.sendResponseMessage(resp);
							counts++;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return counts;

	}

	/**
	 * @return the _broadcast
	 */
	public ConcurrentLinkedQueue<EchoBroadcastWebSocket> getBroadcast() {
		return _broadcast;
	}

	/**
	 * @param _broadcast
	 *            the _broadcast to set
	 */
	public void setBroadcast(
			ConcurrentLinkedQueue<EchoBroadcastWebSocket> _broadcast) {
		this._broadcast = _broadcast;
	}
	
	/**
	 * 
	 * @return
	 */
	public Multimap<String, String> getSubscribedIDs() {
		return subscribedIDs;
	}
	
	/**
	 * 
	 * @param subscribedIDs
	 */
	public void setSubscribedIDs(Multimap<String, String> subscribedIDs) {
		this.subscribedIDs = subscribedIDs;
	}

	public void sendEvent(DeviceContainer ev, String client) {
		// TODO Auto-generated method stub
		
	}
}
