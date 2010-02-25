import java.io.*;
import java.net.*;

public class Proxy extends Thread
{
	private Socket httpSocket;
	private BufferedReader in;
	private PrintWriter out;
	private HTTPObject httpObject;
	private boolean start = true;
		
	public Proxy()
	{
		httpSocket = null;
		in = null;
		out = null;
	}
	
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
	
	public void run()
	{
		char[] buffer = new char[4086];
		String strBuf = "";
		String message = "";
		while(start)
		{
			if(httpSocket != null)
			{
				while(httpSocket.isConnected() && httpSocket.isBound())
				{
					try
					{	
						strBuf = "";
						if(in != null && in.ready())
						{
							in.read(buffer);
							
							/*
							while(in.ready())
							{
								strBuf += in.readLine() + "\n";
							}
							*/
												
							message = BufferToString(buffer);
							//message = strBuf;
							
							System.out.println(message + "\n----\n");									
							httpObject = new HTTPObject(message);
							System.out.println("Host: " + httpObject.getHost());
							
							Socket internet = new Socket(httpObject.getHost(),80);
							Connection connection = new Connection(internet);
							connection.SendRequest(httpObject.getMessage());
							
							buffer = connection.GetResponse();
							//strBuf = connection.GetResponseStr();
							
							message = BufferToString(buffer);
							//message = strBuf;
							
							System.out.println(message + "\n----\n");
							
							out.println(message);
							connection.CloseConnection();
						}
					}
					catch (IOException ioe)
					{
						System.err.println("Error: " + ioe.getMessage());
						ioe.printStackTrace();
					}
				}
				
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
