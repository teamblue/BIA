package logic;

import java.io.InputStream;
import java.net.Socket;
import java.util.Vector;
import javax.swing.JOptionPane;

import btclient.SimpleBluetoothClient;
import logic.HTTPHeader;
import modal.SessionDetails;

/**
 * Thread that sends outbound traffic from the browser to the net.
 * 
 * Inbound and outbound traffic are handled in separate threads as a packet in
 * one direction may elicit a different number of packets in the other
 * direction. Also, networks do not guarantee packet order.
 */
public class OutboundTraffic extends Thread {
	private Socket browserSession;
	private Vector<Socket> serverSessions = new Vector<Socket>();
    
	public OutboundTraffic(Socket browserSession) {
		this.browserSession = browserSession;
	}

	public void run() {
		try {
			InputStream is = browserSession.getInputStream();

			final int MAX_BUFFER_LEN = 1024;
			byte[] buffer = new byte[MAX_BUFFER_LEN];
			int bufferLength;
			final int CONNECTION_CLOSED = -1;
			// is.read waits until there's something to read or the connection
			// is closed. Other methods of checking if a connection is closed
			// only work once you attempt to read data from the closed
			// connection.
			while ((bufferLength = is.read(buffer)) != CONNECTION_CLOSED) {
				String message = new String(buffer);
				
				// Read in more data if the HTTP header isn't complete.
				final int NOT_FOUND = -1;
				while (message.indexOf("\r\n") == NOT_FOUND) {
					// Wait for more buffer to be read in.
					int r = is.read(buffer, bufferLength,
							MAX_BUFFER_LEN - bufferLength);
					
					// Exit the loop if the connection was closed.
					if (r == -1) {
						break;
					}
					
					// Update the buffer and its length.
					bufferLength += r;
					message = new String(buffer);
				}
				
				//String host = new HTTPHeader(message).getHost();

				// Look for an existing socket to the destination
				// server THAT IS OWNED BY THIS BROWSER SOCKET.
				// If we share a socket to a destination server with
				// multiple browser sockets, one browser socket's request
				// may affect what this destination server socket returns
				// to another unrelated browser socket!
				/*Socket existingServerSession = null;
				for (Socket serverSession : serverSessions) {
					if (serverSession.getInetAddress().getHostName().equals(
							host)) {
						existingServerSession = serverSession;
						break;
					}
				}*/

				// TODO: This block doesn't seem to get used, even though I
				// thought it would. Do we really not need to check if a
				// socket, which can establish multiple outgoing connections
				// to different servers, has the necessary input stream to
				// receive data back from a given server? Wouldn't problems
				// be caused if that input stream that returns information to
				// us is closed when the socket needs to send data to that
				// server again?
				/*if (existingServerSession != null
						&& existingServerSession.isClosed()) {
					JOptionPane.showMessageDialog(null,
							"Found closed connection!");
					serverSessions.remove(existingServerSession);
					existingServerSession = null;
				}

				if (existingServerSession == null) {
					// Establish a new socket to the destination server.
					SessionDetails.addEvent("New Host: " + host);
					existingServerSession = new Socket(host, 80);
					serverSessions.add(existingServerSession);

					// Listen to what the new server socket has to say
					// in reply to what we'll soon send it.
					//InboundTraffic inboundTraffic = new InboundTraffic(
						//	browserSession, existingServerSession
							//		.getInputStream());
					//inboundTraffic.start();
				} else {
					SessionDetails.addEvent("Old Host: " + host);
				}
*/
				// Send outbound traffic to the destination socket.
				
				//existingServerSession.getOutputStream().write(buffer, 0,
					//	bufferLength);
				
				
				//SimpleBluetoothClient client = new SimpleBluetoothClient();
				System.out.println("omg");
				SimpleBluetoothClient client = new SimpleBluetoothClient();
				//browserSession.getOutputStream().write(
				String a = client.write(new String(buffer));
				System.out.println("start");
				System.out.println(a);
				System.out.println("end");
				
				browserSession.getOutputStream().write(a.getBytes());;
				// browserSession.getOutputStream().write(simpleBTClient.write(buffer));
				SessionDetails.addBytesOut(bufferLength);
			}

			SessionDetails.addEvent("Outbound connection closed");

		} catch (Exception e) {
			SessionDetails.addEvent("Outbound Exception: " + e);
		}
	}
}