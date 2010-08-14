package org.jemian.countdownj;

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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
	int discussionTime_s = 5*60;
	int overtimeReminder_s = 60;

	public Gui(Shell s) {
		clockTimer = new ClockTimer(this);
		finalShell = s;

		s.setMaximized (true);
		
		d = s.getDisplay();
		lastPhaseText = "";
		last_time_s = 0;

		FormLayout layout = new FormLayout();
		s.setLayout(layout);
		
		int inset = 5;

		FormData alignment = new FormData();
		alignment.top = new FormAttachment(0, inset);
		alignment.left = new FormAttachment(0, inset);
		alignment.bottom = new FormAttachment(60, -inset);
		alignment.right = new FormAttachment(100, -inset);
		mmssComposite = new LabelPaneComposite(s);
		mmssComposite.setLayoutData(alignment);

		alignment = new FormData();
		alignment.top = new FormAttachment(mmssComposite, inset);
		alignment.left = new FormAttachment(0, inset);
		alignment.right = new FormAttachment(100, -inset);
		phaseComposite = new LabelPaneComposite(s);

		FormData fdata = new FormData();
		// NO fdata.top: that way, the tabs box stays its default height
		fdata.left = new FormAttachment(0, inset);
		fdata.bottom = new FormAttachment(100, -inset);
		fdata.right = new FormAttachment(100, -inset);
		CTabFolder tabs = setupTabbedPanels(s);
		tabs.setLayoutData(fdata);

		// phaseComposite.bottom attached to tabs.top
		alignment.bottom = new FormAttachment(tabs, -inset);
		phaseComposite.setLayoutData(alignment);

		mmssComposite.setTextSize();
		phaseComposite.setTextSize();
		mmssComposite.setNamedForegroundColor("white");
		phaseComposite.setNamedForegroundColor("white");

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

	public void timerCallback(String str) {
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
				updateText(mmssComposite, finalMmssText);
				updateText(phaseComposite, finalPhaseText);
				mmssComposite.setNamedForegroundColor(finalColor);
				phaseComposite.setNamedForegroundColor(finalColor);
				double time_s = clockTimer.getTime_s();
				if (time_s < 0) {
					updateButtonText(mmssBtnComposite, startKeyMmss, "stop");
				}
				for (int i = 0; i < beepCount; i++) {
					try {
						Thread.sleep(333);  // intervals between beeps
					} catch (InterruptedException e) {
						// ignore this possibility, per
						// http://download.oracle.com/javase/tutorial/essential/concurrency/sleep.html
					}
					beep();
				}
			}
		});
	}
	
	/**
	 * change the button text if it is different
	 * @param c : ButtonsComposite object
	 * @param key : enumeration key
	 * @param text : new text
	 */
	private void updateButtonText(ButtonsComposite c, String key, String text) {
		if (text.compareTo(c.getButtonText(key)) != 0)
			c.setButtonText(key, text);
	}
	
	/**
	 * change the label text if it is different
	 * @param c : LabelPaneComposite object
	 * @param text : new text
	 */
	private void updateText(LabelPaneComposite c, String text) {
		if (text.compareTo(c.getText()) != 0)
			c.setText(text);
	}
	
	/**
	 * sound a beep
	 */
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
				if (time_s > discussionTime_s) {
					phaseText = "Presentation";
				} else  {
					phaseText = "Discussion";
				}
			}
		}
		return phaseText;
	}

	private CTabFolder setupTabbedPanels(Shell s) {
		CTabFolder tf = new CTabFolder(s, SWT.NONE);

		CTabItem ti1 = new CTabItem(tf, SWT.NONE);
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
		
	private void setupOtherTabs(CTabFolder tf) {
		CTabItem ti2 = new CTabItem(tf, SWT.NONE);
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

		CTabItem ti3 = new CTabItem(tf, SWT.NONE);
		ti3.setText("configure presets");
		key = "configPre";
		presetConfigureBtnComposite = new ButtonsComposite(this, tf, key, 4);
		ti3.setControl(presetConfigureBtnComposite);
		presetConfigureBtnComposite.setButtonText(key + "-0", "<A>");
		presetConfigureBtnComposite.setButtonText(key + "-1", "<B>");
		presetConfigureBtnComposite.setButtonText(key + "-2", "<C>");
		presetConfigureBtnComposite.setButtonText(key + "-3", "<D>");

		CTabItem ti4 = new CTabItem(tf, SWT.NONE);
		ti4.setText("configure");
		key = "configure";
		configureBtnComposite = new ButtonsComposite(this, tf, key, 1);
		ti4.setControl(configureBtnComposite);
		configureBtnComposite.setButtonText(key + "-0", "configure");
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
			if ("presets-0".compareTo(key) == 0) phaseComposite.setText("not yet");
			if ("presets-1".compareTo(key) == 0) phaseComposite.setText("not yet");
			if ("presets-2".compareTo(key) == 0) phaseComposite.setText("not yet");
			if ("presets-3".compareTo(key) == 0) phaseComposite.setText("not yet");
		}
		if (parent == presetConfigureBtnComposite) {
			String key = presetConfigureBtnComposite.getButtonKey(b);
			if ("configPre-0".compareTo(key) == 0) phaseComposite.setText("not yet");
			if ("configPre-1".compareTo(key) == 0) phaseComposite.setText("not yet");
			if ("configPre-2".compareTo(key) == 0) phaseComposite.setText("not yet");
			if ("configPre-3".compareTo(key) == 0) phaseComposite.setText("not yet");
		}
		if (parent == configureBtnComposite)
			doConfigureButton();
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
	
	private void doConfigureButton() {
	    // System.out.println("discussion (s): ");
	    ConfigureDialog cd = new ConfigureDialog(finalShell);
	    cd.setText("global parameters");
	    cd.setDiscussionTime_s(discussionTime_s);
	    cd.setOvertimeReminder_s(overtimeReminder_s);
	    if (cd.open()) {
		    int i = cd.getDiscussionTime_s();
	    	discussionTime_s = i;
		    i = cd.getOvertimeReminder_s();
		    overtimeReminder_s = i;
	    }
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
