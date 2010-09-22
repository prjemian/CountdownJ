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
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * TODO read/write configuration as XML
 * TODO make GUI to read/write configuration files
 * TODO build code to validate XML files
 * TODO add code to read/write default/working configuration as XML
 */

/**
 *
 * @author Pete
 */
public class GuiSwing extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3167378700428228696L;

	private TalkTimer talkTimer;
	private HashMap<String, Color> colorTable;
	private HashMap<String, TalkConfiguration> settings;
	private static String defaultSettingsFile;
	private static String userSettingsFile;
	private static final String RC_FILE = ".countdownjrc";


    /**
     * creates ConfigureJ application main display
     */
    public GuiSwing() {
    	// define the TalkConfigurations
    	settings = new HashMap<String, TalkConfiguration>();
        settings.put("basic", new TalkConfiguration());
        for (int i = 0; i < ConfigureDialog.NUMBER_OF_TABS; i++)
        	settings.put("preset" + (i+1), new TalkConfiguration());
        overrideInitialTalkConfigurations();
        
        // default files
        String dir = System.getProperty("user.home");
        String delim = System.getProperty("file.separator");
        defaultSettingsFile = "{not defined yet}";
        defaultSettingsFile = dir + delim + RC_FILE;
        userSettingsFile = "{not defined yet}";

		// setup the GUI
    	initializeColorTable();
        initComponents();
        ConfigFile cfg = ConfigFile.getInstance();
        // TODO can we get most recent revision number from project directory?
        String svnRev = "$Revision$".split(" ")[1];
        String format = String.format("%s, (%s) <%s>", 
        		cfg.getName(), "svn:%s", cfg.getEmail());
        String title = String.format(format, svnRev);
        this.setTitle(title);
        initializeButtonLabels();

        // font sizes track with window size
        blackPanel.addHierarchyBoundsListener(
				new HierarchyBoundsListener() {
				    public void ancestorResized(HierarchyEvent e) {
				        doAdjustLabelSizes();
				    }
				    public void ancestorMoved(HierarchyEvent e) {}
				});


    	talkTimer = new TalkTimer(this, settings.get("basic"));
        setExtendedState(MAXIMIZED_BOTH);     // full screen
    }
    
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
    	colorTable.put("black", new Color(0, 0, 0));
    	colorTable.put("white", new Color(0xff, 0xff, 0xff));
    	colorTable.put("red", new Color(0xff, 0, 0));
    	colorTable.put("yellow", new Color(0xff, 0xff, 0));
    	colorTable.put("green", new Color(0, 0xff, 0));
    	colorTable.put("lightblue", new Color(0xad, 0xd8, 0xe6));
    	colorTable.put("default", new Color(0, 0, 0xff));
    }

    /**
     * set the button text from the JButton name 
     * and bind it for response
     * @param button JButton object
     */
    private void initializeButton(JButton button) {
        String text = button.getName();
        button.setText(text);
        final JButton fButton = button;
        button.addActionListener( // bind a button click to this action
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doButton(fButton);
					}
				});
    }

    /**
     * GUI was setup with NetBeans in an untouchable code block below.
     * Now adjust things to our desires.
     */
    private void initializeButtonLabels() {
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

        setTextStopButtons("clear");

        initializeButton( configureButton );

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
    }

    private void setTextStartButtons(String text) {
        if (text.compareTo(mmssButtonStart.getText()) != 0)
        	mmssButtonStart.setText(text);
        if (text.compareTo(presetButtonStart.getText()) != 0)
        	presetButtonStart.setText(text);
    }

    private void setTextStopButtons(String text) {
        if (text.compareTo(mmssButtonStop.getText()) != 0)
        	mmssButtonStop.setText(text);
        if (text.compareTo(presetButtonStop.getText()) != 0)
        	presetButtonStop.setText(text);
    }

    public void doButton(JButton button) {
    	String label = button.getName();
    	Container parent = button.getParent();
        if (parent == mmssTabPane) {
            if (label.compareTo(mmssButton1.getName()) == 0) {incrementTime(10*60);}
            if (label.compareTo(mmssButton2.getName()) == 0) {incrementTime(60);}
            if (label.compareTo(mmssButton3.getName()) == 0) {incrementTime(10);}
            if (label.compareTo(mmssButton4.getName()) == 0) {incrementTime(1);}
            if (label.compareTo(mmssButtonStart.getName()) == 0) {doStartButton();}
            if (label.compareTo(mmssButtonStop.getName()) == 0) {doStopButton();}
        }
        if (parent == presetTabPane) {
        	if (label.compareTo(presetButton1.getName()) == 0) {doPresetButton(label);}
            if (label.compareTo(presetButton2.getName()) == 0) {doPresetButton(label);}
            if (label.compareTo(presetButton3.getName()) == 0) {doPresetButton(label);}
            if (label.compareTo(presetButton4.getName()) == 0) {doPresetButton(label);}
            if (label.compareTo(presetButtonStart.getName()) == 0) {doStartButton();}
            if (label.compareTo(presetButtonStop.getName()) == 0) {doStopButton();}
        }
        if (parent == tabbedPane) {
            if (label.compareTo("Configure") == 0) {doConfigureButton();}
        }
    }

    private void incrementTime(int seconds) {
        talkTimer.incrementTime(seconds);
    }

    private void doStartButton() {
		if (!talkTimer.isCounting() && talkTimer.getClockTime()>0) {
			talkTimer.start();
			setTextStartButtons("pause");
			setTextStopButtons("stop");
		} else {
			talkTimer.pause();
			talkTimer.incrementTime(0);  // force a timer event?
			if (talkTimer.getClockTime()>0) {
				setTextStartButtons("resume");
				setTextStopButtons("clear");
			} else {
				// pause while in overtime, same as a full stop
				doStopButton();
			}
		}
    }

    private void doStopButton() {
		if (!talkTimer.isCounting()) {
			talkTimer.stop();
			talkTimer.clearCounter();
			setTextStartButtons("start");
			setTextStopButtons("clear");
		} else {
			talkTimer.stop();
			talkTimer.clearCounter();
			setTextStartButtons("start");
			setTextStopButtons("clear");
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

    public void doAdjustLabelSizes() {
        adjustLabelSize(mmssText);
        adjustLabelSize(msgText);
    }

    public void adjustLabelSize(JLabel label) {
        Font font = label.getFont();
        String fontName = font.getFontName();
        int fontStyle = font.getStyle();
        Rectangle rect = label.getBounds();
        int height = rect.height * 4 / 10;
        if (height < 12) height = 12;  // but not too small
        Font newFont = new Font(fontName, fontStyle, height);
        label.setFont(newFont);
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
		if (widget.getText().compareTo(text) != 0) {
			result = true;
			widget.setText(text);
		}
		return result;
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        blackPanel = new javax.swing.JPanel();
        mmssTextPanel = new javax.swing.JPanel();
        mmssText = new javax.swing.JLabel();
        msgTextPanel = new javax.swing.JPanel();
        msgText = new javax.swing.JLabel();
        tabbedPane = new javax.swing.JTabbedPane();
        mmssTabPane = new javax.swing.JPanel();
        mmssButton1 = new javax.swing.JButton();
        mmssButton2 = new javax.swing.JButton();
        mmssButton3 = new javax.swing.JButton();
        mmssButton4 = new javax.swing.JButton();
        mmssButtonStart = new javax.swing.JButton();
        mmssButtonStop = new javax.swing.JButton();
        presetTabPane = new javax.swing.JPanel();
        presetButton1 = new javax.swing.JButton();
        presetButton2 = new javax.swing.JButton();
        presetButton3 = new javax.swing.JButton();
        presetButton4 = new javax.swing.JButton();
        presetButtonStart = new javax.swing.JButton();
        presetButtonStop = new javax.swing.JButton();
        configureButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        blackPanel.setBackground(new java.awt.Color(0, 0, 0));

        mmssTextPanel.setBackground(new java.awt.Color(0, 0, 0));
        mmssTextPanel.setForeground(new java.awt.Color(255, 255, 255));

        mmssText.setBackground(new java.awt.Color(0, 0, 0));
        mmssText.setFont(new java.awt.Font("Tahoma", 1, 48));
        mmssText.setForeground(new java.awt.Color(255, 255, 255));
        mmssText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mmssText.setText("mm:ss");

        javax.swing.GroupLayout mmssTextPanelLayout = new javax.swing.GroupLayout(mmssTextPanel);
        mmssTextPanel.setLayout(mmssTextPanelLayout);
        mmssTextPanelLayout.setHorizontalGroup(
            mmssTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mmssText, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
        );
        mmssTextPanelLayout.setVerticalGroup(
            mmssTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mmssText, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        );

        msgTextPanel.setBackground(new java.awt.Color(0, 0, 0));
        msgTextPanel.setForeground(new java.awt.Color(255, 255, 255));

        msgText.setBackground(new java.awt.Color(0, 0, 0));
        msgText.setFont(new java.awt.Font("Tahoma", 0, 24));
        msgText.setForeground(new java.awt.Color(255, 255, 255));
        msgText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        msgText.setText("message");

        javax.swing.GroupLayout msgTextPanelLayout = new javax.swing.GroupLayout(msgTextPanel);
        msgTextPanel.setLayout(msgTextPanelLayout);
        msgTextPanelLayout.setHorizontalGroup(
            msgTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(msgText, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
        );
        msgTextPanelLayout.setVerticalGroup(
            msgTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(msgText, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blackPanelLayout = new javax.swing.GroupLayout(blackPanel);
        blackPanel.setLayout(blackPanelLayout);
        blackPanelLayout.setHorizontalGroup(
            blackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mmssTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(msgTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blackPanelLayout.setVerticalGroup(
            blackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, blackPanelLayout.createSequentialGroup()
                .addComponent(mmssTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msgTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.setToolTipText("");

        mmssButton1.setText("jButton1-1");
        mmssButton1.setName("+10:00"); // NOI18N

        mmssButton2.setText("jButton1-2");
        mmssButton2.setName("+1:00"); // NOI18N

        mmssButton3.setText("jButton1-3");
        mmssButton3.setName("+0:10"); // NOI18N

        mmssButton4.setText("jButton1-4");
        mmssButton4.setName("+0:01"); // NOI18N

        mmssButtonStart.setText("jButton1-5");
        mmssButtonStart.setName("start"); // NOI18N

        mmssButtonStop.setText("jButton1-6");
        mmssButtonStop.setName("stop"); // NOI18N

        javax.swing.GroupLayout mmssTabPaneLayout = new javax.swing.GroupLayout(mmssTabPane);
        mmssTabPane.setLayout(mmssTabPaneLayout);
        mmssTabPaneLayout.setHorizontalGroup(
            mmssTabPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mmssTabPaneLayout.createSequentialGroup()
                .addComponent(mmssButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mmssButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mmssButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mmssButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(mmssButtonStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mmssButtonStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mmssTabPaneLayout.setVerticalGroup(
            mmssTabPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mmssTabPaneLayout.createSequentialGroup()
                .addGroup(mmssTabPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mmssButton1)
                    .addComponent(mmssButton2)
                    .addComponent(mmssButton3)
                    .addComponent(mmssButton4)
                    .addComponent(mmssButtonStop)
                    .addComponent(mmssButtonStart))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Basic", mmssTabPane);

        presetButton1.setText("jButton2-1");
        presetButton1.setName("< A >"); // NOI18N

        presetButton2.setText("jButton2-2");
        presetButton2.setName("< B >"); // NOI18N

        presetButton3.setText("jButton2-3");
        presetButton3.setName("< C >"); // NOI18N

        presetButton4.setText("jButton2-4");
        presetButton4.setName("< D >"); // NOI18N

        presetButtonStart.setText("jButton2-5");
        presetButtonStart.setName("start"); // NOI18N

        presetButtonStop.setText("jButton2-6");
        presetButtonStop.setName("stop"); // NOI18N

        javax.swing.GroupLayout presetTabPaneLayout = new javax.swing.GroupLayout(presetTabPane);
        presetTabPane.setLayout(presetTabPaneLayout);
        presetTabPaneLayout.setHorizontalGroup(
            presetTabPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(presetTabPaneLayout.createSequentialGroup()
                .addComponent(presetButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(presetButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(presetButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(presetButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(presetButtonStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(presetButtonStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        presetTabPaneLayout.setVerticalGroup(
            presetTabPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(presetTabPaneLayout.createSequentialGroup()
                .addGroup(presetTabPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(presetButton1)
                    .addComponent(presetButton2)
                    .addComponent(presetButton3)
                    .addComponent(presetButton4)
                    .addComponent(presetButtonStop)
                    .addComponent(presetButtonStart))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Presets", presetTabPane);

        configureButton.setText("Configure");
        configureButton.setToolTipText("change discussion time, overtime reminders, presets, and messages");
        configureButton.setName("Configure"); // NOI18N
        tabbedPane.addTab("Other", configureButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
            .addComponent(blackPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(blackPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiSwing().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel blackPanel;
    private javax.swing.JButton configureButton;
    private javax.swing.JButton mmssButton1;
    private javax.swing.JButton mmssButton2;
    private javax.swing.JButton mmssButton3;
    private javax.swing.JButton mmssButton4;
    private javax.swing.JButton mmssButtonStart;
    private javax.swing.JButton mmssButtonStop;
    private javax.swing.JPanel mmssTabPane;
    private javax.swing.JLabel mmssText;
    private javax.swing.JPanel mmssTextPanel;
    private javax.swing.JLabel msgText;
    private javax.swing.JPanel msgTextPanel;
    private javax.swing.JButton presetButton1;
    private javax.swing.JButton presetButton2;
    private javax.swing.JButton presetButton3;
    private javax.swing.JButton presetButton4;
    private javax.swing.JButton presetButtonStart;
    private javax.swing.JButton presetButtonStop;
    private javax.swing.JPanel presetTabPane;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

}

