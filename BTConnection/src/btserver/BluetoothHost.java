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

import btcommon.Constants;
import testinternet.RequestHandler;

public class BluetoothHost
{
  private LocalDevice localDevice;  
  private BluetoothRequest btRequest;

  public BluetoothHost( BluetoothRequest btRequest ) throws BluetoothStateException
  {
    initHost( btRequest );    
  }
  
  private void initHost( BluetoothRequest btRequest ) throws BluetoothStateException
  {
    localDevice = LocalDevice.getLocalDevice();
    localDevice.setDiscoverable(DiscoveryAgent.GIAC);    
    this.btRequest = btRequest;
    
    System.out.println( "BluetoothHost -  Created at addess " + localDevice.getBluetoothAddress() );
  }

  public void acceptConnections()
  {
    // service url
    String serviceURL = "btspp://localhost:" + Constants.SERVICE_UUID + ";name=tether";
  
    try 
    {
      // create a server connection
      StreamConnectionNotifier notifier = (StreamConnectionNotifier) Connector.open(serviceURL);
      
      while ( true )
      {
        // accept client connections
        System.out.println( "BluetoothHost - Waiting for client connection" );
        StreamConnection connection = notifier.acceptAndOpen();
        System.out.println( "BluetoothHost - Client connected" );
        
        // Fork a new thread to handle the connection
        new HandleConnection( connection );
      }
    } 
    catch (IOException e) 
    {
      e.printStackTrace();
    }
  }

  class HandleConnection extends Thread
  {
    public HandleConnection( StreamConnection connection )
    {           
      try 
      {
        String        request = getRequest( connection );
        OutputStream  os      = connection.openOutputStream();
        
        os.write( getResponse( request ) );
        os.flush();
        os.write( Constants.END_TRANSFER_CODE.getBytes() );
        os.flush();
        os.close();
      }
      catch ( IOException e )
      {
        System.out.println( "BluetoothHost - exception: " + e.getMessage() );
      }
    }
    
    private String getRequest( StreamConnection connection ) throws IOException
    {
      InputStream   is        = connection.openInputStream();
      byte          buffer[]  = new byte[ Constants.TRANSFER_MAX_BYTES ];            
      int           readBytes = 0;
      StringBuffer  request   = new StringBuffer();
      boolean       done      = false;
    
      // HT: The request might be longer than 100 bytes, so we must read 100 bytes at a time, until there is no more data to read
      while ( !done )
      {           
        String input;
        
        readBytes = is.read( buffer );
        input = new String( btRequest.dataRequested( buffer, readBytes ), 0, readBytes );
        request.append( input );
        done = Constants.containsEndingCode( input );
        
        System.out.println( "Got: " + input );
      }
      
      is.close();
      request.delete( request.length() - Constants.END_TRANSFER_CODE.length(), request.length() );
      
      return request.toString();
    }       
    
    /*
     * HT: This method is a place-holder for the mobile application.  The mobile application is responsible for
     * fetching the data from the given request (which is a URL).
     * The RequestHandler class, given a URL, will fetch the content and return the entire c
     */
    private byte[] getResponse( String request )
    {
      RequestHandler handler = new RequestHandler();
      byte[] data = handler.fetch( request );
      
      return data != null ? data : ( new String() ).getBytes();           
    }
  }
}