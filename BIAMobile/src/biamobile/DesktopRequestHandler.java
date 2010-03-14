package biamobile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Handles requests from the desktop.  Parses requests, and sends to the RemoteConnectionHandler.
 * Will require refactoring.  Simply wrote this in an attempt to get my tests to pass.
 *
 */
public class DesktopRequestHandler
{
	private RemoteConnectionHandler rh = new RemoteConnectionHandler();
	
	/**
	 * Attaches the passed input stream to this request handler and starts the request handler.
	 * 
	 * TODO what form should the input data be?  An input stream/
	 * 
	 * @param is
	 */
	public void startRequestHandler(InputStream is)
	{
		// start to read request stream
		try
		{
			mainLoop(is);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// does main processing
	// reads requests, pulls out relevant information
	// 		Host, etc
	// sends to RemoteConnectionHandler
	private void mainLoop(InputStream is) throws IOException
	{
		StringBuffer requestInfo = new StringBuffer();
		String requestStr;
		String remoteHost;
		
		int ch;
		while ((ch = is.read()) != -1)
		{
			requestInfo.append((char)ch);
		}
		
		remoteHost = "http://localhost:8741"; // extract remote host here
		requestStr = requestInfo.toString();
		
		// send to RemoteConnectionHandler here
	}
}
