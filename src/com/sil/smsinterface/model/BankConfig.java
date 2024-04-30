package com.sil.smsinterface.model;

import java.util.LinkedHashMap;

public class BankConfig {

	public String bankId;
	public String password;
	public String baseURL;
	public String path;
	public String mobileParam;
	public String smsParam;
	public String method;
	public String footer = "";
	public final LinkedHashMap<String, String> params = new LinkedHashMap<>();
	
}
