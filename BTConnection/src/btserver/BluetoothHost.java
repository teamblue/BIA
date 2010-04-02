package btserver;

import javax.bluetooth.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothHost 
{
	private LocalDevice localDevice;
	
	private static final String uuid = "E6FEC3B275744C079B2F8883DBE38937";
	
	public static int MAX_BYTES = 100;
	
	private BluetoothRequest btRequest;
	
	public BluetoothHost( BluetoothRequest btRequest ) throws BluetoothStateException
	{
		localDevice = LocalDevice.getLocalDevice();
		localDevice.setDiscoverable(DiscoveryAgent.GIAC);
		
		this.btRequest = btRequest;
		
		System.out.println( "BluetoothHost created at addess " + localDevice.getBluetoothAddress() );
	}
	
	public void acceptConnections()
	{	
		// service url
		String serviceURL = "btspp://localhost:" + uuid + ";name=tether";
		
		try {
			// create a server connection
			StreamConnectionNotifier notifier = (StreamConnectionNotifier) Connector
					.open(serviceURL);

			while ( true )
			{
				// accept client connections
				System.out.println("Waiting for client connection");
				StreamConnection connection = notifier.acceptAndOpen();
				System.out.println("Client connected");
	
				// Fork a new thread to handle the connection
				new HandleConnection( connection );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class HandleConnection extends Thread
	{
		public HandleConnection( StreamConnection connection )
		{
			HashMap<String,String> sites = new HashMap<String, String>();
			
			sites.put("google.com", "This is google!");
			sites.put("engadget.com", "Tech news!");
			sites.put("umanitoba.ca", "Education!");
			
			try 
			{
				// prepare to send/receive data
				byte buffer[] = new byte[100];
				byte response[];
				
				InputStream is = connection.openInputStream();
				int readBytes = is.read( buffer );
				
				String request = new String(btRequest.dataRequested( buffer, readBytes ), 0, readBytes);
				response = sites.get(request).getBytes();
				
				OutputStream os = connection.openOutputStream();
				os.write( response );
				os.flush();
				os.close();
	
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}