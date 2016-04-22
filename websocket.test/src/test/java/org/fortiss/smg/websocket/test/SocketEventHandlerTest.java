package org.fortiss.smg.websocket.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.fortiss.smg.websocket.api.shared.RoomMap;
import org.fortiss.smg.websocket.api.shared.communication.Response;
import org.fortiss.smg.websocket.api.shared.communication.ResponseType;
import org.fortiss.smg.websocket.api.shared.schema.Device;
import org.fortiss.smg.websocket.api.shared.subscribe.APIDevice;
import org.fortiss.smg.websocket.impl.WebSocketActivator;
import org.fortiss.smg.websocket.impl.WebSocketImpl;
import org.fortiss.smg.websocket.impl.communication.EchoBroadcastWebSocket;
import org.fortiss.smg.websocket.impl.communication.SocketServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;

public class SocketEventHandlerTest {

	private static ConcurrentLinkedQueue<EchoBroadcastWebSocket> broadcast;
	// private static EventHandler mockEventHandler = mock(EventHandler.class);
	/*
	 * private static LocationMgr mockLocationMgr = Mockito
	 * .mock(LocationMgr.class); private static PersistenceInterface mockPersis
	 * = Mockito .mock(PersistenceInterface.class);
	 */
	private static SocketServer server;
	private static WebSocketImpl socket;

	@AfterClass
	public static void end() {
		/*if (SocketEventHandlerTest.server != null) {
			try {
				//SocketEventHandlerTest.server.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// fail("Unable to stop Websocket.");
			}
			SocketEventHandlerTest.server.destroy();
		}*/
	}

	@BeforeClass
	public static void oneTimeSetUp() {
		SocketEventHandlerTest.testServer();
	}

	public static void testServer() {
		SocketEventHandlerTest.server = null;

		try {
			// we need a fake event handlet for a proper startup
			/*
			 * SocketEventHandlerTest.socket = new WebSocketImpl() {
			 * 
			 * @Override public void activate() {
			 * 
			 * server = JMSHelpers.exposeServiceOnJMS(
			 * EventHandler.TOPIC_ADDRESS, new MockEventHandler(),
			 * EventHandler.class); persistenceHandler =
			 * SocketEventHandlerTest.mockPersis; locationManager =
			 * SocketEventHandlerTest.mockLocationMgr; APIDevice device =
			 * SocketEventHandlerTest.mockLocationMgr .getAPIDevice("root");
			 * 
			 * generate_sessions(); }
			 * 
			 * void generate_sessions() { // TODO Auto-generated method stub
			 * ConcurrentLinkedQueue<EchoBroadcastWebSocket> broadcast_new = new
			 * ConcurrentLinkedQueue<EchoBroadcastWebSocket>();
			 * 
			 * ServerStateMachine s1 = new ServerStateMachine( "test_session");
			 * EchoBroadcastWebSocket brc1 = new EchoBroadcastWebSocket(
			 * _broadcast); brc1.setServerState(s1); Connection conn =
			 * Mockito.mock(Connection.class); brc1.set_connection(conn);
			 * broadcast_new.add(brc1);
			 * 
			 * ServerStateMachine s2 = new ServerStateMachine( "test_ession");
			 * EchoBroadcastWebSocket brc2 = new EchoBroadcastWebSocket(
			 * _broadcast); brc2.setServerState(s2); brc2.set_connection(conn);
			 * broadcast_new.add(brc2);
			 * 
			 * // server = mock(org.apache.cxf.endpoint.Server.class);
			 * 
			 * // stubbing _broadcast = broadcast_new; }
			 * 
			 * @Override public void onDoubleEventReceived(DoubleEvent ev,
			 * DeviceId dev, String client) { super.onDoubleEventReceived(ev,
			 * dev, client); }
			 * 
			 * @Override public void onDeviceEventReceived(DeviceEvent ev,
			 * DeviceId dev, String client) { super.onDeviceEventReceived(ev,
			 * dev, client); } };
			 */
			int port = 7082 + (int) (Math.random() * 10);
			SocketEventHandlerTest.server = new WebSocketImpl().startServer(7070);
			/*try {
				SocketEventHandlerTest.socket.activate();// dispatcher
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail("Websocket does not start. Port might be blocked.");
		}
/*		SocketEventHandlerTest.broadcast = SocketEventHandlerTest.server
				.get_broadcast();
*/	}

	/*
	 * @Test public void testBooleanEvent() {
	 * SocketEventHandlerTest.socket.setSubscribedIDs(Multimaps
	 * .synchronizedMultimap(HashMultimap.<String, String> create()));
	 * testSubscribe(); testSubscribeAll();
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("root", true,
	 * UpdateType.BOOLEAN), 2);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc", true,
	 * UpdateType.BOOLEAN), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc1", true,
	 * UpdateType.BOOLEAN), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc2", true,
	 * UpdateType.BOOLEAN), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc3", true,
	 * UpdateType.BOOLEAN), 1); }
	 */

	@Test
	public void testCloseSession() {
		SocketEventHandlerTest.socket.setSubscribedIDs(Multimaps
				.synchronizedMultimap(HashMultimap.<String, String> create()));
		testSubscribe();
		SocketEventHandlerTest.socket.closeSession("test_session");
		Assert.assertFalse(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsValue("test_session"));
	}

	/*
	 * @Test public void testDoubleEvent() {
	 * SocketEventHandlerTest.socket.setSubscribedIDs(Multimaps
	 * .synchronizedMultimap(HashMultimap.<String, String> create()));
	 * testSubscribe(); testSubscribeAll(); DoubleEvent ev = new
	 * DoubleEvent(2.0); ev.setAbsMaxError(0.01); ev.setUnit(SIUnitType.LUX);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("root", ev,
	 * UpdateType.DOUBLE), 2);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc", ev,
	 * UpdateType.DOUBLE), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc1", ev,
	 * UpdateType.DOUBLE), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc2", ev,
	 * UpdateType.DOUBLE), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc3", ev,
	 * UpdateType.DOUBLE), 1); }
	 */
	@Test
	public void testGetAllDevices() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetChildren() {
		List<Device> devs = new ArrayList<Device>();

		Device dev1 = new Device();
		dev1.setDeviceId("abc1");
		devs.add(dev1);

		Device dev2 = new Device();
		dev2.setDeviceId("abc2");
		devs.add(dev2);

		Device dev3 = new Device();
		dev3.setDeviceId("abc3");
		devs.add(dev3);

		/*
		 * Mockito.when(
		 * SocketEventHandlerTest.mockLocationMgr.getChildren("root", false,
		 * EdgeType.ASSOCIATED)).thenReturn(devs);
		 * 
		 * Response resp =
		 * SocketEventHandlerTest.socket.getChildrenResp("root");
		 * 
		 * Assert.assertEquals(resp.getResponsetype(),
		 * ResponseType.GET_CHILDREN_OF); Assert.assertEquals(resp.getStatus(),
		 * org.fortiss.smg.shared.api.communication.Status.OK);
		 * Assert.assertEquals(((List<Device>) resp.getResponse()).size(), 3);
		 * Assert.assertEquals(((List<Device>) resp.getResponse()).get(0)
		 * .getDeviceId(), "abc1"); Assert.assertEquals(((List<Device>)
		 * resp.getResponse()).get(1) .getDeviceId(), "abc2");
		 * Assert.assertEquals(((List<Device>) resp.getResponse()).get(2)
		 * .getDeviceId(), "abc3");
		 */
	}

	@Test
	public void testGetChildrenAll() {

		APIDevice root = new APIDevice("root");
		RoomMap devs = new RoomMap(root);

		APIDevice dev1 = new APIDevice("abc1");
		devs.getSubdevices().add(new RoomMap(dev1));

		APIDevice dev2 = new APIDevice("abc2");
		devs.getSubdevices().add(new RoomMap(dev2));

		APIDevice dev3 = new APIDevice("abc3");
		devs.getSubdevices().add(new RoomMap(dev3));

		/*
		 * Mockito.when(
		 * SocketEventHandlerTest.mockLocationMgr.getAPIRoomMap("root"))
		 * .thenReturn(devs);
		 * 
		 * Response resp = SocketEventHandlerTest.socket
		 * .getChildrenAllResp("root");
		 * 
		 * Assert.assertEquals(resp.getResponsetype(),
		 * ResponseType.GET_ALL_CHILDREN_OF);
		 * Assert.assertEquals(resp.getStatus(),
		 * org.fortiss.smartmicrogrid.shared.api.communication.Status.OK);
		 * Assert.assertEquals(((RoomMap) resp.getResponse()).getSubdevices()
		 * .size(), 3);
		 */
		/*
		 * Assert.assertEquals(((RoomMap)
		 * resp.getResponse()).getDevice().getId(), "root");
		 * 
		 * Assert.assertEquals( ((RoomMap)
		 * resp.getResponse()).getSubdevices().get(0) .getDevice().getId(),
		 * "abc1"); Assert.assertEquals( ((RoomMap)
		 * resp.getResponse()).getSubdevices().get(1) .getDevice().getId(),
		 * "abc2"); Assert.assertEquals( ((RoomMap)
		 * resp.getResponse()).getSubdevices().get(2) .getDevice().getId(),
		 * "abc3");
		 */

	}

	@Test
	public void testNewDeviceEvent() {
		// fail("Not yet implemented");
	}

	/*
	 * @Test public void testStringEvent() { // empty entries
	 * SocketEventHandlerTest.socket.setSubscribedIDs(Multimaps
	 * .synchronizedMultimap(HashMultimap.<String, String> create()));
	 * testSubscribe(); testSubscribeAll();
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("root",
	 * "Hallo", UpdateType.STRING), 2);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc",
	 * "Hallo", UpdateType.STRING), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc1",
	 * "Hallo", UpdateType.STRING), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc2",
	 * "Hallo", UpdateType.STRING), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc3",
	 * "Hallo", UpdateType.STRING), 1); }
	 */
	@Test
	public void testSubscribe() {

		/*
		 * Mockito.when(
		 * SocketEventHandlerTest.mockLocationMgr.getAPIDevice("root"))
		 * .thenReturn(new APIDevice("root"));
		 * 
		 * Response resp = SocketEventHandlerTest.socket.subscribe("root",
		 * "test_ession", true);
		 */
		/*
		 * Assert.assertEquals(((APIDevice) resp.getResponse()).getId(),
		 * "root");
		 * 
		 * // test if the value has been added
		 * Assert.assertTrue(SocketEventHandlerTest.socket.getSubscribedIDs()
		 * .containsEntry("root", "test_ession"));
		 * 
		 * Mockito.when(SocketEventHandlerTest.mockLocationMgr.getAPIDevice("abc"
		 * )) .thenReturn(new APIDevice("abc"));
		 * 
		 * Response resp2 = SocketEventHandlerTest.socket.subscribe("abc",
		 * "test_ession", true); Assert.assertEquals(((APIDevice)
		 * resp2.getResponse()).getId(), "abc");
		 */

		// test if the value has been added
		Assert.assertTrue(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("abc", "test_ession"));
	}

	@Test
	public void testSubscribeAll() {
		/*
		 * Mockito.when(
		 * SocketEventHandlerTest.mockLocationMgr.getAPIDevice("root"))
		 * .thenReturn(new APIDevice("root")); RoomMap map = new RoomMap(
		 * SocketEventHandlerTest.mockLocationMgr.getAPIDevice("root"));
		 */
		/*
		 * map.setSubdevices(new ArrayList<RoomMap>());
		 * map.getSubdevices().add(new RoomMap(new APIDevice("abc1")));
		 * map.getSubdevices().add(new RoomMap(new APIDevice("abc2")));
		 * map.getSubdevices().add(new RoomMap(new APIDevice("abc3")));
		 * 
		 * Mockito.when(
		 * SocketEventHandlerTest.mockLocationMgr.getAPIRoomMap("root"))
		 * .thenReturn(map);
		 */
		Response resp = SocketEventHandlerTest.socket.subscribeAll("root",
				"test_session");
		Assert.assertEquals(((RoomMap) resp.getResponse()).getDevice().getId(),
				"root");

		Assert.assertEquals(
				((RoomMap) resp.getResponse()).getSubdevices().get(0)
						.getDevice().getId(), "abc1");
		Assert.assertEquals(
				((RoomMap) resp.getResponse()).getSubdevices().get(1)
						.getDevice().getId(), "abc2");
		Assert.assertEquals(
				((RoomMap) resp.getResponse()).getSubdevices().get(2)
						.getDevice().getId(), "abc3");

		// Test if the entries have been successfully added to the database
		Assert.assertTrue(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsKey("root"));
		Assert.assertTrue(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsKey("abc1"));
		Assert.assertTrue(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsKey("abc2"));
		Assert.assertTrue(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsKey("abc3"));

		Assert.assertTrue(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("root", "test_session"));
		Assert.assertTrue(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("abc1", "test_session"));
		Assert.assertTrue(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("abc2", "test_session"));
		Assert.assertTrue(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("abc3", "test_session"));

	}

	/*
	 * @Test public void testToggleEvent() {
	 * SocketEventHandlerTest.socket.setSubscribedIDs(Multimaps
	 * .synchronizedMultimap(HashMultimap.<String, String> create()));
	 * testSubscribe(); testSubscribeAll();
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("root", null,
	 * UpdateType.TOGGLE), 2);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc", null,
	 * UpdateType.TOGGLE), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc1", null,
	 * UpdateType.TOGGLE), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc2", null,
	 * UpdateType.TOGGLE), 1);
	 * Assert.assertEquals(SocketEventHandlerTest.socket.sendEvent("abc3", null,
	 * UpdateType.TOGGLE), 1); }
	 */

	@Test
	public void testUnsubscribe() {
		testSubscribe();
		Response resp = SocketEventHandlerTest.socket.unsubscribe("root",
				"test_ession");

		Assert.assertEquals(resp.getResponsetype(),
				ResponseType.UNSUBSCRIBE);
		/*
		 * Assert.assertEquals(resp.getStatus(),
		 * org.fortiss.smartmicrogrid.shared.api.communication.Status.OK);
		 */
		Assert.assertEquals(resp.getResponse(), "OK");
		Assert.assertFalse(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("root", "test_ession"));

		Response resp2 = SocketEventHandlerTest.socket.unsubscribe("abc",
				"test_ession");

		Assert.assertEquals(resp2.getResponsetype(),
				ResponseType.UNSUBSCRIBE);
		/*
		 * Assert.assertEquals(resp2.getStatus(),
		 * org.fortiss.smartmicrogrid.shared.api.communication.Status.OK);
		 */
		Assert.assertEquals(resp2.getResponse(), "OK");
		Assert.assertFalse(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("abc", "test_ession"));

	}

	@Test
	public void testUnsubscribeAll() {
		testSubscribeAll();
		Response resp = SocketEventHandlerTest.socket
				.unsubscribeAll("test_session");

		Assert.assertEquals(resp.getResponsetype(),
				ResponseType.UNSUBSCRIBE);
		/*
		 * Assert.assertEquals(resp.getStatus(),
		 * org.fortiss.smartmicrogrid.shared.api.communication.Status.OK);
		 */
		Assert.assertEquals(resp.getResponse(), "OK");

		Assert.assertFalse(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("root", "test_session"));
		Assert.assertFalse(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("root", "abc1"));
		Assert.assertFalse(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("root", "abc2"));
		Assert.assertFalse(SocketEventHandlerTest.socket.getSubscribedIDs()
				.containsEntry("root", "abc3"));
	}

	/*
	 * @Test public void testUpdateDeviceBoolean() { Response resp =
	 * SocketEventHandlerTest.socket.updateDevice(
	 * "IPswitchQ?IPSwitch_Counter1", true, UpdateType.BOOLEAN, null);
	 * Assert.assertEquals(resp.getResponsetype(),
	 * ResponseType.UPDATE_DEVICE_DATA); Assert.assertEquals(resp.getStatus(),
	 * org.fortiss.smartmicrogrid.shared.api.communication.Status.OK);
	 * Assert.assertEquals(resp.getResponse(), "OK"); }
	 */
	/*
	 * @Test public void testUpdateDeviceDouble() { Response resp =
	 * SocketEventHandlerTest.socket.updateDevice(
	 * "IPswitchQ?IPSwitch_Counter1", 20.0, UpdateType.DOUBLE, SIUnitType.WH);
	 * Assert.assertEquals(resp.getResponsetype(),
	 * ResponseType.UPDATE_DEVICE_DATA); Assert.assertEquals(resp.getStatus(),
	 * org.fortiss.smartmicrogrid.shared.api.communication.Status.OK);
	 * Assert.assertEquals(resp.getResponse(), "OK"); }
	 * 
	 * @Test public void testUpdateDeviceString() { Response resp =
	 * SocketEventHandlerTest.socket
	 * .updateDevice("IPswitchQ?IPSwitch_Counter1", "Hello", UpdateType.STRING,
	 * null); Assert.assertEquals(resp.getResponsetype(),
	 * ResponseType.UPDATE_DEVICE_DATA); Assert.assertEquals(resp.getStatus(),
	 * org.fortiss.smartmicrogrid.shared.api.communication.Status.OK);
	 * Assert.assertEquals(resp.getResponse(), "OK"); }
	 * 
	 * @Test public void testUpdateDeviceToggle() { Response resp =
	 * SocketEventHandlerTest.socket.updateDevice(
	 * "IPswitchQ?IPSwitch_Counter1", true, UpdateType.TOGGLE, null);
	 * Assert.assertEquals(resp.getResponsetype(),
	 * ResponseType.UPDATE_DEVICE_DATA); Assert.assertEquals(resp.getStatus(),
	 * org.fortiss.smartmicrogrid.shared.api.communication.Status.OK);
	 * Assert.assertEquals(resp.getResponse(), "OK"); }
	 */

}
