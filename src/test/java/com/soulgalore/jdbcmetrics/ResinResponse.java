package com.soulgalore.jdbcmetrics;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.soulgalore.jdbcmetrics.server.ResinServer;
import com.soulgalore.jdbcmetrics.server.Server;

public class ResinResponse extends AbstractResponseTest {

	private static Server server = new ResinServer();
	
    @BeforeClass
    public static void start() throws Exception {
        db.start();
    	server.start();
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
