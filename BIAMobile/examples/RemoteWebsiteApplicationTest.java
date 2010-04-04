import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;

import biamobile.DesktopRequestHandler;
import biamobileTest.MobileTestConstants;


public class RemoteWebsiteApplicationTest extends MIDlet implements CommandListener
{
	private Form mMainForm;
	
	public RemoteWebsiteApplicationTest()
	{
		mMainForm = new Form("RemoteWebsiteApplicationTest");
		mMainForm.addCommand(new Command("Exit", Command.EXIT, 0));
		mMainForm.setCommandListener(this);
	}
	
	private void testRequestResponse()
	{
		DesktopRequestHandler drh = new DesktopRequestHandler();
		
		byte[] response = null;
		
		String request = "GET http://www.google.com/ HTTP/1.1\r\n" +
							"Host: www.google.com\r\n"+
							"User-Agent: Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.1.8) Gecko/20100214 Ubuntu/9.10 (karmic) Firefox/3.5.8\r\n"+
							"\r\n";
		
		try
		{
			response = drh.sendRequest(request.getBytes());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String responseStr = new String(response);
		
		System.out.println(responseStr);
		
	}
	
	public void startApp()
	{
		testRequestResponse();
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
