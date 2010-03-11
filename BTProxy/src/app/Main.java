package app;
import java.net.*;
import java.io.*;

public class Main 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		ServerSocket server = null; //Socket Server
		boolean isListening = true; //Used to loop forever (untill shutdown)
		int port = 3128;
		Proxy proxy = null;
		
		try
		{
			server = new ServerSocket(port); //Listen on a specified port
			System.out.println("Bound to port " + port + " successfully.");
				
			proxy = new Proxy(); //Create a new proxy object and start it
			proxy.start();
			
			//Continue listening until the browser initiates an http request. Bind port and let proxy handle it
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
