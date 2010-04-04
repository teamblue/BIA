package btclient;
/*  
 * this is a simple wrapper to abstract the bluetooth client functionality into something simple
 * 
 * */

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

public class SimpleBluetoothClient 
{  
	public BluetoothClient client;
	public boolean connected = false;
	public String write(String request){
		String result = null;

		if(client==null){
			setup();
		}
		try
	    {
			System.out.println("asdasd");
			result = processRequest( request );              
	    }
	    catch ( IOException e )
	    {
	    	System.out.println( "Error with request" );
	    }     

		return result;
	}

	public void setup(){
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

	    try {
	    	System.out.println("Connecting");
			client.connect();
			connected = true;
	    	System.out.println("Connected");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	public void tearDown(){
		try {
			client.disconnect();
			connected = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

  private String processRequest( String request ) throws IOException
  {
    String result = "";
    client.postRequest( request.getBytes() );
    while ( client.hasMoreData() )
    {
      byte[] response = client.receive();
      
      if ( response != null )
      {
    	result += new String( response, 0, response.length );
       }
    }
    return result;
  }
}