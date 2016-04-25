package org.fortiss.smg.informationbroker.api;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonAutoDetect(fieldVisibility = Visibility.ANY, creatorVisibility = Visibility.ANY)
@XmlRootElement(name="double")
public class DoublePoint extends DataPoint {

	@JsonProperty("value")
	private Double value;
	@JsonProperty("maxAbsError")
	private double maxAbsError;

	DoublePoint() {
		super(0);	
	}

	public DoublePoint(@JsonProperty("value") Double value,
			@JsonProperty("maxAbsError") double maxAbsError,
			@JsonProperty("time") long time) {
		super(time);
		this.value = value;
		this.maxAbsError = maxAbsError;
	}

	@Override
	public String toString() {
		return "DoublePoint [value=" + value + ", maxAbsError=" + maxAbsError
				+ ", time=" + time + "]";
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public double getMaxAbsError() {
		return maxAbsError;
	}

	public void setMaxAbsError(Double maxAbsError) {
		this.maxAbsError = maxAbsError;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DoublePoint other = (DoublePoint) obj;
		if(this.time != other.time){
			return false;
		}
		if (Double.doubleToLongBits(maxAbsError) != Double
				.doubleToLongBits(other.maxAbsError))
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}

}
