package com.sil.smsinterface.ws;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.sil.smsinterface.db.DBUtil;
import com.sil.smsinterface.model.BankConfig;
import com.sil.smsinterface.model.Request;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetroClient {

	public static final ConcurrentHashMap<String, RetroClient> clients = new ConcurrentHashMap<>();
	
	private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();  
	private static final OkHttpClient.Builder httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(); 
	
	static {
		logging.setLevel(Level.BODY);
		httpClient.addInterceptor(logging);
	}
	
	public static final void init() {
		try {
			final List<BankConfig> banks = DBUtil.getBanks();
			banks.forEach(bank -> {
				clients.put(bank.bankId, new RetroClient(bank));
			});
			
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public final BankConfig config;
	private final Retrofit retrofit;
	
	public RetroClient(BankConfig config) {
		this.config = config;
		retrofit = new Retrofit.Builder().baseUrl(config.baseURL).addConverterFactory(ScalarsConverterFactory.create()).client(httpClient.build())
				   .build();
	}

	public String send(Request request) {
		String response = null;
		try {
			request.smstext =  request.smstext + ("".equals(config.footer) ? "" : " "+config.footer);
			DBUtil.registerSMSRequest(request);
			HashMap<String, String> localmap = new HashMap<>();
			localmap.putAll(config.params);
			localmap.put(config.mobileParam, request.mobile);
			localmap.put(config.smsParam, request.smstext);
			SMSSender sender = retrofit.create(SMSSender.class);
			Call<String> call = "POST".equalsIgnoreCase(config.method) ? sender.post(config.path, localmap) : sender.get(config.path, localmap);
			Response<String>  retroResponse = call.execute();
			response =  retroResponse.body();
			DBUtil.registerSMSResponse(request.id, response);
		} catch (Exception e) {e.printStackTrace();}
		return response;
	}
	
	
}
