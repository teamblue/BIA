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
		bluetooth.BluetoothHost btHost = new bluetooth.BluetoothHost( new Request() );
		btHost.acceptConnections();
	}
}
/*
	public TestServer() throws Exception {
		localDevice = LocalDevice.getLocalDevice();
		localDevice.setDiscoverable(DiscoveryAgent.GIAC);
		System.out.println("Starting Server on "
				+ localDevice.getBluetoothAddress() + " [ "
				+ localDevice.getFriendlyName() + " ]");
	}

	/**
	 * Starts the sample spp service, and waits for a client connection.
	 
	public void startServer() {

		HashMap<String,String> sites = new HashMap<String, String>();
		
		sites.put("google.com", "This is google!");
		sites.put("engadget.com", "Tech news!");
		sites.put("umanitoba.ca", "Education!");
		
		// service url
		String serviceURL = "btspp://localhost:" + uuid + ";name=tether";

		try {
			
			// create a server connection
			StreamConnectionNotifier notifier = (StreamConnectionNotifier) Connector
					.open(serviceURL);

			// accept client connections
			System.out.println("Waiting for client connection");
			StreamConnection connection = notifier.acceptAndOpen();
			System.out.println("Client connected");

			// prepare to send/receive data
			byte buffer[] = new byte[100];
			
			InputStream is = connection.openInputStream();
			int readBytes = is.read(buffer);
			
			String result = sites.get(new String(buffer, 0, readBytes));
			if(result == null){
				result = "Not found!";
			}
			
			OutputStream os = connection.openOutputStream();
			os.write(result.getBytes());
			os.flush();

			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
*/