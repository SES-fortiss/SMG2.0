package org.fortiss.smg.websocket.api.shared.communication;


public class Request<T> {
	private RequestType type;
	private T data;
	private String mID;
	
	public Request() {
		// TODO Auto-generated constructor stub
	}
	
	Request(RequestType type){
		this.type = type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setmId(String mId) {
		this.mID = mId;
	}

	public RequestType getType() {
		return type;
	}

	public T getData() {
		return data;
	}

	public String getmId() {
		return mID;
	}

	public Request(RequestType type, T data, String mId) {
		this.type =type;
		this.data = data;
		this.mID = mId;
	}
}
