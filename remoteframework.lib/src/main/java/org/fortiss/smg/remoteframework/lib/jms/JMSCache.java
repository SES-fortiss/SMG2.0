package org.fortiss.smg.remoteframework.lib.jms;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JMSCache {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(JMSCache.class);
	
	private Map<String, JMSCacheObject> cache;

	public JMSCache () {
		cache = new HashMap<String, JMSCacheObject>();
	}

	/**
	 * @param id
	 * @param object
	 * @return Boolean indicating whether the ID has already been present before caching the new object.
	 */
	public boolean put(String id, Object object) {
		boolean b = cache.containsKey(id);
		logger.debug("Adding new Object to Cache");
		cache.put(id, new JMSCacheObject(object));
		return b;
	}
	
	/**
	 * Retrieves Proxy Objects from the Cache
	 * @param id
	 * @return The Proxy Object or null if no longer valid/not present
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String id, Class<T> interfce) {
		JMSCacheObject cached = cache.get(id);
		if (cached != null) {
			T cachedProxy = (T) cached.getProxy(); //This checks for invalidation after a timeframe and returns null if so!
			if (cachedProxy != null) {
				logger.debug("Cached Proxy exists - returning cached Proxy");
				return cachedProxy;				
			} else {
				logger.debug("Cached Proxy invalidated");
				cache.remove(id);
			}
		}
		return null;
	}
}
