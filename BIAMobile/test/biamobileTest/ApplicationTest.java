package biamobileTest;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import biamobile.MobileDesktopHandler;

/**
 * Tests out the full mobile application by attempting to send a request to the application, and waits for
 *  the expected responses.
 *  
 * Note: before you start this test, you must run the BIATestServer Test server code.
 * @author aaron
 *
 */
public class ApplicationTest extends MIDlet implements CommandListener
{
	private Form mMainForm;
	
	private void testRequestResponse()
	{
		MobileDesktopHandler mdh = new MobileDesktopHandler();
		
		byte[] response;
		
		response = mdh.sendRequest(MobileTestConstants.getTCPRequestMessage().getBytes());
		
		String responseStr = new String(response);
		
		if (responseStr.equals(MobileTestConstants.getTCPRequestMessage()))
		{
			mMainForm.append("Passed...");
		}
		else
		{
			mMainForm.append("Failed...");
		}
		
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		// TODO Auto-generated method stub
		
	}

	protected void pauseApp()
	{
		// TODO Auto-generated method stub
		
	}

	protected void startApp() throws MIDletStateChangeException
	{
		mMainForm = new Form("ApplicationTest");
		mMainForm.addCommand(new Command("Exit", Command.EXIT, 0));
		mMainForm.setCommandListener(this);
		
		testRequestResponse();
	}

	public void commandAction(Command c, Displayable s)
	{
		notifyDestroyed();
	}
}
