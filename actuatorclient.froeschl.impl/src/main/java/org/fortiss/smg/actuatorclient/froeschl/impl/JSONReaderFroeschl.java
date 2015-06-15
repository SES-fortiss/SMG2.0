/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.froeschl.impl;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.slf4j.LoggerFactory; 
import org.apache.commons.codec.binary.Base64;
import org.neo4j.shell.util.json.JSONArray;
import org.neo4j.shell.util.json.JSONException;
import org.neo4j.shell.util.json.JSONObject;

public class JSONReaderFroeschl {
	



	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(JSONReaderFroeschl.class);
	private Calendar lastEventTime;

	private List<String[]> eventDataList = new ArrayList<String[]>();
	private JSONObject froeschlJson;
	List<String[]> results;

	public JSONReaderFroeschl() {
		// TODO Auto-generated constructor stub
		lastEventTime = new GregorianCalendar();
		lastEventTime.add(Calendar.HOUR, -1); // server is 1 hr slower than the current time.
		lastEventTime.add(Calendar.HOUR, -1); // to solve summer time difference between server and realtime
	}
	public Calendar getLastEventString() {
		return lastEventTime;
	}

	public void setLastEventString(Calendar lastEventString) {
		this.lastEventTime = lastEventString;
	}

	public List<String[]> getEventDataList() {
		return eventDataList;
	}

	public void setEventDataList(List<String[]> eventDataList) {
		this.eventDataList = eventDataList;
	}

/**
 *  @param requestUrlStr TODO
 * @return
 *  	[0] = Actual Consumption
 *  	[1] = Actual Consumption Unit
 *  	[2] = actueller Zaehlerstand
 *  	[3] = actueller Zaehlerstand Unit
 */
    public String[] readJsonFroeschl(String requestUrlStr) {

			String[] results;
			//String requestUrlStr = "http://192.168.21.214/GetMeasuredValue.cgi";
			String username = "user";
			String password = "user";
			JSONObject froeschlJson = null;
			JSONObject froeschlJsonDevice=null;
			JSONArray froeschlJsonArray = null; 
			results = null;
		    try {
		    	Authenticator.setDefault(new MyAuthenticator(username, password));
		    	URL requestUrl = new URL(requestUrlStr);
	
		    	Scanner scanner = new Scanner(requestUrl.openStream());
		        String response = scanner.useDelimiter("\\Z").next();
		        froeschlJson = new JSONObject(response.toString());
		        scanner.close();
		    	}
		    	catch (Exception e) {
		    		logger.info("JSONReaderFroeschl could not connect to the parsing webpage " +requestUrlStr+ " " + e);
			}
			
		    try {
		    	if (froeschlJson != null) {
			    	results = new String[4];
					froeschlJsonArray = (JSONArray) froeschlJson.get("entry");
					froeschlJsonArray = (JSONArray) froeschlJsonArray.getJSONObject(0).get("periodEntries");
					for(int i=0; i<froeschlJsonArray.length();i++ )
					{
						//Actual Value
						if(froeschlJsonArray.getJSONObject(i).getString("obis").contains("1.7.0"))	{
							results[0] = froeschlJsonArray.getJSONObject(i).getString("value");
							results[1] = froeschlJsonArray.getJSONObject(i).getString("unit");
						}
						//Aggregated Value
						if(froeschlJsonArray.getJSONObject(i).getString("obis").contains("1.8.0"))	{
							results[2] = froeschlJsonArray.getJSONObject(i).getString("value");
							results[3] = froeschlJsonArray.getJSONObject(i).getString("unit");
						}
					}
					
					System.out.println("############### JSON Data parsed[Froeschl] ###############");

		    	}
		    	else {
		    		logger.debug("Froeschl URL connection Failed");
		    	}
				return results;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		    
			return new String[4];
    }
	
    static class MyAuthenticator extends Authenticator {
        private String username, password;

        public MyAuthenticator(String user, String pass) {
          username = user;
          password = pass;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
        	String promptString = getRequestingPrompt();
            System.out.println(promptString);
            String hostname = getRequestingHost();
            System.out.println(hostname);
            InetAddress ipaddr = getRequestingSite();
            System.out.println(ipaddr);
            int port = getRequestingPort();
        	
        	
        	
        	return new PasswordAuthentication(username, password.toCharArray());
        }
      }
	
	
}
