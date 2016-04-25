package org.fortiss.smg.usermanager.api;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.ambulance.api.HealthCheck;


public interface KeyManagerInterface extends HealthCheck{



	/**
	 * Create the signature of a public key and request
	 * 
	 * @param secret_key
	 * @param request
	 * @return response String
	 */
    public String calcSignature( String accessKey,
            String request) throws TimeoutException ;

	/**
	 * Checks whether the signature of a public key is valid or not. Modifies
	 * lastSeen -> now
	 * 
	 * @param access_key
	 * @param request
	 * @param signature
	 * @return
	 */
    public boolean checkSignature(
            String accessKey,
            String request,  String signature) throws TimeoutException ;


	/**
	 * generates public + private Key
	 * 
	 * @param userid
	 * 
	 * @param DeviceId
	 * 
	 * @return tuple of the generate pair
	 */
    public Tuple generateKeys(int userId, int deviceID) throws TimeoutException ;

    
    /**
	 * Gives a list of all Keys
	 * 
	 * @param userid
	 * 
	 * 
	 * @return key
	 */
    public Key getKey(String accessKey) throws TimeoutException ;
    
	
	/**
	 * Update OS of device with given access_key
	 * 
	 * @param access_key 
	 * 			String it is known as public key
	 * 
	 * @return boolean true if update in DB is successful
	 */
    public boolean setOS(String accessKey,  String setOS) throws TimeoutException ;
    
    
    /**
	 * Update Device Id of device with given access_key
	 * 
	 * @param access_key 
	 * 			String it is known as public key
	 * 
	 * @param setDevId 
	 * 			
	 * 
	 * @return boolean true if update in DB is successful
	 */
    public boolean setDevId(String accessKey, int setDevId) throws TimeoutException ;


    public List<String> getKeys(int userId) throws TimeoutException ;


    public User getUser(String accessKey) throws TimeoutException ;


    /**
	 * Destroys all keys in the database of a user. He will have to start from
	 * scratch.
	 * 
	 * @param userid
	 * 
	 * @return boolean true if deleting form DB is successful
	 */
    public boolean removeAllKeys( int userId) throws TimeoutException ;

    
    /**
	 * Removes a Key from the Manager This will sign the (mobile) application
	 * linked to it.
	 * 
	 * @param access_key
	 * 
	 * @return boolean true if deleting form DB is successful
	 */
    public boolean removeKey( String accessKey) throws TimeoutException ;
}