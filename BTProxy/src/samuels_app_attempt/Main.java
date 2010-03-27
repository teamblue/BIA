package samuels_app_attempt;


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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;


/**
* Provides proxies to browsers.
* 
* @author Samuel Pauls
*/

public class Main {
	private static final int EVENTS_SIZE = 100;
	
	private static String[] events = new String[EVENTS_SIZE];
	private static int numEvents = 0;
	private static long totalBytesIn = 0;
	private static long totalBytesOut = 0;
	
	//Used to store the cost per KB from the Status window after the user has closed that window
	//This prevents forcing the user to always re-enter the value. Possible alternate ways of doing this (e.g file).
	public static float costPerKB = -1;
	
	public static void emptyEventsArray()
	{
		events = new String[EVENTS_SIZE];
	}
	
	public static void addBytesIn(int num)
	{
		totalBytesIn += num;
	}
	
	public static void addBytesOut(int num)
	{
		totalBytesOut += num;
	}
	
	public static void main(String[] args) {
		
		   new Thread(new Runnable() {
			      public void run() {
			  		try {
						setupTrayIcon();
					} catch (Exception e) {
						e.printStackTrace();
					}
			      }
		   }).start();

		addEvent("Program started");
		
		try {
			ServerSocket browser = new ServerSocket(3128);
			
			// Provide a socket for each proxy request from the browser.
			// Firefox, among other modern browsers, establish multiple sockets
			// at a time.
			while (true) {
				Socket browserSession = browser.accept();
				System.err.println("accepted browser connection");
				addEvent("Accepted browser connection");
				OutboundTraffic outboundTraffic = new OutboundTraffic(browserSession);
				outboundTraffic.start();
			}
		} catch(Exception e) {}
		
	}
	
	private static void addEventToArray(String text) {
		int lastElement;
		
		lastElement = events.length - 1;
        
        //array is already full so we need to make some room
		if (numEvents == events.length)
		{
			//shift all elements to the left (first element will be deleted)
			for (int i=0; i<lastElement; i++) {
				events[i] = events[i+1];
			}
			
			//insert new element at the end;
			events[lastElement] = text;
		}
		else
		{
			events[numEvents] = text;
			numEvents++;
		}
	}
	
	public static void addEvent(String event) {
		String strDate;
		final String text; //must be final for inner class method
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        strDate = dateFormat.format(new Date());
		
        text = strDate + "   " + event;

        //lstList is not initialised (i.e. Debug window is closed) so just update the events array
        if (DebugInfo.isListNull()) {
        	addEventToArray(text);
        }
        //debug window is open so let's update the debug window's list box and the events array
        else {
			//Java's GUI frameworks are not thread safe. This prevents an invalid thread access exception due
        	//to attempting to access a widget from a different thread that created it.
        	new Thread(new Runnable() {
				public void run() {
					try { Thread.sleep(750); } catch (Exception e) { }
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								//max list size is already reached so we need to make some room
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
		final String ICON_TEXT = "Bluetooth Internet Adapter";
		
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		final Image image = new Image (display, ICON_FILENAME);
		
		final Tray tray = display.getSystemTray ();
		if (tray != null) {
			final TrayItem item = new TrayItem (tray, SWT.NONE);
			item.setToolTipText(ICON_TEXT);

			
			final Menu menu = new Menu (shell, SWT.POP_UP);
			
			MenuItem statusItem = new MenuItem(menu, SWT.NONE);
			statusItem.setText("Status");
			MenuItem debugItem = new MenuItem(menu, SWT.NONE);
			debugItem.setText("Debug");
			MenuItem lineItem = new MenuItem(menu, SWT.SEPARATOR);
			lineItem.setText(""); //get rid of eclipse warning, "local variable is never read"	
			MenuItem exitItem = new MenuItem(menu, SWT.NONE);				
			exitItem.setText("Exit");
			
			statusItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					new Status(totalBytesIn, totalBytesOut);
				}
			});
			
			debugItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					new DebugInfo(events);
				}
			});				
			
			exitItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						image.dispose();
						display.dispose();
						shell.dispose();
						System.exit(0);
				}
			});			
			
			item.addListener (SWT.MenuDetect, new Listener () {
				public void handleEvent (Event event) {
					menu.setVisible (true);
				}
			});
			item.setImage (image);
		}

		//this while loop is why we need a separate thread
		//(with an AWT tray icon, we would still need another thread to make the menu pop-up when another shell/window is open)
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		
		image.dispose();
		display.dispose();
		shell.dispose();
		System.exit(0);
	}
}