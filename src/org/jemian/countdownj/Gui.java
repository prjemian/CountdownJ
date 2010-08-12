package org.jemian.countdownj;

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
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
	ControlsButtons mmssBtnComposite;
	ControlsButtons presetBtnComposite;
	ConfigurePresets presetConfigureBtnComposite;
	ConfigureComposite configureBtnComposite;

	public Gui(Shell s) {
		clockTimer = new ClockTimer(this);
		final Shell finalShell = s;

		GridLayout layout = new GridLayout(1, true);
		s.setLayout(layout);

		GridData griddata = new GridData(GridData.FILL_BOTH);
		mmssComposite = new LabelPaneComposite(s);
		mmssComposite.setLayoutData(griddata);

		griddata = new GridData(GridData.FILL_BOTH);
		phaseComposite = new LabelPaneComposite(s);
		phaseComposite.setLayoutData(griddata);

		phaseComposite.SetText("second text composite");
		mmssComposite.SetTextSize();
		phaseComposite.SetTextSize();
		mmssComposite.SetNamedForegroundColor("red");
		phaseComposite.SetNamedForegroundColor("yellow");

		griddata = new GridData(GridData.FILL_HORIZONTAL);
		TabFolder tabs = setupTabbedPanels(s);
		tabs.setLayoutData(griddata);
		clockTimer.setTime_s(15 * 60);

		s.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Resize:
					Rectangle rect = finalShell.getClientArea();
					System.out.println("Shell resized: height=" + rect.height);
					break;
				}
			}
		});
	}

	public void callbackFunction(String str) {
		System.out.println("callbackFunction: " + str);
		// can't set the widget text from here
		// got to be more crafty, it seems
		// mmssComposite.SetText(str);
		// throw some event to set this instead
	}

	private TabFolder setupTabbedPanels(Shell s) {
		TabFolder tf = new TabFolder(s, SWT.NONE);
		// Display d = s.getDisplay();
		// tf.setBackground(new Color(d, 0, 0, 0));
		// tf.setForeground(new Color(d, 255, 255, 255));

		TabItem ti1 = new TabItem(tf, SWT.NONE);
		ti1.setText("mm:ss controls");
		String key = "mm:ss";
		mmssBtnComposite = new ControlsButtons(this, tf, key);
		ti1.setControl(mmssBtnComposite);
		mmssBtnComposite.setButtonText(key + "-0", "+10:00");
		mmssBtnComposite.setButtonText(key + "-1", "+01:00");
		mmssBtnComposite.setButtonText(key + "-2", "+00:10");
		mmssBtnComposite.setButtonText(key + "-3", "+00:01");
		mmssBtnComposite.setButtonText("start", "start");
		mmssBtnComposite.setButtonText("stop", "clear");

		TabItem ti2 = new TabItem(tf, SWT.NONE);
		ti2.setText("preset controls");
		key = "presets";
		presetBtnComposite = new ControlsButtons(this, tf, key);
		ti2.setControl(presetBtnComposite);
		presetBtnComposite.setButtonText(key + "-0", "<A>");
		presetBtnComposite.setButtonText(key + "-1", "<B>");
		presetBtnComposite.setButtonText(key + "-2", "<C>");
		presetBtnComposite.setButtonText(key + "-3", "<D>");
		presetBtnComposite.setButtonText("start", "start");
		presetBtnComposite.setButtonText("stop", "clear");

		TabItem ti3 = new TabItem(tf, SWT.NONE);
		ti3.setText("configure presets");
		key = "configPre";
		presetConfigureBtnComposite = new ConfigurePresets(this, tf);
		ti3.setControl(presetConfigureBtnComposite);
		presetConfigureBtnComposite.setButtonText(key + "-0", "<A>");
		presetConfigureBtnComposite.setButtonText(key + "-1", "<B>");
		presetConfigureBtnComposite.setButtonText(key + "-2", "<C>");
		presetConfigureBtnComposite.setButtonText(key + "-3", "<D>");

		TabItem ti4 = new TabItem(tf, SWT.NONE);
		ti4.setText("configure");
		key = "configure";
		configureBtnComposite = new ConfigureComposite(this, tf);
		ti4.setControl(configureBtnComposite);
		configureBtnComposite.setButtonText(key, "configure");

		return tf;
	}

	public void buttonResponder(Button b, String label) {
		Composite parent = b.getParent();
		if (parent == mmssBtnComposite)
			System.out.println("mmssBtnComposite: " + label);
		if (parent == presetBtnComposite)
			System.out.println("presetBtnComposite: " + label);
		if (parent == presetConfigureBtnComposite)
			System.out.println("presetConfigureBtnComposite: " + label);
		if (parent == configureBtnComposite)
			System.out.println("configureBtnComposite: " + label);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String revision = "$Revision$";
		Display d = new Display();
		Shell s = new Shell(d, SWT.SHELL_TRIM);
		s.setText("CountdownJ, by Pete Jemian: " + revision);
		s.setSize(500, 500);
		s.setBackground(new Color(d, 0, 0, 0));

		Gui gui = new Gui(s);

		// s.pack();
		s.open();

		while (!s.isDisposed()) {
			if (!d.readAndDispatch())
				d.sleep();
		}
		gui.clockTimer.stop();
		d.dispose();
	}

}
