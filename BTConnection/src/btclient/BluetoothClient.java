package btclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.bluetooth.BluetoothStateException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import btserver.BluetoothHost;

public class BluetoothClient {
	

	private StreamConnection connection;
	private final String serviceUUID = "E6FEC3B275744C079B2F8883DBE38937";
	private BluetoothDiscoverer discoverer;	
	
	public BluetoothClient() throws BluetoothStateException{
		discoverer = new BluetoothDiscoverer();
	}
	
	public boolean getBTConnection() throws BluetoothStateException{
		discoverDevicesAndServices(serviceUUID);
		
		boolean retVal = false;
		if(discoverer.getService() != null){
			retVal = true;
		}
		
		return retVal;
	}
	
	private void connect() throws IOException
	{	
		String url = discoverer.getService().getConnectionURL(0, false);
		connection = (StreamConnection)Connector.open(url);	
	}
	
	public void disconnect() throws IOException
	{
		connection.close();
		connection = null;
	}
	
	private void sendPrivate( byte[] data ) throws IOException
	{
		OutputStream os = connection.openOutputStream();
		
		os.write( data );
		os.flush();
		os.close();
	}
	
	private byte[] receivePrivate() throws IOException
	{
		InputStream is = connection.openInputStream();
		
		byte[] response = new byte[BluetoothHost.MAX_BYTES];
		
		is.read( response );
		is.close();
		
		return response;
	}
	
	private byte[] requestPrivate( byte[] data ) throws IOException
	{
		sendPrivate( data );
		return receivePrivate();
	}
	
	public byte[] request( byte[] data ) throws IOException, BluetoothStateException
	{
		byte[] response;
		
		// Open a connection
		connect();
		
		// Get the response
		response = requestPrivate( data );
		
		// Clean up the connection 
		disconnect();
		
		// return the response
		return response;
	}	
	
	/**
	 * Called by consumer to discover a service.
	 * @param uuid
	 * @throws BluetoothStateException
	 */
	public void discoverDevicesAndServices( String uuid ) throws BluetoothStateException
	{
		discoverer.discoverDevices();
		discoverer.discoverServices( uuid );
	}
}
