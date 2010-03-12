package samuels_app_attempt;

import java.net.ServerSocket;
import java.net.Socket;

/**
* Provides proxies to browsers.
* 
* @author Samuel Pauls
*/

public class Main {
	public static void main(String[] args) {
		try {
			ServerSocket browser = new ServerSocket(3128);
			
			// Provide a socket for each proxy request from the browser.
			// Firefox, among other modern browsers, establish multiple sockets
			// at a time.
			while (true) {
				Socket browserSession = browser.accept();
				System.err.println("accepted browser connection");
				
				OutboundTraffic outboundTraffic = new OutboundTraffic(browserSession);
				outboundTraffic.start();
			}
		} catch(Exception e) {
		}
	}
}