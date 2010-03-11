package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import junit.framework.TestCase;

/**
 * Tests the libraries that are used by the Bluetooth Internet adapter desktop
 * client.
 * 
 * Testing these libraries is useful as a form of documentation for how this
 * program interacts with the system underneath and in the case where an
 * implementation of the libraries on a given OS may have issues.
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
			fail("Unable to connect to remote host " + HOST + " on port " +
			     PORT + "!");
		}
	}
	
	// Creates server and client sockets and attempts to send messages in both
	// directions.
	public void testSockets() {
		final String HOST = "127.0.0.1";
		final int PORT = 3128;
		final String MESSAGE_TO_SERVER = "message to server\n";
		final String MESSAGE_TO_CLIENT = "message to client\n";
		
		try {
			// Create a server socket.
			ServerSocket serverSocket = new ServerSocket(PORT);
			
			// Send a request to the server.
			byte[] buffer = new byte[2048];
			buffer = MESSAGE_TO_SERVER.getBytes();
			Socket client = new Socket(HOST, PORT);
			client.getOutputStream().write(buffer);
			
			// Accept the request on the server end.
			Socket serverConnection = serverSocket.accept();
			
			// Verify the request.
			InputStream is = serverConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String receivedMessage = br.readLine();
			assertEquals(receivedMessage + '\n', MESSAGE_TO_SERVER);
			
			// Send a message from the server to the client.
			OutputStream os = serverConnection.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write(MESSAGE_TO_CLIENT);
			pw.flush();
			
			// Receive the message on the client end and verify it.
			InputStream clientIS = client.getInputStream();
			InputStreamReader clientISR = new InputStreamReader(clientIS);
			BufferedReader clientBR = new BufferedReader(clientISR);
			receivedMessage = clientBR.readLine();
			assertEquals(receivedMessage + '\n', MESSAGE_TO_CLIENT);
		} catch(IOException e) {
			fail("Unable to create socket on port " + PORT + "!");
		}
	}
}