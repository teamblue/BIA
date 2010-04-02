package bluetooth;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;

// A wrapper for the BluetoothDevice(s)
public class Bluetooth
{
	private BluetoothDevices devices;
	private BluetoothDevice device;
	
	public Bluetooth() throws BluetoothStateException
	{
		devices = null;
		device = null;
	}
	
	public boolean connect()
	{
		try
		{
			devices = new BluetoothDevices();
		}
		catch( BluetoothStateException e )
		{
			return false;
		}
		
		device = devices.connect();
		
		return device != null;
	}
	
	public boolean isConnected()
	{
		return device != null;
	}
	
	//////////////////////////////////
	///// ACCESSORS AND MUTATORS /////
	//////////////////////////////////
	
	public String name()
	{
		return device.name();
	}
	
	public String address()
	{
		return device.address();
	}
	
	///////////////////////////////////////
	///// PUBLIC SEND/RECEIVE/REQUEST /////
	///////////////////////////////////////
	
	/*
	public void send( byte[] data ) throws IOException, BluetoothStateException
	{
		device.send(  data );
	}
	
	public byte[] receive() throws IOException, BluetoothStateException
	{
		return device.receive();
	}
	*/
	public byte[] request( byte[] data ) throws IOException, BluetoothStateException
	{
		return device.request( data );
	}
}
