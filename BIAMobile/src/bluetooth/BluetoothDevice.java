package bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.DeviceClass;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class BluetoothDevice 
{
	private RemoteDevice device;
	private DeviceClass dClass;
	private StreamConnection connection;
	private String services;
	
	public BluetoothDevice( RemoteDevice device )
	{
		this.device = device;
		connection = null;
		dClass = null;
		services = null;
	}
	
	public BluetoothDevice( RemoteDevice device, DeviceClass dClass )
	{
		this.device = device;
		connection = null;
		this.dClass = dClass;
		services = null;
	}
	
	public void setServices( String services )
	{
		this.services = services;
	}
	
	public String getServices()
	{
		return services;
	}
	
	public RemoteDevice getRemoteDevice()
	{
		return device;
	}
	
	public DeviceClass getDeviceClass()
	{
		return dClass;
	}
	
	/*
	public boolean isConnected()
	{
		return connection != null;
	}
	*/
	
	public String name()
	{
		try 
		{
			return device.getFriendlyName(false);
		} 
		catch (IOException e) 
		{
			return "";
		}
	}
	
	public String address()
	{
		return device.getBluetoothAddress();
	}
	
	private void connect() throws IOException, BluetoothStateException
	{
		if ( services != null )
			connection = (StreamConnection)Connector.open(services);
		else
			throw new BluetoothStateException();
	}
	
	private void disconnect() throws IOException
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
	
	public void send( byte[] data ) throws IOException, BluetoothStateException
	{
		// Open a connection
		connect();
		
		sendPrivate( data );
		
		// Clean up the connection
		disconnect();
	}
	
	public byte[] receive() throws IOException, BluetoothStateException
	{
		byte[] response;
		
		// Open connection
		connect();
		
		response = receivePrivate();
		
		// Clean up
		disconnect();
		
		return response;
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
}
