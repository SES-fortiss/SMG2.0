package org.fortiss.smg.rulesystem.impl.executor;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.kie.api.runtime.rule.AccumulateFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Compare implements AccumulateFunction {
	private static final Logger logger = LoggerFactory.getLogger(Compare.class);

	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		
	}
	
	public Boolean test() {
		return true;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void accumulate(Serializable arg0, Object arg1) {
		ContextData data = (ContextData) arg0;
		logger.debug("accumulating {}", arg1);
		double dblValue = (Double) arg1;
		data.values.add(dblValue);
		data.lastReceivedValue = dblValue;		
	}

	public Serializable createContext() {
		return new ContextData();		
	}

	public Object getResult(Serializable arg0) throws Exception {
		logger.debug("wants results...");
		ContextData data = (ContextData) arg0;
		
		Double previousVal = 0.0;
		Boolean order= false;
		for(Iterator<Double> iter = data.values.iterator(); iter.hasNext();)
		{
			Double CurrentVal = iter.next();
			double temp = CurrentVal - previousVal; 
			System.out.println("CurrentVal: " +CurrentVal + " previousVal: " +previousVal + "difference: " + temp);		
			if ( (temp > -3000 )&& (temp < -50 )){
				order = true;
				System.out.println( " order: " +order);
			}
			else{
				order = false;
			}
			previousVal = CurrentVal;
		}	
		return new Boolean(order);
	}

	public void init(Serializable arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void reverse(Serializable arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean supportsReverse() {
		// TODO Auto-generated method stub
		return false;
	}
	private static class ContextData implements Serializable {
		private static final long serialVersionUID = -4156068641643641478L;
		public Collection<Double> values = new ArrayList<Double>();
		public double lastReceivedValue = 0;
	}
	@Override
	public Class<?> getResultType() {
		// TODO Auto-generated method stub
		return null;
	}
}
