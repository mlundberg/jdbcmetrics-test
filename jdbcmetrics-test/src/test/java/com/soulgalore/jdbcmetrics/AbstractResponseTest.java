package com.soulgalore.jdbcmetrics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.soulgalore.jdbcmetrics.database.Database;
import com.soulgalore.jdbcmetrics.server.Server;
import com.soulgalore.jdbcmetrics.servlet.TestServlet;


public abstract class AbstractResponseTest {

	private static final String RESPONSE_HEADER_NAME_NR_OF_READS = "nr-of-reads";
	private static final String RESPONSE_HEADER_NAME_NR_OF_WRITES = "nr-of-writes";

	abstract Server getServer();
	
    protected static Database db = new Database();

// BeforeClass must be static - so extend class must implement manually
//    @BeforeClass
//    public static void start() throws Exception {
//    	server.start();
//        db.start();
//    }
    
    @Test
    public void noRequestHeaderShouldResultInNoResponseHeader() throws Exception
    {
        WebResponse webResponse = makeHttpCall(false, null, 0);
        assertThat(webResponse.getResponseHeaderValue(RESPONSE_HEADER_NAME_NR_OF_READS), is((String)null));
        assertThat(webResponse.getResponseHeaderValue(RESPONSE_HEADER_NAME_NR_OF_WRITES), is((String)null));
    }
    
    @Test
    public void requestHeaderShouldResultInResponseHeader() throws Exception
    {
        WebResponse webResponse = makeHttpCall(true, null, 0);
        assertThat(webResponse.getResponseHeaderValue(RESPONSE_HEADER_NAME_NR_OF_READS), is("0"));
        assertThat(webResponse.getResponseHeaderValue(RESPONSE_HEADER_NAME_NR_OF_WRITES), is("0"));
    }

    @Test
    public void dbReadsShouldResultInResponseHeader() throws Exception
    {
        WebResponse webResponse = makeHttpCall(true, "read", 3);
        assertThat(webResponse.getResponseHeaderValue(RESPONSE_HEADER_NAME_NR_OF_READS), is("3"));
        assertThat(webResponse.getResponseHeaderValue(RESPONSE_HEADER_NAME_NR_OF_WRITES), is("0"));
    }

    private WebResponse makeHttpCall(boolean setHeader, String param, int value) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
    	WebClient webClient = new WebClient();
        if (setHeader) {
        	webClient.addRequestHeader("jdbcmetrics", "true");
        }
        String queryString = "";
        if (param != null) {
        	queryString = param + "=" + String.valueOf(value) + "&" + queryString; 
        }
        String uri = getServer().getURI().toString() + "?" + queryString;
        
        //System.out.println("Making http call to " + uri);
        
        Page page = webClient.getPage(uri);
        WebResponse webResponse = page.getWebResponse();
    	return webResponse;
    }
    
    @Test
    public void testPage() throws Exception
    {
        WebResponse webResponse = makeHttpCall(false, null, 0);
		String respStr = webResponse.getContentAsString();
        assertThat(respStr, is(TestServlet.RESPONSE));
    }

// AfterClass must be static - so extend class must implement manually
//    @AfterClass
//    public static void shutdown() throws Exception {
//    	server.shutDown();
//    	db.shutDown();
//    }
}
