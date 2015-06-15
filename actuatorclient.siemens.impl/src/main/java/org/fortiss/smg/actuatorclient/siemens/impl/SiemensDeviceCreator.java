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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.xml.ws.WebServiceException;

import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
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


	public class SiemensDeviceCreator {
		
		private static org.slf4j.Logger logger = LoggerFactory
				.getLogger(SiemensDeviceCreator.class);
		
		ServicePortType service;
		String logicalDevice = "";
		String assocID;
		List<String> refs = new ArrayList<String>(); 
		ActuatorClientImpl impl;

		Map<String, DeviceContainer> devices = new HashMap<String, DeviceContainer>();
	
		public SiemensDeviceCreator(ActuatorClientImpl impl, String lDevice) {
			this.impl = impl;
			this.logicalDevice = lDevice;
			this.service = impl.getService(); 
		}
		
		
		public void setLogicalDevice(String logicalDevice) {
			this.logicalDevice = logicalDevice;
		}


		public Map<String, DeviceContainer> getDevices() { //Map<String, DeviceContainer> 
			this.assocID = impl.getAssociate(); //impl.associate();
			
			
			refs = new ArrayList<String>();

			// filled later when retrieving data from the powerbridge.
			List<String> lnRefs = new ArrayList<String>();
			
			lnRefs = this.impl.fetchLogicalNodes(logicalDevice, this.service, this.assocID);
			for (String lnRef : lnRefs) {
				for (String logicalNodeSubstringStart : this.impl.getLogicalNodeSubstringStarts()) {
					if (lnRef.startsWith(logicalNodeSubstringStart)) {
						refs.add(logicalDevice+ "/" + lnRef);
					}
				}
			}

			
			logger.debug("SiemensWrapper - Updater: run: refs: {}", refs);

		
				for (String ref : refs) {
					
					for (TFC type : this.impl.getUsedTypeList()) {
						
						logger.debug("SiemensWrapper - Updater: run: Getting data for ref "+ //getQueueName()+
						" {} , type: {}",  ref,
								type);
						TFcdFcdaType req = new TFcdFcdaType();
						req.setRef(ref);
						// req.setRef("C1/MMXU1");
						req.setFC(type);
						//req.setFC(TFC.MX);
						
						GetDataValuesRequest request = new GetDataValuesRequest();
						request.setRef(req);
						request.setUUID(UUID.randomUUID().toString());
						request.setAssocID(this.assocID);
						//GetDataValuesResponse response;
						try {
							//System.out.println(service.toString() + " ! " + request.toString() + " ! ");
							GetDataValuesResponse response = service.getDataValues(request);
							if (response.getServiceError() != null
									&& response.getServiceError().size() > 0) {
								for (TServiceError error : response
										.getServiceError()) {
									logger.warn("SiemensWrapper - Updater: run: Error while getting data: {}",
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
							logger.warn("SiemensWrapper - Updater: run: Could not request new data ", e1);
						}
					}
				}

				return devices;
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
			DeviceId devID;
			for (Object tdat : daType.getDACompOrPrimComp()) {
				
				
				if (tdat instanceof TDAType) {
					processTDAType((TDAType) tdat, measurementsname + "."
							+ daType.getDAName());
				} else if (tdat instanceof TBasicType) {
					String datatype = daType.getDAName();
					
					
					
					if (datatype.equals("f")) {
						
						measurementsname = measurementsname.replaceAll("/", "_");
						
						DeviceContainer container = null;
						
						/*
						if (measurementsname.endsWith("TotW.mag.mag")) {
							devID = new DeviceId(measurementsname, impl.getWrapperTag());
							container = new DeviceContainer(devID, impl.getWrapperTag()
								+ ".powermeter");
							
							container.setRange(0,Double.MAX_VALUE, 0.001);
							container.setHrName("Powermeter [W]");
							container.setDeviceType(SIDeviceType.ConsumptionPowermeter);
							
							devices.put(measurementsname, container);
							logger.debug("Siemens Device Creator: Device Container Added {}: {}", measurementsname,
									((TBasicType) tdat).getFloat32());
						}
						else */ 
						if (measurementsname.contains("W.phs")) {
							devID = new DeviceId(measurementsname, impl.getWrapperTag());
							container = new DeviceContainer(devID, impl.getWrapperTag()
								+ ".powermeter");
							
							container.setRange(0,Double.MAX_VALUE, 0.00001);
							container.setHrName("Powermeter Phase [W]");
							container.setDeviceType(SIDeviceType.ConsumptionPowermeter);
							
							
							devices.put(measurementsname, container);
							logger.debug("Siemens Device Creator: Device Container Added {}: {}", measurementsname,
									((TBasicType) tdat).getFloat32());
						}
						else if (measurementsname.contains(".A.phs")) {
							devID = new DeviceId(measurementsname, impl.getWrapperTag());
							container = new DeviceContainer(devID, impl.getWrapperTag()
								+ ".amperemeter");
							
							container.setRange(0,Double.MAX_VALUE, 0.001);
							container.setHrName("Amperemeter Phase [A]");
							container.setDeviceType(SIDeviceType.ConsumptionAmperemeter);
							
							
							devices.put(measurementsname, container);
							logger.debug("Siemens Device Creator: Device Container Added {}: {}", measurementsname,
									((TBasicType) tdat).getFloat32());
						}
						else if (measurementsname.contains("PhV.phs")) {
							devID = new DeviceId(measurementsname, impl.getWrapperTag());
							container = new DeviceContainer(devID, impl.getWrapperTag()
								+ ".voltmeter");
							
							container.setRange(0,Double.MAX_VALUE, 0.001);
							container.setHrName("Voltmeter Phase [V]");
							container.setDeviceType(SIDeviceType.ConsumptionVoltmeter);
							
							
							devices.put(measurementsname, container);
							logger.debug("Siemens Device Creator: Device Container Added {}: {}", measurementsname,
									((TBasicType) tdat).getFloat32());
						}
						else if (measurementsname.endsWith("Hz.mag.mag")) {
							devID = new DeviceId(measurementsname, impl.getWrapperTag());
							container = new DeviceContainer(devID, impl.getWrapperTag()
								+ ".frequency");
							
							container.setRange(0,Double.MAX_VALUE, 0.001);
							container.setHrName("Frequency [Hz]");
							container.setDeviceType(SIDeviceType.Frequency);
							
							
							devices.put(measurementsname, container);
							logger.debug("Siemens Device Creator: Device Container Added {}: {}", measurementsname,
									((TBasicType) tdat).getFloat32());
						}
						
						
						
						if (container != null) {
							try {
								
								impl.getMaster().sendDeviceEvent(new DeviceEvent(container), impl.getClientId());
								logger.info("Dev:" + container.getDeviceId().getDevid() + " -> "
										+ impl.getClientId());
							} catch (TimeoutException e) {
								logger.debug("Failed to send " + container.getDeviceId()
										+ " to master");
							}
							
						}
						
						
						/*
						 * TODO add other devices Voltmeter, Phaseshift, etc.
						 */
						
						
						/*
						
						
						if (!((TBasicType) tdat).getFloat32().isNaN()) {
							
						}
					} else if (datatype.equals("t")) {
						logger.debug("SiemensWrapper - Updater: processTDAType: Device {}: {}", measurementsname,
								((TBasicType) tdat).getTimeStamp().getSecSE());
					} else if (datatype.equals("q")) {
						logger.debug("SiemensWrapper - Updater: processTDAType: Device {}: {}", measurementsname,
								((TBasicType) tdat).getQuality().getValidity());
					} else if (datatype.equals("b")) {
						logger.debug("SiemensWrapper - Updater: processTDAType: Device {}: {}", measurementsname,
								((TBasicType) tdat).isBoolean());
					} else if (datatype.equals("stVal")
							&& ((TBasicType) tdat).getEnum() != null) { // on/off
																		// exists
																		// -
						// sent as stVal,
						// not boolean
						logger.debug("SiemensWrapper - Updater: processTDAType: Device {}: {}", measurementsname,
								((TBasicType) tdat).getEnum().getValue());
					} else if (datatype.equals("actVal")) {
						//checkNew(QUEUE_NAME + "?" + measurementsname,
						//		Double.class, lookUpUnit(measurementsname));
						BigInteger value = ((TBasicType) tdat).getInt128();
						//TODO
						// TASK Retrieve accuracy from Quality
						//getEventHandler().doubleEvent(QUEUE_NAME + "?"
						//		+ measurementsname, value.doubleValue(),
						//		lookUpUnit(measurementsname), Double.NaN);
						logger.debug("SiemensWrapper - Updater: processTDAType: Device {}: {}", measurementsname, value); */
					}
				}
			}
		}
}
