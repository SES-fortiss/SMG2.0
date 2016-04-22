package org.fortiss.smg.websocket.test.utils;

import static org.junit.Assert.assertEquals;

import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.websocket.api.shared.communication.Request;
import org.fortiss.smg.websocket.api.shared.communication.RequestType;
import org.fortiss.smg.websocket.api.shared.subscribe.SubscribeData;
import org.fortiss.smg.websocket.api.shared.subscribe.UpdateDeviceData;
import org.fortiss.smg.websocket.impl.usermanagement.User;
import org.fortiss.smg.websocket.impl.utils.RequestDeserializer;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RequestDeserializerTest {

	@Before
	public void setUp() throws Exception {

	}

	
	public Gson getGson() {
		GsonBuilder gson_builder = new GsonBuilder();
		gson_builder.registerTypeAdapter(Request.class,
				new RequestDeserializer());
		Gson gson = gson_builder.create();
		return gson;
	}

	@Test
	public void testDeserialize() {

		Gson gson = getGson();

		for (RequestType type : RequestType.values()) {

			if (type != RequestType.SEND_COMMAND) {

				String message;
				if (type == RequestType.LOGIN) {
					String user = "foo";
					String password = "bar";
					message = "{\"mID\":\"123\", \"data\":{\"user\":\"" + user
							+ "\", \"password\":\"" + password
							+ "\"}, \"type\":\"" + type.toString() + "\"}";
				} else {
					message = "{\"mID\":\"123\", \"data\":{\"devid\":\"bla\"}, \"type\":\""
							+ type.toString() + "\"}";
				}

				Request request = gson.fromJson(message, Request.class);
				assertEquals(request.getType(), type);
				assertEquals(request.getmId(), "" + 123);

				if (type == RequestType.LOGIN) {
					assertEquals(((User) request.getData()).getUserName(), "foo");
					assertEquals(((User) request.getData()).getPassword(),
							"bar");

				} else {
					assertEquals(((SubscribeData) request.getData()).getId(),
							"bla");
				}
			}
		}
	}

	@Test
	public void updateDeviceDataDoubleTest() {
		Gson gson = getGson();

		Request<UpdateDeviceData<Double>> r_double = new Request<UpdateDeviceData<Double>>();
		r_double.setmId("123");
		r_double.setType(RequestType.SEND_COMMAND);

		UpdateDeviceData<Double> r_double_data = new UpdateDeviceData<Double>();
		r_double_data.setConId("foo2");
		r_double_data.setType(SIDeviceType.Brightness);
		r_double_data.setCommand(2.0);
		r_double.setData(r_double_data);

		Gson gson2 = new Gson();
		String r_double_message = gson2.toJson(r_double);

		Request request = gson.fromJson(r_double_message, Request.class);
		assertEquals(request.getType(), RequestType.SEND_COMMAND);
		assertEquals(request.getmId(), "" + 123);

		assertEquals(((UpdateDeviceData<Double>) request.getData()).getType(),
				SIDeviceType.Brightness);
		double value = (Double) ((UpdateDeviceData<Double>) request.getData())
				.getCommand();
		assertEquals(value, 2.0, 0.001);
		assertEquals(((UpdateDeviceData<Double>) request.getData()).getConId(),
				"foo2");
		assertEquals(((UpdateDeviceData<Double>) request.getData()).getType(),
				SIDeviceType.Brightness);

	}

	

	@Test
	public void updateDeviceDataBooleanTest() {
		Gson gson = getGson();

		Request<UpdateDeviceData<Boolean>> r_double = new Request<UpdateDeviceData<Boolean>>();
		r_double.setmId("123");
		r_double.setType(RequestType.SEND_COMMAND);

		UpdateDeviceData<Boolean> r_double_data = new UpdateDeviceData<Boolean>();
		r_double_data.setConId("foo2");
		r_double_data.setCommand(0);
		r_double_data.setType(SIDeviceType.Door);
		r_double.setData(r_double_data);

		Gson gson2 = new Gson();
		String r_double_message = gson2.toJson(r_double);

		Request request = gson.fromJson(r_double_message, Request.class);
		assertEquals(request.getType(), RequestType.SEND_COMMAND);
		assertEquals(request.getmId(), "" + 123);

		double value = (double) ((UpdateDeviceData<Double>) request
				.getData()).getCommand();
		assertEquals(((UpdateDeviceData<Boolean>) request.getData()).getConId(),
				"foo2");
		assertEquals(((UpdateDeviceData<Boolean>) request.getData()).getType(),
				SIDeviceType.Door);

	}

	
}
