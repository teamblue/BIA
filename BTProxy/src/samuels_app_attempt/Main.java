package samuels_app_attempt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import java.net.ServerSocket;
import java.net.Socket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Display;


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
		try {
			addEvent("Program started");
			
			setupTrayIcon();
			
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
		} catch(Exception e) {
		}
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
        if (DebugInfo.isListNull())
        {
        	addEventToArray(text);
        }
        //debug window is open so let's update the debug window's list box and the events array
        else
		{
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
		// Setup a system tray icon.
		if (SystemTray.isSupported()) {
			// Get the icon graphic.
			final String ICON_FILENAME = "icon.png";
			Image image = Toolkit.getDefaultToolkit().getImage(ICON_FILENAME);
			
			// Create a popup menu.
			PopupMenu menu = new PopupMenu();
			MenuItem statusItem = new MenuItem("Status");
			MenuItem debugItem = new MenuItem("Debug");
			MenuItem lineItem = new MenuItem("-");
			MenuItem exitItem = new MenuItem("Exit");
			
			
			ActionListener statusListener = new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					new Status(totalBytesIn, totalBytesOut);
				}
			};						
			
			ActionListener debugListener = new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					new DebugInfo(events);
				}
			};			
			
			ActionListener exitListener = new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					System.exit(0);
				}
			};
	
			statusItem.addActionListener(statusListener);
			debugItem.addActionListener(debugListener);
			exitItem.addActionListener(exitListener);
			
			menu.add(statusItem);
			menu.add(debugItem);
			menu.add(lineItem);
			menu.add(exitItem);
			
			// Create the tray icon.
			TrayIcon trayIcon = new TrayIcon(image, "Bluetooth Internet Adapter", menu);
			trayIcon.setImageAutoSize (true);
			
			SystemTray.getSystemTray().add(trayIcon);
		} else {
			System.err.println("System tray not supported!");
			System.err.println("Java runtime environments such as OpenJDK " +
					            "don't support system tray icons.  If you " +
					            "are using an alternative, try the offical " +
					            "Sun JRE.");
			throw new Exception();
		}
	}
}