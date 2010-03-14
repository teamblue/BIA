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
			"GET / HTTP/1.1" + getHTTPNewline() +
			"Host: localhost" + getHTTPNewline() +
			getHTTPNewline();
		
		return message;
	}
	
	public static String getExpectedResponse()
	{
		String response =
			getHTTPNewline() +
			"HTTP/1.1 200 OK" + getHTTPNewline() +
			getHTTPNewline();
		
		return response;
	}
}
