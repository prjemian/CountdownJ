package org.jemian.countdownj.Swing;

//TODO needs copyright and license header

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Pete
 */
public class Configure extends javax.swing.JDialog {

	// @see http://download.oracle.com/javase/tutorial/uiswing/components/dialog.html
	
	/** Creates new form Configure */
    public Configure(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        buttonPressed = NO_BUTTON_PRESSED;
        settings = new HashMap<String, TalkConfiguration>();
        panel = new HashMap<String, ConfigurePanel>();
        create();
        setTalkDefaults();
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    }

    /**
     * create the dialog's widgets and layouts
     */
    private void create() {
    	setLayout(new GridBagLayout());

    	// + + + + + + + + + + + + + + + + + + + + + + + +
    	// main layout

    	JLabel title = new JLabel("Configuration details");
    	title.setFont(new Font("Tahoma", Font.BOLD, 24));
    	title.setAlignmentX(CENTER_ALIGNMENT);
    	add(title, makeConstraints(0, 0, 1, 0, 1, 1));

    	JTabbedPane mainTabs = new JTabbedPane();
    	add(mainTabs, makeConstraints(0, 1, 1, 0, 1, 1));
    	
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
    	add(buttonPanel, makeConstraints(0, 2, 1, 0, 1, 1));

    	// + + + + + + + + + + + + + + + + + + + + + + + +
    	// main button panel
    	
		buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.setLayout(new FlowLayout());
		JButton btnOk = new JButton("Ok");
		buttonPanel.add(btnOk);
		getRootPane().setDefaultButton(btnOk);
		btnOk.addActionListener( // bind a button click to this action
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doOkAction();
					}
				});

		JButton btnCancel = new JButton("Cancel");
		buttonPanel.add(btnCancel);
		btnCancel.addActionListener( // bind a button click to this action
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doCancelAction();
					}
				});

    	// + + + + + + + + + + + + + + + + + + + + + + + +
    	// main tabs

		JPanel basicTab = new JPanel();
    	basicTab.setName("Basic");
    	basicTab.setAlignmentX(CENTER_ALIGNMENT);
    	mainTabs.add(basicTab);
    	basicTab.setLayout(new GridBagLayout());
    	basicTab.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                basicTab.getBorder()));
    	panel.put("basic", new ConfigurePanel(basicTab, false));

    	JPanel presetsTab = new JPanel();
    	presetsTab.setName("Presets");
    	presetsTab.setAlignmentX(CENTER_ALIGNMENT);
    	presetsTab.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                presetsTab.getBorder()));
    	mainTabs.add(presetsTab);

    	JPanel aboutBoxPanel = new JPanel();
    	aboutBoxPanel.setName("About");
    	aboutBoxPanel.setAlignmentX(CENTER_ALIGNMENT);
    	aboutBoxPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                aboutBoxPanel.getBorder()));
    	mainTabs.add(aboutBoxPanel);

    	// + + + + + + + + + + + + + + + + + + + + + + + +
    	// presets tab panel
    	
    	presetsTab.setLayout(new GridBagLayout());

    	JTabbedPane subtabs = new JTabbedPane();
    	subtabs.setAlignmentX(CENTER_ALIGNMENT);
    	subtabs.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                subtabs.getBorder()));
    	presetsTab.add(subtabs, makeConstraints(0, 0, 1, 0, 1, 1));

    	for (int i = 0; i < NUMBER_OF_TABS; i++) {
    		String name = getPresetTabKey(i+1);
        	JPanel tab = new JPanel();
        	if (settings.get(name) != null)
        		tab.setName(settings.get(name).getName());
        	else
            	tab.setName(name);
        	tab.setLayout(new GridBagLayout());
        	subtabs.add(tab);

        	// This part of the GUI is constructed irregularly.
        	// The bottom ConfigurePanel comes first since it creates the panel
        	ConfigurePanel tabPanel = new ConfigurePanel(tab, true);
        	panel.put(name, tabPanel);
    	}

    	// + + + + + + + + + + + + + + + + + + + + + + + +
    	// About Box

    	int row = 0;
    	aboutBoxPanel.setLayout(new GridBagLayout());
    	JLabel text = new JLabel("CountdownJ");
    	text.setFont(new Font("Tahoma", Font.BOLD, 32));
    	text.setAlignmentX(CENTER_ALIGNMENT);
    	aboutBoxPanel.add(text, makeConstraints(0, row++, 1.0, 0.0, 1, 1));

    	text = new JLabel("by Pete R. Jemian");
    	text.setFont(new Font("Tahoma", Font.PLAIN, 16));
    	aboutBoxPanel.add(text, makeConstraints(0, row++, 1.0, 0.0, 1, 1));

    	text = new JLabel("<prjemian@gmail.com>");
    	text.setFont(new Font("Courier", Font.PLAIN, 12));
    	aboutBoxPanel.add(text, makeConstraints(0, row++, 1.0, 0.0, 1, 1));

    	text = new JLabel(" ");
    	text.setFont(new Font("Tahoma", Font.PLAIN, 12));
    	aboutBoxPanel.add(text, makeConstraints(0, row++, 1.0, 0.0, 1, 1));

    	text = new JLabel(svnId);
    	text.setFont(new Font("Courier", Font.PLAIN, 12));
    	aboutBoxPanel.add(text, makeConstraints(0, row++, 1.0, 0.0, 1, 1));

    	text = new JLabel(" ");
    	text.setFont(new Font("Tahoma", Font.PLAIN, 12));
    	aboutBoxPanel.add(text, makeConstraints(0, row++, 1.0, 0.0, 1, 1));
    	
    	text = new JLabel("Software license");
    	text.setFont(new Font("Tahoma", Font.PLAIN, 12));
    	aboutBoxPanel.add(text, makeConstraints(0, row++, 1.0, 0.0, 1, 1));

    	String license_text = "";
    	try {
			license_text = readFile("LICENSE");
		} catch (IOException e1) {
			// backup license text if LICENSE cannot be found
	    	license_text = "ConfigureJ - a timer for conference presentations\n" +
	    			"Copyright (c) 2010 - Pete R. Jemian\n" +
	    			"\n" +
	    			"See the LICENSE file included in the " +
	    			"distribution for full details.";
		}
    	TextArea area = new TextArea(license_text);
    	area.setEditable(false);
    	aboutBoxPanel.add(area, makeConstraints(0, row++, 1.0, 1.0, 1, 1));

    	// + + + + + + + + + + + + + + + + + + + + + + + +

    	pack();
    }

    /**
     * initially, assign defaults for each talk's settings
     */
    private void setTalkDefaults() {
    	setBasicSettings(new TalkConfiguration());
    	for (int i = 0; i < NUMBER_OF_TABS; i++)
    		setPresetSettings(i+1, new TalkConfiguration());
    }
    
    /**
     * Read a file into a string
     * @param filename
     * @return
     * @throws IOException
     */
    private String readFile(String filename) throws IOException {
    	BufferedReader in = new BufferedReader(new FileReader(filename));
    	StringBuffer stringBuffer = new StringBuffer("");
        //read file into a string
    	String input = "";
    	while ((input = in.readLine()) != null)
    		stringBuffer.append(input + "\n");
    	in.close();
    	return stringBuffer.toString();
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
    	c.insets = new Insets(2, 4, 2, 8);
    	return c;
    }
    
    /**
     * @param index
     * @return
     */
    private String getPresetTabKey(int index) {
    	String key = null;
    	if (1 <= index && index <= NUMBER_OF_TABS)
    		key = "preset" + index;
    	return key;
    }
    
    private void doOkAction() {
        buttonPressed = OK_BUTTON;
        setVisible(false);
    }
    
    private void doCancelAction() {
        buttonPressed = CANCEL_BUTTON;
        setVisible(false);
    }
	
	private TalkConfiguration getSettingsByKey(String key) {
		TalkConfiguration talk;
		if (settings.containsKey(key))
			talk = settings.get(key);
		else
			talk = null;
		return talk;
	}

    /**
	 * @return the settings of the basic panel
	 */
	public TalkConfiguration getBasicSettings() {
		// copy widget values to local HashMap
		settings.put("basic", panel.get("basic").getConfig());
		return getSettingsByKey("basic");
	}

	/**
	 * @param index index number [1..NUMBER_OF_TABS]
	 * @param talk ConfigurePanel object
	 */
	public void setBasicSettings(TalkConfiguration talk) {
		settings.put("basic", talk);
		panel.get("basic").setConfig(talk, true);	// set the widgets, too
	}

    /**
	 * @param index index number [1..NUMBER_OF_TABS]
	 * @return the settings of the named key or null
	 */
	public TalkConfiguration getPresetSettings(int index) {
		String key = getPresetTabKey(index);
		if (key != null) {
			// copy widget values to local HashMap
			settings.put(key, panel.get(key).getConfig());
		}
		return getSettingsByKey(key);
	}

	/**
	 * @param index index number [1..NUMBER_OF_TABS]
	 * @param talk ConfigurePanel object
	 */
	public void setPresetSettings(int index, TalkConfiguration talk) {
		String key = getPresetTabKey(index);
		if (key != null) {
			settings.put(key, talk);
			panel.get(key).setConfig(talk, true);	// set the widgets, too
		}
	}

	/**
	 * @return the buttonPressed
	 */
	public int getButtonPressed() {
		return buttonPressed;
	}

	/**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	TalkConfiguration talk;
            	Configure dialog = new Configure(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                // =========================================
                // set the Configurations
                talk = new TalkConfiguration();
                talk.setDiscussion(3*60);
                talk.setOvertime(15);
                dialog.setBasicSettings(talk);
                for (int i = 0; i < Configure.NUMBER_OF_TABS; i++)
                	dialog.setPresetSettings(i+1, new TalkConfiguration());
                // =========================================
                dialog.setVisible(true);	// run the dialog and wait ...
                // =========================================
                int pressed = dialog.getButtonPressed();
                System.out.println("pressed: " + pressed);
                if (pressed == Configure.OK_BUTTON) {
	                talk = dialog.getBasicSettings();
	                String str = String.format("<talk id=\"basic\" %s />", talk);
                    System.out.println(str);
	                for (int i = 0; i < Configure.NUMBER_OF_TABS; i++) {
	                	talk = dialog.getPresetSettings(i+1);
	                	str = String.format("<talk id=\"preset%s\" %s />", i+1, talk);
	                    System.out.println(str);
	                }
                }
                dialog.dispose();
                System.exit(0);
            }
        });
    }

	private static final long serialVersionUID = -6246475747169907480L;

	private HashMap<String, TalkConfiguration> settings;
	private HashMap<String, ConfigurePanel> panel;
	private int buttonPressed;

	public static final int NUMBER_OF_TABS = 4;
	public static final int OK_BUTTON = 0;
	public static final int NO_BUTTON_PRESSED = 1;
	public static final int CANCEL_BUTTON = 2;

	//private String svnRev = "$Revision$";
	private String svnId = "$Id$";
}
