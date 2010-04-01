package gui;

import logic.Main;
import modal.SessionDetails;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import org.eclipse.swt.SWT;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Status {

	private Shell shell;
	private Display display;
	private Composite cmpNotify;
	private Composite cmpBandwidth;
	private Composite cmpCost;
	private Composite cmpMain;

	private Combo cboUnits;

	private Label lblBandwidthTitle;
	private Label lblCostPerKB;
	private Label lblCostTitle;
	private Label lblCostTotalNum;
	private Label lblCostTotalTitle;
	private Label lblDataInNum;
	private Label lblDataInTitle;
	private Label lblDataOutNum;
	private Label lblDataOutTitle;
	private Label lblDataTotalNum;
	private Label lblDataTotalTitle;
	private Label lblUnits;

	private Text txtCostPerKB;

	private long bytesIn = 0;
	private long bytesOut = 0;
	private long bytesTotal;

	private Label lblNotifyTitle;
	private Label lblNotifyDesc;
	private Label lblNotifyCost;
	private Label lblNotifyCost2;
	private Label lblNotifyKB;
	private Label lblNotifyKB2;
	private Label lblNotifyKB3;
	private Text txtNotifyCost;
	private Text txtNotifyKB;

	private final Font SUBTITLE = new Font(display, "Arial", 9, SWT.BOLD);

	public Status() {
		long bytesIn = SessionDetails.getTotalBytesIn();
		long bytesOut = SessionDetails.getTotalBytesOut();
		display = Display.getDefault();
		shell = new Shell(display);

		this.bytesIn = bytesIn;
		this.bytesOut = bytesOut;
		this.bytesTotal = bytesIn + bytesOut;

		createWindow();
		runWindow();
	}

	private void runWindow() {
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void createWindow() {
		Image icon; // program's icon

		shell = new Shell(SWT.CLOSE);
		shell.setLayout(new GridLayout());
		shell.setText("Status");

		createCmpMain();
		createCmpBandwidth();
		createCmpCost();
		createcmpNotify();
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

	private void createCmpMain() {
		final int numCols = 1;

		GridLayout grlMain = new GridLayout();
		grlMain.numColumns = numCols;

		cmpMain = new Composite(shell, SWT.NONE);
		cmpMain.setLayout(grlMain);
	}

	private void createCmpBandwidth() {

		GridLayout grlBandwidth = new GridLayout();
		grlBandwidth.numColumns = 2;

		GridData grdSubTitle = new GridData();
		grdSubTitle.horizontalSpan = 2;
		grdSubTitle.horizontalAlignment = SWT.CENTER;

		GridData grdFill = new GridData();
		grdFill.horizontalAlignment = SWT.FILL;

		cmpBandwidth = new Composite(cmpMain, SWT.BORDER);
		cmpBandwidth.setLayout(grlBandwidth);
		cmpBandwidth.setLayoutData(grdFill);

		lblBandwidthTitle = new Label(cmpBandwidth, SWT.NONE);
		lblBandwidthTitle.setText("Data Usage");
		lblBandwidthTitle.setFont(SUBTITLE);
		lblBandwidthTitle.setLayoutData(grdSubTitle);

		lblUnits = new Label(cmpBandwidth, SWT.NONE);
		lblUnits.setText("Unit:");

		// cboUnits start
		cboUnits = new Combo(cmpBandwidth, SWT.DROP_DOWN | SWT.READ_ONLY);
		cboUnits.add("b");
		cboUnits.add("B");
		cboUnits.add("Kb");
		cboUnits.add("KB");
		cboUnits.add("Mb");
		cboUnits.add("MB");
		cboUnits.setText("B");
		// cboUnits end

		lblDataInTitle = new Label(cmpBandwidth, SWT.NONE);
		lblDataInTitle.setText("Data In:");

		lblDataInNum = new Label(cmpBandwidth, SWT.NONE);
		lblDataInNum.setText("" + bytesIn);

		lblDataOutTitle = new Label(cmpBandwidth, SWT.NONE);
		lblDataOutTitle.setText("Data Out:");

		lblDataOutNum = new Label(cmpBandwidth, SWT.NONE);
		lblDataOutNum.setText("" + bytesOut);

		lblDataTotalTitle = new Label(cmpBandwidth, SWT.NONE);
		lblDataTotalTitle.setText("Data Total:");

		lblDataTotalNum = new Label(cmpBandwidth, SWT.NONE);
		lblDataTotalNum.setText("" + bytesTotal);

	}

	private void createCmpCost() {

		GridLayout grlCost = new GridLayout();
		grlCost.numColumns = 2;

		GridData grdSubTitle = new GridData();
		grdSubTitle.horizontalSpan = 2;
		grdSubTitle.horizontalAlignment = SWT.CENTER;

		GridData grdCost = new GridData();
		grdCost.widthHint = 33;

		GridData grdFill = new GridData();
		grdFill.horizontalAlignment = SWT.FILL;

		cmpCost = new Composite(cmpMain, SWT.BORDER);
		cmpCost.setLayout(grlCost);
		cmpCost.setLayoutData(grdFill);

		lblCostTitle = new Label(cmpCost, SWT.NONE);
		lblCostTitle.setText("Cost Estimate");
		lblCostTitle.setFont(SUBTITLE);
		lblCostTitle.setLayoutData(grdSubTitle);

		lblCostPerKB = new Label(cmpCost, SWT.NONE);
		lblCostPerKB.setText("Cost per KB: $");

		// txtCostPerKB start
		txtCostPerKB = new Text(cmpCost, SWT.NONE);
		txtCostPerKB.setTextLimit(6);
		txtCostPerKB.setLayoutData(grdCost);

		if (SessionDetails.costPerKB > -1)
			txtCostPerKB.setText("" + SessionDetails.costPerKB);
		// txtCostPerKB end

		lblCostTotalTitle = new Label(cmpCost, SWT.NONE);
		lblCostTotalTitle.setText("Usage Cost:  $");

		lblCostTotalNum = new Label(cmpCost, SWT.NONE);

		updateCost();
	}

	private void createcmpNotify() {

		final char GREATER_EQUAL_THAN = 8805;

		GridLayout grlNotify = new GridLayout();
		grlNotify.numColumns = 4;

		GridData grdSubTitle = new GridData();
		grdSubTitle.horizontalSpan = 4;
		grdSubTitle.horizontalAlignment = SWT.CENTER;

		GridData grdSpan2 = new GridData();
		grdSpan2.horizontalSpan = 2;

		GridData grdSpan3 = new GridData();
		grdSpan3.horizontalSpan = 4;

		GridData grdAlert = new GridData();
		grdAlert.widthHint = 33;

		GridData grdAlert2 = new GridData();
		grdAlert2.widthHint = 33;
		grdAlert2.horizontalSpan = 2;

		GridData grdFill = new GridData();
		grdFill.horizontalAlignment = SWT.FILL;

		cmpNotify = new Composite(cmpMain, SWT.BORDER);
		cmpNotify.setLayout(grlNotify);
		cmpNotify.setLayoutData(grdFill);

		lblNotifyTitle = new Label(cmpNotify, SWT.NONE);
		lblNotifyTitle.setText("Notifications");
		lblNotifyTitle.setFont(SUBTITLE);
		lblNotifyTitle.setLayoutData(grdSubTitle);

		lblNotifyDesc = new Label(cmpNotify, SWT.NONE);
		lblNotifyDesc.setText("Notify me when...");
		lblNotifyDesc.setLayoutData(grdSpan3);

		lblNotifyCost = new Label(cmpNotify, SWT.NONE);
		lblNotifyCost.setText("Cost:");

		lblNotifyCost2 = new Label(cmpNotify, SWT.NONE);
		lblNotifyCost2.setText(String.valueOf(GREATER_EQUAL_THAN) + " $");

		// txtNotifyCost start
		txtNotifyCost = new Text(cmpNotify, SWT.NONE);
		txtNotifyCost.setTextLimit(6);
		txtNotifyCost.setLayoutData(grdAlert2);

		if (SessionDetails.notifyCost > -1)
			txtNotifyCost.setText("" + SessionDetails.notifyCost);
		// txtNotifyCost end

		lblNotifyKB = new Label(cmpNotify, SWT.NONE);
		lblNotifyKB.setText("Usage:  ");

		lblNotifyKB2 = new Label(cmpNotify, SWT.NONE);
		lblNotifyKB2.setText(String.valueOf(GREATER_EQUAL_THAN));

		// txtNotifyKB start
		txtNotifyKB = new Text(cmpNotify, SWT.NONE);
		txtNotifyKB.setTextLimit(6);
		txtNotifyKB.setLayoutData(grdAlert);

		if (SessionDetails.notifyKB > -1)
			txtNotifyKB.setText("" + SessionDetails.notifyKB);
		// txtNotifyKB end

		lblNotifyKB3 = new Label(cmpNotify, SWT.NONE);
		lblNotifyKB3.setText("KB");

	}

	private void addListeners() {
		// 'X' button listener
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {
				e.doit = false;
				doExit();
			}
		});

		// cboUnits selection listener
		cboUnits.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changeUnits(cboUnits.getText());
			}
		});

		// txtCostPerKB text modify listener
		txtCostPerKB.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateCost();
			}
		});

		// txtNotifyCost text modify listener
		txtNotifyCost.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateNotifyCost();
			}
		});

		// txtNotifyKB text modify listener
		txtNotifyKB.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateNotifyKB();
			}
		});

	}

	private void updateCost() {
		float cost;
		try {
			cost = (float) ((bytesTotal / 1024.0) * Float
					.parseFloat(txtCostPerKB.getText()));
			cost = (float) (Math.round(cost * 1000.0) / 1000.0);
			lblCostTotalNum.setText("" + cost);
		} catch (Exception ex) {
			lblCostTotalNum.setText("N/A");
		}
		lblCostTotalNum.pack();
	}

	private void updateNotifyCost() {

		try {
			SessionDetails.notifyCost = (float) Float.parseFloat(txtNotifyCost.getText());
			SessionDetails.notifiedCost = false;
		} catch (Exception ex) {
			SessionDetails.notifyCost = -1;
		}

	}

	private void updateNotifyKB() {

		try {
			SessionDetails.notifyKB = Integer.parseInt(txtNotifyKB.getText());
			SessionDetails.notifiedKB = false;
		} catch (Exception ex) {
			SessionDetails.notifyKB = -1;
		}

	}

	private void changeUnits(String newUnit) {
		int denominator;

		float dataIn;
		float dataOut;
		float dataTotal;

		dataIn = bytesIn;
		dataOut = bytesOut;
		dataTotal = bytesTotal;

		if (newUnit.contains("b")) {
			dataIn = (dataIn * 8);
			dataOut = (dataOut * 8);
			dataTotal = (dataTotal * 8);
		}

		if (newUnit.equals("Kb") || newUnit.equals("KB"))
			denominator = 1024;
		else if (newUnit.equals("Mb") || newUnit.equals("MB"))
			denominator = 1048576;
		else
			denominator = 1;

		if (denominator > 1) {
			dataIn = (float) (Math.round((dataIn / denominator) * 100.0) / 100.0);
			dataOut = (float) (Math.round((dataOut / denominator) * 100.0) / 100.0);
			dataTotal = (float) (Math.round((dataTotal / denominator) * 100.0) / 100.0);
		}

		lblDataInNum.setText("" + dataIn);
		lblDataOutNum.setText("" + dataOut);
		lblDataTotalNum.setText("" + dataTotal);
		lblDataInNum.pack();
		lblDataOutNum.pack();
		lblDataTotalNum.pack();
	}

	private void doExit() {
		// save the costPerKB value so user doesn't have to re-enter it again
		try {
			SessionDetails.costPerKB = Float.parseFloat(txtCostPerKB.getText());
		} catch (Exception ex) {
		}

		shell.dispose();
	}
}
