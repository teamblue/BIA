package logic;

import gui.DebugInfo;
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
					setupTrayIcon();
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

	public static void setupTrayIcon() throws Exception {
		final String ICON_FILENAME = "icon.png";
		final Display display = new Display();
		final Shell shell = new Shell(display);
		final Image image = new Image(display, ICON_FILENAME);
		final Tray tray = display.getSystemTray();

		if (tray != null) {
			final TrayItem trayItem = new TrayItem(tray, SWT.NONE);
			final Menu menu = new Menu(shell, SWT.POP_UP);
			trayItem.setToolTipText(PROG_NAME);

			MenuItem statusItem = new MenuItem(menu, SWT.NONE);
			statusItem.setText("Status");
			MenuItem debugItem = new MenuItem(menu, SWT.NONE);
			debugItem.setText("Debug");
			MenuItem lineItem = new MenuItem(menu, SWT.SEPARATOR);
			lineItem.setText(""); // get rid of eclipse warning,
									// "local variable is never read"
			MenuItem exitItem = new MenuItem(menu, SWT.NONE);
			exitItem.setText("Exit");

			// menu's status item
			statusItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					new Status(sessionDetails);
				}
			});

			// menu's debug item
			debugItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					new DebugInfo(sessionDetails);
				}
			});

			// menu's exit item
			exitItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					image.dispose();
					display.dispose();
					shell.dispose();
					System.exit(0);
				}
			});

			// icon right-click
			trayItem.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});

			// icon double-click
			trayItem.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(Event event) {
					new Status(sessionDetails);
				}
			});

			trayItem.setImage(image);
		}

		// this while loop is required for SWT and is why we need a separate
		// thread
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		image.dispose();
		display.dispose();
		shell.dispose();
		System.exit(0);
	}
}