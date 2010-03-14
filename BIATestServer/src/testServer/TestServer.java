package testServer;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * A server running on the local machine, used to test out connections.
 * Should be run with regular java
 * Should be run before running other tests.
 * @author aaron
 *
 */
public class TestServer
{
	private static final String HTTP_NEWLINE = "\r\n";
	
	public static final int serverPort = 8741;
	
	public static String getRequestMessage1()
	{
		String message =
			"GET / HTTP/1.1" + HTTP_NEWLINE +
			"Host: localhost" + HTTP_NEWLINE +
			HTTP_NEWLINE;
		
		return message;
	}
	
	public static String getExpectedResponse()
	{
		String response =
			"" + HTTP_NEWLINE +
			"HTTP/1.1 200 OK" + HTTP_NEWLINE +
			HTTP_NEWLINE;
		
		return response;
	}
	
	public static void main(String[] args)
	{
		try
		{
			ServerSocket server = new ServerSocket(serverPort);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
