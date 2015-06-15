/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.impl;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.fortiss.smg.rulesystem.api.RuleInterface;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RuleInterface", propOrder = { "id", "name", "cron", "ruleCondition", "consequence", "userId", "containerId", "ruleType" })
public class Rule implements RuleInterface {

	protected int id;
	protected String name;
	protected int duration;
	@XmlElement(required = true)
	protected String ruleCondition;
	@XmlElement(required = true)
	protected String consequence; 
	@XmlElement(required = true)
	public int userId;
	@XmlElement(required = true)
	public String containerId;
	@XmlElement(required = true)
	protected String ruleType;
	protected int salience;
	protected String command;
	protected String notificationType;
	private String mailNotifyContent;
	private String smsNotifyContent;
	private String tweetNotifyContent;	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int value) {
		this.id = value;
		
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String value) {
		this.name = value;
	}
	
	@Override
	public int getCron() {
		return duration;
	}
	
	@Override
	public void setCron(int duration) {
		this.duration = duration;
		
	}
	@Override
	public String getConsequence() {
		return consequence;
	}
	@Override
	public void setConsequence(String value) {
		this.consequence = value;
		
	}

	@Override
	public String getRuleCondition() {
		return ruleCondition;
	}


	@Override
	public void setRuleCondition(String value) {
		this.ruleCondition = value;
		
	}
	
	@Override
	public String getRuleType() {
		return ruleType;
	}

	@Override
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
		
	}
	@Override
	public long getUserId() {
		return userId;
	}
	@Override
	public void setUserId(int userId) {
		this.userId = userId;		
	}
	@Override
	public String getContainerId() {
		return containerId;
	}
	@Override
	public void setContainerId(String containerId) {
		this.containerId = containerId;
		
	}


	@Override
	public String getRuleString() {
		StringBuilder sb = new StringBuilder();
		sb.append("rule \"");
		sb.append("rule");
		sb.append(id);
		sb.append(": ");
		sb.append(name);
		sb.append("\"\n");
		if (duration != 0 ) {
			sb.append(duration);
			sb.append("\n");
		}
		sb.append("when\n");
		sb.append(ruleCondition);
		sb.append("\n");
		sb.append(userId);
		sb.append("\n");
		sb.append(containerId);
		sb.append("\n");
		sb.append(ruleType);
		sb.append("\n");
		sb.append("then\n");
		sb.append(consequence);
		sb.append("\n");
		sb.append("end");
		return sb.toString();
	}
	@Override
	public HashMap<String, Object> serializeRule() {
		HashMap<String, Object> ruleData = new HashMap<String, Object>();
		ruleData.put("ruleName", this.name);
		ruleData.put("cron", this.duration);
		ruleData.put("condition", this.ruleCondition);
		ruleData.put("userId", this.userId);
		ruleData.put("containerID", this.containerId);
		ruleData.put("ruleType", this.ruleType);
		
		return ruleData;
	}

    public String toString() {
        return "Rule:: Name=" + this.name + 
//        		" RulePriority="+ this.salience + 
        		" Cron =" + this.duration +
        		" RuleCondition=" + this.ruleCondition +
        		" UserId =" + this.userId +
        		" ContainerId =" + this.containerId +
        		" RuleType="+ this.ruleType +
        		" notificationType=" + this.notificationType +
        		" mailNotifyContent=" + this.mailNotifyContent +
        		" SMSNotifyContent="+this.smsNotifyContent +
        		" tweetNotifyContent=" + this.tweetNotifyContent +
        		" Command=" +this.command ;
        
    }
	@Override
	public int getSalience() {
		return salience;
	}
	@Override
	public void setSalience(int salience) {
		this.salience = salience;
		
	}
	@Override
	public String getCommand() {
		return command;
	}
	@Override
	public void setCommand(String command) {
		this.command = command;
		
	}
	
	@Override
	public String getMailNotifyContent() {
		return mailNotifyContent;
	}
	@Override
	public void setMailNotifyContent(String mailNotifyContent) {
		this.mailNotifyContent= mailNotifyContent;
		
	}

	@Override
	public String getSMSNotifyContent() {
		return smsNotifyContent;
	}
	@Override
	public void setSMSNotifyContent(String smsNotifyContent) {
		this.smsNotifyContent= smsNotifyContent;
		
	}
	@Override
	public String getTweetNotifyContent() {
		return tweetNotifyContent;
	}
	@Override
	public void setTweetNotifyContent(String tweetNotifyContent) {
		this.tweetNotifyContent = tweetNotifyContent;
	}
	@Override
	public String getNotificationType() {
		return notificationType;
	}
	@Override
	public void setNotificationType(String notificatonType) {
		this.notificationType = notificationType;
	}

}
