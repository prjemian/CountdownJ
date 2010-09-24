/*
 * To change this template, choose Tools | Templates
 * and open the template in the NetBeans editor.
 */

/*
 * GuiSwing.java
 *
 * Created on Aug 23, 2010, 8:13:14 PM
 */

package org.jemian.countdownj;

/*
    CountdownJ, (c) 2010 Pete R. Jemian <prjemian@gmail.com>
    See LICENSE (GPLv3) for details.
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * Provide a visible countdown clock as feedback for presentations.
 */
public class CountdownJ extends JFrame {

	/**
     * creates ConfigureJ application main display
     */
    public CountdownJ() {
    	defineInitialTalkConfigurations();
        overrideInitialTalkConfigurations();
        setDefaultFiles();
        restoreSettings();        // restore last known program settings
    	initializeColorTable();
    	createGui();
    	setTitle();
        talkTimer = new TalkTimer(this, settings.get("basic"));
        setExtendedState(MAXIMIZED_BOTH);     // full screen
    }

    // ----------------------------------------------------------------------
    
    /**
     * construct the GUI widgets
     */
    private void createGui() {
    	defineGuiWidgets();
    	constructGuiWidgets();
    	configureGuiWidgets();
    	pack();
    }
    
    private void defineGuiWidgets() {
    	// define the GUI widgets (alphabetical order by name)
    	aboutButton = new JButton("About ...");
        blackPanel = new JPanel();
        configureButton = new JButton("Configure ...");
        mmssButton1 = new JButton("+10:00");
        mmssButton2 = new JButton("+1:00");
        mmssButton3 = new JButton("+0:10");
        mmssButton4 = new JButton("+0:01");
        mmssButtonStart = new JButton("Start");
        mmssButtonStop = new JButton("Stop");
        mmssTabPane = new JPanel();
        mmssText = new JLabel("MM:SS");
        mmssTextPanel = new JPanel();
        msgText = new JLabel("Message here");
        msgTextPanel = new JPanel();
        otherTabPane = new JPanel();
        presetButton1 = new JButton("preset1");
        presetButton2 = new JButton("preset2");
        presetButton3 = new JButton("preset3");
        presetButton4 = new JButton("preset4");
        presetButtonStart = new JButton("Start");
        presetButtonStop = new JButton("Stop");
        presetTabPane = new JPanel();
        tabbedPane = new JTabbedPane();
    }
    
    /**
     * arrange the widgets
     */
    private void constructGuiWidgets() {
    	setLayout(new GridBagLayout());
    	add(blackPanel, makeConstraints(0, 0, 1.0, 1.0, 1, 1));

    	blackPanel.setLayout(new GridBagLayout());
    	blackPanel.add(mmssTextPanel, makeConstraints(0, 0, 1.0, 3.0, 1, 1));	// 3.0 is a judgment call
    	blackPanel.add(msgTextPanel, makeConstraints(0, 1, 1.0, 1.0, 1, 1));
    	blackPanel.add(tabbedPane, makeConstraints(0, 2, 1.0, 0.0, 1, 1));

    	mmssTextPanel.setLayout(new GridBagLayout());
        mmssTextPanel.add(mmssText, makeConstraints(0, 0, 1.0, 1.0, 1, 1));

        msgTextPanel.setLayout(new GridBagLayout());
    	msgTextPanel.add(msgText, makeConstraints(0, 0, 1.0, 1.0, 1, 1));

    	tabbedPane.add("basic", mmssTabPane);
    	tabbedPane.add("presets", presetTabPane);
    	tabbedPane.add("other", otherTabPane);

    	mmssTabPane.setLayout(new GridBagLayout());
    	mmssTabPane.add(mmssButton1, makeConstraints(0, 0, 1.0, 0.0, 1, 1));
    	mmssTabPane.add(mmssButton2, makeConstraints(1, 0, 1.0, 0.0, 1, 1));
    	mmssTabPane.add(mmssButton3, makeConstraints(2, 0, 1.0, 0.0, 1, 1));
    	mmssTabPane.add(mmssButton4, makeConstraints(3, 0, 1.0, 0.0, 1, 1));
    	mmssTabPane.add(new JPanel(), makeConstraints(4, 0, 1.0, 0.0, 1, 1));
    	mmssTabPane.add(mmssButtonStart, makeConstraints(5, 0, 1.0, 0.0, 1, 1));
    	mmssTabPane.add(mmssButtonStop, makeConstraints(6, 0, 1.0, 0.0, 1, 1));

    	presetTabPane.setLayout(new GridBagLayout());
    	presetTabPane.add(presetButton1, makeConstraints(0, 0, 1.0, 0.0, 1, 1));
    	presetTabPane.add(presetButton2, makeConstraints(1, 0, 1.0, 0.0, 1, 1));
    	presetTabPane.add(presetButton3, makeConstraints(2, 0, 1.0, 0.0, 1, 1));
    	presetTabPane.add(presetButton4, makeConstraints(3, 0, 1.0, 0.0, 1, 1));
    	presetTabPane.add(new JPanel(), makeConstraints(4, 0, 1.0, 0.0, 1, 1));
    	presetTabPane.add(presetButtonStart, makeConstraints(5, 0, 1.0, 0.0, 1, 1));
    	presetTabPane.add(presetButtonStop, makeConstraints(6, 0, 1.0, 0.0, 1, 1));

    	otherTabPane.setLayout(new GridBagLayout());
    	otherTabPane.add(configureButton, makeConstraints(0, 0, 1.0, 0.0, 1, 1));
    	otherTabPane.add(aboutButton, makeConstraints(1, 0, 1.0, 0.0, 1, 1));
    }
    
    /**
     * make GridBagConstraints for a GridBagLayout item
     * @param x
     * @param y
     * @param weightx
     * @param weighty
     * @param rows
     * @param cols
     * @return
     */
    private GridBagConstraints makeConstraints(int x, int y, 
    		double weightx, double weighty, int cols, int rows) {
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.BOTH;
    	c.gridx = x;
    	c.gridy = y;
    	c.weightx = weightx;
    	c.weighty = weighty;
    	c.gridwidth = cols;
    	c.gridheight = rows;
    	c.insets = new Insets(2, 2, 2, 2);
    	return c;
    }
    
    /**
     * local customization of standard widgets
     */
    private void configureGuiWidgets() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        blackPanel.setBackground(Color.black);

        // font sizes track with window size
        blackPanel.addHierarchyBoundsListener(
				new HierarchyBoundsListener() {
				    public void ancestorResized(HierarchyEvent e) {
				        doAdjustLabelSizes();
				    }
				    public void ancestorMoved(HierarchyEvent e) {}
				});

        mmssTextPanel.setBackground(Color.black);
        mmssTextPanel.setForeground(Color.white);

        mmssText.setBackground(Color.black);
        mmssText.setFont(new Font("Tahoma", 1, 48));
        mmssText.setForeground(Color.white);
        mmssText.setHorizontalAlignment(SwingConstants.CENTER);
        mmssText.setText("mm:ss");

        msgTextPanel.setBackground(Color.black);
        msgTextPanel.setForeground(Color.white);

        msgText.setBackground(Color.black);
        msgText.setFont(new Font("Tahoma", 0, 24));
        msgText.setForeground(Color.white);
        msgText.setHorizontalAlignment(SwingConstants.CENTER);
        msgText.setText("message");

        initializeButton( mmssButton1 );
        initializeButton( mmssButton2 );
        initializeButton( mmssButton3 );
        initializeButton( mmssButton4 );
        initializeButton( mmssButtonStart );
        initializeButton( mmssButtonStop );

        initializeButton( presetButton1 );
        initializeButton( presetButton2 );
        initializeButton( presetButton3 );
        initializeButton( presetButton4 );
        initializeButton( presetButtonStart );
        initializeButton( presetButtonStop );

        setTextStopButtons("Clear");

        initializeButton( configureButton );
        initializeButton( aboutButton );

        mmssButton1.setToolTipText("add ten minutes to clock");
        mmssButton2.setToolTipText("add one minute to clock");
        mmssButton3.setToolTipText("add ten seconds to clock");
        mmssButton4.setToolTipText("add one second to clock");

        presetButton1.setToolTipText("preset button 1");
        presetButton2.setToolTipText("preset button 2");
        presetButton3.setToolTipText("preset button 3");
        presetButton4.setToolTipText("preset button 4");

        presetButton1.setName("preset1");
        presetButton2.setName("preset2");
        presetButton3.setName("preset3");
        presetButton4.setName("preset4");

        presetButton1.setText(settings.get("preset1").getName());
        presetButton2.setText(settings.get("preset2").getName());
        presetButton3.setText(settings.get("preset3").getName());
        presetButton4.setText(settings.get("preset4").getName());

        configureButton.setToolTipText("change discussion times, messages, presets, save/read settings, ...");
        aboutButton.setToolTipText("About this program, the author, the copyright, and license.");
    }

    // ----------------------------------------------------------------------

    private void doAboutButton() {
    	AboutDialog dialog = new AboutDialog(null);
    	dialog.setVisible(true);
    }

    /**
     * font sizes tracks with window size
     */
    private void doAdjustLabelSizes() {
        adjustLabelSize(mmssText);
        adjustLabelSize(msgText);
    }

    /**
     * adjust the size of one JLabel widget
     * @param label
     */
    private void adjustLabelSize(JLabel label) {
        Font font = label.getFont();
        String fontName = font.getFontName();
        int fontStyle = font.getStyle();
        Rectangle rect = label.getBounds();
        int height = rect.height * 4 / 10;	// judgment here
        if (height < 12) height = 12;  // but not too small
        Font newFont = new Font(fontName, fontStyle, height);
        label.setFont(newFont);
    }

    // ----------------------------------------------------------------------
    
    /**
     * @return the Ant build number from the ant build.num file
     */
    private int getBuildNumber() {
    	int buildNumber = -1;
    	InputStream in = getClass().getResourceAsStream("/build.num");
    	if (in != null) {
	    	InputStreamReader isr = new InputStreamReader(in);
	        BufferedReader br = new BufferedReader(isr);
	        try {
				br.readLine();
		        br.readLine();
		        String line = br.readLine();
		        buildNumber = new Integer(line.split("=")[1]);
			} catch (IOException e) {
				// ignore this error
			}
    	}

    	return buildNumber;
    }

	/**
	 * initialization routine
	 */
	private void defineInitialTalkConfigurations () {
    	settings = new HashMap<String, TalkConfiguration>();
        settings.put("basic", new TalkConfiguration());
        for (int i = 0; i < ConfigureDialog.NUMBER_OF_TABS; i++)
        	settings.put("preset" + (i+1), new TalkConfiguration());
	}
    
    /**
     * use some sensible default settings
     */
    private void overrideInitialTalkConfigurations() {
        TalkConfiguration talk = settings.get("basic");
		talk.setAudible(true);
		talk.setPresentation(15 * 60);
		talk.setDiscussion(5*60);
		talk.setOvertime(15);
		talk.setMsg_pretalk("coming up next");
		talk.setMsg_presentation("listen up");
		talk.setMsg_discussion("questions?");
		talk.setMsg_overtime("stop");
		talk.setMsg_paused("... waiting ...");
		talk.setName("5 minutes");

		for (int i = 0; i < ConfigureDialog.NUMBER_OF_TABS; i++) {
        	String key = "preset" + (i+1);
        	talk = settings.get(key);
        	talk.setPresentation((i+1)*5*60);
        	talk.setDiscussion((i+1)*60);
        	talk.setOvertime((i+1)*15);
        	talk.setName(talk.getPresentationStr() + " talk");
        	talk.setMsg_presentation(talk.getPresentationStr() + " talk");
        	talk.setMsg_pretalk(talk.getPresentationStr() + " talk is next");
        	talk.setMsg_discussion(talk.getDiscussionStr() + " period");
        	talk.setMsg_overtime(talk.getPresentationStr() + " is over");
        }
    }

    /**
     * Create the different color objects 
     * just once and re-use them as needed
     */
    private void initializeColorTable() {
    	colorTable = new HashMap<String, Color>();
    	colorTable.put("black", Color.black);
    	colorTable.put("white", Color.white);
    	colorTable.put("red", Color.red);
    	colorTable.put("yellow", Color.yellow);
    	colorTable.put("green", Color.green);
    	colorTable.put("lightblue", new Color(0xad, 0xd8, 0xe6));
    	colorTable.put("default", new Color(0, 0, 0xff));
    }
	
	/**
	 * choose a color by name
	 * @param colorName
	 */
    private void setColor(String colorName) {
		Color color = null;
		if (colorTable.containsKey(colorName)) {
			color = colorTable.get(colorName);
		} else {
			color = colorTable.get("default");
		}
		mmssText.setForeground(color);
		msgText.setForeground(color);
	}

    private void setTextStartButtons(String text) {
        if (!strEq(text, mmssButtonStart.getText()))
        	mmssButtonStart.setText(text);
        if (!strEq(text, presetButtonStart.getText()))
        	presetButtonStart.setText(text);
    }

    private void setTextStopButtons(String text) {
        if (!strEq(text, mmssButtonStop.getText()))
        	mmssButtonStop.setText(text);
        if (!strEq(text, presetButtonStop.getText()))
        	presetButtonStop.setText(text);
    }

    public void doButton(JButton button) {
    	String label = button.getName();
    	Container parent = button.getParent();
        if (parent == mmssTabPane) {
            if (strEq(label, mmssButton1.getName())) {talkTimer.incrementTime(10*60);}
            if (strEq(label, mmssButton2.getName())) {talkTimer.incrementTime(60);}
            if (strEq(label, mmssButton3.getName())) {talkTimer.incrementTime(10);}
            if (strEq(label, mmssButton4.getName())) {talkTimer.incrementTime(1);}
            if (strEq(label, mmssButtonStart.getName())) {doStartButton();}
            if (strEq(label, mmssButtonStop.getName())) {doStopButton();}
        }
        if (parent == presetTabPane) {
        	if (strEq(label, presetButton1.getName())) {doPresetButton(label);}
            if (strEq(label, presetButton2.getName())) {doPresetButton(label);}
            if (strEq(label, presetButton3.getName())) {doPresetButton(label);}
            if (strEq(label, presetButton4.getName())) {doPresetButton(label);}
            if (strEq(label, presetButtonStart.getName())) {doStartButton();}
            if (strEq(label, presetButtonStop.getName())) {doStopButton();}
        }
        if (parent == otherTabPane) {
            if (strEq(label, "Configure ...")) {doConfigureButton();}
            if (strEq(label, "About ...")) {doAboutButton();}
        }
    }

    /**
     * set the button text from the JButton name 
     * and bind it for response
     * @param button JButton object
     */
    private void initializeButton(JButton button) {
        String text = button.getText();
        button.setName(text);
        final JButton fButton = button;
        button.addActionListener( // bind a button click to this action
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doButton(fButton);
					}
				});
    }
	
	/**
	 * compare if two Strings are equal
	 * @param s1
	 * @param s2
	 * @return true if equal
	 */
	public static boolean strEq(String s1, String s2) {
		if (s1 == null) return false;
		if (s2 == null) return false;
		return (s1.compareTo(s2) == 0);
	}

    private void doStartButton() {
		if (!talkTimer.isCounting() && talkTimer.getClockTime()>0) {
			setTextStartButtons("Pause");
			setTextStopButtons("Stop");
        	ManageRcFile.setUserSettingsFile(userSettingsFile);
        	ManageRcFile.setSettings(settings);
        	ManageRcFile.writeRcFile();
			talkTimer.start();
		} else {
			talkTimer.pause();
			talkTimer.incrementTime(0);  // force a timer event?
			if (talkTimer.getClockTime()>0) {
				setTextStartButtons("Resume");
				setTextStopButtons("Clear");
			} else {
				doStopButton();	// pause overtime, same as full stop
			}
		}
    }

    private void doStopButton() {
		if (!talkTimer.isCounting()) {
			talkTimer.stop();
			talkTimer.clearCounter();
			setTextStartButtons("Start");
			setTextStopButtons("Clear");
		} else {
			talkTimer.stop();
			talkTimer.clearCounter();
			setTextStartButtons("Start");
			setTextStopButtons("Clear");
		}   
	}

    private void doConfigureButton() {
    	ConfigureDialog dialog = new ConfigureDialog(new javax.swing.JFrame());
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                //System.exit(0);
            }
        });
        // =========================================
        // pass known configurations to dialog
        dialog.setBasicSettings(settings.get("basic"));
        for (int i = 0; i < ConfigureDialog.NUMBER_OF_TABS; i++) {
        	String key = "preset" + (i+1);
        	dialog.setPresetSettings(i+1, settings.get(key));
        }
        dialog.setDefaultSettingsFile(defaultSettingsFile);
        dialog.setUserSettingsFile(userSettingsFile);
        // =========================================
        dialog.setVisible(true);
        // =========================================
        // get configurations from dialog
        switch (dialog.getButtonPressed()) {
		case ConfigureDialog.OK_BUTTON:
			settings.put("basic", dialog.getBasicSettings());
	        for (int i = 0; i < ConfigureDialog.NUMBER_OF_TABS; i++) {
	        	String key = "preset" + (i+1);
	        	settings.put(key, dialog.getPresetSettings(i+1));
	        }
	        // update the button labels
	        presetButton1.setText(settings.get("preset1").getName());
	        presetButton2.setText(settings.get("preset2").getName());
	        presetButton3.setText(settings.get("preset3").getName());
	        presetButton4.setText(settings.get("preset4").getName());
	        userSettingsFile = dialog.getUserSettingsFile();
        	ManageRcFile.setUserSettingsFile(userSettingsFile);
        	ManageRcFile.setSettings(settings);
        	ManageRcFile.writeRcFile();
			break;

		case ConfigureDialog.CANCEL_BUTTON:
			// System.out.println("<Cancel>");
			break;

		default:
			// System.out.println("other thing: " + dialog.getButtonPressed());
			break;
		}
        // =========================================
        dialog.dispose();        // finally
    }

    private void doPresetButton(String label) {
    	if (talkTimer.isPaused() || talkTimer.isCounting()) {
    		// do not install a new preset while talk is paused or running
    		smartSetText(msgText, "must [clear] first");
    	} else {
	    	// copy presets to basic settings
			settings.put("basic", settings.get(label).deepCopy());
			// and make a new timer object
			talkTimer.stop();	// stop the old one first
			talkTimer = new TalkTimer(this, settings.get("basic"));
    	}
    }

	/**
	 * This routine is called by TalkTimer.doTimerEvent() 
	 * in response to timer updates.
	 * @param mmss
	 * @param msgTextStr
	 * @param numBeeps
	 * @param color
	 */
    public void doDisplayCallback(
			double time,
    		String mmss,
			String msgTextStr, 
			int numBeeps, 
			String color) {

		smartSetText(mmssText, mmss);
		if (smartSetText(msgText, msgTextStr))
			setColor(color);
		soundBeeps(numBeeps);

		if (time < 0)
			setTextStopButtons("stop");
	}
	
	/**
	 * sound an alert at key transitions in the talk
	 * @param numBeeps
	 */
	private void soundBeeps(int numBeeps) {
		for (int i = 0; i < numBeeps; i++) {
			try {
				Thread.sleep(500);  // intervals between beeps
			} catch (InterruptedException e) {
				// ignore this possibility, per
				// http://download.oracle.com/javase/tutorial/essential/concurrency/sleep.html
			}
			beep();
		}
	}

    /**
     * local name for the audible annunciator
     */
    private void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

	/**
	 * only set the widget text if the new text is different
	 * @param widget JLabel object
	 * @param text new text
	 * @return true if text was changed
	 */
	private boolean smartSetText(JLabel widget, String text) {
		boolean result = false;
		if (!strEq(widget.getText(), text)) {
			result = true;
			widget.setText(text);
		}
		return result;
	}

	/**
	 * initialization routine
	 */
	private void setDefaultFiles() {
        String dir = System.getProperty("user.home");
        String delim = System.getProperty("file.separator");
        defaultSettingsFile = "{not defined yet}";
        defaultSettingsFile = dir + delim + RC_FILE;
        userSettingsFile = "{not defined yet}";
        boolean exists = new File(defaultSettingsFile).exists();
        if (!exists) {
        	ManageRcFile.setUserSettingsFile(userSettingsFile);
        	ManageRcFile.setSettings(settings);
        	ManageRcFile.setRC_FILE(defaultSettingsFile);
        	ManageRcFile.writeRcFile();
        }
	}

	/**
	 * initialization routine
	 */
	private void restoreSettings() {
        ManageRcFile.setRC_FILE(defaultSettingsFile);
        userSettingsFile = ManageRcFile.getUserSettingsFile();
        HashMap<String, TalkConfiguration> tempSettings;
        tempSettings = ManageRcFile.getSettings();
        if (tempSettings != null)
        	settings = tempSettings;
	}

	/**
	 * initialization routine
	 */
	private void setTitle() {
        ConfigFile cfg = ConfigFile.getInstance();
        int buildNumber = getBuildNumber();
        // WONTFIX can we get most recent revision number from project directory?
        // String svnRev = "$Revision$".split(" ")[1];
        String format = String.format("%s, (%s) <%s>", 
        		cfg.getName(), "build:%d", cfg.getEmail());
        String title = String.format(format, buildNumber);
        this.setTitle(title);
	}

	// ----------------------------------------------------------------------

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CountdownJ().setVisible(true);
            }
        });
    }

    // ----------------------------------------------------------------------

    // declare the GUI widgets (alphabetical order by widget)
    private JButton aboutButton;
    private JButton configureButton;
    private JButton mmssButton1;
    private JButton mmssButton2;
    private JButton mmssButton3;
    private JButton mmssButton4;
    private JButton mmssButtonStart;
    private JButton mmssButtonStop;
    private JButton presetButton1;
    private JButton presetButton2;
    private JButton presetButton3;
    private JButton presetButton4;
    private JButton presetButtonStart;
    private JButton presetButtonStop;
    private JLabel mmssText;
    private JLabel msgText;
    private JPanel blackPanel;
    private JPanel mmssTabPane;
    private JPanel mmssTextPanel;
    private JPanel msgTextPanel;
    private JPanel otherTabPane;
    private JPanel presetTabPane;
    private JTabbedPane tabbedPane;

    // class variables
	private TalkTimer talkTimer;
	private HashMap<String, Color> colorTable;
	private HashMap<String, TalkConfiguration> settings;
	private static String defaultSettingsFile;
	private static String userSettingsFile;
	private static final String RC_FILE = ".countdownjrc";

	private static final long serialVersionUID = 1744010558421662336L;
}

