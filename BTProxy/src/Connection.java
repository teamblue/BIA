import java.io.*;
import java.net.*;
import java.nio.CharBuffer;

/**
 * 
 * @author DJSymBiotiX
 * The connection class connects to a webserver and send send/receive data to/from it.
 */
public class Connection
{
	private Socket httpSocket;
	private BufferedReader in;
	private PrintWriter out;
	
	/**
	 * Connection Constructor: Takes in an already connected socket and grabs the input/output streams from it.
	 * @param socket: An already connected socket
	 */
	public Connection(Socket socket)
	{
		httpSocket = socket;
		System.out.println("New Connection on Port: " + httpSocket.getPort());

		try
		{
			out = new PrintWriter(httpSocket.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(httpSocket.getInputStream()));
		}
		catch (IOException ioe)
		{
			System.err.println("Error: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Closes the Connection
	 */
	public void CloseConnection()
	{
		try
		{
			in.close();
			out.close();
			httpSocket.close();
		}
		catch (IOException ioe)
		{
			System.out.println("Error: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Send a request through the httpsocket
	 * @param message: Message to send
	 */
	public void SendRequest(String message)
	{
		out.println(message);
	}
	
	public void SendRequest(byte[] message)
	{
		//out.println(message);
		try 
		{
			httpSocket.getOutputStream().write(message);
		} 
		catch (IOException ioe) 
		{
			ioe.printStackTrace();
		}
	}
	
	/**
	 * GetResponse returns the data that is sent from the server to this client program.
	 * @return char array of data.
	 */
	public byte[] GetResponse()
	{
		byte[] buffer = new byte[32768];
		int amountReadIn = 0;

		try
		{			
			//in.read(buffer);
			while(httpSocket.getInputStream().available() == 0);			
			
			amountReadIn = httpSocket.getInputStream().read(buffer);
			System.out.println("\n--Response Length: " + amountReadIn + "--\n");
		}
		catch (IOException ioe)
		{
			System.err.println("Error: " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return buffer;
	}
}
