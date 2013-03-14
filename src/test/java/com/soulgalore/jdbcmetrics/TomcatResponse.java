package com.soulgalore.jdbcmetrics;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.soulgalore.jdbcmetrics.server.Server;
import com.soulgalore.jdbcmetrics.server.TomcatServer;

public class TomcatResponse extends AbstractResponseTest {

	private static Server server = new TomcatServer();
	
    @BeforeClass
    public static void start() throws Exception {
    	start(db, server);
    }

	@AfterClass
    public static void shutDown() throws Exception {
		shutDown(db, server);
    }

	@Override
	Server getServer() {
		return server;
	}
}
