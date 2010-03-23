package testServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
		return MobileTestConstants.getHTTPNewline();
	}
	
	private static int getServerPort()
	{
		return MobileTestConstants.getServerPort();
	}
	
	public static String getRequestMessage()
	{
		return MobileTestConstants.getRequestMessage();
	}
	
	public static String getExpectedResponse()
	{
		return MobileTestConstants.getExpectedResponse();
	}
	
	private static String readRequest(BufferedReader br) throws IOException
	{
		String request = new String();
		String line;
		
		line = br.readLine();
		while (line != null && !line.isEmpty()) // if line is empty, then we hit the second \r\n
		{
			request += line + getHTTPNewline();
			
			line = br.readLine();
		}
		
		return request;
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
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			OutputStream os = clientSocket.getOutputStream();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
			
			request = readRequest(br);
			System.out.println("Request:");
			System.out.println(request);
			System.out.println();
			
			// if we got correct request
			if (getRequestMessage().equals(request))
			{
				// send response
				bw.write(getExpectedResponse());
			}
			else
			{
				bw.write("error");
				bw.write(getHTTPNewline() + getHTTPNewline());
			}
			
			bw.close();
			server.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
