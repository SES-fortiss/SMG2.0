/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.siemens.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.xml.ws.WebServiceException;

import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.slf4j.LoggerFactory;

import ch.iec._61400.ews._1.GetDataValuesRequest;
import ch.iec._61400.ews._1.GetDataValuesResponse;
import ch.iec._61400.ews._1.TBasicType;
import ch.iec._61400.ews._1.TDAType;
import ch.iec._61400.ews._1.TDataAttribute;
import ch.iec._61400.ews._1.TDataAttributeValue;
import ch.iec._61400.ews._1.TFC;
import ch.iec._61400.ews._1.TFcdFcdaType;
import ch.iec._61400.ews._1.TServiceError;
import ch.iec._61400.ews._1_0.ServicePortType;

public class SiemensLooper implements Runnable {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(SiemensLooper.class);

	ServicePortType service;
	List<String> logicalDeviceList = new ArrayList<String>();
	String assocID;
	List<String> availableLogicalDevice = new ArrayList<String>();
	List<String> refs = new ArrayList<String>();
	ActuatorClientImpl impl;

	private HashMap<String, String> deviceToSpecIDMapping;

	public SiemensLooper(ActuatorClientImpl impl, List<String> availableLogicalDevice2, String assocID) {
		this.impl = impl;
		this.logicalDeviceList = availableLogicalDevice2;
		this.service = impl.getService();
		this.assocID = assocID;
	}

	@Override
	public void run() {
		//this.assocID = impl.associate();
		logger.debug("SiemensWrapper - Updater: run: AssocID {}", assocID);

		refs = new ArrayList<String>();

		// filled later when retrieving data from the powerbridge.
		deviceToSpecIDMapping = new HashMap<String, String>();
		List<String> lnRefs = new ArrayList<String>();
		for (String logicalDevice : logicalDeviceList) {
		lnRefs = this.impl.fetchLogicalNodes(logicalDevice, this.service,
				this.assocID);
		for (String lnRef : lnRefs) {
			for (String logicalNodeSubstringStart : this.impl
					.getLogicalNodeSubstringStarts()) {
				if (lnRef.startsWith(logicalNodeSubstringStart)) {
					refs.add(logicalDevice + "/" + lnRef);
				}
			}
		}
		logger.debug("SiemensWrapper - Updater: run: refs: {}", refs);
		for (String ref : refs) {
			/*
			 * DONT do it for all Types for (TFC type : TFC.values()) { if
			 * (usedTypeList.contains(type)) {
			 */
			for (TFC type : this.impl.getUsedTypeList()) {
				logger.debug(
						"SiemensWrapper - Updater: run: Getting data for ref {}, type: {}",
						ref, type);
				TFcdFcdaType req = new TFcdFcdaType();
				req.setRef(ref);
				// req.setRef("C1/MMXU1");
				req.setFC(type);
				// req.setFC(TFC.MX);
				GetDataValuesRequest request = new GetDataValuesRequest();
				request.setRef(req);
				request.setUUID(UUID.randomUUID().toString());
				request.setAssocID(this.assocID);
				GetDataValuesResponse response;
				try {
					response = this.service.getDataValues(request);
					if (response.getServiceError() != null
							&& response.getServiceError().size() > 0) {
						for (TServiceError error : response.getServiceError()) {
							logger.warn(
									"SiemensWrapper - Updater: run: Error while getting data: {}",
									error.value());
							if (error
									.equals(TServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE)
									|| error.equals(TServiceError.ACCESS_VIOLATION)) {
								this.assocID = this.impl.associate();
							}
						}
					} else {
						processResponse(response);
					}
				} catch (WebServiceException e1) {
					logger.warn(
							"SiemensWrapper - Updater: run: Could not request new data ",
							e1);
				}
			}
		}
		}
	}

	private void processResponse(GetDataValuesResponse response) {
		for (TDataAttributeValue measurements : response.getDataAttrVal()) {
			processTDataAttributeValue(measurements);
		}
	}

	private void processTDataAttributeValue(TDataAttributeValue measurements) {
		String measurementsname = measurements.getDataAttrRef();
		TDataAttribute value = measurements.getValue();
		processTDAType(value.getDAType(), measurementsname);
	}

	private void processTDAType(TDAType daType, String measurementsname) {
		for (Object tdat : daType.getDACompOrPrimComp()) {

			if (tdat instanceof TDAType) {
				processTDAType((TDAType) tdat,
						measurementsname + "." + daType.getDAName());
			} else if (tdat instanceof TBasicType) {
				String datatype = daType.getDAName();

				if (datatype.equals("f")) {
					Float value = ((TBasicType) tdat).getFloat32();
					
					measurementsname = measurementsname.replaceAll("/", "_");
					
					if (this.impl.devices.containsKey(measurementsname)) {
						logger.debug(
								"SiemensLooper - New Event: processTDAType: Device {}: {}",
								measurementsname, value.doubleValue());
						DoubleEvent event = new DoubleEvent(value.doubleValue());
						try {
							this.impl.getMaster().sendDoubleEvent(
									event,
									this.impl.devices.get(measurementsname)
											.getDeviceId(),
									this.impl.getClientId());
							logger.info(
									"SiemensLooper - New Event from Device {}: {}",
									this.impl.devices.get(measurementsname)
											.getDeviceId(), value.doubleValue());

						} catch (TimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						/*
						 * TODO besides Float also Time, Boolean and Qualitiy
						 * types could be relevant
						 */

						// ((TBasicType) tdat).getFloat32());
						// if (!((TBasicType) tdat).getFloat32().isNaN()) {
						/*
						 * checkNew(QUEUE_NAME + "?" + measurementsname,
						 * Double.class, lookUpUnit(measurementsname)); //TODO
						 */
						// TASK Retrieve accuracy from Quality
						// System.out.println("TODO Eventhandler " + QUEUE_NAME
						// + " " + ((TBasicType) tdat).getFloat32());
						/*
						 * getEventHandler().doubleEvent(QUEUE_NAME + "?" +
						 * measurementsname, ((TBasicType) tdat).getFloat32(),
						 * lookUpUnit(measurementsname), Double.NaN);
						 */
					}
				}

			}
		}
	}
}
