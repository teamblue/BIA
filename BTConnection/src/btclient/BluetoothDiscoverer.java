package btclient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

public class BluetoothDiscoverer implements DiscoveryListener
{
	private static Object lock = new Object();
	
	private HashMap<String, RemoteDevice> devices;
	private ServiceRecord service;
	private DiscoveryAgent dAgent;
	
	private boolean serviceFound;
	
	public BluetoothDiscoverer() throws BluetoothStateException
	{
		serviceFound = false;
		devices = new HashMap<String, RemoteDevice>();
		
		dAgent = LocalDevice.getLocalDevice().getDiscoveryAgent();
	}
	
	/**
	 * Required by DiscoveryListener
	 */
	public void deviceDiscovered(RemoteDevice device, DeviceClass devClass) 
	{
		
		System.out.println( "Device discovered at " + device.getBluetoothAddress());
		
		if ( !devices.containsKey( device ) ){
			devices.put(device.getBluetoothAddress(), device);
		}
	}
	
	/**
	 * Required by DiscoveryListener
	 */
	public void inquiryCompleted( int arg0 ) 
	{
        synchronized ( lock )
        {
            lock.notify();
        }
	}
	
	/**
	 * Required by DiscoveryListener
	 */
	public void servicesDiscovered( int transID, ServiceRecord[] servRecord ) 
	{	
        if ( servRecord != null && servRecord.length > 0 )
        {
        	serviceFound = true;
       		service = servRecord[0];
       		
       		String serviceURL = service.getConnectionURL(0, false);
       		
       		System.out.println("Service discovered at " +  serviceURL);

       	}
        
        synchronized ( lock )
        {
            lock.notify();
        }
	}
	
	/**
	 * Required by DiscoveryListener
	 */
	public void serviceSearchCompleted( int arg0, int arg1 )
	{
        synchronized ( lock )
        {
            lock.notify();
        }
	}	
	
	public ServiceRecord getService(){
		return service;
	}
	
	public void discoverDevices() throws BluetoothStateException
	{
		devices.clear();
		
		dAgent.startInquiry(DiscoveryAgent.GIAC, this);
		
		try 
		{
	        synchronized(lock)
	        {
	            lock.wait();
	        }
	    }
	    catch (InterruptedException e) 
	    {
	        e.printStackTrace();
	    }
	}	
	
	/**
	 * Looks for service with specified serviceUUID from set of previously discovered devices.
	 * @param serviceUUID
	 * @throws BluetoothStateException
	 */
	public void discoverServices( String serviceUUID ) throws BluetoothStateException
	{
		service = null;
		
		UUID[] uuidSet = new UUID[1];
		uuidSet[0] = new UUID(serviceUUID, false);
		
		Iterator<RemoteDevice> deviceIterator = devices.values().iterator();
		
		while ( deviceIterator.hasNext() && !serviceFound)
		{
			dAgent.searchServices( null, uuidSet, deviceIterator.next(), this );
			
			try 
			{
		        synchronized(lock)
		        {
		            lock.wait();
		        }
		    }
		    catch ( InterruptedException e ) 
		    {
		        e.printStackTrace();
		    }
		}

	}	

	private HashMap<String, RemoteDevice> getDevices()
	{
		return devices;
	}

	private Collection<RemoteDevice> getDevicesCollection()
	{
		return devices.values();
	}
}
