package mobileDesktopHandler;
import javax.bluetooth.*;
import java.io.*;

public class BluetoothDevice 
{	
	private RemoteDevice 	remoteDevice;
	private DeviceClass 	deviceClass;
	private String 			serviceURL;
	
	private static boolean askFriendlyNameEachTime = false;
	
	public BluetoothDevice()
	{
		remoteDevice = null;
		deviceClass = null;
		serviceURL = null;
	}
	
	public BluetoothDevice(RemoteDevice device, DeviceClass dClass)
	{
		this.remoteDevice = device;
		this.deviceClass = dClass;
		serviceURL = null;
	}

	//Set the services URL for this device. Should not be modified by any class other than BluetoothDevices.
	public void setServices(String services)
	{
		serviceURL = services;
	}

	//Returns the services URL for this device.
	public String getServices()
	{
		return serviceURL;
	}

	//Returns the RemoteDevice for this instance.
	public RemoteDevice getRemoteDevice()
	{
		return(remoteDevice);
	}


	//Returns the DeviceClass for this instance.
	public DeviceClass getDeviceClass()
	{
		return(deviceClass);
	}

	//Returns the Bluetooth friendly name of this instance.
	public String name() throws IOException
	{
		// Nested function throws exception; may need further handling
		
		return(remoteDevice.getFriendlyName(askFriendlyNameEachTime));
	}

	//Returns the Bluetooth address of this instance.
	public String address()
	{
		return(remoteDevice.getBluetoothAddress());
	}

	// Send data to this device.
	public void send(byte[] data ) throws IOException, BluetoothStateException
	{
	}

	//Try to receive data from this device.
	public byte[] receive() throws IOException, BluetoothStateException
	{
		byte[] returnBuffer = new byte[100];
		
		// receive data as per connection specification
		
		return(returnBuffer);
	}

	
	//Send data to this device and receive a response from this device.
	public byte[] request( byte[] data ) throws IOException, BluetoothStateException
	{
		byte[] returnBuffer = new byte[100];
		
		// Send data as per connection specification and receive response
		
		return(returnBuffer);
	}

	

}
