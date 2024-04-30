package com.sil.smsinterface.ws;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface SMSSender {

	@GET("{path}")
	public Call<String> get(@Path("path") String path, @QueryMap Map<String, String> map);
	
	@POST("{path}")
	@FormUrlEncoded
	public Call<String> post(@Path("path") String path, @FieldMap Map<String, String> map);
}
