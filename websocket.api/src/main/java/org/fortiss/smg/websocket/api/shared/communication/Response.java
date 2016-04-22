package org.fortiss.smg.websocket.api.shared.communication;


public class Response<T> {
	private String mId;
	private T response;
	private int status;
	private ResponseType responsetype;

	Response(){
		mId="-1";
		response=(T) "";
		status = -1;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Response(T json) {
		response = json;
	}

	public Response(T json, String id) {
		response = json;
		this.mId= id;
	}

	public Response(T o, int status, String messageID) {
		this(o, messageID);
		this.status = status;
	}

	public Response(T o, int status) {
		response = o;
		this.status= status;
	}

	public Response(T o, int status, ResponseType type) {
		this.response = o;
		this.status = status;
		this.responsetype = type;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	public ResponseType getResponsetype() {
		return responsetype;
	}

	public void setResponsetype(ResponseType responsetype) {
		this.responsetype = responsetype;
	}
}
