package org.fortiss.smg.rulesystem.api;


public class Rule {

	protected int id;
	protected String name;
	protected int frequency;
	protected String condition;
	protected String consequence;
	public int userId;
	public String containerId;
	protected String ruleType;
	protected int salience;
	protected boolean command;
	protected String agendaGroup;
	protected boolean notification;
	protected String notificationType;
	

	public int getRuleId() {
		return id;
	}

	public void setRuleId(int value) {
		this.id = value;

	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}


	public int getCron() {
		return frequency;
	}


	public void setCron(int duration) {
		this.frequency = duration;

	}


	public String getRuleCondition() {
		return condition;
	}

	public void setRuleCondition(String value) {
		this.condition = value;

	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;

	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;		
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;

	}

	public int getSalience() {
		return salience;
	}

	public void setSalience(int salience) {
		this.salience = salience;		
	}

	public boolean getCommand() {
		return command;
	}

	public void setCommand(boolean command) {
		this.command = command;
	}

	public boolean getNotification() {
		return notification;
	}

	public void setNotification(boolean notification) {
		this.notification = notification;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getConsequence() {
		return consequence;
	}

	public void setConsequence(String consequence) {
		this.consequence = consequence;
	}
	
	public String getAgendaGroup() {
		return agendaGroup;
	}

	public void setAgendaGroup(String agendaGroup) {
		this.agendaGroup = agendaGroup;
	}
	
	public String toDRL(){
		
		return "\n\nrule " + "\"" + name + "\"" +
				"\nagenda-group " + "\"" + agendaGroup + "\"" +
				"\nwhen " + condition +
				"\nthen " + consequence + ";" +
				"\nend";
	}
}
