package tests;

import logic.HTTPObject;
import junit.framework.TestCase;

/**
 * Tests the HTTP object's ability to parse information out of a stream.
 * 
 * @author Samuel Pauls
 */
public class HTTPObjectTests extends TestCase {
	public void testGetHost() {
		final String HOST = "www.google.ca";
		final String REQUEST = "GET / HTTP/1.1\r\n"
		                     + "Host: " + HOST;
		
		HTTPObject http = new HTTPObject(REQUEST);
		
		assertEquals (HOST, http.getHost());
	}
}