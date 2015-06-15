/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.api;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

public interface RuleInterface {
	public String getRuleType(); 
	public void setRuleType(String ruleType) ;

	public long getUserId(); 
	public void setUserId(int userId) ;

	public String getContainerId();
	public void setContainerId(String containerId);

	public int getId();
	public void setId(int value);
	
	public String getName();
	public void setName(String value);
	
	public String getRuleCondition();
	public void setRuleCondition(String value);
	
	public void setCron(int duration);
	public int getCron();
	
	public String getConsequence();
	public void setConsequence(String value) ;
	
	public String getCommand();
	public void setCommand(String value) ;
	
	public String getRuleString();
	
	public HashMap<String, Object> serializeRule();
	
    public String toString();
    
	public int getSalience();
	public void setSalience(int value);
	
	public String getNotificationType();
	public void setNotificationType(String value);
	
	public String getMailNotifyContent();
	public void setMailNotifyContent(String value);
	
	public String getSMSNotifyContent();
	public void setSMSNotifyContent(String smsNotifyContent);
	
	public String getTweetNotifyContent();
	public void setTweetNotifyContent(String tweetNotifyContent);

}
