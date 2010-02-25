import java.io.*;
import java.net.*;
import java.nio.CharBuffer;

public class Connection
{
	private Socket httpSocket;
	private BufferedReader in;
	private PrintWriter out;
	
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
	
	public void SendRequest(String message)
	{
		out.println(message);
	}
	
	public String GetResponseStr()
	{
		String response = "";
		
		try
		{
			//while(!in.ready());
			
			while(in.ready())
			{
				response += in.readLine() + "\n";
			} 
		}
		catch (IOException ioe)
		{
			System.err.println("Error: " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return response;
	
	}
	
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
