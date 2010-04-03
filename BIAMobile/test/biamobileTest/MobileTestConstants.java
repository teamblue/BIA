package biamobileTest;

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
	
	public static String getTestServerHostName()
	{
	    return "localhost";
	}
	
	public static String getFullApplicationTestRequestBluetooth()
	{
		String message =
			"GET http://" + getTestServerHostName() + ":" + getServerPort() + "/ HTTP/1.0" + getHTTPNewline() +
			"Host: " + getTestServerHostName() + getHTTPNewline() +
			getHTTPNewline();
		
		return message;
	}
	
	public static String getFullApplicationTestRequestMobileApp() // the request from mobile app (without http://blah blah)
	{
		String message =
			"GET / HTTP/1.0" + getHTTPNewline() +
			"Host: " + getTestServerHostName() + getHTTPNewline() +
			getHTTPNewline();
		
		return message;
	}
	
	public static String getTCPRequestMessage()
	{
		String message =
			"GET http://" + getTestServerHostName() + ":" + getServerPort() + "/ HTTP/1.0" + getHTTPNewline() +
			"Host: " + getTestServerHostName() + getHTTPNewline() +
			getHTTPNewline();
		
		return message;
	}
	
	public static String getHTTPRequestMessage()
	{
		//The HttpConnection object uses HTTP 1.1 (not 1.0), and there's nothing we can do to change this,
		//so we need a separate request message for http (since TestServer compares the actual request with
		//this message)
		String message =
			"GET / HTTP/1.1" + getHTTPNewline() +
			"Content-Length: 0" + getHTTPNewline() +
			"Host: " + getTestServerHostName() + ":" + getServerPort() + getHTTPNewline() +
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
