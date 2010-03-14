package testServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server running on the local machine, used to test out connections.
 * Should be run with regular java
 * Should be run before running other tests for mobile application.
 * @author aaron
 *
 */
public class TestServer
{	
	private static String getHTTPNewline()
	{
		return "\r\n";
	}
	
	private static int getServerPort()
	{
		return 8741;
	}
	
	public static String getRequestMessage()
	{
		String message =
			"GET / HTTP/1.1" + getHTTPNewline() +
			"Host: localhost" + getHTTPNewline() +
			getHTTPNewline();
		
		return message;
	}
	
	public static String getExpectedResponse()
	{
		String response =
			getHTTPNewline() +
			"HTTP/1.1 200 OK" + getHTTPNewline() +
			getHTTPNewline();
		
		return response;
	}
	
	private static String readRequest(InputStream is) throws IOException
	{
		int data;
		StringBuffer request = new StringBuffer();
		
		while ((data = is.read()) != -1)
		{
			request.append((char)data);
		}
		
		return request.toString();
	}
	
	public static void main(String[] args)
	{
		ServerSocket server = null;
		
		String request = getRequestMessage();
		String response = getExpectedResponse();
		try
		{
			server = new ServerSocket(getServerPort());
			System.out.println("Server running on " + server.getInetAddress() + ":" + server.getLocalPort());
			
			Socket clientSocket = server.accept(); // waits for connection
			InputStream is = clientSocket.getInputStream();
			OutputStream os = clientSocket.getOutputStream();
			
			request = readRequest(is);
			System.out.println(request);
			
			server.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
