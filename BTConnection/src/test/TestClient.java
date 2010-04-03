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

public class TestClient 
{  
  public static void main( String args[] )
  {
    BluetoothClient client;
    String requests[] = {"http://www.google.com", "This is a very long request, and it surely is bigger than 100 bytes.  I want to test the ability of the BluetoothHost to determine if it is capable of reading in a stream of arbitrary length.  This capability is necessary to send/receive data."};
    
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
    
    for ( String request : requests )
    {
      System.out.println("Sending request: " + request );         
    
      try
      {
        processRequest( client, request );              
      }
      catch ( IOException e )
      {
        System.out.println( "Error with request" );
      }     
    }

    System.out.println( "Done testing client." );
  }
  
  private static void processRequest( BluetoothClient client, String request ) throws IOException
  {
    client.connect();
    client.postRequest( request.getBytes() );
    
    while ( client.hasMoreData() )
    {
      byte[] response = client.receive();
      
      if ( response != null )
      {
        System.out.println( new String( response, 0, response.length ) );
      }
    }
    
    client.disconnect();
  }
}