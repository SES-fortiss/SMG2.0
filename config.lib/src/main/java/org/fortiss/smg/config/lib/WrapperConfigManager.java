/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.config.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WrapperConfigManager {
	
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WrapperConfigManager.class);
    
	
	String fileName = "/opt/felix/wrapperconfig.xml";
	
	

	private static boolean allowmultiple = false;
	private static ConcurrentHashMap<String, ArrayList<WrapperConfig>> wrapperConfigList = new ConcurrentHashMap<String, ArrayList<WrapperConfig>>(); 
	private static ConcurrentHashMap<String, ArrayList<WrapperConfig>> wrapperConfigListTaken = new ConcurrentHashMap<String, ArrayList<WrapperConfig>>(); 
		
	
	static WrapperConfigManager instance = new WrapperConfigManager();
	
	public WrapperConfigManager() {
		/*InputStream is = WrapperConfigManager.class.getResourceAsStream("/wrapperconfig.xml");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        String line;
        try {
			while ((line = reader.readLine()) != null) {
			    out.append(line);
			}
			System.out.println(out.toString());   //Prints the string content read from input stream
	        reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
		
		
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			fileName = "c:/opt/felix/wrapperconfig.xml";
		}
		System.out.println(fileName);
		/*
		 * Config Structure 
		 * allowmultiple (true): one wrapper can handle more devices/configs with the same key
		 */
		parseConfig();
	}
	
	public static WrapperConfigManager getInstance() {
		return instance;
	}
	
	private void parseConfig() {
		try {
			File fXmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			
			
			if ("true".equals(doc.getElementsByTagName("allowmultiple").item(0).getTextContent())) {
				allowmultiple = true;
			}
			
			NodeList nList = doc.getElementsByTagName("wrapper");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String key = eElement.getElementsByTagName("key").item(0).getTextContent();
					int polling = 0;
					try {
						polling = Integer.parseInt(eElement.getElementsByTagName("pollingfrequency").item(0).getTextContent());
					} catch (NumberFormatException e) {
						logger.debug("Number Format Exception " + e);
					}
					
					ArrayList<Subdevice> subdevices = new ArrayList<Subdevice>();
					
					NodeList subdeviceList = null;
					if (eElement.getElementsByTagName("subdevices").item(0) != null) {
						Node subdevicesNode = eElement.getElementsByTagName("subdevices").item(0);
						if (subdevicesNode.getNodeType() == Node.ELEMENT_NODE) {
							Element subdevicesElement = (Element) subdevicesNode;
							subdeviceList = subdevicesElement.getElementsByTagName("subdevice");
							for (int i = 0; i < subdeviceList.getLength(); i++) {
								Element subdeviceElement = (Element) subdeviceList.item(i);
								int devicecode = Integer.parseInt(subdeviceElement.getElementsByTagName("devicecode").item(0).getTextContent());
								float threshold = Float.parseFloat(subdeviceElement.getElementsByTagName("threshold").item(0).getTextContent());
								String pin = subdeviceElement.getElementsByTagName("pin").item(0).getTextContent();
								Subdevice subdevice = new Subdevice(devicecode, threshold, pin);
								subdevices.add(subdevice);
							}
						}
					}
					
					WrapperConfig config = new WrapperConfig(
							key,
							eElement.getElementsByTagName("wrapperid").item(0).getTextContent(),
							eElement.getElementsByTagName("wrappername").item(0).getTextContent(),
							eElement.getElementsByTagName("protocol").item(0).getTextContent(),
							eElement.getElementsByTagName("host").item(0).getTextContent(),
							eElement.getElementsByTagName("port").item(0).getTextContent(),
							eElement.getElementsByTagName("path").item(0).getTextContent(),
							eElement.getElementsByTagName("username").item(0).getTextContent(),
							eElement.getElementsByTagName("password").item(0).getTextContent(),
							polling,
							subdevices
							);
					
					if (wrapperConfigList.containsKey(key)) {
						wrapperConfigList.get(key).add(config);
					}
					else {
						ArrayList<WrapperConfig> tmp = new ArrayList<WrapperConfig>();
						tmp.add(config);
						wrapperConfigList.put(key, tmp);
					}
				}
			}
			
			
		}
		catch (FileNotFoundException e) {
			logger.info("No Configuration File found: " + fileName + " No configs will be available !");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public synchronized ArrayList<WrapperConfig> getConfig(String key) throws TimeoutException {
		ArrayList<WrapperConfig> configs = new ArrayList<WrapperConfig>();
		if (wrapperConfigList.get(key) != null) {
		// smartphoneserver must be on different devices !!
		if (allowmultiple && !key.equals("smartphoneserver")) {
			configs.addAll(wrapperConfigList.get(key));
			wrapperConfigListTaken.put(key, configs);
			wrapperConfigList.remove(key);
			return configs;
		}
		else {
			ArrayList<WrapperConfig> tmp = wrapperConfigList.get(key);
			if (tmp == null) {
				wrapperConfigList.remove(key);
			}
			else if (tmp.size() > 0) {
				configs.add(tmp.get(0));
				wrapperConfigListTaken.put(key, configs);
				tmp.remove(0);
				wrapperConfigList.put(key, tmp);
			}
			logger.debug("Currently taken configurations by key: " + wrapperConfigListTaken.keySet());
			logger.debug("Providing " + configs.size() + " configurations for " + key);
		}
		}
			return configs;
			
		
		
	}
	
	public synchronized void recoverConfig(String wrapperName) throws TimeoutException {
		String recover = wrapperName; 
		Set<String> currentlyTaken = wrapperConfigListTaken.keySet();
		
		for (String key : currentlyTaken) {
			ArrayList<WrapperConfig> list = wrapperConfigListTaken.get(key);
			for (WrapperConfig singleConfig : list) {
				
				if (singleConfig.getWrapperName().equals(recover)) {
					// smartphoneserver must be on different devices !!
					if (allowmultiple && !wrapperName.contains("smartphoneserver")) {
						wrapperConfigList.put(key, wrapperConfigListTaken.get(key));
						wrapperConfigListTaken.remove(key);
						logger.debug("Found configs to return for " + recover + " using key " + key);
						break;
					}
					else {
						if (wrapperConfigList.containsKey(key)) {
							wrapperConfigList.get(key).add(singleConfig);
							wrapperConfigListTaken.get(key).remove(singleConfig);
							logger.debug("Found single config to return for " + recover + " using key " + key);
							break;
						}
						else {
							ArrayList<WrapperConfig> newList = new ArrayList<WrapperConfig>();
							newList.add(singleConfig);
							wrapperConfigList.put(key, newList);
							wrapperConfigListTaken.get(key).remove(singleConfig);
							if (wrapperConfigListTaken.get(key).size() == 0) {
								wrapperConfigListTaken.remove(key);
							}
							logger.debug("Found single config to return for " + recover + " using key " + key);
							break;
						}
					}
				}
			}
			
		}
	}
	
}
