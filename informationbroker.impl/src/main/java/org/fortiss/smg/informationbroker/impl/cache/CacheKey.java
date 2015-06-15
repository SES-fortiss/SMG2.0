/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.informationbroker.impl.cache;


public class CacheKey {
		private String devId; 
		private String wrapperId;
		
		public CacheKey() {
			
		}
		

		public CacheKey(String devId, String wrapperId) {
			//super();
			this.devId = devId;
			this.wrapperId = wrapperId;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((devId == null) ? 0 : devId.hashCode());
			result = prime * result
					+ ((wrapperId == null) ? 0 : wrapperId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheKey other = (CacheKey) obj;
			if (devId == null) {
				if (other.devId != null)
					return false;
			} else if (!devId.equals(other.devId))
				return false;
			if (wrapperId == null) {
				if (other.wrapperId != null)
					return false;
			} else if (!wrapperId.equals(other.wrapperId))
				return false;
			return true;
		}

		public String getDevId() {
			return devId;
		}

		public String getWrapperId() {
			return wrapperId;
		}


		@Override
		public String toString() {
			return "CacheKey [devId=" + devId + ", wrapperId=" + wrapperId
					+ "]";
		}
	
		
}
