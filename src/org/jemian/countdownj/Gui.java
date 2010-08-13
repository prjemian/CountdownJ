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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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
	ButtonsComposite mmssBtnComposite;
	ButtonsComposite presetBtnComposite;
	ButtonsComposite presetConfigureBtnComposite;
	ButtonsComposite configureBtnComposite;
	final Shell finalShell;
	String lastPhaseText;
	double last_time_s;
	Display d;
	String startKeyMmss;
	String stopKeyMmss;
	String startKeyPresets;
	String stopKeyPresets;

	public Gui(Shell s) {
		clockTimer = new ClockTimer(this);
		finalShell = s;
		
		d = s.getDisplay();
		lastPhaseText = "";
		last_time_s = 0;

		GridLayout layout = new GridLayout(1, true);
		s.setLayout(layout);

		GridData griddata = new GridData(GridData.FILL_BOTH);
		mmssComposite = new LabelPaneComposite(s);
		mmssComposite.setLayoutData(griddata);

		griddata = new GridData(GridData.FILL_BOTH);
		phaseComposite = new LabelPaneComposite(s);
		phaseComposite.setLayoutData(griddata);

		mmssComposite.SetTextSize();
		phaseComposite.SetTextSize();
		mmssComposite.SetNamedForegroundColor("white");
		phaseComposite.SetNamedForegroundColor("white");

		griddata = new GridData(GridData.FILL_HORIZONTAL);
		TabFolder tabs = setupTabbedPanels(s);
		tabs.setLayoutData(griddata);
		clockTimer.setTime_s(15 * 60);
		clockTimer.update();

		// TODO: Do we need this listener?
//		s.addListener(SWT.Resize, new Listener() {
//			public void handleEvent(Event e) {
//				switch (e.type) {
//				case SWT.Resize:
//					Rectangle rect = finalShell.getClientArea();
//					//System.out.println("Shell resized: height=" + rect.height);
//					break;
//				}
//			}
//		});
	}

	public void callbackFunction(String str) {
		// can't set the widget text directly from here
		//  (We were called by a timer from another thread)
		// here's the way to do it
		// http://www.eclipse.org/swt/faq.php#uithread
		Display d = finalShell.getDisplay();
		double time_s = clockTimer.getTime_s();

		int numBeeps = 0;
		String color = "white";
		final String finalPhaseText = calcPhaseText();
		if ("Overtime".compareTo(finalPhaseText)==0) {
			color = "red";
			if ("Discussion".compareTo(lastPhaseText) == 0)
				numBeeps = 2;
			if (last_time_s < 0) {
				// TODO: need to get from configuration
				double overtimeReminder_s = 60.0;
				double t1 = Math.abs(time_s) % overtimeReminder_s;
				double t2 = Math.abs(last_time_s) % overtimeReminder_s;
				if ( t1 < t2)
					numBeeps = 3;				
			}
		}
		if ("Discussion".compareTo(finalPhaseText)==0) {
			color = "yellow";
			if ("Presentation".compareTo(lastPhaseText) == 0)
				numBeeps = 1;
		}
		if ("Presentation".compareTo(finalPhaseText)==0) {
			color = "green";
		}

		// for next time
		lastPhaseText = finalPhaseText;
		last_time_s = time_s;

		final String finalMmssText = str;
		final String finalColor = color;
		final int beepCount = numBeeps;

		d.syncExec(new Runnable() {
			public void run() {
				mmssComposite.SetText(finalMmssText);
				phaseComposite.SetText(finalPhaseText);
				mmssComposite.SetNamedForegroundColor(finalColor);
				phaseComposite.SetNamedForegroundColor(finalColor);
				for (int i = 0; i < beepCount; i++)
					beep();
			}
		});
	}
	
	private void beep() {
		// AWT method
		//Toolkit.getDefaultToolkit().beep();

		// ASCII method
		//		System.out.print("\007");	// ASCII BEL
		//		System.out.flush();
		//		double t = clockTimer.getTime_s();

		// SWT method
		d.beep();
	}

	private String calcPhaseText() {
		double time_s = clockTimer.getTime_s();
		String phaseText = "";
		if (clockTimer.isCounting()) {
			if (time_s < 0) {
				phaseText = "Overtime";
			} else {
				// TODO: need to get from configuration
				double discussionTime_s = 5*60;
				if (time_s > discussionTime_s) {
					phaseText = "Presentation";
				} else  {
					phaseText = "Discussion";
				}
			}
		}
		return phaseText;
	}

	private TabFolder setupTabbedPanels(Shell s) {
		TabFolder tf = new TabFolder(s, SWT.NONE);

		TabItem ti1 = new TabItem(tf, SWT.NONE);
		ti1.setText("mm:ss controls");
		String key = "mm:ss";
		mmssBtnComposite = new ButtonsComposite(this, tf, key, 6);
		ti1.setControl(mmssBtnComposite);
		mmssBtnComposite.setButtonText(key + "-0", "+10:00");
		mmssBtnComposite.setButtonText(key + "-1", "+01:00");
		mmssBtnComposite.setButtonText(key + "-2", "+00:10");
		mmssBtnComposite.setButtonText(key + "-3", "+00:01");
		startKeyMmss = "start";
		stopKeyMmss = "stop";
		startKeyMmss = key + "-4";
		stopKeyMmss = key + "-5";
		mmssBtnComposite.setButtonText(startKeyMmss, "start");
		mmssBtnComposite.setButtonText(stopKeyMmss, "clear");

		setupOtherTabs(tf);

		return tf;
	}
		
	private void setupOtherTabs(TabFolder tf) {
		TabItem ti2 = new TabItem(tf, SWT.NONE);
		ti2.setText("preset controls");
		String key = "presets";
		presetBtnComposite = new ButtonsComposite(this, tf, key, 6);
		ti2.setControl(presetBtnComposite);
		presetBtnComposite.setButtonText(key + "-0", "<A>");
		presetBtnComposite.setButtonText(key + "-1", "<B>");
		presetBtnComposite.setButtonText(key + "-2", "<C>");
		presetBtnComposite.setButtonText(key + "-3", "<D>");
		startKeyPresets = "start";
		stopKeyPresets = "stop";
		startKeyPresets = key + "-4";
		stopKeyPresets = key + "-5";
		presetBtnComposite.setButtonText(startKeyPresets, "start");
		presetBtnComposite.setButtonText(stopKeyPresets, "clear");

		TabItem ti3 = new TabItem(tf, SWT.NONE);
		ti3.setText("configure presets");
		key = "configPre";
		presetConfigureBtnComposite = new ButtonsComposite(this, tf, key, 4);
		ti3.setControl(presetConfigureBtnComposite);
		presetConfigureBtnComposite.setButtonText(key + "-0", "<A>");
		presetConfigureBtnComposite.setButtonText(key + "-1", "<B>");
		presetConfigureBtnComposite.setButtonText(key + "-2", "<C>");
		presetConfigureBtnComposite.setButtonText(key + "-3", "<D>");

		TabItem ti4 = new TabItem(tf, SWT.NONE);
		ti4.setText("configure");
		key = "configure";
		configureBtnComposite = new ButtonsComposite(this, tf, key, 1);
		ti4.setControl(configureBtnComposite);
		configureBtnComposite.setButtonText(key, "configure");
	}

	public void buttonResponder(Button b, String label) {
		Composite parent = b.getParent();
		if (parent == mmssBtnComposite) {
			String key = mmssBtnComposite.getButtonKey(b);
			if (startKeyMmss.compareTo(key) == 0)   startButtonHandler();
			if (stopKeyMmss.compareTo(key) == 0)    stopButtonHandler();
			if ("mm:ss-0".compareTo(key) == 0) incrementButtonHandler(10*60);
			if ("mm:ss-1".compareTo(key) == 0) incrementButtonHandler(60);
			if ("mm:ss-2".compareTo(key) == 0) incrementButtonHandler(10);
			if ("mm:ss-3".compareTo(key) == 0) incrementButtonHandler(1);
		}
		if (parent == presetBtnComposite) {
			String key = presetBtnComposite.getButtonKey(b);
			// System.out.println("presetBtnComposite   " + key);
			if (startKeyPresets.compareTo(key) == 0) startButtonHandler();
			if (stopKeyPresets.compareTo(key) == 0) stopButtonHandler();
			if ("presets-0".compareTo(key) == 0) phaseComposite.SetText("not yet");
			if ("presets-1".compareTo(key) == 0) phaseComposite.SetText("not yet");
			if ("presets-2".compareTo(key) == 0) phaseComposite.SetText("not yet");
			if ("presets-3".compareTo(key) == 0) phaseComposite.SetText("not yet");
		}
		if (parent == presetConfigureBtnComposite) {
			String key = presetConfigureBtnComposite.getButtonKey(b);
			if ("configPre-0".compareTo(key) == 0) phaseComposite.SetText("not yet");
			if ("configPre-1".compareTo(key) == 0) phaseComposite.SetText("not yet");
			if ("configPre-2".compareTo(key) == 0) phaseComposite.SetText("not yet");
			if ("configPre-3".compareTo(key) == 0) phaseComposite.SetText("not yet");
		}
		if (parent == configureBtnComposite)
			phaseComposite.SetText("not yet");
	}
	
	private void incrementButtonHandler(int seconds) {
		if (!clockTimer.isCounting())
			clockTimer.incrTime_s(seconds);
			clockTimer.update();
	}
	
	private void startButtonHandler() {
		if (!clockTimer.isCounting() && clockTimer.getTime_s()>0) {
			clockTimer.start();
			mmssBtnComposite.setButtonText(startKeyMmss, "pause");
			mmssBtnComposite.setButtonText(stopKeyMmss, "stop");
			presetBtnComposite.setButtonText(startKeyPresets, "pause");
			presetBtnComposite.setButtonText(stopKeyPresets, "stop");
		} else {
			clockTimer.stop();
			// endTime = 0;
			if (clockTimer.getTime_s()>0) {
				mmssBtnComposite.setButtonText(startKeyMmss, "resume");
				mmssBtnComposite.setButtonText(stopKeyMmss, "clear");
				presetBtnComposite.setButtonText(startKeyPresets, "resume");
				presetBtnComposite.setButtonText(stopKeyPresets, "clear");
			} else {
				// pause while in overtime, same as a full stop
				stopButtonHandler();
			}
		}
		clockTimer.update();
	}
	
	private void stopButtonHandler() {
		if (!clockTimer.isCounting()) {
			clockTimer.stop();
			clockTimer.clearCounter();
			mmssBtnComposite.setButtonText(startKeyMmss, "start");
			mmssBtnComposite.setButtonText(stopKeyMmss, "clear");
			presetBtnComposite.setButtonText(startKeyPresets, "start");
			presetBtnComposite.setButtonText(stopKeyPresets, "clear");
		} else {
			clockTimer.stop();
			clockTimer.clearCounter();
			mmssBtnComposite.setButtonText(startKeyMmss, "start");
			mmssBtnComposite.setButtonText(stopKeyMmss, "clear");
			presetBtnComposite.setButtonText(startKeyPresets, "start");
			presetBtnComposite.setButtonText(stopKeyPresets, "clear");
		}
		clockTimer.update();
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
