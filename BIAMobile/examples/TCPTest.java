import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class TCPTest extends MIDlet implements CommandListener
{
	private Form mMainForm;
	private String host = "umanitoba.ca";
	private String resource = "/index.html";
	private int port = 80;
	
	public TCPTest()
	{
		mMainForm = new Form("TCPTest");
		mMainForm.addCommand(new Command("Exit", Command.EXIT, 0));
		mMainForm.setCommandListener(this);
		mMainForm.append("Attempting to connect to " + host + "...");
		
		try
		{
			//open raw TCP connection to umanitoba.ca on port 80
			SocketConnection client = (SocketConnection) Connector.open("socket://" + host + ":" + port);
			//grab the streams
			InputStream is = client.openInputStream();
			OutputStream os = client.openOutputStream();
			
			//send a basic GET request for "index.html"
			mMainForm.append("Connected. Sending GET request...");
			//the extra blank line at the end is important
			byte[] getReq = ("GET " + resource + " HTTP/1.1\nHost: " + host + "\n\n").getBytes();
			os.write(getReq);
			os.flush(); //flushing the stream is also important
			
			//parse the response
			mMainForm.append("Recieving response...");
			mMainForm.append("Response:");
			StringBuffer response = new StringBuffer("");
			
			int c = is.read();
			while(c != -1)
			{
				response.append((char) c);
				c = is.read();
			}
			mMainForm.append(response.toString());
			
			//close
			mMainForm.append("Closing connection...");
			client.close();
			mMainForm.append("Connection closed.");
		}
		
		catch(Exception e)
		{
			mMainForm.append("Exception:" + e.getMessage());
		}
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException
	{
		// TODO Auto-generated method stub

	}

	protected void pauseApp()
	{
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException
	{
		Display.getDisplay(this).setCurrent(mMainForm);
	}

	public void commandAction(Command c, Displayable s)
	{
		notifyDestroyed();
	}
}
