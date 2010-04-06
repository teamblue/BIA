package mobileDesktopHandler;
import javax.microedition.io.*;
import javax.bluetooth.*;

// Wrapper for LocalDevice; adds callback to handle data reception over bluetooth, and defines
// when connections can be allowed by localdevice
public class BluetoothHost
{
	public final static int MAX_BYTES = 100;
	
	private static LocalDevice localDevice;
	
	BluetoothRequest bluetoothRequest;
	
	public BluetoothHost(BluetoothRequest bluetoothRequest) throws BluetoothStateException
	{
		localDevice = LocalDevice.getLocalDevice();
		this.bluetoothRequest = bluetoothRequest;
	}
	
	public void acceptConnections()
	{
		// Code allowing connections to be accepted
		// Create connection, or simply unblock here?
	}
	
	// Since we can't inherit LocalDevice, must 
	// duplicate relevant parts of its interface
	public DiscoveryAgent getDiscoveryAgent()
	{
		return(localDevice.getDiscoveryAgent());
	}
}
