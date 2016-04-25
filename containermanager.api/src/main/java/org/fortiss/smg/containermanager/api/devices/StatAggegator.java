package org.fortiss.smg.containermanager.api.devices;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.math3.stat.descriptive.StatisticalSummaryValues;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class StatAggegator {

	  public static StatisticalSummaryValues aggregate(Collection<StatisticalSummaryValues> statistics) {
	        if (statistics == null) {
	            return null;
	        }
	        Iterator<StatisticalSummaryValues> iterator = statistics.iterator();
	        if (!iterator.hasNext()) {
	            return null;
	        }
	        StatisticalSummaryValues current = iterator.next();
	        long n = current.getN();
	        double min = current.getMin();
	        double sum = current.getSum();
	        double max = current.getMax();
	        double m2 = 0; //current.getSecondMoment();
	        double mean = current.getMean();
	        while (iterator.hasNext()) {
	            current = iterator.next();
	            if (current.getMin() < min || Double.isNaN(min)) {
	                min = current.getMin();
	            }
	            if (current.getMax() > max || Double.isNaN(max)) {
	                max = current.getMax();
	            }
	            sum += current.getSum();
	            final double oldN = n;
	            final double curN = current.getN();
	            n += curN;
	            final double meanDiff = current.getMean() - mean;
	            mean = sum / n;
	            m2 = m2 + 0 +//current.getSecondMoment() + 
	            		meanDiff * meanDiff * oldN * curN / n;
	        }
	        final double variance;
	        if (n == 0) {
	            variance = Double.NaN;
	        } else if (n == 1) {
	            variance = 0d;
	        } else {
	            variance = m2 / (n - 1);
	        }
	        return new StatisticalSummaryValues(mean, variance, n, max, min, sum);
	    }
	
}
