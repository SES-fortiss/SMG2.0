package org.fortiss.smg.informationbroker.impl.cache;

import org.slf4j.Logger;

import net.sf.ehcache.CacheManager;


public class LocalCacheManager {
		CacheManager cacheManager;
		public DoubleCache doubleCache;
		
		public LocalCacheManager() {
			//cacheManager = CacheManager.create();
			cacheManager = CacheManager.newInstance();
		}
		
		public CacheManager getCacheManager() {
			return cacheManager;
		}
	
		
		public void startDoubleCache(String name, CacheManager cacheManager, Logger logger) {
			doubleCache = new DoubleCache(name);
			cacheManager.addCache(doubleCache.getCache());
		}
		
}
