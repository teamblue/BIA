package samuels_app_attempt;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Thread that accepts inbound traffic and directs it to the browser.
 * 
 * Inbound and outbound traffic are handled in separate threads as a packet in
 * one direction may elicit a different number of packets in the other direction.
 * Also, networks do not guarantee packet order.
 * 
 * @author Samuel Pauls
 */
public class InboundTraffic extends Thread {
	private Socket browserSession;
	private InputStream serverIS;
	private boolean isClosed = false;
	
	public InboundTraffic(Socket browserSession, InputStream serverIS) {
		this.browserSession = browserSession;
		this.serverIS = serverIS;
	}
	
	public boolean isClosed() {
		return isClosed;
	}
	
	public void run() {
		try {
			OutputStream browserOS = browserSession.getOutputStream();
			
			byte[] buffer = new byte[1024];
			int bufferLength;
			final int CONNECTION_CLOSED = -1;
			// serverIS.read waits until there's something to read or the
			// connection is closed.  Other methods of checking if a connection
			// is closed only work once you attempt to read data from the closed
			// connection.
			while((bufferLength = serverIS.read(buffer)) != CONNECTION_CLOSED) {
				browserOS.write(buffer, 0, bufferLength);
			}

			isClosed = true;
			System.out.println ("Inbound connection closed!");
		} catch(Exception e) {
			System.out.println ("Inbound: " + e);
		}
	}
}
