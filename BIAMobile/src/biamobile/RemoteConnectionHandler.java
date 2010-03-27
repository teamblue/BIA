package biamobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
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
		byte[] response = null;
		//open raw TCP connection
		SocketConnection client = (SocketConnection) Connector.open("socket://" + hostname + ":" + port);
		//grab the streams
		InputStream is = client.openInputStream();
		OutputStream os = client.openOutputStream();
		
		//send the request
		os.write(request);
		os.flush();
		
		String respStr = getResponseFromInputStream(is);
		client.close();
		
		return respStr.getBytes();
	}
	
	public byte[] performConnectHTTP(String method, Hashtable headerHash, String hostname, int port, byte[] request) throws IOException
	{
		HttpConnection client = (HttpConnection) Connector.open("http://" + hostname + ":" + port);
		client.setRequestMethod(method);
		
		Enumeration keyEnum = headerHash.keys();
		String curHeader;
		String curValue;
		
		while (keyEnum.hasMoreElements())
		{
			curHeader = (String) keyEnum.nextElement();
			curValue = (String) headerHash.get(curHeader);
			
			client.setRequestProperty(curHeader, curValue);
		}
		
		//opening the input stream has the side effect of causing the request to be sent
		InputStream is = client.openInputStream();
		
		String respStr = getResponseFromInputStream(is);
		client.close();
		
		return respStr.getBytes();
	}
	
	private String getResponseFromInputStream(InputStream is) throws IOException
	{
		//parse the response using a StringBuffer
		StringBuffer responseStr = new StringBuffer("");
		
		int c = is.read();
		while(c != -1)
		{
			responseStr.append((char) c);
			c = is.read();
		}
		
		return responseStr.toString();
	}
}
