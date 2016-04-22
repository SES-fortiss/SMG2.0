package org.fortiss.smg.websocket.impl.utils;

import java.lang.reflect.Type;

import org.fortiss.smg.usermanager.api.User;
import org.fortiss.smg.websocket.api.shared.communication.Request;
import org.fortiss.smg.websocket.api.shared.communication.RequestType;
import org.fortiss.smg.websocket.api.shared.subscribe.SubscribeData;
import org.fortiss.smg.websocket.api.shared.subscribe.UpdateDeviceData;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class RequestDeserializer implements JsonDeserializer<Request> {

	public Request deserialize(JsonElement obj, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {

		
		String mID;
		try {
			mID = obj.getAsJsonObject().get("mID").getAsString();
		} catch (Exception e) {
			e.printStackTrace();
			return new Request<Object>(null, null, "");
		}

		RequestType r;
		try {
			r = arg2.deserialize(obj.getAsJsonObject().get("type"),
					RequestType.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new Request<Object>(null, null, mID);
		}

		
		if(r == null){
			return new Request<Object>(null, null, mID);
		}
		
		try {
			switch (r) {

			case LOGIN:
				User user = arg2.deserialize(obj.getAsJsonObject().get("data"),
						User.class);
				return new Request<User>(r, user, mID);

			case SUBSCRIBE_DEVICE: //TODO
			case SUBSCRIBE_ROOM:
			case UNSUBSCRIBE_DEVICE:
			case UNSUBSCRIBE_ROOM:
			//case GET_CHILDREN_IDS_OF:
			case GET_ROOM_MAP:
				SubscribeData device = arg2.deserialize(obj.getAsJsonObject()
						.get("data"), SubscribeData.class);
				return new Request<SubscribeData>(r, device, mID);
			case SEND_COMMAND:
				
				UpdateDeviceData update = arg2.deserialize(obj
						.getAsJsonObject().get("data"), UpdateDeviceData.class);
				//SIDeviceType type= update.getType();
				
				Object value;
				
				//somehow this became useless
				/*switch(type){
				case BOOLEAN:
					value =  update.getValue();
					break;
				case DOUBLE:
					value = update.getValue();
					break;
				case STRING:
					value =  update.getValue();
					break;
				case TOGGLE:
					value =  update.getValue();
					break;
				}
				*/
				Request<UpdateDeviceData> re= new Request<UpdateDeviceData>(r, update, mID);
				return re;
	
			default:
				// error
				break;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Request<Object>(null, null, mID);
		}
		return null;
	}
}