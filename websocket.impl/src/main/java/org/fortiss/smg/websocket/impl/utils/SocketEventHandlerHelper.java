package org.fortiss.smg.websocket.impl.utils;

import java.util.Iterator;
import java.util.List;

import org.fortiss.smg.usermanager.api.Tuple;

public class SocketEventHandlerHelper {


	public static String getProperty(List<Tuple> properties, String propID) {
		String property = null;
		if (properties != null) {
			for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
				Tuple tuple = (Tuple) iterator.next();
				if (tuple.getTupleKey().equals(propID)) {
					property = tuple.getValue();
				}
			}
		}
		return property;
	}
/*TODO
	public static WrapperInterface getProxyForWrapper(String targetQueue) {
		return JMSHelpers.getProxy("jms:queue:" + targetQueue,
				WrapperInterface.class, ComponentAuthentication.get()
						.getDummySession());
	}*/

	/**
	 * First part of a deviceID
	 * 
	 * @param completeDevName
	 * @return
	 */
	public static String getQueue(String completeDevName) {
		if (completeDevName == null) {
			return null;
		} else {
			return completeDevName.split("\\?")[0];
		}
	}

	/**
	 * Second part of a deviceID
	 * 
	 * @param completeDevName
	 * @return
	 */
	public static String getDev(String completeDevName) {
		if (completeDevName == null) {
			return null;
		} else {
			String[] split = completeDevName.split("\\?");
			if (split.length > 1) {
				return split[1];
			} else {
				return null;
			}
		}
	}

}
