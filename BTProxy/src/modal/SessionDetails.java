package modal;

import gui.DebugInfo;
import gui.General;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Display;

public class SessionDetails {
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
	
	
	public static String[] getEvents() {
		return events;
	}
	
	public static long getTotalBytesIn() {
		return totalBytesIn;
	}
	
	public static long getTotalBytesOut() {
		return totalBytesOut;
	}
	
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
					General.displayNotification("Bandwidth limit of " + notifyKB
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
					General.displayNotification("Cost limit of $" + notifyCost
							+ " reached");
				}
			}
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
	
	
	//Update cost and notify the program that the cost was changed. Called from Status class.
	public static void updateNotifyCost(String notifyCost) {

		try {
			SessionDetails.notifyCost = (float) Float.parseFloat(notifyCost);
			SessionDetails.notifiedCost = false;
		} catch (Exception ex) {
			SessionDetails.notifyCost = -1;
		}

	}

	//Update KB and notify the program that the cost was changed. Called from Status class.
	public static void updateNotifyKB(String notifyKB) {

		try {
			SessionDetails.notifyKB = Integer.parseInt(notifyKB);
			SessionDetails.notifiedKB = false;
		} catch (Exception ex) {
			SessionDetails.notifyKB = -1;
		}

	}	
	
}