package com.sil.smsinterface.db;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.sil.smsinterface.model.BankConfig;
import com.sil.smsinterface.model.Request;

public class DBUtil {

	private static final Properties				dbProperties	= new Properties();
	private static final SQLServerDataSource	ds				= new SQLServerDataSource();

	static {
		try {
			dbProperties.load(new FileInputStream(new File("ini/localdb.properties")));
			ds.setUser(dbProperties.getProperty("db_user"));
			ds.setPassword(dbProperties.getProperty("db_pass"));
			ds.setServerName(dbProperties.getProperty("db_ip"));
			ds.setPortNumber(Integer.parseInt(dbProperties.getProperty("db_port")));
			ds.setDatabaseName(dbProperties.getProperty("db_name"));
		} catch (Exception e) {
			System.out.println("Application Initialization Error : localdb.properties not found");
			System.out.println(e);
		}
	}

	
	public static final void registerSMSRequest(Request request) {
		try(Connection con = ds.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO SMS (MOBILE, SMSTEXT, BANK_ID, CHANNEL_ID) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, request.mobile);
			ps.setString(2, request.smstext);
			ps.setString(3, request.bankid);
			ps.setString(4, request.chanelid);
			ps.execute();
			try(ResultSet rs = ps.getGeneratedKeys()){
				if(rs.next()) request.id = rs.getLong(1);
			} catch (Exception e) {e.printStackTrace();}
		} catch (Exception e) {e.printStackTrace();}
	}
	
	
	public static final void registerSMSResponse(long id, String response) {
		try(Connection con = ds.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE SMS SET RESPONSE = ? WHERE ID = ?")){
			ps.setString(1, response);
			ps.setLong(2, id);
			ps.execute();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static final List<BankConfig> getBanks() {
		final List<BankConfig> banks = new ArrayList<>();
		try(Connection con = ds.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT DISTINCT(BANK_ID) FROM BANK_CONFIG");
			ResultSet rs = ps.executeQuery();
			PreparedStatement ps1 = con.prepareStatement("SELECT PARAM, VALUE FROM BANK_CONFIG WHERE BANK_ID = ?")){
			while(rs.next())
			{
				BankConfig config = new BankConfig();
				config.bankId = rs.getString("BANK_ID");
				ps1.setString(1, config.bankId);
				try(ResultSet rs1 = ps1.executeQuery())
				{
					while(rs1.next()) {
						final String param = rs1.getString(1);
						final String value = rs1.getString(2);
						if("BASEURL".equalsIgnoreCase(param)) {
							config.baseURL = value;
						}
						else if("PATH".equalsIgnoreCase(param)) {
							config.path = value;
						}
						else if("BANK_PASS".equalsIgnoreCase(param)) {
							config.password = value;
						}
						else if("SMSPARAM".equalsIgnoreCase(param)) {
							config.smsParam = value;
						}
						else if("MOBILEPARAM".equalsIgnoreCase(param)) {
							config.mobileParam = value;
						}
						else if("FOOTER".equalsIgnoreCase(param)) {
							config.footer = value;
						}
						else if("METHOD".equalsIgnoreCase(param)) {
							if(List.of("POST", "GET", "get", "post").contains(value)) config.method = value;
							else config.params.put(param, value);
						}
						else config.params.put(param, value);
					}
				} catch (Exception e) {e.printStackTrace();}
				banks.add(config);
			}
		} catch (Exception e) {e.printStackTrace();}
		return banks;
	}
}
