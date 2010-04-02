package gui;

import java.text.DecimalFormat;

import modal.SessionDetails;
import modal.SessionDetails.DataUnit;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Status {
	private Shell shell;
	private Display display = Display.getDefault();
	private Composite cmpNotify;
	private Composite cmpBandwidth;
	private Composite cmpCost;
	private Composite cmpMain;

	private Combo cboUnits;
	private SessionDetails.DataUnit unit;

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
	private DecimalFormat numberFormat;

	public Status() {
		// Setup the number format.
		numberFormat = new DecimalFormat("0.00");
		numberFormat.setNegativePrefix("");
		
		createWindow();
		runWindow();
	}

	private void runWindow() {
		shell.open();

		while (!shell.isDisposed()) {
			updateWindow();
			
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

	//Create main composite
	private void createCmpMain() {
		final int numCols = 1;

		GridLayout grlMain = new GridLayout();
		grlMain.numColumns = numCols;

		cmpMain = new Composite(shell, SWT.NONE);
		cmpMain.setLayout(grlMain);
	}

	//Create bandwidth composite
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

		cboUnits = new Combo(cmpBandwidth, SWT.DROP_DOWN | SWT.READ_ONLY);
		cboUnits.add("b");
		cboUnits.add("B");
		cboUnits.add("Kb");
		cboUnits.add("KB");
		cboUnits.add("Mb");
		cboUnits.add("MB");
		cboUnits.setText("B");
		updateUnits();

		lblDataInTitle = new Label(cmpBandwidth, SWT.NONE);
		lblDataInTitle.setText("Data In:");
		lblDataInNum = new Label(cmpBandwidth, SWT.NONE);

		lblDataOutTitle = new Label(cmpBandwidth, SWT.NONE);
		lblDataOutTitle.setText("Data Out:");
		lblDataOutNum = new Label(cmpBandwidth, SWT.NONE);

		lblDataTotalTitle = new Label(cmpBandwidth, SWT.NONE);
		lblDataTotalTitle.setText("Data Total:");
		lblDataTotalNum = new Label(cmpBandwidth, SWT.NONE);
	}

	//Create cost composite
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
		// cboUnits selection listener
		cboUnits.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateUnits();
				updateWindow();
			}
		});

		// txtCostPerKB text modify listener
		txtCostPerKB.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateCost();
				
				// save the costPerKB value so user doesn't have to re-enter it again
				try {
					SessionDetails.costPerKB = Float.parseFloat(txtCostPerKB.getText());
				} catch (Exception ex) {
				}
			}
		});

		// txtNotifyCost text modify listener
		txtNotifyCost.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				SessionDetails.updateNotifyCost(txtNotifyCost.getText());
			}
		});

		// txtNotifyKB text modify listener
		txtNotifyKB.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				SessionDetails.updateNotifyKB(txtNotifyKB.getText());
			}
		});
	}

	private void updateCost() {
		try {
			float dataTotal = SessionDetails.getTotal(SessionDetails.DataUnit.KB);
			float cost = dataTotal * SessionDetails.costPerKB;
			String costText = numberFormat.format(cost);
			lblCostTotalNum.setText(costText);
		} catch (Exception ex) {
			lblCostTotalNum.setText("N/A");
		}
		lblCostTotalNum.pack();
	}

	private void updateUnits() {
		String unitText = cboUnits.getText();
		
		if (unitText.equals("b")) {
			unit = DataUnit.b;
		} else if (unitText.equals("B")) {
			unit = DataUnit.B;
		} else if (unitText.equals("Kb")) {
			unit = DataUnit.Kb;
		} else if (unitText.equals("KB")) {
			unit = DataUnit.KB;
		} else if (unitText.equals("Mb")) {
			unit = DataUnit.Mb;
		} else if (unitText.equals("MB")) {
			unit = DataUnit.MB;
		}
	}
	
	public void updateWindow() {
		// Format the data statistics.
		String in = numberFormat.format(SessionDetails.getTotalIn(unit));
		String out = numberFormat.format(SessionDetails.getTotalOut(unit));
		String total = numberFormat.format(SessionDetails.getTotal(unit));
		
		// Update the label text.
		lblDataInNum.setText(in);
		lblDataOutNum.setText(out);
		lblDataTotalNum.setText(total);
		
		// Resize labels so that their text is all visible... AND NOTHING MORE >:-)
		lblDataInNum.pack();
		lblDataOutNum.pack();
		lblDataTotalNum.pack();
		
		updateCost();
	}
}
