package com.sil.smsinterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.logging.LoggingFeature.Verbosity;
import org.glassfish.jersey.server.ResourceConfig;

import com.sil.smsinterface.ws.RetroClient;

public class Application {

	private static final Properties properties = new Properties();
	
	static {
		try {
			properties.load(new FileReader(new File("ini/app.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final URI BASE_URI = URI.create(properties.getProperty("BASEURL"));

	public static void main(String[] args) throws IOException {
		try {
			RetroClient.init();
			final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, create(), false);
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					server.shutdownNow();
				}
			}));
			server.start();
			System.out.println(String.format("Application started.%n" + "Try out %s%n " + "Stop the application using CTRL+C", BASE_URI));
			Thread.currentThread().join();
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private static ResourceConfig create() throws IOException {
		final ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(JacksonFeature.class);
		resourceConfig.packages("com.sil.smsinterface.ws");
		resourceConfig.register(new LoggingFeature(Logger.getLogger("app"), Level.INFO, Verbosity.PAYLOAD_TEXT, 8*1024));
		return resourceConfig;
	}
}
