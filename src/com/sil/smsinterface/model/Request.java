package com.sil.smsinterface.model;


public class Request {

	public long id;
	public String mobile;
	public String smstext;
	public String bankid;
	public String chanelid;
	public String password;
	
	public boolean validate() {
		if(mobile != null && smstext != null && bankid != null/* && chanelid != null && password != null*/) return true;
		return false;
	}
}
