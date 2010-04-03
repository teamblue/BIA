package btclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.bluetooth.BluetoothStateException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import btcommon.Constants;

public class BluetoothClient 
{
  private StreamConnection    connection;  
  private BluetoothDiscoverer discoverer;
  private InputStream         inputStream;
  private boolean             moreData;

  public BluetoothClient() throws BluetoothStateException
  {    
    initClient();    
  }
  
  private void initClient() throws BluetoothStateException
  {
    discoverer = new BluetoothDiscoverer();
  }

  public boolean getBTConnection() throws BluetoothStateException
  {
    discoverDevicesAndServices( Constants.SERVICE_UUID );
    
    boolean retVal = false;
    if(discoverer.getService() != null)
    {
      retVal = true;
    }

    return retVal;
  }
  
  public void postRequest( byte[] data ) throws IOException, BluetoothStateException
  {               
    OutputStream os = connection.openOutputStream();
    
    os.write( data );
    os.flush();
    os.write( Constants.END_TRANSFER_CODE.getBytes() );
    os.flush();
    os.close();
  }

  public void connect() throws IOException
  {
    String url = discoverer.getService().getConnectionURL(0, false);
    connection = (StreamConnection)Connector.open(url);
    inputStream = connection.openInputStream();
    moreData   = true;
  }

  public void disconnect() throws IOException
  {
    connection.close();
    connection = null;
    inputStream.close();
    moreData   = false;
  }
  
  public boolean hasMoreData()
  {
    return moreData;
  }
  
  public byte[] receive() throws IOException
  {        
    byte[] response   = null;
    
    if ( moreData )
    {
      response = new byte[ Constants.TRANSFER_MAX_BYTES ];
      int numBytesRead  = inputStream.read( response );   
      moreData = ( numBytesRead != -1 ) && !Constants.containsEndingCode( new String( response, 0, numBytesRead ) );
    }
    
    return formatResponse( response );
  }
  
  private byte[] formatResponse( byte[] response )
  {
    if ( response != null && !moreData )
    {
      String line = new String( response, 0, response.length );       
      
      return line.substring( 0, line.indexOf( Constants.END_TRANSFER_CODE ) ).getBytes();
    }
    
    return response;
  }

  /**
  * Called by consumer to discover a service.
  * @param uuid
  * @throws BluetoothStateException
  */
  private void discoverDevicesAndServices( String uuid ) throws BluetoothStateException
  {
    discoverer.discoverDevices();
    discoverer.discoverServices( uuid );
  }
}