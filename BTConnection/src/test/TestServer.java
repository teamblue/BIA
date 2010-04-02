package test;

// To test bluetooth:
// Expand 'Referenced Libraries'
// Expand 'bluecove-emu-2.1.0.jar'
// Expand 'com.intel.bluetooth.emu'
// Run 'EmuServer.class' as a Java Application
//
// Once configured, run TestServer.java as a Java Application
// Once configured, run TestClient.java as a Java Application

// To run the server:
// Go to 'Run As' > 'Run Configurations...'
// Setup a new Java Application and under 'Arguments' add '-Dbluecove.stack=emulator' to 'VM arguments'

public class TestServer 
{
	// The starting point
	public static void main(String arg[]) throws Exception 
	{
		btserver.BluetoothHost btHost = new btserver.BluetoothHost( new Request() );
		btHost.acceptConnections();
	}
}
