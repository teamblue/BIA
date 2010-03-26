package biamobile;

import javax.microedition.io.*;
import javax.bluetooth.*;
import javax.obex.*;

import java.util.Vector;

// @class MobileDesktopHandler 
// Negotiates connection with desktop client
// Receives and parses requests between mobile and desktop platforms

public class MobileDesktopHandler implements DiscoveryListener{

	// temporarily create as own object in own class to use discovery agent standalone
	public static MobileDesktopHandler desktopHandler = new MobileDesktopHandler();
	
	// Vector holding devices found nearby on last search
	// no guarantee of OBEX services
	private static Vector foundDevices = new Vector();
	
	private static LocalDevice 		thisCell = null; 
	private static RemoteDevice		desktopComputer = null;
	private static DiscoveryAgent 	discoveryAgent = null; 
	private static ClientSession 	connectionSession = null;
	
	private UUID obexUUID = new UUID(8);
	
	private static String desktopURL = null;

	public static void main(String args[])
	{
		// initialize local device and discovery agent
		
		System.out.println("About to initialize local device...");
		
		try
		{
			thisCell = LocalDevice.getLocalDevice();
			discoveryAgent = thisCell.getDiscoveryAgent();
		}
		catch(BluetoothStateException exception)
		{
			// getLocalDevice/getDiscoveryAgent failed
			// thrown when device is otherwise being used
			// temporarily exit
			System.out.println("Local device busy; unable to initialize BlueTooth adapter.");
			System.exit(-1);
		}
		// search for other bluetooth devices
		
		try
		{
			System.out.println("About to search for nearby devices..");
			discoveryAgent.startInquiry(DiscoveryAgent.GIAC, desktopHandler);
		}
		catch(BluetoothStateException exception)
		{
			System.out.println("Local device busy; unable to search for nearby devices..");
			System.exit(-1);
		}		

		// check for OBEX service on nearby device(s)
		// currently assuming desktop application is only BlueTooth device in range
		// for testing purposes
        
		if(foundDevices.size() > 0)
		{
			desktopComputer = (RemoteDevice)foundDevices.elementAt(0);
	        UUID[] uuidSet = new UUID[1];
	        uuidSet[0]=new UUID("1105",true);
            
	        try
	        {
	        	System.out.println("\nSearching for service...");
	        	discoveryAgent.searchServices(null, uuidSet, desktopComputer, desktopHandler);
	        }
	        catch(BluetoothStateException exception)
	        {
	        	System.out.println("Local device busy; unable to check services on remote device..");
				System.exit(-1);
	        } 
		}
	
		// check device for OBEX service	
	}

	public void deviceDiscovered(RemoteDevice foundDevice, DeviceClass deviceClass) 
	{
		// TODO Auto-generated method stub
		foundDevices.addElement(foundDevice);
	}

	public void inquiryCompleted(int discType) {
		// TODO Auto-generated method stub
		

	}

	public void serviceSearchCompleted(int transID, int respCode) 
	{
		// TODO Auto-generated method stub
		
	}

	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) 
	{
		// TODO Auto-generated method stub
		if(servRecord!=null && servRecord.length>0)
            desktopURL = servRecord[0].getConnectionURL(0,false);
	}
	
	/**
	 * Sends the passed byte array as a request.  Blocks until we get a response, returning it as a byte array.
	 * @param request  The request to send.
	 * @return  A byte array containing the response.
	 */
	public byte[] sendRequest(byte[] request)
	{
		// extract remote host from headers in request
		String remoteHost = extractRemoteHost(request);
		byte[] response = null;
		
		
		
		
		return response;
	}
	
	private String extractRemoteHost(byte[] request)
	{
		String remoteHost = null;
		
		// gets first line
		StringBuffer firstLine = new StringBuffer();
		for (int i = 0; i < request.length && request[i] != '\n'; i++)
		{
			firstLine.append(request[i]);
		}
		
		
		
		return remoteHost;
	}
	
}
