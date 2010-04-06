package mobileDesktopHandler;
import javax.microedition.io.*;
import javax.bluetooth.*;
import javax.obex.*;

import java.io.IOException;
import java.util.Vector;

// @class MobileDesktopHandler 
// Negotiates connection with desktop client
// Receives and parses requests between mobile and desktop platforms
// Currently does all work in one thread for testing; will be split into two
// to implement server functionality (HTTP requests from desktop) and 
// delivery of data to desktop

public class MobileDesktopHandler implements DiscoveryListener
{

	// temporarily create as own object in own class to use discovery agent standalone
	public static MobileDesktopHandler desktopHandler = new MobileDesktopHandler();
	
	// Vector holding devices found nearby on last search
	// no guarantee of OBEX services
	private static Vector foundDevices = new Vector();
	static final String serverUUID = "999999";

	
	// Bluetooth-related objects
	private static LocalDevice 				thisCell = null; 
	private static RemoteDevice				desktopComputer = null;
	private static DiscoveryAgent 			discoveryAgent = null; 
	private static ClientSession 			connectionSession = null;
	private static String 					desktopURL = null;
	
	// OBEX-related objects
	private UUID 					obexUUID = new UUID(8);
	private ServerRequestHandler 	serverRequestHandler 	= null;
	private	SessionNotifier 		serverConnection		= null; 	
	private	DesktopRequestHandler	desktopRequestHandler 	= null;

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
			System.out.println("Local device busy or unavailable; unable to initialize BlueTooth adapter.");
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
			System.out.println("Local device busy or unavailable; unable to search for nearby devices..");
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
	        	System.out.println("Local device busy or unavailable; unable to check services on remote device..");
				System.exit(-1);
	        } 
		}
	
		// check device for OBEX service	
	}

	
	//********************
	// BlueTooth interface methods
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
		if(desktopURL != null)
		{
			try 
			{
				serverConnection = (SessionNotifier)Connector.open("btgoep://localhost:" + serverUUID + ";name=ObexServer");
				desktopRequestHandler = new DesktopRequestHandler();
				serverConnection.acceptAndOpen(desktopRequestHandler);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Unable to set desktopRequestHandler to listen for OBEX requests.");
				e.printStackTrace();
			}
		}
	}

	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) 
	{
		// TODO Auto-generated method stub
		if(servRecord!=null && servRecord.length > 0)
            desktopURL = servRecord[0].getConnectionURL(0,false);
	}
}



