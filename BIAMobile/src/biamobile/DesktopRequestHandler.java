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
		RequestHTTPView httpView = new RequestHTTPView(request);
		
		String remoteHost = httpView.getRemoteHost();
		int remotePort = httpView.getRemotePort();
		byte[] response = null;
		
		response = rch.performConnectTCP(remoteHost, remotePort, request);
		
		return response;
	}
}
