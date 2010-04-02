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

import javax.bluetooth.BluetoothStateException;

import btclient.BluetoothClient;

public class TestClient {	
	public static void main( String args[] )
	{
		BluetoothClient client;
		
		System.out.println( "Starting client test.\nDiscovering devices and services..." );
		
		try
		{
			client = new BluetoothClient();
		}
		catch ( BluetoothStateException e )
		{
			e.printStackTrace();
			System.out.println( "Error initializing devices." );
			return;
		}
		
		try
		{
			client.getBTConnection();
		}
		catch ( BluetoothStateException e )
		{
			System.out.println( "No dice." );
		}
		
		System.out.println( "Done discovery" );
		
		String requests[] = {"umanitoba.ca", "google.com"};
				
		for ( String request : requests )
		{
			System.out.println("Sending request: " + request );
			
			byte[] response = null;
			
			try
			{
				response = client.request( request.getBytes() );
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
		
		
		System.out.println( "Done testing client." );
	}
}
