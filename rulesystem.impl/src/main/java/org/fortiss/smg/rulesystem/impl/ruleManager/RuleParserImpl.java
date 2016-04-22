package org.fortiss.smg.rulesystem.impl.ruleManager;

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

import org.fortiss.smg.rulesystem.api.Rule;


public class RuleParserImpl {
	static final String name = "name";
	static final String rule = "rule";
	static final String salience = "salience";
	static final String duration = "duration";
	static final String noLoop = "no-loop";
	static final String agendaGroup = "agenda-group";
	static final String ruleCondition = "ruleCondition";
	static String xmlResourcesPath = "/opt/felix/rules.xml";

	public RuleParserImpl() {
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			xmlResourcesPath = "c:/opt/felix/rules.xml";
		}
		List<Rule> ruleList = parseXML(xmlResourcesPath);
	}	
	/**
	 * @param fileName
	 * @return
	 */
	public static List<Rule> parseXML(String fileName) {
		List<Rule> ruleList = new ArrayList<Rule>();
		Rule rule= new Rule();
	
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		try {
			XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlResourcesPath));
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
					//parsing the value of agendaGroup variable from xml agendaGroup tag
					else if(startElement.getName().getLocalPart().equals("agenda-group")){
						xmlEvent = xmlEventReader.nextEvent();
						rule.setAgendaGroup((xmlEvent.asCharacters().getData()));
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
						xmlEvent = xmlEventReader.nextEvent();
						Attribute notificationTypeAttr = startElement.getAttributeByName(new QName("notificationType"));
						//notificationTypeAttr.getValue();
						rule.setNotification(true);
						if(notificationTypeAttr.getValue().equals("mail") || notificationTypeAttr.getValue().equals("tweet")||
								notificationTypeAttr.getValue().equals("SMS")){
							rule.setNotificationType(notificationTypeAttr.getValue().toString());

						}
					}
					// parsing the notification contents based on the notification type
					else if (startElement.getName().getLocalPart().equals("content")){
						xmlEvent = xmlEventReader.nextEvent();
						rule.setConsequence(xmlEvent.asCharacters().getData());
					}
					//parsing the command tag 
					else if(startElement.getName().getLocalPart().equals("command")){
						xmlEvent = xmlEventReader.nextEvent();
						rule.setCommand(true);
						
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
