package com.sil.smsinterface;



public interface Services {

	public String login(String user, String pass);
	
	public String logout(String token);
	
	public String getBalance(String accout);
}
