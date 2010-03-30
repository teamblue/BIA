package logic;

import gui.DebugInfo;
import gui.Status;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

/**
 * Provides proxies to browsers.
 */

public class Main {
	private static final String PROG_NAME = "Bluetooth Internet Adapter";
	private static final int EVENTS_SIZE = 100;
	private static String[] events = new String[EVENTS_SIZE];
	private static int numEvents = 0;
	private static long totalBytesIn = 0;
	private static long totalBytesOut = 0;

	// Used to store the cost per KB from the Status window after the user has
	// closed that window
	// This prevents forcing the user to always re-enter the value. Possible
	// alternate ways of doing this (e.g file).
	public static float costPerKB = -1;
	// same as above...
	public static float notifyCost = -1;
	public static int notifyKB = -1;
	public static boolean notifiedCost;
	public static boolean notifiedKB;

	public static void emptyEventsArray() {
		events = new String[EVENTS_SIZE];
	}

	public static void addBytesIn(int num) {
		totalBytesIn += num;
		checkNotify();
	}

	public static void addBytesOut(int num) {
		totalBytesOut += num;
		checkNotify();
	}

	private static void checkNotify() {
		float totalKB = (float) ((totalBytesIn + totalBytesOut) / 1024.0);

		if (notifiedKB == false) {
			if (notifyKB > -1) {
				if (totalKB >= notifyKB) {
					notifiedKB = true;
					addEvent("User notification: bandwidth limit of "
							+ notifyKB + " KB reached");
					displayNotification("Bandwidth limit of " + notifyKB
							+ " KB reached");
				}
			}
		}

		if (notifiedCost == false) {
			if (notifyCost > -1) {
				if ((totalKB * costPerKB) >= notifyCost) {
					notifiedCost = true;
					addEvent("User notification: cost limit of $" + notifyCost
							+ " reached");
					displayNotification("Cost limit of $" + notifyCost
							+ " reached");
				}
			}
		}

	}

	private static void displayNotification(String message) {
		Display display = new Display();
		Shell shell = new Shell(display);

		// SYSTEM_MODAL forces it to the top
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK
				| SWT.SYSTEM_MODAL);
		messageBox.setText(PROG_NAME);
		messageBox.setMessage(message);
		messageBox.open();

	}

	public static void main(String[] args) {
		final int PORT_NUM = 3128;

		addEvent("Program started");

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
			addEvent("Server socket bound to port " + PORT_NUM);

			// Provide a socket for each proxy request from the browser.
			// Firefox, among other modern browsers, establish multiple sockets
			// at a time.
			while (true) {
				Socket browserSession = browser.accept();
				addEvent("Accepted browser connection");
				OutboundTraffic outboundTraffic = new OutboundTraffic(
						browserSession);
				outboundTraffic.start();
			}
		} catch (Exception e) {
		}

	}

	private static void addEventToArray(String text) {
		int lastElement = events.length - 1;

		// array is already full so we need to make some room
		if (numEvents == events.length) {
			// shift all elements to the left (first element will be deleted)
			for (int i = 0; i < lastElement; i++) {
				events[i] = events[i + 1];
			}

			// insert new element at the end;
			events[lastElement] = text;
		} else {
			events[numEvents] = text;
			numEvents++;
		}
	}

	public static void addEvent(String event) {
		String strDate;
		final String text; // must be final for inner class method

		System.out.println(event);

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		strDate = dateFormat.format(new Date());

		text = strDate + "   " + event;

		// lstList is not initialised (i.e. Debug window is closed) so just
		// update the events array
		if (DebugInfo.isListNull()) {
			addEventToArray(text);
		}
		// debug window is open so let's update the debug window's list box and
		// the events array
		else {
			// SWT is not thread safe. This prevents an invalid thread access
			// exception due
			// to attempting to access a widget from a different thread that
			// created it.
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(750);
					} catch (Exception e) {
					}
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							// max list size is already reached so we need to
							// make some room
							DebugInfo.addToList(text, events.length);
							addEventToArray(text);
						}
					});
				}
			}).start();
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
					new Status(totalBytesIn, totalBytesOut);
				}
			});

			// menu's debug item
			debugItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					new DebugInfo(events);
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
					new Status(totalBytesIn, totalBytesOut);
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