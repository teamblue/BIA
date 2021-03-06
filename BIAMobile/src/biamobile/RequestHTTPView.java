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
	private byte[] entityBody;
	private String remoteHost;
	private int remotePort;
	private Hashtable headerHash;
	
	private String httpRequestMethod;
	
	private final static String WHITESPACE = " ";
	private final String HEADEREND = "\r\n\r\n";
	
	public RequestHTTPView(byte[] data)
	{
		this.rawData = data;
		remoteHost = null;
		
		remotePort = 80; // defaults to 80
		
		extractRequestData();
		extractHeaders();
		extractBody();
	}
	
	// pull out remote host, remote port, etc from request
	// make a byte array containing the request data to be sent
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
			httpRequestMethod = getSection;
			
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
	
	// Retrieve headers from current byte array and enter them into hash table
	// in the format (header field name, header value)
	public void extractHeaders()
	{
		int currentPosition, nextPosition;
		boolean headersPresent, headersRemain;
		String headerField, headerValue;
		String dataString = "";
		
		headersPresent = headersRemain = true;
		headerHash = new Hashtable();
		currentPosition = nextPosition = 0;
		
		if(rawData != null)
		{
			dataString = new String(rawData);
			
			currentPosition = dataString.indexOf('\r') + 1; // sets current position at start of first header
			nextPosition = dataString.indexOf(HEADEREND); // marks end of header section
			
			if(nextPosition <=  currentPosition)
				headersPresent = false;
		}
		else
		{
			headersPresent = false;
			System.out.println("rawData is empty.");
		}
		
		if(headersPresent)
		{
			dataString = dataString.substring(currentPosition, nextPosition); // truncate to header section only
			dataString = dataString + HEADEREND; // Add CRLF to end to help with parsing, as it was stripped off
			
			System.out.println(dataString);
			
			currentPosition = 1; // Skip over linefeed still at beginning of string
			
			while(headersRemain)
			{
				// Get header field name
				nextPosition = dataString.indexOf(':', currentPosition);
				headerField = dataString.substring(currentPosition, nextPosition);
				System.out.println("Header: *" + headerField + "*");
				
				// Get header value
				currentPosition = nextPosition + 2; // skip past whitespace
				nextPosition = dataString.indexOf('\r', currentPosition);
				
				headerValue = dataString.substring(currentPosition, nextPosition);	
				System.out.println("Value: *" + headerValue + "*");
				
				// Insert into hash table
				headerHash.put(headerField, headerValue);
				
				// Skip to next header line
				currentPosition = nextPosition + 2; // Add two to skip past line feed
				System.out.println("Entry added into hash table.");
				
				if(currentPosition >= (dataString.length() - HEADEREND.length()))
					headersRemain = false;
			}
		}
	}
	
	public void extractBody()
	{
		int currentPosition, nextPosition;
		boolean bodyPresent;
		String dataString = "";
		
		bodyPresent = true;
		
		if(rawData != null)
			dataString = new String(rawData);
		else
			bodyPresent = false;
		
		if(bodyPresent)
		{
			// Truncate dataString to only contain request body
			currentPosition = dataString.indexOf(HEADEREND) + HEADEREND.length(); // first good byte of request body data
			nextPosition = dataString.length(); // last byte of request
			
			if(currentPosition < nextPosition)
			{
				dataString = dataString.substring(currentPosition, nextPosition);
				
				System.out.println("Entity body: " + dataString);
				
				entityBody = dataString.getBytes();
		
			}
		}
	}
	
	public String getRemoteHost()
	{
		return remoteHost;
	}
	
	public byte[] getRequestData()
	{
		return requestData;
	}
	
	public byte[] getRawData()
	{
		return this.rawData;
	}
	
	public String getHTTPMethod()
	{
		return httpRequestMethod;
	}

	public int getRemotePort()
	{
		return remotePort;
	}
	
	public Hashtable getHeaderHash()
	{
		return headerHash;
	}
	
	public byte[] getEntityBody()
	{
		return entityBody;
	}
}
