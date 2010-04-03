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
	
	private static String readRequest(BufferedReader br) throws IOException
	{
		String request = new String();
		String line;
		
		line = br.readLine();
		while (line != null && !line.isEmpty()) // if line is empty, then we hit the second \r\n
		{
			request += line + biamobileTest.MobileTestConstants.getHTTPNewline();
			
			line = br.readLine();
		}
		
		//add the extra newline we skipped over when we exited the loop
		return request + biamobileTest.MobileTestConstants.getHTTPNewline();
	}
	
	public static void main(String[] args)
	{
		ServerSocket server = null;
		
		String request = ""; //biamobileTest.MobileTestConstants.getTCPRequestMessage();
		
		while (true)
		{
		    try
		    {
		        server = new ServerSocket(biamobileTest.MobileTestConstants.getServerPort());
		        System.out.println("Server running on " + server.getInetAddress() + ":" + server.getLocalPort());

		        Socket clientSocket = server.accept(); // waits for connection

		        InputStream is = clientSocket.getInputStream();
		        BufferedReader br = new BufferedReader(new InputStreamReader(is));

		        OutputStream os = clientSocket.getOutputStream();
		        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

		        request = readRequest(br);
		        System.out.println("Request:");
		        System.out.print(request);

		        // if we got correct request
		        if (biamobileTest.MobileTestConstants.getTCPRequestMessage().equals(request) ||
		                biamobileTest.MobileTestConstants.getHTTPRequestMessage().equals(request) ||
		                biamobileTest.MobileTestConstants.getFullApplicationTestRequestMobileApp().equals(request))
		        {
		            System.out.println("Request is correct");
		            // send response
		            bw.write(biamobileTest.MobileTestConstants.getExpectedResponse());
		        }
		        
		        else
		        {
		            System.out.println("Request is incorrect");
		            bw.write("error");
		            bw.write(biamobileTest.MobileTestConstants.getHTTPNewline() + biamobileTest.MobileTestConstants.getHTTPNewline());
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
}
