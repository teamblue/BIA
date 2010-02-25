import java.io.*;
import java.net.*;

/**
 * Proxy Class (Handles the connection to/from the browser)
 * @author DJSymBiotiX
 *
 */
public class Proxy extends Thread
{
	private Socket httpSocket; //Socket connected to browser
	private BufferedReader in;
	private PrintWriter out;
	private HTTPObject httpObject; //HTTP Object (For HTTP Packet Parsing)
	private boolean start = true; //Start running the thread (Needed for clean shutdown)
		
	/**
	 * Initiate everything to null
	 */
	public Proxy()
	{
		httpSocket = null;
		in = null;
		out = null;
	}
	
	/**
	 * New Browser request.
	 * @param socket: Socket already connected to browser
	 */
	public void newRequest(Socket socket)
	{		
		try
		{
			httpSocket = socket;			
			in = new BufferedReader(new InputStreamReader(httpSocket.getInputStream()));
			out = new PrintWriter(httpSocket.getOutputStream(),true);
			System.out.println("Socket Accepted: " + httpSocket.getPort() + "\n");
		}
		catch (IOException ioe)
		{
			System.err.println("Error: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Remove un-needed blank characters from char array. Return as a string.
	 * @param buffer
	 * @return
	 */
	private String BufferToString(char[] buffer)
	{
		String result = "";
		int counter = buffer.length-1;
		
		while(buffer[counter--] == 0);
		
		for(int i = counter+1; i >= 0; i--)
		{
			result = buffer[i] + result;
		}
		
		return result;		
	}
	
	/**
	 * Run the proxy thread
	 */
	public void run()
	{
		char[] buffer = new char[4086]; //Data Buffer
		String message = ""; //Message String
		
		//Continue running until start is false
		while(start)
		{
			//Make sure the httpSocket is not null (this loop may run before it is assigned)
			if(httpSocket != null)
			{
				//Continue processing requests until the socket has been closed.
				while(httpSocket.isConnected() && httpSocket.isBound())
				{
					try
					{	
						//Make sure the input stream is assigned and ready (has data in it)
						if(in != null && in.ready())
						{
							in.read(buffer); //Read data into buffer						
												
							message = BufferToString(buffer); //Convert buffer to string
							
							System.out.println(message + "\n----\n");									
							httpObject = new HTTPObject(message); //Create new http Object (for parsing)
							System.out.println("Host: " + httpObject.getHost());
							
							//Create new socket to requested server on port 80
							Socket internet = new Socket(httpObject.getHost(),80);
							Connection connection = new Connection(internet); //Create new connection
							connection.SendRequest(httpObject.getMessage()); //Send http request to requested server
							
							buffer = connection.GetResponse(); //Get response from requested server
							
							message = BufferToString(buffer); //Convert to string
							
							System.out.println(message + "\n----\n");
							
							out.println(message); //Send requested server response back to browser
							connection.CloseConnection(); //Close requested server connection
						}
					}
					catch (IOException ioe)
					{
						System.err.println("Error: " + ioe.getMessage());
						ioe.printStackTrace();
					}
				}
				
				//When the connection is closed (by the browser), safely deallocate (Close all)
				try
				{
					out.close();
					in.close();
					httpSocket.close();
					start = false;
				}
				catch (IOException ioe)
				{
					System.err.println("Error: " + ioe.getMessage());
					ioe.printStackTrace();
				}
			}
		}
	}
}
