/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class RuleParserImpl {
	 static final String name = "name";
	 static final String rule = "rule";
	 static final String salience = "salience";
	 static final String duration = "duration";
	 static final String noLoop = "no-loop";
	 static final String agendaGroup = "agenda-group";
	 static final String ruleCondition = "ruleCondition";
	  
	 public RuleParserImpl() {
		String fileName = "/Users/pragyagupta/Projects/SMG_GIT/smg2/rulesystem.impl/src/main/resources/rules.xml";
		List<Rule> ruleList = parseXML(fileName);
//	        for(Rule rule : ruleList){
//	            System.out.println(rule.toString());
//	        }
	  }	
	    /**
	     * @param fileName
	     * @return
	     */
	    public static List<Rule> parseXML(String fileName) {
	        List<Rule> ruleList = new ArrayList<Rule>();
	        Rule rule= new Rule();
	        String notifyType = null;
	        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
	        try {
	            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
	            while(xmlEventReader.hasNext()){
	                XMLEvent xmlEvent = xmlEventReader.nextEvent();
	               if (xmlEvent.isStartElement()){
	                   StartElement startElement = xmlEvent.asStartElement();
	                   if(startElement.getName().getLocalPart().equals("rule")){
	                       rule = new Rule();
	                       Attribute nameAttr = startElement.getAttributeByName(new QName("name"));
	                       if(nameAttr != null){
	                       rule.setName(nameAttr.getValue());
	                       }
	                   }
	                   //parsing the value of priority variable from xml salience tag
/*	                  else if(startElement.getName().getLocalPart().equals("salience")){
	                       xmlEvent = xmlEventReader.nextEvent();
	                       rule.setSalience(Integer.parseInt(xmlEvent.asCharacters().getData()));
	                   }*/
	                 //parsing the value of cron variable from xml duration tag
	                  else if(startElement.getName().getLocalPart().equals("duration")){
	                       xmlEvent = xmlEventReader.nextEvent();
	                       rule.setCron(Integer.parseInt(xmlEvent.asCharacters().getData()));
	                   }
	                   //parsing the rule condition from xml 
	                  else if(startElement.getName().getLocalPart().equals("ruleCondition")){
	                       xmlEvent = xmlEventReader.nextEvent();
	                       rule.setRuleCondition(xmlEvent.asCharacters().getData());
	                   }
	                   //parsing the rule Type (notification or command) from consequence from xml
	                  else if(startElement.getName().getLocalPart().equals("ruleConsequence")){
	                	  	Attribute ruleTypeAttr = startElement.getAttributeByName(new QName("ruleType"));
	                	  	if(ruleTypeAttr!= null)
	                	  		rule.setRuleType(ruleTypeAttr.getValue());
	                   }
	                   //parsing the userId variable from xml
	                   else if(startElement.getName().getLocalPart().equals("user")){
	                       Attribute userAttr = startElement.getAttributeByName(new QName("userid"));
	                       if(userAttr != null){
	                       rule.setUserId(Integer.parseInt(userAttr.getValue()));
	                       }
	                   }
	                   //parsing the containerID from xml 
	                   else if(startElement.getName().getLocalPart().equals("container")){
	                       Attribute containerAttr = startElement.getAttributeByName(new QName("containerid"));
	                       if(containerAttr != null){
	                       rule.setContainerId(containerAttr.getValue());
	                       }
	                   }
	                   // parsing the notification type ( mail, tweet, SMS) and save it in a string variable
	                  else if(startElement.getName().getLocalPart().equals("notification")){
	                	  Attribute notificationTypeAttr = startElement.getAttributeByName(new QName("notifyType"));
	                	  notificationTypeAttr.getValue();
	                	  if ( notificationTypeAttr.getValue().equals("mail"))
	                		  rule.setNotificationType(notificationTypeAttr.getValue().toString());
	                	  else if (notificationTypeAttr.getValue().equals("tweet"))
	                		  rule.setNotificationType(notificationTypeAttr.getValue().toString());
	                	  else if (notificationTypeAttr.getValue().equals("SMS"))
	                		  rule.setNotificationType(notificationTypeAttr.getValue().toString());
	                	  
	                	  if(notificationTypeAttr.getValue().equals("mail") || 
	                			  notificationTypeAttr.getValue().equals("tweet")||
	                			  notificationTypeAttr.getValue().equals("SMS")){
	                		  notifyType = notificationTypeAttr.getValue().toString();
//	                		  System.out.println (notifyType);
//	                		  rule.setNotificationType(notifyType);
	                	 }
	                  }
	                   // parsing the notification contents based on the notification type
	                  else if (startElement.getName().getLocalPart().equals("content")){
	                		xmlEvent = xmlEventReader.nextEvent();
	                		if (notifyType.equals("mail"))
	                			rule.setMailNotifyContent(xmlEvent.asCharacters().getData());
	                		else if (notifyType.equals("tweet"))
	                			rule.setTweetNotifyContent(xmlEvent.asCharacters().getData());
	                		else  if (notifyType.equals("SMS"))
	                			rule.setSMSNotifyContent(xmlEvent.asCharacters().getData());
	                		
	                  }
	                   //parsing the command tag 
	                  else if(startElement.getName().getLocalPart().equals("command")){
	                       xmlEvent = xmlEventReader.nextEvent();
	                       rule.setCommand(xmlEvent.asCharacters().getData());
	                   }
	                 }
	               //if rule end element is reached, add rule object to list
	               if(xmlEvent.isEndElement()){
	                   EndElement endElement = xmlEvent.asEndElement();
	                   if(endElement.getName().getLocalPart().equals("rule")){
	                       ruleList.add(rule);
	                     }
	               }
	            }
	             
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ruleList;
	    }

}
