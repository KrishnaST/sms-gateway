package com.sil.smsinterface.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sil.smsinterface.model.Request;
import com.sil.smsinterface.model.Response;

@Path("sms")
public class SMSService {

	private static final ObjectMapper OBJECT_MAPPER 	= new ObjectMapper();
	private static final Response ERROR	 				= new Response("01", "error occured.");
	private static final Response VALIDATION_FAILED 	= new Response("01", "validation failed.");
	//private static final Response INVALID_CREDENTIALS 	= new Response("01", "invalid credentials.");
	private static final Response INVALID_BANK_ID 		= new Response("01", "invalid bank id");
	
	static {
		OBJECT_MAPPER.writerWithDefaultPrettyPrinter();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static final Response send(Request request) {
		System.out.println(objectToString(request));
		try
		{
			boolean valid = request.validate();
			if(!valid) return VALIDATION_FAILED;
			RetroClient client = RetroClient.clients.get(request.bankid);
			if(client == null) return INVALID_BANK_ID;
			//if(!(""+client.config.password).equalsIgnoreCase(request.password)) return INVALID_CREDENTIALS;
			String response  = client.send(request);
			if(response == null) return new Response(request.id, "01", "no reponse from vendor.");
			else return new Response(request.id, "00", response);
		} catch (Exception e) {e.printStackTrace();return ERROR;}
	}
	
	public static void main(String[] args) {
		Request request = new Request();
		request.bankid = "SIL";
		request.chanelid = "SIL";
		request.mobile = "8983290664";
		request.smstext = "Hi";
		request.password = "Sil@123";
		System.out.println(objectToString(request));
		
	}
	
	public static final String objectToString(Object o) {
		try {
			return OBJECT_MAPPER.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
