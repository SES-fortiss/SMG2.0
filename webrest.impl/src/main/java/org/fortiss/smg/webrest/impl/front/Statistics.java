/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.impl.front;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.fortiss.smg.containermanager.api.devices.DeviceId;
//import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.webrest.impl.BundleFactory;
import org.fortiss.smg.webrest.impl.ParametersNotValid;
import org.fortiss.smg.analyzer.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/statistics")
public class Statistics {

	private static Logger logger = LoggerFactory.getLogger(Statistics.class);

	@GET
	@Path("getDoubleValue/{node}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	/*public DoublePoint[] getDoubleValue(@PathParam("node") String containerId, @QueryParam("from") long from, @QueryParam("to") long to,
			@QueryParam("unit") SIUnitType unit) {*/
		public DoublePoint[] getDoubleValue(@PathParam("node") String containerId, @QueryParam("from") long from, @QueryParam("to") long to) {
		
		//create Device ID for Container 
		DeviceId deviceID = splitContainer(containerId);
		
		if (containerId.length() < 1) {
			throw new ParametersNotValid("Invalid devid");
		}
		
		/*if (devId.length() < 1) {
			throw new ParametersNotValid("Invalid devid");
		}*/
	
		if (from > to) {
			throw new ParametersNotValid(
					"Date from can't be greater than Date to");
		}
		if (to < 10 || from < 10) {
			throw new ParametersNotValid("Date invalid");
		}

		// Retrieve a list of DoubleValues for a Device between to dates.
		List<DoublePoint> doubleDataPt;
		
		try {
			doubleDataPt = BundleFactory.getInformationBroker()
					.getDoubleValue(deviceID, from, to);
		} catch (TimeoutException e) {
			logger .info("No connection?", e.fillInStackTrace());
			throw new ParametersNotValid("Unable to connect to InformationBroker");
		}

		
		
		if(doubleDataPt == null){
			throw new ParametersNotValid("Invalid devid? [" + deviceID + "]");
		}
		
		System.out.println("size "+doubleDataPt.size());
		
		DoublePoint[] toArr = new DoublePoint[doubleDataPt.size()];
	
			DoublePoint[] resultArr = doubleDataPt.toArray(toArr);
			return resultArr;
	

	}
	//gets the Maximum Value from a time period from the analyzer component
	@GET
	@Path("getMax/{node}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Double getMax(@PathParam("node") String containerID, @QueryParam("from") long from, @QueryParam("to") long to){
		
		if (containerID.length() < 1) {
			throw new ParametersNotValid("Invalid ContainerID");
		}
		
		if (from == to){
			throw new ParametersNotValid("Start Time must be different from end time");
		}
		
		//create Device ID for Container 
		DeviceId deviceID = splitContainer(containerID);
		
		Calendar startCal = Calendar.getInstance();
		Calendar stopCal = Calendar.getInstance();
		startCal.setTimeInMillis(from);
		stopCal.setTimeInMillis(to);
		
		double maxDoubleDataPt;
		try {
			return maxDoubleDataPt = BundleFactory.getAnalyzer()
					.getMax(startCal, stopCal, deviceID);
		} catch (TimeoutException e) {
			logger .info("No connection?", e.fillInStackTrace());
			throw new ParametersNotValid("Unable to connect to Analyzer");
		} catch (IllegalArgumentException e) {
			logger .info("Wrong kind of parameters?", e.fillInStackTrace());
			throw new ParametersNotValid("Wrong kind of Parameters");
		} catch (NoDataFoundException e) {
			logger .info("No data found?", e.fillInStackTrace());
			throw new ParametersNotValid("Could not find any data");
		}
		

	}
	
	//gets the Minimum Value from a time period from the analyzer component
		@GET
		@Path("getMin/{node}")
		@Produces({ MediaType.APPLICATION_JSON })
		public Double getMin(@PathParam("node") String containerID, @QueryParam("from") long from, @QueryParam("to") long to){
			
			if (containerID.length() < 1) {
				throw new ParametersNotValid("Invalid ContainerID");
			}
			
			if (from == to){
				throw new ParametersNotValid("Start Time must be different from end time");
			}
			//create Device ID for Container 
			DeviceId deviceID = splitContainer(containerID);
			
			
			Calendar startCal = Calendar.getInstance();
			Calendar stopCal = Calendar.getInstance();
			startCal.setTimeInMillis(from);
			stopCal.setTimeInMillis(to);
			
			double maxDoubleDataPt;
			try {
				return maxDoubleDataPt = BundleFactory.getAnalyzer()
						.getMin(startCal, stopCal, deviceID);
			} catch (TimeoutException e) {
				logger .info("No connection?", e.fillInStackTrace());
				throw new ParametersNotValid("Unable to connect to Analyzer");
			} catch (IllegalArgumentException e) {
				logger .info("Wrong kind of parameters?", e.fillInStackTrace());
				throw new ParametersNotValid("Wrong kind of Parameters");
			} catch (NoDataFoundException e) {
				logger .info("No data found?", e.fillInStackTrace());
				throw new ParametersNotValid("Could not find any data");
			}
		
		}
		
		//gets the Sum for a time period from the analyzer component
				@GET
				@Path("getSum/{node}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Double getSum(@PathParam("node") String containerID, @QueryParam("from") long from, @QueryParam("to") long to){
					
					if (containerID.length() < 1) {
						throw new ParametersNotValid("Invalid ContainerID");
					}
					
					if (from == to){
						throw new ParametersNotValid("Start Time must be different from end time");
					}
					
					//create Device ID for Container one
					DeviceId deviceID = splitContainer(containerID);
					
					Calendar startCal = Calendar.getInstance();
					Calendar stopCal = Calendar.getInstance();
					startCal.setTimeInMillis(from);
					stopCal.setTimeInMillis(to);
					
					double maxDoubleDataPt;
					try {
						return maxDoubleDataPt = BundleFactory.getAnalyzer()
								.getSum(startCal, stopCal, deviceID);
					} catch (TimeoutException e) {
						logger .info("No connection?", e.fillInStackTrace());
						throw new ParametersNotValid("Unable to connect to Analyzer");
					} catch (IllegalArgumentException e) {
						logger .info("Wrong kind of parameters?", e.fillInStackTrace());
						throw new ParametersNotValid("Wrong kind of Parameters");
					} catch (NoDataFoundException e) {
						logger .info("No data found?", e.fillInStackTrace());
						throw new ParametersNotValid("Could not find any data");
					}
				
				}
				
				//gets the Arithmetic Mean for a time period from the analyzer component (use for time unbound things e.g. temperature)
				@GET
				@Path("getArithmeticMean/{node}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Double getArithmeticMean(@PathParam("node") String containerID, @QueryParam("from") long from, @QueryParam("to") long to){
					
					if (containerID.length() < 1) {
						throw new ParametersNotValid("Invalid ContainerID");
					}
					
					if (from == to){
						throw new ParametersNotValid("Start Time must be different from end time");
					}
					
					//create Device ID for Container one
					DeviceId deviceID = splitContainer(containerID);
					
					Calendar startCal = Calendar.getInstance();
					Calendar stopCal = Calendar.getInstance();
					startCal.setTimeInMillis(from);
					stopCal.setTimeInMillis(to);
					
					double maxDoubleDataPt;
					try {
						return maxDoubleDataPt = BundleFactory.getAnalyzer()
								.getArithmeticMean(startCal, stopCal, deviceID);
					} catch (TimeoutException e) {
						logger .info("No connection?", e.fillInStackTrace());
						throw new ParametersNotValid("Unable to connect to Analyzer");
					} catch (IllegalArgumentException e) {
						logger .info("Wrong kind of parameters?", e.fillInStackTrace());
						throw new ParametersNotValid("Wrong kind of Parameters");
					} catch (NoDataFoundException e) {
						logger .info("No data found?", e.fillInStackTrace());
						throw new ParametersNotValid("Could not find any data");
					}
				
				}
				
				//gets the Arithmetic Mean with Integral for a time period from the analyzer component (use for time unbound things e.g. Wh)
				@GET
				@Path("getArithmeticMeanByTime/{node}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Double getArithmeticMeanByTime(@PathParam("node") String containerID, @QueryParam("from") long from, @QueryParam("to") long to){
					
					if (containerID.length() < 1) {
						throw new ParametersNotValid("Invalid ContainerID");
					}
					
					if (from == to){
						throw new ParametersNotValid("Start Time must be different from end time");
					}
					
					//create Device ID for Container one
					DeviceId deviceID = splitContainer(containerID);
					
					Calendar startCal = Calendar.getInstance();
					Calendar stopCal = Calendar.getInstance();
					startCal.setTimeInMillis(from);
					stopCal.setTimeInMillis(to);
					
					double maxDoubleDataPt;
					try {
						return maxDoubleDataPt = BundleFactory.getAnalyzer()
								.getArithmeticMeanByTime(startCal, stopCal, deviceID);
					} catch (TimeoutException e) {
						logger .info("No connection?", e.fillInStackTrace());
						throw new ParametersNotValid("Unable to connect to Analyzer");
					} catch (IllegalArgumentException e) {
						logger .info("Wrong kind of parameters?", e.fillInStackTrace());
						throw new ParametersNotValid("Wrong kind of Parameters");
					} catch (NoDataFoundException e) {
						logger .info("No data found?", e.fillInStackTrace());
						throw new ParametersNotValid("Could not find any data");
					}
				
				}
				
				//gets the rating for a time period from the analyzer component, takes 2 devices and some period to calculate the rating
				@GET
				@Path("getConsumptionRating/compare")
				@Produces({ MediaType.TEXT_PLAIN })
				public boolean[] getConsumptionRating(
						@QueryParam("node1") String containerID1,
						@QueryParam("from1") long from1, 
						@QueryParam("to1") long to1, 
						@QueryParam("node2") String containerID2, 
						@QueryParam("from2") long from2, 
						@QueryParam("to2") long to2){
					
					if (containerID1.length() < 1 || containerID2.length() < 1) {
						throw new ParametersNotValid("Invalid ContainerID");
					}
					
					if (from1 == to1 || from2 == to2){
						throw new ParametersNotValid("Start Time must be different from end time");
					}
					
					//create Device ID for Container one
					DeviceId deviceID1 = splitContainer(containerID1);
					
					//create Device ID for Container two
					DeviceId deviceID2 = splitContainer(containerID2);
					
					//create Calendar objects and timespans 			
					DataSet setCurrent = new DataSet(setCalendar(from1, to1)[0], setCalendar(from1, to1)[1], deviceID1);
					DataSet setReference = new DataSet(setCalendar(from2, to2)[0], setCalendar(from2, to2)[1], deviceID2);
					
					boolean[] consumptionRating;
					try {
						return consumptionRating = BundleFactory.getAnalyzer()
								.getConsumptionRating(setCurrent, setReference, 30.0);
					} catch (TimeoutException e) {
						logger .info("No connection?", e.fillInStackTrace());
						throw new ParametersNotValid("Unable to connect to Analyzer");
					} catch (IllegalArgumentException e) {
						logger .info("Wrong kind of parameters?", e.fillInStackTrace());
						throw new ParametersNotValid("Wrong kind of Parameters");
					} catch (NoDataFoundException e) {
						logger .info("No data found?", e.fillInStackTrace());
						throw new ParametersNotValid("Could not find any data");
					}
				
				}
				
				//gets the Correlation factor between two devices
				@GET
				@Path("getCorrelation/correlate")
				@Produces({ MediaType.TEXT_PLAIN })
				public double getCorrelation(
						@QueryParam("node1") String containerID1,
						@QueryParam("from1") long from1, 
						@QueryParam("to1") long to1, 
						@QueryParam("node2") String containerID2, 
						@QueryParam("from2") long from2, 
						@QueryParam("to2") long to2){
					
					if (containerID1.length() < 1 || containerID2.length() < 1) {
						throw new ParametersNotValid("Invalid ContainerID");
					}
					
					if (from1 == to1 || from2 == to2){
						throw new ParametersNotValid("Start Time must be different from end time");
					}
					
					//create Device ID for Container one
					DeviceId deviceID1 = splitContainer(containerID1);
					
					//create Device ID for Container two
					DeviceId deviceID2 = splitContainer(containerID2);
					
					//create Calendar objects and timespans 			
					DataSet setCurrent = new DataSet(setCalendar(from1, to1)[0], setCalendar(from1, to1)[1], deviceID1);
					DataSet setReference = new DataSet(setCalendar(from2, to2)[0], setCalendar(from2, to2)[1], deviceID2);
					
					double correlationFactor;
					try {
						return correlationFactor = BundleFactory.getAnalyzer()
								.getCorrelationTwoDevices(setCurrent, setReference, 300);
					} catch (TimeoutException e) {
						logger .info("No connection?", e.fillInStackTrace());
						throw new ParametersNotValid("Unable to connect to Analyzer");
					} catch (IllegalArgumentException e) {
						logger .info("Wrong kind of parameters?", e.fillInStackTrace());
						throw new ParametersNotValid("Wrong kind of Parameters");
					} catch (NoDataFoundException e) {
						logger .info("No data found?", e.fillInStackTrace());
						throw new ParametersNotValid("Could not find any data");
					}
				
				}
				
				
		//Splits the container and wrapper id, so it can be used by the backend
		public DeviceId splitContainer(String containerID){
			String[] split = containerID.split("\\.");
			String devID = split[2];
			String wrapperID = split[0] + "." + split[1];
			DeviceId deviceID = new DeviceId(devID, wrapperID);
			return deviceID;
		}
		//transforms the String Unix time to a calendar Object
		public Calendar[] setCalendar(long from, long to){
			Calendar[] fromto = new Calendar[2];
			Calendar startTime = Calendar.getInstance();
			startTime.setTimeInMillis(from);
			Calendar stopTime = Calendar.getInstance();
			stopTime.setTimeInMillis(to);
			fromto[0] = startTime;
			fromto[1]= stopTime;
			return fromto;
		}
		
		
				
	/*
	@GET
	@Path("getBooleanValue/{node}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public BooleanPoint[] getBooleanValue(@PathParam("node") String devid,
			@QueryParam("from") long from, @QueryParam("to") long to) {

		if (devid.length() < 1) {
			throw new ParametersNotValid("Invalid devid");
		}
		if (from > to) {
			throw new ParametersNotValid(
					"Date from can't be greater than Date to");
		}
		if (to < 10 || from < 10) {
			throw new ParametersNotValid("Date invalid");
		}

		// Retrieve a list of DoubleValues for a Device between to dates.
		List<BooleanPoint> doubleDataPt;
		try {
			doubleDataPt = BundleFactory.getInformationBroker()
					.getBoolValue(devid, from, to);
		} catch (TimeoutException e) {
			logger .info("No connection?", e.fillInStackTrace());
			throw new ParametersNotValid("Unable to connect to InformationBroker");
		}

		if(doubleDataPt == null){
			throw new ParametersNotValid("Invalid devid?");
		}
		
		BooleanPoint[] toArr = new BooleanPoint[doubleDataPt.size()];
		BooleanPoint[] resultArr = doubleDataPt.toArray(toArr);

		return resultArr;
	}
	*/

}
