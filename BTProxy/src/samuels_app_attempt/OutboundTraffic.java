package samuels_app_attempt;

import java.io.InputStream;
import java.net.Socket;
import java.util.Vector;
import app.HTTPObject;

/**
 * Thread that sends outbound traffic from the browser to the net.
 * 
 * Inbound and outbound traffic are handled in separate threads as a packet in
 * one direction may elicit a different number of packets in the other direction.
 * Also, networks do not guarantee packet order.
 * 
 * @author Samuel Pauls
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
			
			byte[] buffer = new byte[1024];
			while (true) {
				if (is.available() > 0) {
					// Get the request from the browser.
					int bufferLength = is.read(buffer);
					String message = new String(buffer);
					String host = new HTTPObject(message).getHost();
					
					// Look for an existing socket to the destination
					// server THAT IS OWNED BY THIS BROWSER SOCKET.
					// If we share a socket to a destination server with
					// multiple browser sockets, one browser socket's request
					// may affect what this destination server socket returns
					// to another unrelated browser socket!
					Socket existingServerSession = null;
					for(Socket serverSession : serverSessions) {
						if (serverSession.getInetAddress().getHostName().equals(host)) {
							existingServerSession = serverSession;
							break;
						}
					}
					
					if (existingServerSession == null) {
						// Establish a new socket to the destination server.
						System.err.println("New Host: " + host);
						existingServerSession = new Socket(host, 80);
						serverSessions.add(existingServerSession);
						
						// Listen to what the new server socket has to say
						// in reply to what we'll soon send it.
						InboundTraffic inboundTraffic = new InboundTraffic(
								browserSession,
								existingServerSession.getInputStream());
						inboundTraffic.start();
					} else {
						System.err.println("Old Host: " + host);
					}
					
					// Send outbound traffic to the destination socket.
					existingServerSession.getOutputStream().
							write(buffer, 0, bufferLength);
				} else {
					yield();
				}
			}
		} catch(Exception e) {
			System.out.println ("Outbound: " + e);
		}
	}
}