/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.remoteframework.test.rabbit.schemas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
public class JSONTestImpl implements JSONTestInterface {

        public boolean called;

		public String greeting(String name) {
            System.out.println("Processing greeting");
            return "Hello, " + name + ", from JSON-RPC over AMQP!";
        }

        public int sum(List<Integer> values) {
            System.out.println("Processing sum");
            int s = 0;
            for (int i: values) { s += i; }
            return s;
        }

		public DoubleDataPoint getValues(int a, long b) {
			DoubleDataPoint d = new DoubleDataPoint((int)Math.abs(a - b),"Days: " +b, b*b );
			return d;
		}
		
		public List<DoubleDataPoint> getValueData(int a, DoubleDataPoint b) {
			DoubleDataPoint d = new DoubleDataPoint((int)Math.abs(a - 1),"Days: " +b, 33);

			ArrayList<DoubleDataPoint> arr = new ArrayList<DoubleDataPoint>();
			arr.add(d);
			arr.add(b);
			arr.add(d);
			return arr;
		}

		public void test(boolean test) {
			System.out.println("Test: " + test);
			if(test){
				called = true;
			}
		}

		public long dummyCall() {
			return  12345679123L;
		}
		
		public int timeOut(int t){
			try {
				Thread.sleep(t);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return t;
		}

		public int getMeBack(int i) {
			return i;
		}

		@Override
		public int testZooParameter(Zoo foo) throws TimeoutException {
			
			return ((Dog) foo).id;
			
		}

		@Override
		public Zoo testZooRetun() throws TimeoutException {
			return new Cat("kitKat");
		}
    }
