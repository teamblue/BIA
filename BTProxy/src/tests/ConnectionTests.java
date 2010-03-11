package tests;

import java.io.IOException;
import java.net.Socket;

import app.Connection;
import junit.framework.TestCase;

public class ConnectionTests extends TestCase {
	public static void testConnection() {
		final String HOST = "www.google.ca";
		final int PORT = 80;
		final String REQUEST = "GET / HTTP/1.1\n"
                             + "Host:www.google.ca\n"
                             + "\n"; // a second newline is required
		
		try {
			// Connect to a remote server.
			Socket serverSocket = new Socket(HOST, PORT);
			Connection serverConnection = new Connection(serverSocket);
			
			// Send a request to the server.
			serverConnection.SendRequest(REQUEST.getBytes());
			
			// Get a response from the server.
			byte response[] = serverConnection.GetResponse();
			
			// Validate the server's response.
			String responseString = new String(response);
			String responseLine = responseString.substring(0,
					responseString.indexOf('\r'));
			assertEquals(responseLine, "HTTP/1.1 200 OK");
			
			// Close the connection to the server.
			serverConnection.CloseConnection();
		} catch(IOException e) {
			fail("IO failed!");
		}
	}
}
