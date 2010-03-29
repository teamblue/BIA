package biamobile;

/**
 * Provides a "View" over a byte array to interpret it as HTTP data.
 * @author aaron
 *
 */
public class RequestHTTPView
{
	private byte[] rawData;
	
	public RequestHTTPView(byte[] data)
	{
		this.rawData = data;
	}
	
	public String getRemoteHost()
	{
		String remoteHost = null;
		
		if (rawData != null)
		{
			// gets first line
			StringBuffer firstLine = new StringBuffer();
			for (int i = 0; i < rawData.length && rawData[i] != '\n'; i++)
			{
				firstLine.append(rawData[i]);
			}
			
			
		}
		
		//return remoteHost;
		return remoteHost;
	}
}
