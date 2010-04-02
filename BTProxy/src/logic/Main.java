package logic;

import gui.General;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import modal.SessionDetails;

/**
 * Provides proxies to browsers.
 */

public class Main {
	public static final String PROG_NAME = "Bluetooth Internet Adapter";

	public static void main(String[] args) {
		final int PORT_NUM = 3128;
		
		new Thread(new Runnable() {
			public void run() {
				ServerSocket browser;
				try {
					browser = new ServerSocket(PORT_NUM);
				
					SessionDetails.addEvent("Server socket bound to port " + PORT_NUM);

					// Provide a socket for each proxy request from the browser.
					// Firefox, among other modern browsers, establish multiple sockets
					// at a time.
					while (true) {
						Socket browserSession = browser.accept();
						SessionDetails.addEvent("Accepted browser connection");
						OutboundTraffic outboundTraffic = new OutboundTraffic(
							browserSession);
						outboundTraffic.start();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		SessionDetails.addEvent("Program started");
		try {
			General.setupTrayIcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SessionDetails.addEvent("Program Actually Ended");
	}
}