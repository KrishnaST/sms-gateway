package com.sil.smsinterface.model;


public class Response {

	public long id;
	public String responsecode;
	public String responseMessage;
	
	public Response() {}

	public Response(String responsecode, String responseMessage) {
		this.responsecode = responsecode;
		this.responseMessage = responseMessage;
	}

	public Response(long id, String responsecode, String responseMessage) {
		this.id = id;
		this.responsecode = responsecode;
		this.responseMessage = responseMessage;
	}
	
}
