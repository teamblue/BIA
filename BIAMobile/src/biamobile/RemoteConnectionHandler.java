package biamobile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.sun.midp.io.j2me.socket.Protocol;

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
		Protocol protocol = new Protocol();
		
		//note: the first parameter must be in the form "//<hostname>:<port>"
		protocol.openPrim("//" + hostname + ":" + port, Connector.READ_WRITE, true);
		
		//send the request
		OutputStream os = protocol.openOutputStream();
		os.write(request);
		
		//wait for the response (this will block until either a byte is received or a timeout occurs)
		InputStream is = protocol.openInputStream();
		byte[] response = getResponseFromInputStream(is);

		protocol.close();

		return response;
	}

	/**
	 * Performs a connection using the HTTPConnection object.
	 * @param method One of HttpConnection.GET or HttpConnection.POST (constants)
	 * @param headerHash
	 * @param hostname
	 * @param port
	 * @param request
	 * @return
	 * @throws IOException
	 */
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

		byte[] response = getResponseFromInputStream(is);
		String respStr = new String(response);

		// need to pull out header values from http connection since it appears that all data we get from InputStream does not contain the headers
		//use the HttpConnection.getHeaderField(int n) and HttpConnection.getHeaderFieldKey(int n) methods
		// http://java.sun.com/javame/reference/apis/jsr118/
		// iterate over the value of "n" until we get null as a return value
		// add all these, plus any data extracted from the InputStream to a byte array to return it

		int n = 0;
		String responseHeaderKey = client.getHeaderFieldKey(n);
		String responseHeaderValue = client.getHeaderField(n);
		while (responseHeaderKey != null)
		{
			// TODO
			// keep header values extracted
			// add to return byte array
			// so that the browser receives the information it expects (headers, other http info)

			n++;
			responseHeaderKey = client.getHeaderFieldKey(n);
			responseHeaderValue = client.getHeaderField(n);
		}
		
		client.close();

		return response; // should return all data, including headers in a byte array
	}
	
	private byte[] getResponseFromInputStream(InputStream is) throws IOException
	{
		ByteArrayOutputStream boStream = new ByteArrayOutputStream();
		
		int c = is.read();
		while(c != -1)
		{
			boStream.write(c);
			c = is.read();
		}
		
		return boStream.toByteArray();
	}
}
