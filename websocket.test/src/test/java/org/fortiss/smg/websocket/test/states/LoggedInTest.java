package org.fortiss.smg.websocket.test.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
//import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.websocket.api.shared.communication.Request;
import org.fortiss.smg.websocket.api.shared.communication.RequestType;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.api.shared.communication.ResponseType;
import org.fortiss.smg.websocket.api.shared.communication.Status;
import org.fortiss.smg.websocket.api.shared.subscribe.SubscribeData;
import org.fortiss.smg.websocket.api.shared.subscribe.UpdateDeviceData;
import org.fortiss.smg.websocket.impl.communication.statemachine.ServerStateMachine;
import org.fortiss.smg.websocket.impl.communication.statemachine.states.LoggedIn;
import org.fortiss.smg.websocket.impl.usermanagement.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoggedInTest {

	private ServerStateMachine smachine;

	@BeforeClass
	public static void setUp() throws Exception {

	}

	@Before
	public void start() {
		testLogin();
	}

	/*
	 * Tests Connected State
	 */
	@Test
	public void testLogin() {
		smachine = new ServerStateMachine("abc");// session id
		Request r1 = new Request<SubscribeData>();
		User user = new User();
		user.setPassword("dev");
		user.setUserName("dev");
		r1.setData(user);
		r1.setType(RequestType.LOGIN);
		Response<String> resp1 = smachine.process(r1);

		assertEquals(resp1.getResponsetype(), ResponseType.LOGIN);
		assertEquals(resp1.getStatus(), Status.LOGGED_IN);
		assertEquals(resp1.getResponse(), "LOGGED_IN");
		assertTrue(smachine.currentState() instanceof LoggedIn);
	}

	/*
	 * Tests Connected State
	 */
	@Test
	public void testLoginFail() {
		ServerStateMachine smachine2 = new ServerStateMachine("abc");
		Request r1 = new Request<SubscribeData>();
		r1.setmId("456");
		User user = new User();
		user.setPassword("dev2");
		user.setUserName("dev2");
		r1.setData(user);
		r1.setType(RequestType.LOGIN);
		smachine2.process(r1);
		assertFalse(smachine2.currentState() instanceof LoggedIn);
	}

	@Test
	public void testSubscribe() {
		// Login has to be executed before
		Request r1 = new Request<SubscribeData>();
		SubscribeData r1_data = new SubscribeData();
		r1_data.setId("foo");
		r1.setData(r1_data);
		r1.setType(RequestType.SUBSCRIBE_DEVICE);
		Response resp1 = smachine.process(r1);
		assertEquals(resp1.getResponsetype(),
				ResponseType.SUBSCRIBE_DEVICE);
		assertEquals(resp1.getStatus(), Status.OK);
	}

	@Test
	public void testSubscribeAll() {
		Request r1 = new Request<SubscribeData>();
		SubscribeData r1_data = new SubscribeData();
		r1_data.setId("foo");//containerId, container != DEVICE
		r1.setData(r1_data);
		r1.setType(RequestType.SUBSCRIBE_ROOM);
		Response resp1 = smachine.process(r1);
		assertEquals(resp1.getResponsetype(),
				ResponseType.SUBSCRIBE_ROOM);
		assertEquals(resp1.getStatus(), Status.OK);
	}

	@Test
	public void testUnsubscribe() {
		Request r1 = new Request<SubscribeData>();
		SubscribeData r1_data = new SubscribeData();
		r1_data.setId("foo");
		r1.setData(r1_data);
		r1.setType(RequestType.UNSUBSCRIBE_DEVICE);
		Response resp1 = smachine.process(r1);
		assertEquals(resp1.getResponsetype(), ResponseType.UNSUBSCRIBE);
		assertEquals(resp1.getStatus(), Status.OK);
	}

	@Test
	public void testUnsubscribeAll() {
		Request r1 = new Request<SubscribeData>();
		SubscribeData r1_data = new SubscribeData();
		r1_data.setId("foo");
		r1.setData(r1_data);
		r1.setType(RequestType.UNSUBSCRIBE_ROOM);
		Response resp1 = smachine.process(r1);
		assertEquals(resp1.getResponsetype(), ResponseType.UNSUBSCRIBE);
		assertEquals(resp1.getStatus(), Status.OK);
	}

	@Test
	public void testSendCommand() {

		Request<UpdateDeviceData<Double>> r1 = new Request<UpdateDeviceData<Double>>();
		UpdateDeviceData<Double> r1_data = new UpdateDeviceData<Double>();
		r1_data.setConId("foo");
		r1_data.setType(SIDeviceType.Noise);
		r1_data.setCommand(1.234);
		r1.setData(r1_data);
		r1.setType(RequestType.SEND_COMMAND);
		Response resp1 = smachine.process(r1);
		assertEquals(resp1.getResponsetype(), ResponseType.SEND_COMMAND);
		assertEquals(resp1.getStatus(), Status.OK);
	}

}
