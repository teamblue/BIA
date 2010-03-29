package biamobile;

/**
 * Provides a "View" over a byte array to interpret it as HTTP data.
 * @author aaron
 *
 */
public class RequestHTTPView
{
	private byte[] rawData;
	
	private final static String WHITESPACE = " ";
	
	public RequestHTTPView(byte[] data)
	{
		this.rawData = data;
	}
	
	public String getRemoteHost()
	{
		String remoteHost = null;
		
		String getSection = null;
		String hostSection = null;
		String remainderSection = null;
		
		int index;
		int prevIndex;
		
		if (rawData != null)
		{
			// first line of header should look like
			//	GET http://localhost HTTP/1.0 \r\n\r\n
			// gets first line
			StringBuffer firstLine = new StringBuffer();
			for (int i = 0; i < rawData.length && rawData[i] != '\n'; i++)
			{
				firstLine.append((char)rawData[i]);
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
				int endIndex = hostSection.indexOf("/", index+2); // look for last index of "/", as in http://www.google.ca/
				
				if (endIndex == -1) // if no such character found
				{
					endIndex = hostSection.length();
				}
				remoteHost = hostSection.substring(index+2, endIndex); // +2 to skip over //
			}
			else
			{
				// no remote host
			}
		}
		
		//return remoteHost;
		return remoteHost;
	}
}
