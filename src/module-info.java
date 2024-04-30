module sms_interface {
	exports com.sil.smsinterface.ws;
	exports com.sil.smsinterface.db;
	exports com.sil.smsinterface.model;
	exports com.sil.smsinterface;

	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.microsoft.sqlserver.jdbc;
	requires grizzly.http.server;
	requires java.logging;
	requires java.naming;
	requires java.sql;
	requires java.ws.rs;
	requires jersey.common;
	requires jersey.container.grizzly2.http;
	requires jersey.media.json.jackson;
	requires jersey.server;
	requires okhttp3;
	requires okhttp3.logging;
	requires retrofit2;
	requires retrofit2.converter.scalars;
}