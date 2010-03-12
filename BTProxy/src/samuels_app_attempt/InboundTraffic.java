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
	
	public InboundTraffic(Socket browserSession, InputStream serverIS) {
		this.browserSession = browserSession;
		this.serverIS = serverIS;
	}
	
	public void run() {
		try {
			OutputStream browserOS = browserSession.getOutputStream();
			
			byte[] buffer = new byte[1024];
			while(true) {
				if (serverIS.available() > 0) {
					int bufferLength = serverIS.read(buffer);
					browserOS.write(buffer, 0, bufferLength);
				} else {
					yield();
				}
			}
		} catch(Exception e) {
			System.out.println ("Inbound: " + e);
		}
	}
}
