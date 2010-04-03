package biamobile;

import java.util.Hashtable;

/**
 * Provides a "View" over a byte array to interpret it as HTTP data.
 * @author aaron
 *
 */
public class RequestHTTPView
{
	private byte[] rawData;
	
	private byte[] requestData;
	private String remoteHost;
	private int remotePort;
	
	private final static String WHITESPACE = " ";
	
	public RequestHTTPView(byte[] data)
	{
		this.rawData = data;
		remoteHost = null;
		
		remotePort = 80; // defaults to 80
		
		extractRequestData();
	}
	
	// pull out remote host, remote port, etc from request
	// make a byte array containing the request data to be sent
	// that is, takes a request like "GET http://www.remotehost.com:888 HTTP/1.0", extracts out the www.remotehost.com
	//		the port number, stores these values in the class variables
	//		and stores the stripped command "GET / HTTP/1.0" in the requestData byte array.
	private void extractRequestData()
	{
		String getSection = null;
		String hostSection = null;
		String extractedHostSection = null;
		String remainderSection = null;
		
		int index;
		int prevIndex;
		
		int headerNewlineIndex;
		
		if (rawData != null)
		{
			// first line of header should look like
			//	GET http://localhost HTTP/1.0 \r\n\r\n
			// gets first line
			StringBuffer firstLine = new StringBuffer();
			for (headerNewlineIndex = 0; headerNewlineIndex < rawData.length && rawData[headerNewlineIndex] != '\n'; headerNewlineIndex++)
			{
				firstLine.append((char)rawData[headerNewlineIndex]);
			}
			String firstLineStr = firstLine.toString();
			
			// up to 1st whitespace is GET part
			index = firstLineStr.indexOf(WHITESPACE);
			
			getSection = firstLineStr.substring(0, index);
			
			prevIndex = index;
			
			// up to 2nd whitespace is remote site
			index = firstLineStr.indexOf(WHITESPACE, prevIndex+1);
			
			hostSection = firstLineStr.substring(prevIndex+1, index);
			
			remainderSection = firstLineStr.substring(index+1);
			
			// now, pull out remote host name
			index = hostSection.indexOf("//");
			
			if (index != -1)
			{
				String remoteHostSection;
				
				int endIndex = hostSection.indexOf("/", index+2); // look for last index of "/", as in http://www.google.ca/
				
				if (endIndex == -1) // if no such character found
				{
					
					remoteHostSection = hostSection.substring(index+2); // +2 to skip over //
					extractedHostSection = "/"; // contains "/" as in GET / HTTP/1.0
				}
				else
				{
					remoteHostSection = hostSection.substring(index+2, endIndex); // +2 to skip over //
					extractedHostSection = hostSection.substring(endIndex); // contains for ex.
					// "/page/index.html" as in GET /page/index.html HTTP/1.0
				}
				
				// look for port in remoteHostSection
				int portIndex = remoteHostSection.indexOf(":"); // look for index of a port in for ex www.google.ca:888
				
				if (portIndex == -1)
				{
					remoteHost = remoteHostSection;
					remotePort = 80;
				}
				else
				{
					remoteHost = remoteHostSection.substring(0, portIndex);
					
					String portString = remoteHostSection.substring(portIndex + 1);
					
					remotePort = Integer.parseInt(portString);
				}
			}
			else
			{
				// no remote host
			}
			
			// now, put the data back together
			// that is, if we had
			//	GET http://www.google.ca/ HTTP/1.0 \r\n
			//  \r\n
			// then we want to place the value
			//  GET / HTTP/1.0 \r\n
			//	\r\n
			// into requestData (we want to extract out the http://www.google.ca/ from the request)
			
			int requestDataLength;
			String firstLineRequestString = getSection + " " + extractedHostSection + " " + remainderSection + "\n"; // add in '\n' since we had searched up to \n initally
			
			requestDataLength = firstLineRequestString.getBytes().length + (rawData.length - headerNewlineIndex-1);
			requestData = new byte[requestDataLength];
			
			byte[] firstLineBytes = firstLineRequestString.getBytes();
			int requestDataIndex;
			
			// copy over the bytes from the first line into the requestData
			for (requestDataIndex = 0; requestDataIndex < firstLineBytes.length; requestDataIndex++)
			{
				requestData[requestDataIndex] = firstLineBytes[requestDataIndex];
			}
			
			// copy over rest of bytes from rawData to requestData
			for (int rawDataIndex = headerNewlineIndex+1; rawDataIndex < rawData.length &&
				requestDataIndex < requestData.length; rawDataIndex++, requestDataIndex++)
			{
				requestData[requestDataIndex] = rawData[rawDataIndex];
			}
		}
	}
	
	// TODO
	/**
	 * @return  Returns a hash table containing all the headers (used for the HTTP request) in the form of key:value.
	 */
	public Hashtable getHeadersHash()
	{
		// note:  this.rawData contains a byte array of all the raw data
		
		return null; 
	}
	
	// TODO
	/**
	 * @return  A byte array containing all the data after the headers.  That is all the data after the /r/n/r/n in the HTTP request.
	 * 				This is used for the HTTP connection.
	 */
	public byte[] getDataAfterHeaders()
	{
		return null;
	}
	
	/**
	 * @return  The remote host contained in the "GET http://remotehost.com" section of the passed byte array.
	 */
	public String getRemoteHost()
	{
		return remoteHost;
	}
	
	/**
	 * @return  The request data (that is, the byte array, but with the "GET http://remotehost.com" stripped down to "GET /"
	 * 	so it can be used to send out to a web server as a request)
	 */
	public byte[] getRequestData()
	{
		return requestData;
	}
	
	/**
	 * @return  The raw, untouched byte array.
	 */
	public byte[] getRawData()
	{
		return this.rawData;
	}

	/**
	 * @return  The remote port, as in "GET http://remotehost.com:88"
	 */
	public int getRemotePort()
	{
		return remotePort;
	}
}
