package mobileDesktopHandler;
import javax.microedition.io.*;
import java.util.*;
import javax.bluetooth.*;
import javax.obex.*;

public class BluetoothDevices implements DiscoveryListener
{
	private static BluetoothHost localDevice;
	private static DiscoveryAgent discoveryAgent;
	
	//bluetoothAddress = key, device = value
	private static Hashtable devices;
	private static Hashtable services;
	
	private static String 	 addressOfSearchedDevice;
	private static String    serviceUUID = "0x0008"; // OBEX service UUID
	
	private BluetoothDevice	deviceInterrogatedForServices = null;
	
	public BluetoothDevices(BluetoothHost localDevice) throws BluetoothStateException
	{
		this.localDevice = localDevice;
		discoveryAgent = localDevice.getDiscoveryAgent();
		
		devices = new Hashtable();
		services = new Hashtable();
		
		addressOfSearchedDevice = null;
	}
	
	public Hashtable getDevices()
	{
		return(devices);
	}
	
	public Hashtable getServices()
	{
		return(services);
	}
	
	//Discovers devices and services.
	public void discoverDevicesAndServices(String uuid) throws BluetoothStateException
	{
		// Likely set lock here, depending on implementation
		discoverDevices();
		discoverServices(serviceUUID);
		
		System.out.println("Device and service discovery completed.");
	}

	//Discovers devices.
	public void discoverDevices() throws BluetoothStateException
	{
		discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
	}

	//Discovers services.
	public void discoverServices(String serviceUUID) throws BluetoothStateException
	{	
		UUID searchUUID[] = {new UUID(serviceUUID, true)};
		int[] attributes = new int[0];
		
		Enumeration keyList = devices.keys();
		
		while(keyList.hasMoreElements())
		{
			addressOfSearchedDevice = (String)keyList.nextElement();
			deviceInterrogatedForServices = (BluetoothDevice)devices.get(addressOfSearchedDevice);
			discoveryAgent.searchServices(attributes, searchUUID, deviceInterrogatedForServices.getRemoteDevice(), this);
		}
	}

	public void deviceDiscovered(RemoteDevice discoveredDevice, DeviceClass deviceClass) 
	{
		ServiceRecord thisRecord;
		BluetoothDevice foundDevice = new BluetoothDevice(discoveredDevice, deviceClass);
		
		devices.put(foundDevice.address(), discoveredDevice);		
	}

	public void inquiryCompleted(int discType) 
	{
		// Likely release lock/semaphore here depending on implementation

		System.out.println("Device discovery completed.");
	}
	
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) 
	{
		if(deviceInterrogatedForServices != null)
		{
			services.put(deviceInterrogatedForServices.address(), servRecord);
			deviceInterrogatedForServices.setServices(servRecord[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
		}
		else
			System.out.println("Attempted to search for services on null device.");
	}

	public void serviceSearchCompleted(int transID, int respCode) 
	{
		// May release lock/semaphore here depending on implementation
		
		addressOfSearchedDevice = null;
		deviceInterrogatedForServices = null;
		
		System.out.println("Services discovery completed.");
		
	}
}
