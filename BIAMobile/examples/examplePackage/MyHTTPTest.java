package examplePackage;
import java.io.*;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class MyHTTPTest extends MIDlet implements CommandListener
{
	private static final String DEFAULTURL = "http://www.google.ca";
	private static final String POSTREQUEST = "This is a request";
	private static final String GET = "GET";
	private static final String POST = "POST";
	
	private Display     display;
	private Form   		frmMain;
	private Form        frmResult;
	private TextField   txtFldUrl;
	private TextField   txtFldMethod;
	
	private String      strPostRequest;
	
	private Command     cmdSend;
	private Command     cmdBack;
	
	public MyHTTPTest()
	{
		cmdSend = new Command("SEND", Command.OK, 1);
		cmdBack = new Command("BACK", Command.OK, 1);
		
		display = Display.getDisplay(this);
		
		frmMain      = new Form("Testing....");
		txtFldUrl    = new TextField("\nType in a URL:",DEFAULTURL,100,0);
		txtFldMethod = new TextField("\nType Http method: GET or POST:",GET, 25,0);
		frmMain.append(txtFldUrl);
		frmMain.append(txtFldMethod);
		frmMain.addCommand(cmdSend);
		frmMain.setCommandListener(this);
		
		frmResult = new Form("The results");
		frmResult.addCommand(cmdBack);
		frmResult.setCommandListener(this);
	}

	protected void startApp() throws MIDletStateChangeException
	{
		display.setCurrent(frmMain);
	}	

	public void commandAction(Command c, Displayable s)
	{
		String strUrl = txtFldUrl.getString();
		String strMethod = txtFldMethod.getString();
		
		if(c == cmdSend && (strMethod.equals(GET)))
			displayResponse(sendHttpGETRequest(strUrl));
		
		else if(c == cmdSend && strMethod.equals(POST))
			displayResponse(sendHttpPOSTRequest(strUrl));
		
		else if (c == cmdBack)
			display.setCurrent(frmMain);
	}
	
	private void displayResponse(String response) {
		frmResult.append(response);
		display.setCurrent(frmResult);
	}	
	
	private String sendHttpGETRequest(String strUrl) {
		
		HttpConnection 	hConn   = null;
		InputStream     dis     = null;
		StringBuffer    bufResponse = new StringBuffer();
		int				rc = 0,ch,
		                cnt = 0;
		
		try {
			//Http connection
			hConn = (HttpConnection)Connector.open(strUrl);					
			
			//get DataInputStream from http connection
			dis = new DataInputStream(hConn.openInputStream());
			
			//get response from server
			bufResponse.append("\nHttp GET Response from: " + strUrl + "\n");
			
			while(cnt <= 200 && (ch = dis.read()) != -1) {
				bufResponse.append((char)ch);
				cnt++;
			}
			bufResponse.append("\n");
		}
		catch (Exception e) {
			try {
				bufResponse.append("\nError connecting to: " + strUrl + "\n");
				e.printStackTrace();
			} catch(Exception ex) {}
		}
		finally {
			try {
				if(hConn != null)
					hConn.close();
				if(dis != null)
					dis.close();
				
				hConn = null;
				dis = null;
			}
			catch(Exception e) {}
		}
		
		return bufResponse.toString();
	}

	private String sendHttpPOSTRequest(String strUrl) {
		
		HttpConnection   hConn = null;
		InputStream      dis   = null;
		DataOutputStream dos   = null;
		
		StringBuffer     bufResponse = new StringBuffer();
		byte[]           requestBody;
		
		int ch, cnt = 0;
		
		try {
			//Http Connection and change request to POST
			hConn = (HttpConnection)Connector.open(strUrl);
			hConn.setRequestMethod(HttpConnection.POST);
			
			//Obtain DataOutputStream and send request to server
			dos = hConn.openDataOutputStream();
			requestBody = POSTREQUEST.getBytes();
			
			for(int x = 0; x < requestBody.length ; x++)
				dos.write(requestBody[x]);
			
			//Open input stream and get response from server
			dis = new DataInputStream(hConn.openInputStream());
			
			bufResponse.append("\nHttp POST Response from: " + strUrl + "\n");
			while(cnt <= 200 && (ch = dis.read()) != -1) {
				bufResponse.append((char)ch);
				cnt++;
			}
			bufResponse.append("\n");					
		}
		catch (Exception e) {
			try {
				bufResponse.append("\nError connecting to: " + strUrl + "\nResponseCode:" + hConn.getResponseCode() + "\n");
				e.printStackTrace();
			} catch(Exception ex) {}			
		}
		finally {
			try {
				if(hConn != null)
					hConn.close();
				if(dis != null)
					dis.close();
				if(dos != null)
					dos.close();
				
				hConn = null;
				dis = null;
				dos = null;				
			}
			catch(Exception e){}
		}
		return bufResponse.toString();
	}	
	
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		display = null;
		frmMain = null;
		frmResult = null;
		txtFldUrl = null;
		txtFldMethod = null;		
	}

	protected void pauseApp()
	{
		// TODO Auto-generated method stub

	}

}
