enum HTTPType
{
	GET,
	POST,
	UNKNOWN
}

/**
 * Class Stub: Currently used to extract host from http packet. May be Expanded on later
 * @author DJSymBiotiX
 *
 */
public class HTTPObject
{
	private HTTPType _type;
	private String _url;
	private String _HTTPVersion;
	private String _host;
	private String _user_agent;
	private String _accept;
	private String _accept_language;
	private String _accept_encoding;
	private String _accept_charset;
	private String _keep_alive;
	private String _proxy_connection;
	private String _cookie;
	private String _message;
	
	public String getHost()
	{
		return _host;
	}
	
	public String getMessage()
	{
		return _message;
	}
	
	public HTTPObject()
	{
		_type = HTTPType.UNKNOWN;
		_url = "";
		_HTTPVersion = "";
		_host = "";
		_user_agent = "";
		_accept = "";
		_accept_language = "";
		_accept_encoding = "";
		_accept_charset = "";
		_keep_alive = "";
		_proxy_connection = "";
		_cookie = "";
		_message = "";
	}

	public HTTPObject(String message)
	{
		_message = message;
		parseHTMLMessage();
	}
	
	private void parseHTMLMessage()
	{
		String line[] = _message.split("\n");
		String split[];
		
		for(int i=0; i < line.length; i++)
		{
			split = line[i].split(" ");
			if(split.length > 1)
			{
				if(split[0].equals("Host:"))
				{
					_host = split[1].trim();
				}
			}
		}
	}
	
}
