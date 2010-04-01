package logic;

import gui.DebugInfo;
import gui.General;
import gui.Status;
import java.net.ServerSocket;
import java.net.Socket;
import modal.SessionDetails;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

/**
 * Provides proxies to browsers.
 */

public class Main {
	public static final String PROG_NAME = "Bluetooth Internet Adapter";
	private static SessionDetails sessionDetails = new SessionDetails();

	public static void main(String[] args) {
		final int PORT_NUM = 3128;

		sessionDetails.addEvent("Program started");

		new Thread(new Runnable() {
			public void run() {
				try {
					General.setupTrayIcon();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		try {
			ServerSocket browser = new ServerSocket(PORT_NUM);
			sessionDetails.addEvent("Server socket bound to port " + PORT_NUM);

			// Provide a socket for each proxy request from the browser.
			// Firefox, among other modern browsers, establish multiple sockets
			// at a time.
			while (true) {
				Socket browserSession = browser.accept();
				sessionDetails.addEvent("Accepted browser connection");
				OutboundTraffic outboundTraffic = new OutboundTraffic(
						browserSession);
				outboundTraffic.start();
			}
		} catch (Exception e) {
		}

	}
}