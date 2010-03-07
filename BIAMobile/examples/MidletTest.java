import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;

public class MidletTest extends MIDlet implements CommandListener
{
	private Form mMainForm;

	public MidletTest()
	{
		mMainForm = new Form("HelloMIDlet");
		mMainForm.addCommand(new Command("Exit", Command.EXIT, 0));
		mMainForm.setCommandListener(this);
		
		String remoteHost = "http://www.google.ca";
			
		try
		{
			mMainForm.append("Connecting to " + remoteHost + "...");
			
			String response = getViaHttpConnection(remoteHost);
			mMainForm.append(new StringItem(null, response));
		}
		catch (Exception e)
		{
			mMainForm.append(e.toString());
		}
	}

	// get information from an http connection
	// taken from
	// http://java.sun.com/javame/reference/apis/jsr118/javax/microedition/io/HttpConnection.html
	private String getViaHttpConnection(String url) throws IOException
	{
		String response = "";

		HttpConnection c = null;
		InputStream is = null;
		int rc;

		try
		{
			c = (HttpConnection) Connector.open(url);
			
			/***** Sets headers ******/
            c.setRequestProperty("User-Agent",
            "Profile/MIDP-2.0 Configuration/CLDC-1.0");
            
            c.setRequestProperty("Content-Language", "en-US");

			/*****************************/
			/** Request is sent here **/
			/*****************************/
			// Getting the response code will open the connection,
			// send the request, and read the HTTP response headers.
			// The headers are stored until requested.
			rc = c.getResponseCode();
			if (rc != HttpConnection.HTTP_OK)
			{
				throw new IOException("HTTP response code: " + rc);
			}

			is = c.openInputStream();

			// Get the ContentType
			String type = c.getType();

			// Get the length and process the data
			int len = (int) c.getLength();
			
			mMainForm.append("Response length: " + len);
			
			if (len > 0)
			{
				int actual = 0;
				int bytesread = 0;
				byte[] data = new byte[len];
				
				// reads response into byte array
				while ((bytesread != len) && (actual != -1))
				{
					actual = is.read(data, bytesread, len - bytesread);
					bytesread += actual;
				}
				
				response = new String(data);
				
			}
			else
			{
				int ch;
				while ((ch = is.read()) != -1)
				{
					//
				}
			}
		}
		catch (ClassCastException e)
		{
			throw new IllegalArgumentException("Not an HTTP URL");
		}
		finally
		{
			if (is != null)
			{
				is.close();
			}
			if (c != null)
			{
				c.close();
			}
		}

		return response;
	}

	public void startApp()
	{
		Display.getDisplay(this).setCurrent(mMainForm);
	}

	public void pauseApp()
	{
	}

	public void destroyApp(boolean unconditional)
	{
	}

	public void commandAction(Command c, Displayable s)
	{
		notifyDestroyed();
	}
}
