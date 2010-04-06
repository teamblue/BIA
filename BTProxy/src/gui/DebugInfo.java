package gui;

import modal.SessionDetails;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class DebugInfo {

	private Shell shell;
	private Display display;
	private Composite cmpMain;

	private Button btnClear;
	private Label lblList;
	private static List lstList;

	private String[] events = null;

	private static boolean isRunning;
	
	/* not a singleton because we may want to destroy and then recreate
	 * multiple instances of this class during the same program execution */
	public static void run() {
		if (!isRunning)
			new DebugInfo();
	}	
	
	//Constructor
	private DebugInfo() {
		isRunning = true;
		String[] events = SessionDetails.getEvents();
		display = Display.getDefault();
		shell = new Shell(display);

		this.events = events;

		createWindow();
		runWindow();
	}

	public static void addToList(String text, int maxCount) {

		if (lstList.getItemCount() == maxCount)
			lstList.remove(0);

		lstList.add(text);
	}

	public static boolean isListNull() {
		return lstList == null;
	}

	private void runWindow() {
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	//Create the GUI
	private void createWindow() {
		Image icon; // program's icon

		shell = new Shell(SWT.CLOSE);
		shell.setLayout(new GridLayout());
		shell.setText("Debug Info");

		createCmpMain(); // create main composite
		addListeners();

		// set program icon
		try {
			icon = new Image(null, "icon.png");
			shell.setImage(icon);
		}
		// Image could not be read (e.g. not found, etc.)
		catch (org.eclipse.swt.SWTException e) {
			System.out.println(e.getMessage());
		}

		shell.pack();
		centreWindow();

		// forces shell to foreground
		shell.setVisible(true);
		shell.forceActive();
	}

	// Centre the window on the screen
	private void centreWindow() {
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds(); // get screen's dimensions
		// (resolution)
		Rectangle rect = shell.getBounds(); // get this window's dimensions

		// calculate the centre
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		shell.setLocation(x, y);
	}

	//Create's main composite
	private void createCmpMain() {
		final int numCols = 3;

		GridLayout grlMain = new GridLayout();
		grlMain.numColumns = numCols;

		cmpMain = new Composite(shell, SWT.NONE);
		cmpMain.setLayout(grlMain);

		// lblList start
		lblList = new Label(cmpMain, SWT.NONE);
		lblList.setText("Last 100 Events:");
		// lblList end

		// lstList start
		lstList = new List(cmpMain, SWT.BORDER | SWT.V_SCROLL);
		GridData gdList = new GridData();
		gdList.horizontalSpan = numCols;
		gdList.heightHint = 10 * lstList.getItemHeight(); // 10 rows
		gdList.widthHint = 400;

		if (events != null) {
			for (int i = 0; i < events.length; i++) {
				if (events[i] != null)
					lstList.add(events[i]);
			}
		}

		lstList.setLayoutData(gdList);
		// lstList end

		// btnClear start
		btnClear = new Button(cmpMain, SWT.NONE);
		btnClear.setText("Clear");
		// btnClear end

	}

	private void addListeners() {
		// 'X' button listener
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {
				e.doit = false;
				doExit();
			}
		});

		// Clear button listener
		btnClear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent arg0) {
				lstList.removeAll();
				SessionDetails.emptyEventsArray();
			}
		});
	}

	//Close this window/class
	private void doExit() {
		lstList = null; // important
		shell.dispose();
		isRunning = false;
	}
}
