package org.fortiss.smg.websocket.api;

import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.containermanager.api.devices.ContainerType;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.usermanager.api.User;
import org.fortiss.smg.websocket.api.shared.communication.Response;

public interface WebSocketInterface extends HealthCheck {
    String doSomething(String arg) throws TimeoutException;

    /**
     * Unsubscribe from a specified node user is able to get and view data (not
     * control)
     * 
     * @param deviceid
     * @param sessionID
     * @return STATUS_OK or CONTENT_ERROR response
     */
    public Response unsubscribe(String deviceid, String sessionID);

    /**
     * Unsubscribe from all nodes user is able to get and view data (not
     * control)
     * 
     * @param deviceid
     * @param sessionID
     * @return STATUS_OK or CONTENT_ERROR response
     */
    public Response unsubscribeAll(String sessionID);

    /**
     * Subscribes to a specified by the user device. Container can be only of DEVICE type. check
     * {@link ContainerType}
     * 
     * @param containerId is a deviceId
     * @param sessionId
     * @return device_NOT_FOUND response if containerid not found otherwise
     *         otherwise OK and container data
     */
    public <T> Response<T> subscribe(String containerId, String sessionId);

    /**
     * Subscribes to a specified by the user container Container might be of
     * {@link ContainerType} type. case room || building || floor : user
     * subscribes to all children
     * 
     * @param containerId is the roomId
     * @param sessionId
     * @return response with list of containers or not found msg
     */
    public Response subscribeAll(String containerId, String sessionId);

    /**
     * 
     * controls devices - updates device data and sends ACK response to client
     * 
     * @param deviceId
     * @param siDeviceType
     * @param d
     * @param unit
     * @return
     */
    public <T> Response sendCommand(String deviceId, SIDeviceType siDeviceType,
            double d);

    /**
     * TODO implement
     * Assign user to a device to be able to control it
     * @param userId
     * @return
     */
    public Response attachDevice(String deviceId, String userId);

    /**
     * TODO implement
     * Remove rights of user to control device
     * @param userId
     * @return
     */
    public Response detachDevice(String deviceId, String userId);

    /**
     * TODO implement Returns to user a response with a list of devices user is
     * able to control
     * 
     * @param userId
     * @return list<container>
     */
    public Response getAssignedRooms(String userId);

    /**
     * Authenticate user using credentials
     * 
     * @param username
     * @param password
     * @return Status.LOGGED_IN for successful login otherwise Status.WRONG_LOGIN_DATA
     */
    public Response<String> authenticateUser(String username, String password);

    /**
     * Authenticate user using macaddress
     * 
     * @param macaddress
     * @return Status.LOGGED_IN for successful login otherwise Status.WRONG_LOGIN_DATA
     */
    public Response<String> authenticateUser(String macaddress);

    /**
     * Creates new user
     * 
     * @param user
     * @return
     */
    public Response registerUser(User user);

    /**
     * Ends user session
     * 
     * @param sessionID
     */
    public void closeSession(String sessionID);

}
