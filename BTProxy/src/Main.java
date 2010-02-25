import java.net.*;
import java.io.*;
import java.util.*;

public class Main 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		ServerSocket server = null;
		boolean isListening = true;
		int port = 3128;
		Proxy proxy = null;
		
		try
		{
			server = new ServerSocket(port);
			System.out.println("Bound to port " + port + " successfully.");
				
			proxy = new Proxy();
			proxy.start();
			
			while(isListening)
			{
				proxy.newRequest(server.accept());
			}
			
			server.close();
		}
		catch (IOException ioe)
		{
			System.err.println("\nERROR: Could not bind to port: " + port);
			System.exit(1);
		}
	}
}
