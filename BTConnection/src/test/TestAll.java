package test;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import org.junit.Assert;
import btclient.BluetoothClient;

public class TestAll {
    
    public static void main(String arg[]) throws InterruptedException
    {
        HostThread();
        Thread.sleep(5000);
        ClientThread();
    }

    synchronized public static Thread HostThread()
    {
        Thread HostThread = new Thread()
        {
            public void run()  
            {
                try
                {
                    btserver.BluetoothHost btHost = new btserver.BluetoothHost( new Request() );
                    btHost.acceptConnections();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            };
        };
        HostThread.start();
        return HostThread;
    }
    
    synchronized public static Thread ClientThread()
    {
        Thread ClientThread = new Thread()
        {
            public void run()  
            {
                try
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
                    Assert.assertTrue("Still here", 1 == 1);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            };
        };
        ClientThread.start();
        return ClientThread;
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
