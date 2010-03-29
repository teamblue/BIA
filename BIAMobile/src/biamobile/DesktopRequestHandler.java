package biamobile;

import java.io.IOException;

public class DesktopRequestHandler
{
	
	private RemoteConnectionHandler rch = new RemoteConnectionHandler();
	
	/**
	 * Sends the passed byte array as a request.  Blocks until we get a response, returning it as a byte array.
	 * @param request  The request to send.
	 * @return  A byte array containing the response.
	 * @throws IOException 
	 */
	public byte[] sendRequest(byte[] request) throws IOException
	{
		// extract remote host from headers in request
		String remoteHost = extractRemoteHost(request);
		byte[] response = null;
		
		response = rch.performConnectTCP(remoteHost, 8741, request);
		
		
		return response;
	}
	
	public String extractRemoteHost(byte[] request)
	{
		String remoteHost = null;
		
		if (request != null)
		{
			// gets first line
			StringBuffer firstLine = new StringBuffer();
			for (int i = 0; i < request.length && request[i] != '\n'; i++)
			{
				firstLine.append(request[i]);
			}
		}
		
		//return remoteHost;
		return remoteHost;
	}
}
