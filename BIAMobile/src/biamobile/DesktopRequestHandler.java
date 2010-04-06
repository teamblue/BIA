package biamobile;

import java.io.IOException;
import java.util.Hashtable;

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
                String extractedHostSection = httpView.getExtractedHostSection();
		int remotePort = httpView.getRemotePort();
		Hashtable headerHash = httpView.getHeaderHash();
		byte[] response = (new String("Null comes from sendRequest()")).getBytes();

		//response = rch.performConnectTCP(remoteHost, remotePort, request);
		response = rch.performConnectHTTP(httpView.getHTTPMethod(), headerHash, remoteHost, extractedHostSection, remotePort, request);
		return response;
	}

}
