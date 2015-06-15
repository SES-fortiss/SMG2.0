/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.solarlog.impl;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

public class SolarLooper implements Runnable {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(SolarLooper.class);
	
	private ActuatorClientImpl impl;
	private static final int CONN_TIMEOUT = 5000;
	/* energy CO2 Saving Factor for Munich as of 2007 */
	private static final double savingFactor = 0.616;
	/*
	 * Factor for polling slowly changing values 6 times the poll rate
	 */
	private static final int REFRESH_LOWRATE_FACTOR = 6;
	private static int counterUpdateSlowChangingValues = 1;
	
	private Object lastLine;
	private String lastStatus ="";
	private String urlday;
	private String urlcurrent;
	private JSONParser parser;

	SolarLooper(ActuatorClientImpl impl){
		this.impl = impl;
	    this.parser = new JSONParser();
	}
	
	 @Override
	    public void run() {
		 	DeviceId origin;// = new DeviceId("", impl.getWrapperTag());
		 	DoubleEvent ev;
		 
		 		JSONObject valuesOfWR = null;
	
				
		 		logger.debug("SolarLogConnector: run: SolarLog loop");
				
				try {
					
					
					String url = impl.getHost()+"/getjp";
					URL obj = new URL(url);
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			 
					//add reuqest header
					con.setRequestMethod("POST");
			 
					String urlParameters = "{\"801\":{\"170\":null}}";
			 
					// Send post request
					con.setDoOutput(true);
					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					wr.writeBytes(urlParameters);
					wr.flush();
					wr.close();
					
					int responseCode = con.getResponseCode();
					System.out.println("\nSending 'POST' request to URL : " + url);
					System.out.println("Post parameters : " + urlParameters);
					System.out.println("Response Code : " + responseCode);
			 
					BufferedReader in = new BufferedReader(
					        new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();
			 
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
			 
					
					
//					  |awk -F : '{if($1=="\"101\"") {print "PAC Leistung WR " ($2/1000) " kWh" }};
//			             {if($1=="\"102\"") {print "PAC Leistung aller WR " ($2/1000) " kWh" }};
//			             {if($1=="\"103\"") {print "Spannung U AC Wechselspannung " $2 " Volt" }};
//			             {if($1=="\"104\"") {print "Spannung U DC Gleichspannung " $2 " Volt" }};
//			             {if($1=="\"105\"") {print "Tages-Ertrag der WR " ($2/1000) " kWh" }};
//			             {if($1=="\"106\"") {print "Tages-Ertrag der WR von gestern " ($2/1000) " kWh" }};
//			             {if($1=="\"107\"") {print "Monats-Ertrag der WR " ($2/1000) " kWh" }};
//			             {if($1=="\"108\"") {print "Jahres-Ertrag der WR  " ($2/1000) " kWh" }};
//			             {if($1=="\"109\"") {print "Gesamtertrag aller WR " ($2/1000) " kWh" }};
//			             {if($1=="\"110\"") {print "momentaner Verbrauch " ($2/1000) " kWh" }};
//			             {if($1=="\"111\"") {print "Tages-Verbrauch " ($2/1000) " kWh" }};
//			             {if($1=="\"112\"") {print "Tages-Verbrauch von gestern " ($2/1000) " kWh" }};
//			             {if($1=="\"113\"") {print "Monats-Verbrauch " ($2/1000) " kWh" }};
//			             {if($1=="\"114\"") {print "Jahres-Verbrauch " ($2/1000) " kWh" }};
//			             {if($1=="\"115\"") {print "Summe aller Verbraucher " ($2/1000) " kWh" }};
//			             {if($1=="\"116\"") {print "Installierte Generatorleistung " ($2/1000) " kWp" }}'
					
					//print result
					JSONObject jsonObject = (JSONObject) this.parser.parse(response.toString());
					
					System.out.println(response.toString());
					
					String currentStatus = "";
					JSONObject numberWR = (JSONObject) jsonObject.get("801");
					valuesOfWR = (JSONObject) numberWR.get("170");
					Double einspeiseW = Double.parseDouble(valuesOfWR.get("101").toString());
					Double generatorW = Double.parseDouble(valuesOfWR.get("102").toString());
					
					System.out.println(einspeiseW);
					System.out.println(generatorW);
					/*
					 * Read current data every 5 seconds (Einspeisung Pac [W], Generatorleistung Pdc [W], Status Code) 
					 * other information in 30 seconds interval ( )
					 * 
					 * CO2 Savings Factor EnergyProduction x 0.616 = EmissionsSaved kg
					 * 
					 * 
					 * read urlcurrent first and every 6th time also read the min_day
					 */
			
					/*
					 * generate Events
					 */
					origin = impl.getDeviceSpecs().get(0).getDeviceId();
                    ev = new DoubleEvent(generatorW);
                    impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
                    logger.info("SolarWrapper: run(): getEventHandler - new Event from " + origin + " value " + generatorW);
                    //getEventHandler().doubleEvent(DEV2_Gen_COMPLETE, generatorW, SIUnitType.W, 0);
                    
                    origin = impl.getDeviceSpecs().get(4).getDeviceId();
                    ev = new DoubleEvent(einspeiseW);
                    impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
                    logger.info("SolarWrapper: run(): getEventHandler - new Event from " + origin + " value " + einspeiseW);
                    //getEventHandler().doubleEvent(DEV1_Ein_COMPLETE, einspeiseW, SIUnitType.W, 0);
				}
				catch (FileNotFoundException fe) {
					logger.error("SolarLogConnector: run: SolarLog Connection - File not found ", fe);
				}
				catch (IOException e1) {
					logger.error("SolarLogConnector: run: SolarLog Connection - IO Error ", e1);
				}
				catch (TimeoutException e) {
		        	 logger.error("timeout sending to master", e);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					
					
					
					
					
				if (counterUpdateSlowChangingValues >= REFRESH_LOWRATE_FACTOR) {
					
					counterUpdateSlowChangingValues = 1;
					
					try  {
					
					if (valuesOfWR != null) {
						
								logger.info("SolarLogConnector: run: SolarLog Data");
						// NEW DATA
						// m[mi++]="08.02.13 12:10:00|44;46;22;199;1"
						//		String split[] = firstLine.split("\\|")[1].split(";");
						
						//0 = Einspeisung Pac [W]
						//1 = Generatorleistung Pdc [W]
						//2 = DaySum [Wh]
						//3 = Udc1 [V]
						//4 = Tmp [Degree Celsius]
						
								Double daySumWh = Double.parseDouble(valuesOfWR.get("105").toString());
								Double udc1V = Double.parseDouble(valuesOfWR.get("104").toString());
								Double tempC = -1.0; //Double.parseDouble(test2.get("102").toString());
								
								origin = impl.getDeviceSpecs().get(6).getDeviceId();
			                    ev = new DoubleEvent(daySumWh);
			                    impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
			                    logger.info("SolarWrapper: run(): getEventHandler - new Event from " + origin + " value " + daySumWh);
								//getEventHandler().doubleEvent(DEV1_Ein_COMPLETE, daySumWh, SIUnitType.WH, 0);
			                    
			                    origin = impl.getDeviceSpecs().get(5).getDeviceId();
			                    ev = new DoubleEvent(tempC);
			                    impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
			                    logger.info("SolarWrapper: run(): getEventHandler - new Event from " + origin + " value " + tempC);
								//getEventHandler().doubleEvent(DEV1_Ein_COMPLETE, tempC, SIUnitType.CELSIUS, 0);
						
			                    origin = impl.getDeviceSpecs().get(2).getDeviceId();
			                    ev = new DoubleEvent(udc1V);
			                    impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
			                    logger.info("SolarWrapper: run(): getEventHandler - new Event from " + origin + " value " + udc1V);
								//getEventHandler().doubleEvent(DEV2_Gen_COMPLETE, udc1V, SIUnitType.V, 0);
								
								
								origin = impl.getDeviceSpecs().get(1).getDeviceId();
			                    ev = new DoubleEvent(tempC);
			                    impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
			                    logger.info("SolarWrapper: run(): getEventHandler - new Event from " + origin + " value " + tempC);
								//getEventHandler().doubleEvent(DEV2_Gen_COMPLETE, tempC, SIUnitType.CELSIUS, 0);
						
							/*
						 	* Energy saved 
						 	*/
			                    origin = impl.getDeviceSpecs().get(3).getDeviceId();
			                    ev = new DoubleEvent(((daySumWh/1000.0) * savingFactor));
			                    impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
			                    logger.info("SolarWrapper: run(): getEventHandler - new Event from " + origin + " value " + ((daySumWh/1000.0) * savingFactor));
								//getEventHandler().doubleEvent(DEVCO2Saved, ((daySumWh/1000.0) * savingFactor), SIUnitType.KG, 0);
							//lastLine = firstLine;
							}
					

						
					
				}
					catch (TimeoutException e) {
			        	 logger.error("timeout sending to master", e);
					}
				}
				else {
					counterUpdateSlowChangingValues++;
				}
				
				try { 
					Thread.sleep(impl.getPollFrequency());
				}	
				catch (InterruptedException e) {
					logger.error("SolarLogConnector: run: SolarLog sleep interrupted", e);
				} 			
				
			}

	
	
	 }
