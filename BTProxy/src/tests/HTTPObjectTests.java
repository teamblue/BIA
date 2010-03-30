package tests;

import logic.HTTPHeader;
import junit.framework.TestCase;

/**
 * Tests the HTTP object's ability to parse information out of a stream.
 */
public class HTTPObjectTests extends TestCase {
	public void testGetHost() {
		final String HOST = "www.google.ca";
		final String REQUEST = "GET / HTTP/1.1\r\n" + "Host: " + HOST;

		HTTPHeader http = new HTTPHeader(REQUEST);

		assertEquals(HOST, http.getHost());
	}
}