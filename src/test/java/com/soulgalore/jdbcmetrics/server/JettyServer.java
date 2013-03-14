package com.soulgalore.jdbcmetrics.server;

import java.io.FileInputStream;
import java.net.URI;

import javax.naming.Context;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

public class JettyServer implements com.soulgalore.jdbcmetrics.server.Server {

	private Server server;
	
	@Override
	public void start() throws Exception {
		// Set to avoid problems for jetty jndi (confs from other servers)
    	System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.eclipse.jetty.jndi.InitialContextFactory");
    	System.setProperty(Context.URL_PKG_PREFIXES, "org.eclipse.jetty.jndi");
    	
		server = new Server(0);
        WebAppContext webAppContext = new WebAppContext("src/main/webapp", "/context");
        
        // this is needed for jndi lookup in java:/comp/env/
        Configuration.ClassList classes = Configuration.ClassList.serverDefault(server);
        classes.add("org.eclipse.jetty.plus.webapp.EnvConfiguration");
        classes.add("org.eclipse.jetty.plus.webapp.PlusConfiguration");
        webAppContext.setConfigurationClasses(classes);

        // Setting jettys own log, if not depend on slf4j
//        StdErrLog logger = new StdErrLog("test");
//        logger.setHideStacks(false);
//        org.eclipse.jetty.util.log.Log.setLog(logger);
//        webAppContext.setLogger(logger);
        
        server.setHandler(webAppContext);

        FileInputStream jettyXml = new FileInputStream("src/test/resources/jetty/jetty-env.xml");
        XmlConfiguration config = new XmlConfiguration(jettyXml);
        config.configure(server);

        server.start();
	}

	@Override
	public void shutDown() throws Exception {
    	if (server != null && server.isStarted()) {
    		server.stop();
    		server.destroy();
    	}
    }

	@Override
	public URI getURI() {
		return server.getURI();
	}

}
