package tests;

import java.io.IOException;
import java.net.Socket;
import junit.framework.TestCase;

/**
 * Tests the libraries that are used by the Bluetooth internet adapter desktop
 * client.
 * 
 * @author Samuel Pauls
 */
public class LibraryTests extends TestCase {
	public void testHttpGet() {
		final String HOST = "www.google.ca";
		final int PORT = 80;
		final String REQUEST = "GET / HTTP/1.1\n"
		                     + "Host:www.google.ca\n"
		                     + "\n"; // a second newline is required
		
		try {
			// Send a request to the server.
			byte[] buffer = new byte[2048];
			buffer = REQUEST.getBytes();
			Socket socket = new Socket(HOST, PORT);
			socket.getOutputStream().write(buffer);
			
			// Wait for a response from the server.
			while(socket.getInputStream().available() == 0) {
				;
			}
			
			// Check the response.
			socket.getInputStream().read(buffer);
			String message = new String(buffer);
			message = message.substring(0, message.indexOf('\r'));
			assertEquals (message, "HTTP/1.1 200 OK");
		} catch (IOException e) {
			fail("Unable to connect to remote host " + HOST + " on port " + PORT);
		}
	}
}