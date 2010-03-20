package testServer;

public class MobileTestConstants
{
	public static String getHTTPNewline()
	{
		return "\r\n";
	}
	
	public static int getServerPort()
	{
		return 8741;
	}
	
	public static String getRequestMessage()
	{
		String message =
			"GET http://localhost/ HTTP/1.0" + getHTTPNewline() +
			"Host: localhost" + getHTTPNewline() +
			getHTTPNewline();
		
		return message;
	}
	
	public static String getExpectedResponse()
	{
		String response =
			getHTTPNewline() +
			"HTTP/1.0 200 OK" + getHTTPNewline() +
			getHTTPNewline();
		
		return response;
	}
}
