package com.soulgalore.jdbcmetrics.server;

import java.net.URI;

public interface Server {
	
	public void start() throws Exception;
	public void shutDown() throws Exception;
	public URI getURI();

}
