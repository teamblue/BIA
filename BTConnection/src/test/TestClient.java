package test;

//To test bluetooth:
//Expand 'Referenced Libraries'
//Expand 'bluecove-emu-2.1.0.jar'
//Expand 'com.intel.bluetooth.emu'
//Run 'EmuServer.class' as a Java Application
//
//Once configured, run TestServer.java as a Java Application
//Once configured, run TestClient.java as a Java Application

// To run the client:
// Go to 'Run As' > 'Run Configurations...'
// Setup a new Java Application and under 'Arguments' add '-Dbluecove.stack=emulator' to 'VM arguments'

import java.io.IOException;
import java.util.Collection;

import javax.bluetooth.BluetoothStateException;

public class TestClient {
	private static final String UUID = "E6FEC3B275744C079B2F8883DBE38937";
	
	public static void main( String args[] )
	{
		bluetooth.BluetoothDevices devices;
		Collection<bluetooth.BluetoothDevice> deviceCollection;
		
		System.out.println( "Starting client test.\nDiscovering devices and services..." );
		
		try
		{
			devices = new bluetooth.BluetoothDevices();
		}
		catch ( BluetoothStateException e )
		{
			e.printStackTrace();
			System.out.println( "Error initializing devices." );
			return;
		}
		
		try
		{
			devices.discoverDevicesAndServices( UUID );
		}
		catch ( BluetoothStateException e )
		{
			System.out.println( "No dice." );
		}
		
		System.out.println( "Done discovery" );
		
		deviceCollection = devices.getDevicesCollection();
		
		for ( bluetooth.BluetoothDevice device : deviceCollection )
		{
			System.out.println( "Device " + device.name() + " at address " + device.address() );
			String requests[] = {"umanitoba.ca", "google.com"};
				
			for ( String request : requests )
			{
				System.out.println("Sending request: " + request );
					
				byte[] response = null;
					
				try
				{
					response = device.request( request.getBytes() );
				}
				catch ( IOException e )
				{
					System.out.println( "Error with request" );
				}
					
				if ( response != null )
				{
					int bytes=0;
					while(response[bytes] != 0){
						bytes++;
					}
					String result = new String(response, 0, bytes);
		
					System.out.println("Recieved: " + result);
				}
			}
		}
		System.out.println( "Done testing client." );
	}
}
