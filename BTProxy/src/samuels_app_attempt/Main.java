package samuels_app_attempt;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
			setupTrayIcon();
			
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
	
	public static void setupTrayIcon() throws Exception {
		// Setup a system tray icon.
		if (SystemTray.isSupported()) {
			// Get the icon graphic.
			final String ICON_FILENAME = "icon.png";
			Image image = Toolkit.getDefaultToolkit().getImage (ICON_FILENAME);
			
			// Create a popup menu.
			PopupMenu menu = new PopupMenu();
			MenuItem exitItem = new MenuItem ("Exit");
			ActionListener exitListener = new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					System.exit (0);
				}
			};
			exitItem.addActionListener (exitListener);
			menu.add (exitItem);
			
			// Create the tray icon.
			TrayIcon trayIcon = new TrayIcon (image, "Bluetooth Internet Adapter", menu);
			trayIcon.setImageAutoSize (true);
			
			SystemTray.getSystemTray().add (trayIcon);
		} else {
			System.err.println ("System tray not supported!");
			System.err.println ("Java runtime environments such as OpenJDK " +
					            "don't support system tray icons.  If you " +
					            "are using an alternative, try the offical " +
					            "Sun JRE.");
			throw new Exception();
		}
	}
}