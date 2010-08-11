/**
 * 
 */
package org.jemian.countdownj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * @author Pete
 * 
 */
public class Gui {

	public ClockTimer clockTimer;
	public LabelPaneComposite mmssComposite;
	public LabelPaneComposite phaseComposite;
	
	public Gui(Shell s) {
		clockTimer = new ClockTimer(this);

		GridLayout layout = new GridLayout(1, true);
		s.setLayout(layout);

		GridData griddata = new GridData(GridData.FILL_BOTH);
		mmssComposite = new LabelPaneComposite(s);
		mmssComposite.setLayoutData(griddata);

		griddata = new GridData(GridData.FILL_BOTH);
		phaseComposite = new LabelPaneComposite(s);
		phaseComposite.setLayoutData(griddata);

		phaseComposite.SetText("second text composite");
		mmssComposite.SetTextSize(50);
		phaseComposite.SetTextSize(20);
		mmssComposite.SetNamedForegroundColor("red");
		phaseComposite.SetNamedForegroundColor("yellow");

		griddata = new GridData(GridData.FILL_BOTH);
		TabFolder tabs = setupTabbedPanels(s);
		tabs.setLayoutData(griddata);
		clockTimer.SetCounter(25);
		clockTimer.start();
	}

	public void callbackFunction(String str) {
		System.out.println("callbackFunction: " + str);
		// can't set the widget text from here 
		// got to be more crafty, it seems
		// mmssComposite.SetText("curious");
	}
	
	private static TabFolder setupTabbedPanels(Shell s) {
		TabFolder tf = new TabFolder(s, SWT.NONE);

		TabItem ti1 = new TabItem(tf, SWT.NONE);
		ti1.setText("mm:ss controls");
		ControlsMmss cm1 = new ControlsMmss(tf);
		ti1.setControl(cm1);

		TabItem ti2 = new TabItem(tf, SWT.NONE);
		ti2.setText("preset controls");
		ControlsPresets pc2 = new ControlsPresets(tf);
		ti2.setControl(pc2);

		TabItem ti3 = new TabItem(tf, SWT.NONE);
		ti3.setText("configure presets");
		ConfigurePresets cp3 = new ConfigurePresets(tf);
		ti3.setControl(cp3);

		TabItem ti4 = new TabItem(tf, SWT.NONE);
		ti4.setText("configure");
		ConfigureComposite cc4 = new ConfigureComposite(tf);
		ti4.setControl(cc4);

		return tf;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display d = new Display();
		Shell s = new Shell(d, SWT.CLOSE | SWT.RESIZE | SWT.MAX);
		s.setSize(500, 500);
		s.setText("CountdownJ, by Pete Jemian");
		
		Gui gui = new Gui(s);

		s.pack();
		s.open();

		while (!s.isDisposed()) {
			if (!d.readAndDispatch())
				d.sleep();
		}
		gui.clockTimer.stop();
		d.dispose();
	}

}
