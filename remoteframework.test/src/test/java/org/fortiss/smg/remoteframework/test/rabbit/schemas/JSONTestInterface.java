package org.fortiss.smg.remoteframework.test.rabbit.schemas;

import java.util.List;
import java.util.concurrent.TimeoutException;
public interface JSONTestInterface {
        public String greeting(String name) throws TimeoutException;
        public int sum(List<Integer> values) throws TimeoutException;
        public DoubleDataPoint getValues(int a, long b) throws TimeoutException;
        public List<DoubleDataPoint> getValueData(int a, DoubleDataPoint b) throws TimeoutException;
        public void test(boolean test) throws TimeoutException;
        public long dummyCall() throws TimeoutException;
        public int timeOut(int t) throws TimeoutException;
        public int getMeBack(int i);
        
        public int testZooParameter(Zoo foo) throws TimeoutException;
        public Zoo testZooRetun() throws TimeoutException;
}