package bluetooth;

import java.util.Collection;
import java.util.HashMap;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

public class BluetoothDevices implements DiscoveryListener
{
	private HashMap<String, BluetoothDevice> devices = new HashMap<String, BluetoothDevice>();
	private HashMap<String,String> services = new HashMap<String, String>();
	private static Object lock = new Object();
	
	private DiscoveryAgent dAgent;
	
	public BluetoothDevices() throws BluetoothStateException
	{
		dAgent = LocalDevice.getLocalDevice().getDiscoveryAgent();
	}
	
	public HashMap<String, BluetoothDevice> getDevices()
	{
		return devices;
	}
	
	public Collection<BluetoothDevice> getDevicesCollection()
	{
		return devices.values();
	}
	
	public HashMap<String, String> getServices()
	{
		return services;
	}
	
	public void discoverDevicesAndServices( String uuid ) throws BluetoothStateException
	{
		discoverDevices();
		discoverServices( uuid );
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
	
	public void discoverServices( String serviceUUID ) throws BluetoothStateException
	{
		services.clear();
		
		UUID[] uuidSet = new UUID[1];
		uuidSet[0] = new UUID(serviceUUID, false);
		
		Collection<BluetoothDevice> devices = this.devices.values();
		
		for ( BluetoothDevice device : devices )
		{
			dAgent.searchServices( null, uuidSet, device.getRemoteDevice(), this );
			
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

	public void deviceDiscovered(RemoteDevice device, DeviceClass devClass) 
	{
		BluetoothDevice btDevice = new BluetoothDevice( device, devClass );
		
		System.out.println( "Device discovered." );
		
		if ( !devices.containsKey( btDevice ) ){
			devices.put(device.getBluetoothAddress(), btDevice);
		}
	}

	public void inquiryCompleted( int arg0 ) 
	{
        synchronized ( lock )
        {
            lock.notify();
        }
	}

	public void serviceSearchCompleted( int arg0, int arg1 )
	{
        synchronized ( lock )
        {
            lock.notify();
        }
	}

	public void servicesDiscovered( int transID, ServiceRecord[] servRecord ) 
	{	
        if ( servRecord != null && servRecord.length > 0 )
        {
        	for( int i = 0; i < servRecord.length; i++ )
        	{
        		System.out.println("Service discovered at " +  servRecord[i].getConnectionURL(0, false));
        		String btAddr = servRecord[i].getHostDevice().getBluetoothAddress();
        		String servs = servRecord[i].getConnectionURL(0, false);
        		
        		// Add the service to the HashMap
        		services.put(btAddr, servs);
        		// Update the BluetoothDevice with its service
        		(devices.get( btAddr )).setServices( servs );
        	}
        }
        synchronized ( lock )
        {
            lock.notify();
        }
	}
}

/*
 * DISCOVERER.JAVA

package bluetooth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

public class Discoverer implements DiscoveryListener {
	
	private HashMap<RemoteDevice, String> devices = new HashMap<RemoteDevice, String>();
	private HashMap<String,String> services = new HashMap<String, String>();
	private static Object lock = new Object();
	
	private DiscoveryAgent dAgent;
	
	public Discoverer(DiscoveryAgent dAgent){
		this.dAgent = dAgent;
	}
	
	public void discoverDevices() throws BluetoothStateException{
		devices.clear();
		
		dAgent.startInquiry(DiscoveryAgent.GIAC, this);
		
		try {
	        synchronized(lock){
	            lock.wait();
	        }
	    }
	    catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
	
	public void discoverServices(Set<RemoteDevice> devices, String serviceUUID) throws BluetoothStateException{
		services.clear();
		
		UUID[] uuidSet = new UUID[1];
		uuidSet[0] = new UUID(serviceUUID, false);
		
		for(RemoteDevice device : devices){
			dAgent.searchServices(null, uuidSet, device, this);
			
			try {
		        synchronized(lock){
		            lock.wait();
		        }
		    }
		    catch (InterruptedException e) {
		        e.printStackTrace();
		    }			
		}
	}
	
	public HashMap<RemoteDevice, String> getDevices() {
		return devices;
	}
	
	public HashMap<String, String> getServices(){
		return services;
	}
	
//	@Override
	public void deviceDiscovered(RemoteDevice device, DeviceClass deviceClass) {
		String name;
		try{
			name = device.getFriendlyName(false);
		}
		catch(IOException e){
			name = "[Unknown]";
		}
		
		System.out.println("Device discovered: " + name + " at " + device.getBluetoothAddress());
		
		if(!devices.containsKey(device)){
			devices.put(device, name);
		}
	}

//	@Override
	public void inquiryCompleted(int discType) {

        synchronized(lock){
            lock.notify();
        }
	}

//	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
        synchronized(lock){
            lock.notify();
        }
	}

//	@Override
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        if(servRecord!=null && servRecord.length>0){
        	for(int i=0; i<servRecord.length; i++){
        		System.out.println("Service discovered at " +  servRecord[i].getConnectionURL(0, false));
        		services.put(servRecord[i].getHostDevice().getBluetoothAddress(), servRecord[i].getConnectionURL(0, false));
        	}
        }
        synchronized(lock){
            lock.notify();
        }
	}

}

*/
