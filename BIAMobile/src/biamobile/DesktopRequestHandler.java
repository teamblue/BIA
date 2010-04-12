package biamobile;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Handles requests from the desktop.  Obtains such a request as a byte[], forwards over to RemoteConnectionHandler,
 * 	and sends the response back as a byte array.
 */
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
                String extractedHostSection = httpView.getExtractedPathSection();
		int remotePort = httpView.getRemotePort();
		Hashtable headerHash = httpView.getHeaderHash();
		byte[] response = (new String("Null comes from sendRequest()")).getBytes();

		//response = rch.performConnectTCP(remoteHost, remotePort, request); // for TCP handler, doesn't work (due to signing issues)
		response = rch.performConnectHTTP(httpView.getHTTPMethod(), headerHash, remoteHost, extractedHostSection, remotePort, request);
		return response;
	}

}
