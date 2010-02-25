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
	
	/**
	 * GetResponse returns the data that is sent from the server to this client program.
	 * @return char array of data.
	 */
	public char[] GetResponse()
	{
		char[] buffer = new char[4086];

		try
		{			
			in.read(buffer);				
		}
		catch (IOException ioe)
		{
			System.err.println("Error: " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return buffer;
	}
}
