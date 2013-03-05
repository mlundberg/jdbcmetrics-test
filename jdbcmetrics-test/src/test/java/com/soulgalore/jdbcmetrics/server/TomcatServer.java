package com.soulgalore.jdbcmetrics.server;

import java.io.File;
import java.net.URI;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer implements com.soulgalore.jdbcmetrics.server.Server {

	private Tomcat tomcat;
	private URI uri;
	
	@Override
	public void start() throws Exception {
		tomcat = new Tomcat();
		tomcat.setPort(0);
		tomcat.setBaseDir(".");
		tomcat.getHost().setAppBase(".");

		String contextPath = "/context";
		String appBase = "src/main/webapp";

		Context context = tomcat.addWebapp(contextPath, appBase);
		File configFile = new File("src/test/resources/tomcat/context.xml");
		context.setConfigFile(configFile.toURI().toURL());
		tomcat.enableNaming();
		tomcat.start();
		
		Connector c = tomcat.getConnector();
		uri = new URI(c.getScheme(), null, tomcat.getServer().getAddress(), c.getLocalPort(), contextPath, null, null);
	}

	@Override
	public void shutDown() throws Exception {
		tomcat.stop();
		tomcat.getServer().await();
	}

	@Override
	public URI getURI() {
		return uri;
	}

}
