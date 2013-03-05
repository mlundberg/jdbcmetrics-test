package com.soulgalore.jdbcmetrics.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;

import com.caucho.resin.HttpEmbed;
import com.caucho.resin.ResinEmbed;
import com.caucho.resin.WebAppEmbed;

public class ResinServer implements Server {

	private ResinEmbed server;
	private URI uri;
	
	@Override
	public void start() throws Exception {
		server = new ResinEmbed();
		int port = getFreePort();
		HttpEmbed http = new HttpEmbed(port);
		server.addPort(http);
		WebAppEmbed webApp = new WebAppEmbed("/context", "src/main/webapp");
		server.addWebApp(webApp);
		server.start();		

		uri = new URI(webApp.getWebApp().getURL());
	}

	@Override
	public void shutDown() throws Exception {
		server.stop();
		server.destroy();
		
	}

	@Override
	public URI getURI() {
		return uri;
	}
	
	private int getFreePort() {
		int port = 8080;
		try {
			ServerSocket socket = new ServerSocket(0);
			port = socket.getLocalPort();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return port;
	}

}
