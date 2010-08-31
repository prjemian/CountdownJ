/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Configure.java
 *
 * Created on Aug 24, 2010, 10:14:35 AM
 */

package org.jemian.countdownj.Swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

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
        settings = new Hashtable<String, ConfigurePanel>();
        initialize();
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

    }

    private void initialize() {
    	Container pane = getContentPane();
    	BoxLayout layout = new BoxLayout(pane, BoxLayout.PAGE_AXIS);
    	this.setLayout(layout);

    	JLabel title = new JLabel("Configuration details (not yet ready)");
    	title.setFont(new Font("Tahoma", Font.BOLD, 24));
    	title.setAlignmentX(CENTER_ALIGNMENT);
    	this.add(title);

    	JTabbedPane mainTabs = new JTabbedPane();
    	//mainTabs.setAlignmentX(CENTER_ALIGNMENT);
    	this.add(mainTabs);

    	JPanel basicTab = new JPanel();
    	basicTab.setName("Basic");
    	basicTab.setAlignmentX(CENTER_ALIGNMENT);
    	mainTabs.add(basicTab);
    	ConfigurePanel panel = new ConfigurePanel(basicTab);
    	settings.put("basic", panel);

    	JPanel presetsTab = new JPanel();
    	presetsTab.setName("Presets");
    	presetsTab.setAlignmentX(LEFT_ALIGNMENT);
    	presetsTab.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GREEN),
                presetsTab.getBorder()));
    	mainTabs.add(presetsTab);
    	// TODO Use a different layout container to control subitem width
    	presetsTab.setLayout(new FlowLayout(FlowLayout.CENTER));

    	JTabbedPane subtabs = new JTabbedPane();
    	subtabs.setAlignmentX(CENTER_ALIGNMENT);
    	subtabs.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                subtabs.getBorder()));
    	presetsTab.add(subtabs);

    	for (int i = 0; i < NUMBER_OF_TABS; i++) {
    		String name = getPresetTabKey(i+1);
        	JPanel tab = new JPanel();
        	tab.setName(name);
        	subtabs.add(tab);
        	ConfigurePanel tabPanel = new ConfigurePanel(tab);
        	settings.put(name, tabPanel);
        	// TODO entry widget not same as others
        	JTextField tabName = tabPanel.label_entry(tab, 0, 
        			"tab name", 
        			"title of this page of settings",
        			name, 
        			"suggested: Invited or Plenary or Contributed or ...");
        	// FIXME need to set the entry fields from current values
        	GridBagConstraints c = new GridBagConstraints();
        	c.fill = GridBagConstraints.BOTH;
        	c.gridx = 0;
        	c.gridwidth = 2;
        	c.gridy = 0;
        	c.insets = new Insets(4, 4, 4, 4);
        	tab.add(tabName);
        	tabPanel.separator(tab, 1);
    	}
    	
    	// TODO Put the About box as the next tab
    	JPanel aboutBoxPanel = new JPanel();
    	aboutBoxPanel.setName("About");
    	aboutBoxPanel.setAlignmentX(CENTER_ALIGNMENT);
    	mainTabs.add(aboutBoxPanel);
    	
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
    	this.add(buttonPanel);
    	buttonPanel.setLayout(new FlowLayout());
    	JButton btnOk = new JButton("Ok");
    	buttonPanel.add(btnOk);
    	getRootPane().setDefaultButton(btnOk);
    	btnOk.addActionListener(	// bind a button click to this action
        		new ActionListener() {
        		    public void actionPerformed(ActionEvent e) {
        		    	doOkAction();
        		    }
        		}
        	);
    	JButton btnCancel = new JButton("Cancel");
    	buttonPanel.add(btnCancel);
    	btnCancel.addActionListener(	// bind a button click to this action
        		new ActionListener() {
        		    public void actionPerformed(ActionEvent e) {
        		    	doCancelAction();
        		    }
        		}
        	);
    	
    	pack();
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
        this.setVisible(false);
    }
    
    private void doCancelAction() {
        buttonPressed = CANCEL_BUTTON;
        this.setVisible(false);
    }
	
	private ConfigurePanel getSettingsByKey(String key) {
		ConfigurePanel result;
		if (settings.containsKey(key))
			result = settings.get(key);
		else
			result = null;
		return result;
	}

    /**
	 * @return the settings of the basic panel
	 */
	public ConfigurePanel getBasicSettings() {
		return getSettingsByKey("basic");
	}

    /**
	 * @param index index number [1..NUMBER_OF_TABS]
	 * @return the settings of the named key or null
	 */
	public ConfigurePanel getPresetSettings(int index) {
		return getSettingsByKey(getPresetTabKey(index));
	}

	/**
	 * @param index index number [1..NUMBER_OF_TABS]
	 * @param value ConfigurePanel object
	 */
	public void setSettings(int index, ConfigurePanel value) {
		String key = getPresetTabKey(index);
		if (key != null)
			this.settings.put(key, value);
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
                Configure dialog = new Configure(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
                int pressed = dialog.getButtonPressed();
                System.out.println("pressed: " + pressed);
                if (pressed == Configure.OK_BUTTON) {
	                TalkConfiguration talk = dialog.getBasicSettings().getConfig();
	                System.out.println("basic talk:\n" + talk);
	                for (int i = 0; i < Configure.NUMBER_OF_TABS; i++) {
	                	talk = dialog.getPresetSettings(i+1).getConfig();
	                    System.out.println("preset " + (i+1) + " talk:\n" + talk);
	                }
                }
                dialog.dispose();
                System.exit(0);
            }
        });
    }

	private static final long serialVersionUID = -6246475747169907480L;

	private Hashtable<String, ConfigurePanel> settings;
	public final static int NUMBER_OF_TABS = 4;
	private int buttonPressed;
	public static final int NO_BUTTON_PRESSED = 0;
	public static final int OK_BUTTON = 1;
	public static final int CANCEL_BUTTON = 2;
}
