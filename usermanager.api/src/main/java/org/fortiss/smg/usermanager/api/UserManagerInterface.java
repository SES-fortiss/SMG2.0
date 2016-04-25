package org.fortiss.smg.usermanager.api;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.HealthCheck;

public interface UserManagerInterface extends HealthCheck {

	/**
	 * Create new user 
	 * 
	 * @param user
	 *            User type , it contains fields username, RolId, NickName , Status of current logged in , ...
	 * @param password
	 * 			  String user's password that is hashed before written in database
	 * @return boolean that whether the inserting new user was successful or not
	 */
	boolean createSimpleUser(String login, String password) throws Exception ;

	
	/**
	 * Create new user 
	 * 
	 * @param user
	 *            User type , it contains fields username, RolId, NickName , Status of current logged in , ...
	 * @param password
	 * 			  String user's password that is hashed before written in database
	 * @return boolean that whether the inserting new user was successful or not
	 */
	boolean createUser(User user, String password) throws Exception ;

	
	/**
	 * Modify existing user 
	 * 
	 * @param user
	 *            User type , it contains fields username, RolId,NickName , status of current logged in , ...
	 * @param password
	 * 			  String user's password that is hashed before written in database
	 * @return boolean that whether the updating  user was successful or not
	 */
	boolean modifyUser(User user,String password)throws Exception;

	
	/**
	 * Modify user's password
	 * 
	 * @param username
	 *            String the username that user used to log in
	 *
	 *@param password
	 *            String the password that user used to log in , that is hashed before being searched in DB
	 *            
	 *@param newPassword
	 *            String the new password that user will use to log in , that is hashed before being inserted to DB
     *     
	 * @return boolean that whether the update was successful or not
	 */
	boolean modifyPassword(String username, String password,String newPassword)throws Exception;
	
	/**
	 * Delete Existing User
	 * 
	 * @param username
	 *            String the username that user used to log in
	 *
	 * @return boolean that whether the deletion was successful or not
	 */
	boolean deleteUser(String userName,String password )throws Exception;


	/**
	 * Add User Contacts information to existing user
	 * 
	 * @param user
	 *            User type , it contains fields username, email ,phone, primaryContactInfo , ....
	 *
	 * @return boolean that whether the inserting the contact information of user was successful or not
	 */
	boolean createUserContacts(User user) throws Exception ;

	
	/**
	 * Modify existing User Contacts information 
	 * 
	 * @param user
	 *            User type , it contains fields username, email ,phone, primaryContactInfo , ....
	 *
	 * @return boolean that whether the update was successful or not
	 */
	boolean modifyUserContacs(User user,String password)throws Exception;
	
	
	/**
	 * Get Valid User
	 * 
	 * @param username
	 *            String the username that user used to log in
	 *
	 *@param password
	 *            String the password that user used to log in
	 *
	 * @return User check whether the user is valid , if yes then return the whole user information
	 */
	User getValidUser(String username, String password) throws Exception ;
	

	/**
	 * Get User
	 * 
	 * @param username
	 *            String the username that user used to log in
	 *
	 * @return User the whole user information
	 */
	User getUserByName(String username) throws Exception;
	
	
	/**
	 * Get User
	 * 
	 * @param userId
	 *            Long user Id
	 *
	 * @return User the whole user information
	 */
	User getUser(long userId)throws Exception;
	
	
	/**
	 * Valid Login
	 * 
	 * @param username
	 *            String the username that user used to log in
	 *
	 *@param password
	 *            String the password that user used to log in
	 *
	 * @return boolean check whether the login is valid or not
	 */
	boolean validLogin(String username, String password) throws Exception ;
	
	
	/**
	 * Valid Login
	 * 
	 * @param username
	 *            String the username that user used to log in
	 *
	 *@param password
	 *            String the hashed password that user used to log in
	 *
	 * @return boolean check whether the login is valid or not
	 */
	boolean validLoginHash(String String, Object passwordHash)throws Exception ;


	/**
	 * Login by device
	 * 
	 * @param macAddress
	 *            String macAddress of Device
	 *
	 * @return String usrname of the user that assigned to this device
	 */
	String loginByDevice(String macAddress)throws Exception;
	
	
	/**
	 * Attach Device to User
	 * 
	 * @param userId
	 *            Long user Id
	 *            
	 * @param deviceId
	 *          Long  Id of Device that will be assigned to the user 
	 *                       
	 * @param macAddress
	 *           String  mac address of device
	 *            
	 * @param primaryDevices
	 *            boolean if this device is the primary device of the user as several devices can assign to a user          
	 *            
	 * @return boolean  whether attaching the device to user is inserted in DB successfully or not
	 */
	boolean attachDevicetoUser(Long userId,Long deviceId,String macAddress,boolean primaryDevice, String deviceName,String os)throws Exception;
	
	
	/**
	 * Detach Device to User
	 * 
	 * @param userId
	 *            Long user Id
	 *            
	 * @param deviceId
	 *            Long Id of Device that will be detached from the user 
	 *            
	 * @return boolean  whether detaching the device from user by deleting it form DB successfully done or not
	 */
	boolean detachDevicefromUser(Long userId,Long deviceId)throws Exception;
	
	
	/**
	 * Get Device Owner
	 *            
	 * @param deviceId
	 *            Long Id of Device that belongs to the user          
	 *            
	 * @return Long  userId of the owner of Device
	 */
	long getDeviceOwner(Long deviceId)throws Exception;
	
	
	/**
	 * Set Login Status of User
	 * 
	 * @param userId
	 *            Long user Id
	 * @param loggedIn
	 *            boolean if user is logged in or not           
	 *
	 * @return boolean  whether the new status is updated in DB successfully or not
	 */	
	boolean setLoginStatus(Long userId,boolean loggedIn)throws Exception;
	
	
	/**
	 * Login Status
	 * 
	 * @param username
	 *            String macAddress of Device
	 *
	 * @return boolean false if this user is not logged in otherwise true.
	 */
	boolean loginStatus(String username)throws Exception;
	
	
	/**
	 * Valid User To Create Rule
	 * 
	 * @param username
	 *            String userName
	 *            
	 * @param containerId
	 *            String containerId
	 *
	 * @return boolean true if this user is valid otherwise false.
	 */
	boolean validUserToCreateRule(String username,String containerId)throws Exception;
	
	
	/**
	 * Assign User to Room
	 * 
	 * @param userId
	 *            Long user Id
	 * @param containerId
	 *            Long container Id of the room that will assign to the user        
	 *
	 * @return boolean  true if the room is assigned to user with guest role successfully otherwise false.
	 */
	boolean assignUsertoRoom(Long userId,String containerId)throws Exception;
	
	
	/**
	 * Assign User to Room with Specific Role
	 * 
	 * @param userId
	 *            Long user Id
	 * @param containerId
	 *            Long container Id of the room that will          
	 *
	 * @param roleName
	 *            String role name that user will have in this specific room 
	 *
	 * @return boolean  true if the room is assigned to user with specific role successfully otherwise false.
	 */
	boolean assignUsertoRoom(Long userId, String containerId, String roleName)throws Exception;
	
	
	public List<String> getAssignedRooms(Long userId) throws Exception;
	
	/**
	 * Dissociate User from Room 
	 * 
	 * @param userId
	 *            Long user Id
	 * @param containerId
	 *            String container Id of the room that will  have in this specific room 
	 *
	 * @return boolean  true if the room is dissociated form user successfully otherwise false.
	 */
	boolean dissociateUserfromRoom(Long userId,String containerId)throws Exception;
	
	
	/**
	 * Assign User to Role 
	 * 
	 * @param userId
	 *            Long user Id
	 * @param roleId
	 *            Long role Id           
	 *
	 * @return boolean  true if the role is assigned to user and inserted to DB successfully otherwise false.
	 */
	boolean assignRoletoUser(Long userId,Long roleId)throws Exception;
	
	
	/**
	 * Get User Role in Specific Room 
	 * 
	 * @param userId
	 *            Long user Id
	 * @param containerId
	 *            Long container Id           
	 *
	 * @return String  RoleName of the user in specific room
	 */
	String getUserRole(Long userId,String containerId)throws Exception;
	
	
	/**
	 * Create new User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 *            
	 * @param items
	 *            List<String> list of name of items 
	 *                       
	 * @param itemsValue
	 *            List<Double> list of value of items
	 *            
	 * @param profileName
	 *            String the name of profile that these items belongs to it          
	 *            
	 * @return boolean  whether inserting new profile to DB is successful or not
	 */
	boolean addNewUserProfile(Long userId,List<String>items,List<Double>itemsValue,String profileName)throws Exception;
	
	
	/**
	 * Create new User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 *            
	 *            
	 * @param items
	 *           HashMap<String,Double> hash map of items and values of them
	 *                       
	 *                       
	 * @param profileName
	 *            String the name of profile that these items belongs to it          
	 *            
	 * @return boolean  whether inserting new profile to DB is successful or not
	 */
	boolean addNewUserProfile(Long userId, HashMap<Integer,Double> items,String profileName)throws Exception;
	
	
	/**
	 * Modify User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 *            
	 * @param items
	 *            List<String> list of name of items 
	 *                       
	 * @param itemsValue
	 *            List<Double> list of value of items
	 *            
	 * @param profileName
	 *            String the name of profile that these items belongs to it          
	 *            
	 * @return boolean  whether updating the profile in DB is successful or not
	 */
	boolean modifyUserProfile(Long userId,List<String>items,List<Double>itemsValue,String profileName)throws Exception;
	
	
	/**
	 * Modify User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 *            
	 *            
	 * @param items
	 *           HashMap<String,Double> hash map of items and values of them
	 *                       
	 *                       
	 * @param profileName
	 *            String the name of profile that these items belongs to it          
	 *            
	 * @return boolean  whether updating the profile in DB is successful or not
	 */
	boolean modifyUserProfile(Long userId,HashMap<Integer,Double> items,String profileName) throws Exception;
	
	
	/**
	 * Delete User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 *                                   
	 *                       
	 * @param profileName
	 *            String the name of profile that these items belongs to it          
	 *            
	 * @return boolean  whether deleting the profile from DB is successful or not
	 */
	boolean deleteUserProfile(Long userId,String profileName) throws Exception;
	
	
	/**
	 * Get possible profile items
	 *            
	 * @return List<String>  List of profile Items that user able to chose and modify their value
	 */
	List<String> getPossibleProfileItems()throws Exception;
	
	
	/**
	 * Activate User Profile
	 * 
	 * @param userId
	 *            Long user Id
	 *                         
	 *                       
	 * @param profileName
	 *            String the name of profile that these items belongs to it          
	 *            
	 * @return boolean  whether updating the profile in DB is successful or not
	 */
	boolean activateUserProfile(Long userId,String profileName)throws Exception;
	


	/**
	 * Get User Active Profile Detail
	 * 
	 * @param userID
	 * 			Long user ID
	 *        
	 *            
	 * @return HashMap<String,Double> Hash map of  active profile items  to value
	 */
	HashMap<String,Double> getUserActiveProfileDetail(Long userId) throws Exception;
	
	
	/**
	 * Hash Map Device Id to Value
	 * 
	 * @param devicesTypeToValue
	 *           HashMap<String,Double> Hash map of device type to device value
	 *                                      
	 * @param deviceTypeToDeviceID
	 *            HashMap<String,Integer> Hash map of device type to device code          
	 *            
	 * @return HashMap<Integer,Double> Hash map of device ID to their value
	 */
	HashMap<Integer,Double> hashMapDeviceIdToValue(HashMap<String,Double> devicesTypeToValue, HashMap<String,Integer> deviceTypeToDeviceID);
	
	
	/**
	 * Hash Map Device Type to Value
	 * 
	 * @param devicesType
	 *           List<String> SMG Device Types
	 *                                      
	 * @param devicesValue
	 *            List<Double> Values of Device Types          
	 *            
	 * @return HashMap<String,Double> Hash map of device type to their value
	 */
	HashMap<String, Double> hashMapDeviceTypeToValue(List<String> devicesType, List<Double> devicesValue);
	
	
	/**
	 * HashMap Device type to google device Id
	 *            
	 * @return HashMap<String,Integer> hash map of SMG Device Type to Device code(Google device Id)
	 */
	HashMap<String,Integer> hashMapDeviceTypeToID()throws Exception;
	
	
	/**
	 * Get Id Of DeviceType
	 * 
	 * @param deviceTypeToDeviceID
	 *           HashMap<String,Integer> Hash map of device type to device code
	 *                                      
	 * @param deviceType
	 *            String SMG device type          
	 *            
	 * @return Integer  Device Id of given device type
	 */
	int getIdOfDeviceType(HashMap<String,Integer> deviceTypeToDeviceID, String deviceType);
	
	
	/**
	 * get User All Profile Names
	 * 
	 * 
	 * @param userID
	 * 			Long user ID
	 *        
	 *            
	 * @return List<String> name of all the profiles that the given user have
	 */
	List<String> getUserAllProfileNames(Long userId) throws Exception;
	
	
	/**
	 * Get User Specific Profile Details
	 * 
	 * @param userID
	 * 			Long user ID
	 * @param ProfileName
	 * 			String profileName        
	 *            
	 * @return HashMap<String,Double> Hash map of given profile items  to value
	 */
	HashMap<String,Double> getProfile(String profileName, long userId) throws Exception;    
}
