package com.soulgalore.jdbcmetrics;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.soulgalore.jdbcmetrics.server.JettyServer;
import com.soulgalore.jdbcmetrics.server.Server;

public class JettyResponse extends AbstractResponseTest {

	private static Server server = new JettyServer();
	
    @BeforeClass
    public static void start() throws Exception {
    	server.start();
        db.start();
    }

    @AfterClass
    public static void shutdown() throws Exception {
    	server.shutDown();
    	db.shutDown();
    }

	@Override
	Server getServer() {
		return server;
	}
}
