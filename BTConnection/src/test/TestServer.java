package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * Sample server to demonstrate the use of BlueSim JSR-82 Simulator.
 * 
 * This sample program creates an SPP service with the UUID
 * '10203040607040A1B1C1DE100' and waits for a client to connect to it.
 * 
 * The client part can be seen at the TestClient.java sample.
 * 
 * Please note that real device cannot see this service, and cannot connect to
 * it, as this is just a simulated virtual service. But client programs using
 * the BlueSim can see and connect to this service, even if the client is
 * running on a different machine, provided there exist an internal network
 * between the server and client machine.
 * 
 * @copyright JSRSoft
 */
public class TestServer {

	private LocalDevice localDevice;
	
	private static final String uuid = "E6FEC3B275744C079B2F8883DBE38937";

	// The startint point
	public static void main(String arg[]) throws Exception {
		//TestServer test = new TestServer();
		//test.startServer();
		// call this to end the simulator
		//BCC.closeSimulator();
		
		bluetooth.BluetoothHost btHost = new bluetooth.BluetoothHost();
		btHost.acceptConnections();
	}

	public TestServer() throws Exception {
		localDevice = LocalDevice.getLocalDevice();
		localDevice.setDiscoverable(DiscoveryAgent.GIAC);
		System.out.println("Starting Server on "
				+ localDevice.getBluetoothAddress() + " [ "
				+ localDevice.getFriendlyName() + " ]");
	}

	/**
	 * Starts the sample spp service, and waits for a client connection.
	 */
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
