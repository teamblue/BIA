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
		
		String[] remoteHosts = {
				"http://www.google.ca",
				"http://www.umanitoba.ca"};
			
		for (int i = 0; i < remoteHosts.length; i++)
		{
			try
			{
				mMainForm.append("\n\nConnecting to " + remoteHosts[i] + "...");
				String response = getViaHttpConnection(remoteHosts[i]);
				mMainForm.append(new StringItem(null, response));
			}
			catch (Exception e)
			{
				mMainForm.append(e.toString());
			}
		}
	}

	/**
	 * Opens up and gets information from an http connection
	 * taken from http://java.sun.com/javame/reference/apis/jsr118/javax/microedition/io/HttpConnection.html
	 * 
	 * @param url  The url to connect to, ex "http://www.google.ca"
	 * @param host  The host part to place in the header, ex "www.google.ca"
	 */
	private String getViaHttpConnection(String url) throws IOException
	{
		StringBuffer response = new StringBuffer();

		HttpConnection c = null;
		InputStream is = null;
		int rc;

		try
		{
			c = (HttpConnection) Connector.open(url);
			
			/***** Sets headers ******/
            //c.setRequestProperty("Host","hostname");
            //c.setRequestProperty("User-Agent","Profile/MIDP-2.0 Configuration/CLDC-1.0");

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
			
			int ch;
			int bytesRead = 0;
			while ((ch = is.read()) != -1)
			{
				response.append((char)ch);
				bytesRead++;
				
				// only reads 1st 100 bytes, so as to not fill up screen with lots of data
				if (bytesRead > 100)
				{
					break;
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

		return response.toString();
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
