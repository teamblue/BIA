package gui;

import javax.swing.JOptionPane;

import logic.Main;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
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

public class General {
	public static void displayNotification(String message) {
		try {
			// Try displaying the message with SWT, which uses the native look
			// and feel of the OS. 
			
			Display display = new Display();
			Shell shell = new Shell(display);
	
			// SYSTEM_MODAL forces it to the top
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK
					| SWT.SYSTEM_MODAL);
			messageBox.setText(Main.PROG_NAME);
			messageBox.setMessage(message);
			messageBox.open();
		} catch (SWTError e) {
			// In Linux, SWT doesn't implement MessageBox, so use JOptionPane.
			JOptionPane.showMessageDialog(null, message, Main.PROG_NAME,
					JOptionPane.WARNING_MESSAGE);
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
			trayItem.setToolTipText(Main.PROG_NAME);

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
					new Status();
				}
			});

			// menu's debug item
			debugItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					new DebugInfo();
				}
			});

			// menu's exit item
			exitItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					image.dispose();
					display.dispose();
					shell.dispose();
					System.out.println("Program Ended");/* This and the System.exit should only happen from the main*/
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
					new Status();
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