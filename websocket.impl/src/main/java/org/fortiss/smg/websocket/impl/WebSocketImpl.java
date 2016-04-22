package org.fortiss.smg.websocket.impl;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.fortiss.smg.actuatormaster.api.IActuatorListener;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.fortiss.smg.usermanager.api.User;
import org.fortiss.smg.usermanager.api.UserManagerInterface;
import org.fortiss.smg.websocket.api.WebSocketInterface;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.api.shared.communication.ResponseType;
import org.fortiss.smg.websocket.api.shared.communication.Status;
import org.fortiss.smg.websocket.impl.communication.Dispatcher;
import org.fortiss.smg.websocket.impl.communication.EchoBroadcastWebSocket;
import org.fortiss.smg.websocket.impl.communication.SocketServer;
import org.fortiss.smg.websocket.impl.event.SubscriptionManager;
import org.fortiss.smg.websocket.impl.utils.ContainerManagerHelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;

/**
 * Handles subscription, push-server data, control of device
 * 
 * @author Chrysa Papadaki - papadaki.chr@gmail.com
 * 
 */
public class WebSocketImpl extends SubscriptionManager implements
		WebSocketInterface, IActuatorListener {


    @Override
    public Response registerUser(User user) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public Response<String> authenticateUser(String username, String password) {
		Response<String> r = new Response<String>("UNSUPPORTED_REQUEST_TYPE",
				Status.UNSUPPORTED_REQUEST_TYPE, ResponseType.UNSUPPORTED);
		try {
            if (!userMgr.validLogin(username, password)) {
            	r = new Response<String>("LOGGED_IN", Status.WRONG_LOGIN_DATA,
            			ResponseType.LOGIN);
            	return r;
            } else {
            	r = new Response<String>("LOGGED_IN", Status.LOGGED_IN);
            	r.setResponsetype(ResponseType.LOGIN);
            	return r;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return r;
        }
	}

	@Override
    public Response<String> authenticateUser(String macaddress) {
        Response<String> r = new Response<String>("UNSUPPORTED_REQUEST_TYPE",
                Status.UNSUPPORTED_REQUEST_TYPE, ResponseType.UNSUPPORTED);
        try {
            if (userMgr.getUserByName(userMgr.loginByDevice(macaddress))==null) {
                r = new Response<String>("LOGGED_IN", Status.WRONG_LOGIN_DATA,
                        ResponseType.LOGIN);
                return r;
            } else {
                r = new Response<String>("LOGGED_IN", Status.LOGGED_IN);
                r.setResponsetype(ResponseType.LOGIN);
                return r;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return r;
        }
    }

    /**********************************************************************************
	 ** Implementing IActuatorListener to get incoming messages from subscribed
	 * devices*
	 **********************************************************************************/

	@Override
	public void onDoubleEventReceived(DoubleEvent ev, DeviceId dev,
			String client) {
		Dispatcher.getInstance().sendEvent(ev, dev, client);
		log.info("onDoubleEventReceived");
	}

	
   // @Override
    public void onDeviceEventReceived(DeviceContainer ev, String client) {
        // TODO Auto-generated method stub
        log.info("onDeviceEventReceived");
    }

	/********************************************************************
	 * Implementing functionality to control devices* uses UserManager  *
	 *******************************************************************/
	@Override
	public Response attachDevice(String containerId, String userName) {
		try {
		      User user = userMgr.getUserByName(userName);
       //     userMgr.attachDevicetoUser(user.getId(), Long.parseLong(containerId), "macaddress");
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return null;
	}
	
	@Override
	public Response detachDevice(String containerId, String userId) {
		return null;
	}
	//TODO for ios praktikum
	@Override
	public Response getAssignedRooms(String userId) {
		return null;
	}
	
	//TODO for ios praktikum
	@Override
	public <T> Response sendCommand(String id, SIDeviceType type, double command) {
		if (type == null || id.length() < 1) {
			Response r = new Response<String>("WRONG_ID", Status.WRONG_INPUT);
			return r;
		}
		// TODO: Verification of the Implementation  
		//
		DeviceContainer devCon = null;
		try {
			devCon = (DeviceContainer) ContainerManagerHelper.getInstance().getContainerMgr().getContainer(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DeviceId devID = null;
		if (devCon != null) {
			devID = devCon.getDeviceId();
		}
		if (devID == null) {
			Response r = new Response<String>("ERROR", Status.CONTENT_ERROR);
			r.setResponsetype(ResponseType.SEND_COMMAND);
			return r;
		}
		else {
			DoubleCommand comm = new DoubleCommand(command);
			try {
				ContainerManagerHelper.getInstance().getContainerMgr().sendCommand(comm, devID);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Response r = new Response<String>("ERROR", Status.CONTENT_ERROR);
				r.setResponsetype(ResponseType.SEND_COMMAND);
				return r;
			}
			Response r = new Response<String>("OK", Status.ROOM_UPDATE);
			r.setResponsetype(ResponseType.SEND_COMMAND);
			return r;
		}
		
		
	}

	/*****************************************************************************/     

	public WebSocketInterface getInstance(
			ConcurrentLinkedQueue<EchoBroadcastWebSocket> _broadcast) {
		if (instance == null) {
			instance = new WebSocketImpl(_broadcast);
		}
		return instance;
	}


	public String doSomething(String s) {
		return "Hello smg";
	}

	@Override
	public boolean isComponentAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Sets containerManager instance to be used by this component
	 * @param containerMgr
	 */
	private void initContainerManager(ContainerManagerInterface containerMgr) {
		ContainerManagerHelper.getInstance().setContainerMgr(containerMgr);
	}

    private static final Logger log = Logger.getLogger(WebSocketImpl.class
            .getName());
    private UserManagerInterface userMgr;
    private static WebSocketImpl instance;
    private ConcurrentLinkedQueue<EchoBroadcastWebSocket> _broadcast;
    private SocketServer socketServer;


    private WebSocketImpl(
            ConcurrentLinkedQueue<EchoBroadcastWebSocket> _broadcast) {
        this._broadcast = _broadcast;
        Dispatcher.getInstance().setBroadcast(_broadcast);
        Dispatcher.getInstance().setSubscribedIDs(subscribedIDs);
        subscribedIDs = Multimaps.synchronizedMultimap(HashMultimap
                .<String, String> create());
        
    }
    /**
     * Instantiates {@link WebSocketImpl} object
     * 
     * @param containerMgr
     * @param userMgr
     * @param concurrentLinkedQueue
     * @param socketEventHandler
     */
    public WebSocketImpl(ContainerManagerInterface containerMgr,
            UserManagerInterface userMgr,
            ConcurrentLinkedQueue<EchoBroadcastWebSocket> concurrentLinkedQueue) {
        this.userMgr = userMgr;
        Dispatcher.getInstance().setBroadcast(_broadcast);
        initContainerManager(containerMgr);
        log.info("WebSocketImpl obj has been instantiated");
    }

    public WebSocketImpl() {
        subscribedIDs = Multimaps.synchronizedMultimap(HashMultimap
                .<String, String> create());
        Dispatcher.getInstance().setSubscribedIDs(subscribedIDs);
    }
    public WebSocketImpl getInstance() {
        if (instance == null) {
            instance = new WebSocketImpl();
        }
        return instance;
    }
   
    public void initSocketServer() {
        Runnable activateJMS = new Runnable() {
            @Override
            public void run() {
                try {
                    startServer(7070);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    // Log.warn(e);
                }
            }
        };
        activateJMS.run();
    }

    /**
     * starts websocket server
     * 
     * @param port
     * @return socket server for testing purposes
     * @throws Exception
     */
    public SocketServer startServer(int port) throws Exception {
        
        boolean verbose = false;
        String docroot = "frontendapi";

        socketServer = new SocketServer(port);
        socketServer.setVerbose(verbose);
        socketServer.setResourceBase(docroot);
        socketServer.start();
        

        return socketServer;

    }

	@Override
	public void onDeviceEventReceived(DeviceEvent ev, String client) {
		// TODO Auto-generated method stub
		
	}

}
