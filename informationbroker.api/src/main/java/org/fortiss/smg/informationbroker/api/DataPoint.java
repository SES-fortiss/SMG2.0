package org.fortiss.smg.informationbroker.api;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlSchemaType;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility=Visibility.ANY, creatorVisibility=Visibility.ANY)
public class DataPoint{

	@JsonProperty("time")
	protected long time;

	
	DataPoint(){
		
	}

	public DataPoint(long time) {
		this.time = time;
	}


	public long getTime() {
		return time;
	}
	
	public Calendar getCalendarTime(){
		Calendar calendarTime = Calendar.getInstance();
		calendarTime.setTimeInMillis(time);
		return calendarTime;
	}
//	public Timestamp getTimestamp() {
//		return new Timestamp(time);
//	}
	
	
	/**
	 * in milliseconds
	 * @param timestamp
	 */
	public void setTime(long timestamp) {
		this.time = timestamp;
	}
	
	public void setTimestamp(Timestamp t){
		this.time =t.getTime();
	}
	
}
