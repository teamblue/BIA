package biamobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

public class RemoteConnectionHandler
{
	/**
	 * Performs a connection using TCP to the remote host. Returns the response as a byte array.
	 * @param hostname  The host to connect to.
	 * @param port The port on which to establish a connection.
	 * @param request  The data for the request.
	 * @return  The response data.
	 * @throws IOException 
	 */
	public byte[] performConnectTCP(String hostname, int port, byte[] request) throws IOException
	{
		//open raw TCP connection
		SocketConnection client = (SocketConnection) Connector.open("socket://" + hostname + ":" + port);
		//grab the streams
		InputStream is = client.openInputStream();
		OutputStream os = client.openOutputStream();
		
		//send the request
		//System.out.println("Connected. Sending request...");
		os.write(request);
		os.flush();
		
		//parse the response using a StringBuffer (it's ugly, but it works...)
		//System.out.println("Recieving response...");
		StringBuffer responseStr = new StringBuffer("");
		
		int c = is.read();
		while(c != -1)
		{
			responseStr.append((char) c);
			c = is.read();
		}
		
		//System.out.println(responseStr.toString());
		
		//close
		//System.out.println("Closing connection...");
		client.close();
		//System.out.println("Connection closed.");
		
		return responseStr.toString().getBytes();
	}
	
	public void performConnectHTTP(String[][] headers, String hostname, byte[] request)
	{
		
	}
}
