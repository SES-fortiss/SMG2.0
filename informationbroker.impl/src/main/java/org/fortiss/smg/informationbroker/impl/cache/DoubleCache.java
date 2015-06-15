/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.informationbroker.impl.cache;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleCache {
	
	private static final Logger logger = LoggerFactory.getLogger(DoubleCache.class);
	Cache doubleCache;
    String cacheName;
    

	public DoubleCache(String cacheName) {
		/*
		 * Configuration of the Cache
		 */
		this.cacheName = cacheName;
		int ObjectsInCache = 1000;
		int TTL = 60;  // Time until Object expires in Cache - when not requested
		int TTI = 20; // Time until Object expires in Cache - since last request
	
		logger.trace("DoubleCache: Configuring DoubleCache with "
				   + "maximum size: " + ObjectsInCache + " objects"
				   + " expiration time: " +TTL+ " s expiration time since last access: " + TTI 
				   + " s general Policy: 'Last Frequently Used' "
					);
		
		doubleCache = new Cache(cacheName, ObjectsInCache, false, false, TTL, TTI);
		
	}
	
	
	public DoublePoint load(CacheKey key) {
			
	 	if (doubleCache.get(key) != null) {
	 		logger.info("load: Element requested from DoubleCache" + key);
	 		DoublePoint dp = (DoublePoint) doubleCache.get(key).getObjectValue();
	 		CacheKey k = (CacheKey) doubleCache.get(key).getObjectKey();
	 		
	 		return dp;
	 		/*
	 		if (k.getUnit().equals(dp.getUnit())) {
	 			logger.debug("load: Element found in DoubleCache: " + key.getUnit()+"-"+ key.getId() + " : " + dp.getValue());
	 			return dp;
	 		}
	 		
	 		else {
	 			logger.error("load: Element requested from DoubleCache has different units in key and value: " +  key.getUnit());
	 			doubleCache.remove(key);
	 			return null;
	 		}
	 		*/
    	}
    	else {
    		logger.info("load: Element not found in DoubleCache :" + key + " - requesting from DB");
    	   	return null;
    	}
    }
	
	
	/*
	public List<CacheKey> getKeysbyID(String id) {
		logger.info("getKeysbyID: requesting all keys (origin(aka:deviceID)-unit combinations) for deviceID: " + id);
		List<CacheKey> keys = new ArrayList<CacheKey>();
		for (SIUnitType type : SIUnitType.values()) {
			CacheKey key = new CacheKey(type, id);
			if (doubleCache.isKeyInCache(key)) {
				keys.add(key);
			}
		}
		logger.debug("getKeysbyID: found " + keys.size() + " keys for deviceID: " + id);
		return keys;
	}
	*/
	
	public void store(CacheKey key, DoublePoint doubleDataPoint) {
		logger.info("store: Element stored in DoubleCache: " + key+ " -> " + doubleDataPoint.getValue());
		Element element = new Element(key, doubleDataPoint);
		doubleCache.put(element);
	}
	
	
	public Cache getCache() {
		if (doubleCache != null) {
			return doubleCache;
		}
		else {
			return new DoubleCache("DoubleDefault").doubleCache;
		}
	}
	

	public boolean isKeyInCache(CacheKey key) {
		return doubleCache.isKeyInCache(key);
	}
		

	
}
