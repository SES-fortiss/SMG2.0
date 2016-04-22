package org.fortiss.smg.websocket.impl.event;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerType;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.api.shared.communication.ResponseType;
import org.fortiss.smg.websocket.api.shared.communication.Status;
import org.fortiss.smg.websocket.impl.utils.ContainerManagerHelper;

import com.google.common.collect.Multimap;

/**
 * Implements subscription functionality of {@link org.fortiss.smg.websocket.api.WebSocketInterface}
 * 
 * @author Chrysa Papadaki - papadaki.chr@gmail.com
 */
public abstract class SubscriptionManager {

    private static final Logger log = Logger
            .getLogger(SubscriptionManager.class.getName());
    /**
     * For the moment is stored in here but in the future will be moved to UserManager. i.e. usermanager will be responsible for
     * handling subscribed devices and websocket/rest api will invoke respective methods.
     * <deviceId, sessionId>
     */
    protected Multimap<String, String> subscribedIDs;

    public void setSubscribedIDs(Multimap<String, String> subscribedIDs) {
        this.subscribedIDs = subscribedIDs;
    }

    public Multimap<String, String> getSubscribedIDs() {
        return subscribedIDs;
    }

    /*
     * 
     *{@link org.fortiss.smg.websocket.api.WebSocketInterface#subscribe(java.lang.String, java.lang.String)}
     */
    public <T> Response<T> subscribe(String deviceId, String sessionId) {
        SubscriptionManager.log.log(Level.INFO, "Subscribed - key:" + deviceId
                + " value:" + sessionId);
        Container device = null;
        try {
            // gets container of device
            device = ContainerManagerHelper.getInstance()
                    .getContainer(deviceId);
            SubscriptionManager.log.log(Level.INFO, device.getContainerId()
                    + "=id, " + device.getHrName());
        } catch (Exception e) {
            e.printStackTrace();
            device = null;
        }

        if (device == null
                || !device.getContainerType().equals(ContainerType.DEVICE)) {
            Response<String> r = new Response<String>("DEVICE_NOT_FOUND",
                    Status.CONTENT_ERROR);
            r.setResponsetype(ResponseType.DEVICE_NOT_FOUND);
            return (Response<T>) r;
        } else {
            // store container of type ContainerType.DEVICE
            synchronized (subscribedIDs) {
                subscribedIDs.put(deviceId, sessionId);
                System.out.println("key = " + deviceId);
            }
            Response<Container> r = new Response<Container>(device, Status.OK);
            r.setResponsetype(ResponseType.SUBSCRIBE_DEVICE);
            return (Response<T>) r;
        }
    }

    /**
     * Subscribes to container and all its children. It adds to subscibeIds
     * structure the parent container key.
     *  
     *{@link org.fortiss.smg.websocket.api.WebSocketInterface#subscribeAll(java.lang.String, java.lang.String)}
     */
    public Response subscribeAll(String containerId, String sessionId) {
        SubscriptionManager.log.log(Level.FINE, "Subscribed ALL - key:"
                + containerId + " value:" + sessionId);
        List<Container> containers = null;
        try {
            containers = ContainerManagerHelper.getInstance().getContainerMgr()
                    .getRoomMap(containerId).getFirst();

        } catch (Exception e) {
            SubscriptionManager.log
                    .info("getContainers in Containermgr with key:"
                            + containerId + " and valu:e" + sessionId
                            + " has failed. ");
        }

        if (containers == null) {
            Response<String> r = new Response<String>("DEVICE_NOT_FOUND",
                    Status.CONTENT_ERROR);
            r.setResponsetype(ResponseType.DEVICE_NOT_FOUND);
            return r;
        } else {
            // subscribes to container and all its children
            // TODO think of a better solution of storing and retrieving
            // containers
            subscribeAllDevices(sessionId, containers, containerId);

            final Response<List<Container>> r = new Response<List<Container>>(
                    containers, Status.OK);
            r.setResponsetype(ResponseType.SUBSCRIBE_ROOM);
            return r;
        }
    }

    private void subscribeAllDevices(String sessionId,
            List<Container> containers, String containerId) {
        synchronized (subscribedIDs) {
            subscribedIDs.put(containerId, sessionId);
        }
        if (containers.size() > 0) {
            for (Container subContainer : containers) {
                subscribeAllDevices(sessionId, subContainer.getChildren(),
                        containers.get(0).getParent().getContainerId());
            }
        }
    }

    /*
     * documentation at {@link org.fortiss.smg.websocket.api.WebSocketInterface#unsubscribe(java.lang.String, java.lang.String)}
     */
    public Response unsubscribe(String deviceid, String sessionID) {
        boolean success = false;
        synchronized (subscribedIDs) {
            success = subscribedIDs.remove(deviceid, sessionID);
        }
        if (success) {
            return new Response<String>("OK", Status.OK,
                    ResponseType.UNSUBSCRIBE);
        } else {
            return new Response<String>("CONTENT_ERROR", Status.CONTENT_ERROR,
                    ResponseType.UNSUBSCRIBE);
        }
    }

    public Response unsubscribeAll(String sessionID) {
        Collection<String> assignedDevs = getSessionDevices(sessionID);
        boolean success = false;

        if (assignedDevs != null) {
            synchronized (subscribedIDs) {
                for (String deviceId : assignedDevs) {
                    success = subscribedIDs.remove(deviceId,
                            sessionID) || success;
                }
            }
        }
        if (success) {
            return new Response<String>("OK", Status.OK,
                    ResponseType.UNSUBSCRIBE);
        } else {
            return new Response<String>("CONTENT_ERROR", Status.CONTENT_ERROR,
                    ResponseType.UNSUBSCRIBE);
        }
    }

    private Collection<String> getSessionDevices(String sessionID) {
        // TODO Auto-generated method stub
        return subscribedIDs.get(sessionID);
    }

    public void closeSession(String sessionID) {
        synchronized (subscribedIDs) { // Synchronizing on m, not s!
            Collection<String> sessionIDs = subscribedIDs.keySet();
            Iterator<String> i = sessionIDs.iterator();
            while (i.hasNext()) {
                String deviceID = i.next();
                if (subscribedIDs.containsEntry(deviceID, sessionID)) {
                    subscribedIDs.remove(deviceID, sessionID);
                }
            }
        }
    }
}
