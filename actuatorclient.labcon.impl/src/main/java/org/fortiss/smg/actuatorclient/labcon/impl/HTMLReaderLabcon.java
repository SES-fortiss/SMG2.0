/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.labcon.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class HTMLReaderLabcon {
	private static final Logger logger = LoggerFactory.getLogger(HTMLReaderLabcon.class);
	
	private String host;
	private String httpUser;
	private String httpPassword;
	private String remoteFile;	
	private boolean isConnected;

	private List<String[]> eventDataList = new ArrayList<String[]>();

	private Calendar lastEventTime;

	public HTMLReaderLabcon(String httpHost, String sftpUser, String sftpPassword) {

		this.host = httpHost; // "192.168.17.158";
		this.httpUser = sftpUser; // "root";
		this.httpPassword = sftpPassword; // "!SMG2011";
		
		this.remoteFile = "";
		
		lastEventTime = new GregorianCalendar();
		lastEventTime.add(Calendar.HOUR, -1); // server is 1 hr slower than the current time.
		lastEventTime.add(Calendar.HOUR, -1); // to solve summer time difference between server and realtime

		isConnected = false;
		
	}

	public String getHttpHost() {
		return host;
	}

	public void setHttpHost(String sftpHost) {
		this.host = sftpHost;
	}

	public String getHttpUser() {
		return httpUser;
	}

	public void setHttpUser(String sftpUser) {
		this.httpUser = sftpUser;
	}

	public String getHttpPassword() {
		return httpPassword;
	}

	public void setHttpPassword(String sftpPassword) {
		this.httpPassword = sftpPassword;
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
	
	public String getRemoteFile() {
		return remoteFile;
	}

	public void setRemoteFile(String remoteFile) {
		this.remoteFile = remoteFile;
	}

	public boolean isConnected() {
		return isConnected;
	}

//	public void setConnected(boolean isConnected) {
//		this.isConnected = isConnected;
//	}

	URLConnection urlConnection;

	public void connect(String remoteFile) {
		String webPage = this.getHttpHost();
		String name = this.getHttpUser();
		String password = this.getHttpPassword();

		String authString = name + ":" + password;
		
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		
		URL url;
		try {
			url = new URL("http://"+webPage+"/"+remoteFile);
		
			urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
		
			this.isConnected = true;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public List<String[]> readRemoteFile(String remoteFile) {
		
		try {
			
			if (!this.isConnected) {
				connect(remoteFile);
			}
			
			
			
			InputStream in = urlConnection.getInputStream();

			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			
			logger.info("############### HTMLReader BufferedReader Status {} first line in file ###############", r.readLine());
			
			//System.out.println(lastEventTime);
			
			
			eventDataList.clear();
			
			 String line = null;
			 String tmp;
			 
			 
			 //String line2 = new String();
			  //System.out.println("================");
			 while ((tmp = r.readLine()) != null) {
				 
				  line = tmp;
			  //System.out.println(line);
			  }
		/*	  System.out.println("\n================");
			  System.out.println("Last line of the file is : ");
			  System.out.println(line); */
			  in.close();
			  
			// while ((line = r.readLine()) != null) {
	
//				SAMPLE DATA: 2013,5,7,9,50,29,OFF,12.698,
			  if (line != null) {
				String[] lineArray = line.split(",");
				
				Calendar cal = new GregorianCalendar();
				if (lineArray.length > 5) {					
					cal.set(Calendar.YEAR, Integer.parseInt(lineArray[0]));
					cal.set(Calendar.MONTH, Integer.parseInt(lineArray[1])-1);
					cal.set(Calendar.DATE, Integer.parseInt(lineArray[2]));
					cal.set(Calendar.HOUR, Integer.parseInt(lineArray[3]));
					cal.set(Calendar.MINUTE, Integer.parseInt(lineArray[4]));
					cal.set(Calendar.SECOND, Integer.parseInt(lineArray[5]));
					cal.set(Calendar.MILLISECOND, 0);
					cal.set(Calendar.AM_PM, 0);
				}
				logger.info("############### HTMLReader reading cal.YEAR {} ###############", cal.getTime() + " " + lastEventTime.compareTo(cal) + " < 0 ");
				if (lastEventTime.compareTo(cal) < 0) {
			/*		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!lastEventTime: !!!!!!!!!!!!!!!!!!!!!!!!" + lastEventTime.getTime());
					System.out.println("cal time     : " + cal.getTime() + "\n");
					System.out.println("CREATE NEW EVENT NOW -- WITH : " + cal.toString()); */
					eventDataList.add(lineArray);
					//logger.info("############### SSHReader returns {} data before ###############", eventDataList.size());
					lastEventTime = cal;
				}
				
			}
			
			logger.info("############### HTMLReader returns {} data ###############", eventDataList.size());
			
			return eventDataList;
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public List<String[]> readRemoteFile_MultiSensor(String remoteFile) {
			try {
				connect(remoteFile);
				
				
				InputStream in = urlConnection.getInputStream();
				BufferedReader r = new BufferedReader(new InputStreamReader(in));
				logger.info("############### HTMLReader_MultiSensor BufferedReader Status {} first line in file ###############", r.readLine());

				eventDataList.clear();
				String line = null;
				String tmp;

				while ((tmp = r.readLine()) != null) {
					line = tmp;
				}
				in.close();

				// SAMPLE DATA: 2013,8,22,11,58,57,396,24.3,OK,3.99,
				if (line != null) {	
				String[] lineArray = line.split(",");

				Calendar cal = new GregorianCalendar();
				if (lineArray.length > 5) {
					cal.set(Calendar.YEAR, Integer.parseInt(lineArray[0]));
					cal.set(Calendar.MONTH, Integer.parseInt(lineArray[1]) - 1);
					cal.set(Calendar.DATE, Integer.parseInt(lineArray[2]));
					cal.set(Calendar.HOUR, Integer.parseInt(lineArray[3]));
					cal.set(Calendar.MINUTE, Integer.parseInt(lineArray[4]));
					cal.set(Calendar.SECOND, Integer.parseInt(lineArray[5]));
					cal.set(Calendar.MILLISECOND, 0);
					cal.set(Calendar.AM_PM, 0);
				}
				
				if (lastEventTime.compareTo(cal) < 0) {
					eventDataList.add(lineArray);
					lastEventTime = cal;
				}
				}
				logger.info("############### HTMLReader_MultiSensor returns {} data ###############", eventDataList.size());
				
				return eventDataList;
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		
		return null;
	}

}
