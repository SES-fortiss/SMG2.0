package org.fortiss.smg.websocket.impl.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerFunction;
import org.fortiss.smg.containermanager.api.devices.ContainerType;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.api.shared.communication.ResponseType;
import org.fortiss.smg.websocket.api.shared.communication.Status;
import org.fortiss.smg.websocket.api.shared.schema.Device;

/**
 * Helper class to ease the interaction with containerManager and creation of
 * responses
 * 
 * @author Chrysa Papadaki - papadaki.chr@gmail.com
 * 
 */
public class ContainerManagerHelper {

	private ContainerManagerInterface containerMgr;
	protected Map<String, Device> originDeviceMap = new HashMap<String, Device>();

	private static final Logger log = Logger
			.getLogger(ContainerManagerHelper.class.getName());

	public ContainerManagerInterface getContainerMgr() {
		return containerMgr;
	}

	public void setContainerMgr(ContainerManagerInterface containerMgr) {
		this.containerMgr = containerMgr;
	}

	private static ContainerManagerHelper instance;

	/**
	 * thread-safe lazy-initialization
	 * 
	 */
	public static ContainerManagerHelper getInstance() {
		if (instance == null) {
			instance = new ContainerManagerHelper();
		}
		return instance;
	}

	/**
	 * Returns the children of a node
	 * 
	 * @param containerid
	 * @param transitive
	 * @return list of all children
	 */
	public Response getContainerChildren(String containerid) {
		List<Container> children;
		try {
			children = containerMgr.getRoomMap(containerid).getFirst();
		

		if (children != null) {
			if (children.size() > 0) {
				Response<List<Container>> r = new Response<List<Container>>(children,
						Status.OK);
				r.setResponsetype(ResponseType.ROOM_MAP);

				return r;
			}
		}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Response<String>("WRONG_INPUT", Status.WRONG_INPUT,
				ResponseType.ROOM_MAP);
	}


	public Container getContainer(String key) {
		Container c;
		try {
			c = getContainerMgr().getContainer(key);
		} catch (Exception e) {
		    //TODO remove -its here for testing only
			c = new Container("foo", "bar", ContainerType.DEVICE,
					ContainerFunction.KITCHEN, false);
			e.printStackTrace();
		}//TODO change its used this way for testing only
		return (c == null) ? new Container("foo", "bar", ContainerType.DEVICE,
				ContainerFunction.KITCHEN, false) : c;
	}

}
