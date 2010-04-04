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
		
//		System.out.println("hello, sysout println, constructor");
//		mMainForm.append("hello, form.append, constructor\n");
//		mMainForm.append("before code\n");
//		testRequestResponse();
//		
//		mMainForm.append("after code\n");
	}
	
	private void testRequestResponse()
	{
		try
		{
			DesktopRequestHandler drh = new DesktopRequestHandler();
			
			byte[] response = null;
			
			String request = "GET http://www.google.ca/ HTTP/1.1\r\n" +
								"Host: www.google.ca\r\n"+
								"User-Agent: Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.1.8) Gecko/20100214 Ubuntu/9.10 (karmic) Firefox/3.5.8\r\n"+
								"\r\n";
			
			try
			{
				response = drh.sendRequest(request.getBytes());
				System.out.println("sent request");
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				mMainForm.append(e.toString());
				e.printStackTrace();
			}
			
			String responseStr = new String(response);
			
			mMainForm.append(responseStr);
		}
		catch (Exception e)
		{
			mMainForm.append("exception handler\n");
			mMainForm.append(e.toString());
			e.printStackTrace();
		}
		
	}
	
	public void startApp()
	{
		Display.getDisplay(this).setCurrent(mMainForm);
		
//		System.out.println("hello, sysout println, startApp");
//		mMainForm.append("hello, form.append, startApp\n");
		mMainForm.append("before code\n");
		testRequestResponse();
		
		mMainForm.append("after code\n");
	}

	public void pauseApp()
	{
//		System.out.println("hello, sysout println, pauseApp");
//		mMainForm.append("hello, form.append, pauseApp\n");
	}

	public void destroyApp(boolean unconditional)
	{
//		System.out.println("hello, sysout println, destroyApp");
//		mMainForm.append("hello, form.append, destroyApp\n");
	}

	public void commandAction(Command c, Displayable s)
	{
//		System.out.println("hello, sysout println, commandAction");
//		mMainForm.append("hello, form.append, commandAction\n");
		notifyDestroyed();
	}
}
