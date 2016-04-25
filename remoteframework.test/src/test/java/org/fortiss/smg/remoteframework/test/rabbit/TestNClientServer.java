package org.fortiss.smg.remoteframework.test.rabbit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.remoteframework.lib.RabbitRPCProxy;
import org.fortiss.smg.remoteframework.lib.RabbitRPCServer;
import org.fortiss.smg.remoteframework.lib.except.JsonRpcException;
import org.fortiss.smg.remoteframework.test.rabbit.schemas.JSONTestImpl;
import org.fortiss.smg.remoteframework.test.rabbit.schemas.JSONTestInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestNClientServer {


	private JSONTestInterface service;
	private JSONTestInterface service2;
	private JSONTestImpl impl;
	private RabbitRPCServer<JSONTestInterface> server;
	private int s2;
	private RabbitRPCProxy<JSONTestInterface> client2;
	private RabbitRPCProxy<JSONTestInterface> client;

	@Before
	public void setUp() throws IOException, JsonRpcException, TimeoutException, InterruptedException {

		impl = new JSONTestImpl();
		String queueName = "Test-Queue";
		// destroy existing messages
		server = new RabbitRPCServer<JSONTestInterface>(
				JSONTestInterface.class, impl, queueName);
		server.init();
		
		// destroy exisiting messages
		Thread.sleep(2000);

		client = new RabbitRPCProxy<JSONTestInterface>(JSONTestInterface.class, queueName, 1000);
		service = client.init();
		
		client2 = new RabbitRPCProxy<JSONTestInterface>(JSONTestInterface.class, queueName, 1000);
		service2 = client2.init();
	}

	@After
	public void tearDown() throws IOException {
		try {
			server.destroy();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		client.destroy();
		client2.destroy();
	}

	
	@Test(timeout=5000) 
	public void testResponseLater() throws TimeoutException, InterruptedException{
		assertEquals(10, service2.getMeBack(10));
		assertEquals(11, service2.getMeBack(11));
		
		s2 = 10;
		Thread thread= new Thread(new Runnable(){

			public void run() {
				try {
					s2 = service2.timeOut(800);
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		thread.start();
		assertEquals(200, service.timeOut(200));
		while(s2 ==10){
			Thread.sleep(10);
		}
		assertEquals(800, s2);
	}
	
	@Test(timeout=5000) 
	public void testResponseEarlier() throws TimeoutException, InterruptedException{
		assertEquals(10, service2.getMeBack(10));
		assertEquals(11, service2.getMeBack(11));
		
		s2 = 10;
		Thread thread= new Thread(new Runnable(){

			public void run() {
				try {
					s2 = service2.timeOut(20);
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		thread.start();
		assertEquals(200, service.timeOut(200));
		while(s2 ==10){
			Thread.sleep(10);
		}
		assertEquals(20, s2);
	}
	
	@Test(timeout=5000) 
	public void testResponseSpamming() throws TimeoutException, InterruptedException{
		assertEquals(10, service2.getMeBack(10));
		assertEquals(11, service2.getMeBack(11));
		
		s2 = 10;
		Thread thread= new Thread(new Runnable(){

			public void run() {
				try {
					for(int i=0; i< 10; i++){
						s2 = service2.timeOut(20);
					}
					s2 = service2.timeOut(30);
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		thread.start();
		assertEquals(200, service.timeOut(200));
		while(s2 <= 20){
			Thread.sleep(10);
		}
		assertEquals(30, s2);
	}
	
	

}
