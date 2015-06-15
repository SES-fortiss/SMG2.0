/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
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
