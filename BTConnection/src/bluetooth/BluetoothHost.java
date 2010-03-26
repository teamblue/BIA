package bluetooth;

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
	
	public BluetoothHost() throws BluetoothStateException
	{
		localDevice = LocalDevice.getLocalDevice();
		localDevice.setDiscoverable(DiscoveryAgent.GIAC);
		
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
				
				InputStream is = connection.openInputStream();
				int readBytes = is.read(buffer);
				
				String result = sites.get(new String(buffer, 0, readBytes));
				if(result == null){
					result = "Not found!";
				}
				
				OutputStream os = connection.openOutputStream();
				os.write(result.getBytes());
				os.flush();
	
				//connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

/*
 * BLUETOOTHNODE.JAVA

package bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;


/**
 * Sample client to demonstrate the use of BlueSim JSR-82 Simulator.
 * 
 * This sample program tries to search and connect to a SPP service with the
 * UUID '10203040607040A1B1C1DE100'
 * 
 * The TestServer.java sample program shows how to create such a service. To run
 * this client part, run the TestServer.java sample first, so that the client
 * can connect to the service.
 * 
 * 
 * The client program can be run on a different machine on the same network.
 * 
 * @copyright JSRSoft
 * /
public class BluetoothNode {

	public static final String UUID = "E6FEC3B275744C079B2F8883DBE38937";
	private static final int MAX_BYTES = 100;
	
	private LocalDevice localDevice;
	private Discoverer discoverer;
	private StreamConnection connection;

	public BluetoothNode() throws Exception {
		localDevice = LocalDevice.getLocalDevice();
		discoverer = new Discoverer(localDevice.getDiscoveryAgent());
		
		System.out.println("Created node at " + localDevice.getBluetoothAddress() + " [ "
				+ localDevice.getFriendlyName() + " ]");
	}
	
	public void discoverTetherServices(){
		try {
			System.out.println("Starting device inquiry...");
			discoverer.discoverDevices();
			System.out.println("Device inquiry complete!");
			
			System.out.println("Starting service inquiry...");
			discoverer.discoverServices(discoverer.getDevices().keySet(), UUID);
			System.out.println("Service inquiry complete!");
		}
		catch (BluetoothStateException e){
			System.out.println("Device/Service inquiry failed!");
		}
		

	}
	
	public HashMap<String, String> getTetherServices() {
		return discoverer.getServices();
	}
	
	public void connectToService(String address) throws IOException{
		connection = (StreamConnection) Connector.open(discoverer.getServices().get(address));
	}
	
	public byte[] sendRequest(byte[] request) throws IOException{
		InputStream is = connection.openInputStream();
		OutputStream os = connection.openOutputStream();
		
		byte[] response = new byte[MAX_BYTES];
		
		os.write(request);
		os.flush();
		os.close();
		
		is.read(response);
		is.close();
		
		return response;
	}

		
		
		
//		UUID serverUUID = new UUID("10203040607040A1B1C1DE100", false);
//
//		try {
//			System.out.println("Searching for service");
//			String url = dAgent.selectService(serverUUID,
//					ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
//
//			if (url == null) {
//				System.out.println("No matching service found!");
//				return;
//			}
//
//			System.out.println("Service URL : " + url);
//
//			System.out.println("Connecting with server");
//			// open a connection to the server
//			StreamConnection connection = (StreamConnection) Connector
//					.open(url);
//			// Send/receive data
//			try {
//				byte buffer[] = new byte[100];
//				String msg = "hello there, server";
//				InputStream is = connection.openInputStream();
//				OutputStream os = connection.openOutputStream();
//
//				System.out.println("Reading data from server");
//				int read = is.read(buffer);
//				System.out.println("Data Recieved : "
//						+ new String(buffer, 0, read));
//
//				System.out.println("Sending data to server");
//				os.write(msg.getBytes());
//
//				os.flush();
//				os.close();
//
//				System.out.println("Closing server");
//				connection.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

}

*/
