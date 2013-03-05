package com.soulgalore.jdbcmetrics.database;

import java.sql.SQLException;

import org.h2.tools.Server;

public class Database {

	private Server db;
	 
	public void start() throws SQLException {
    	db = org.h2.tools.Server.createTcpServer();
        db.start();
	}
	
	public void shutDown() throws SQLException {
    	String dbUrl = db.getURL();
    	org.h2.tools.Server.shutdownTcpServer(dbUrl, "", true, true);
	}
}
