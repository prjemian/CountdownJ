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

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 *
 * @author Pete
 */
public class Configure extends javax.swing.JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6246475747169907480L;

	/** Creates new form Configure */
    public Configure(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        // initComponents();
        initialize();
    }

    private void initialize() {
    	Container pane = getContentPane();
    	this.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
    	//JPanel mainPanel = new JPanel();

    	JLabel title = new JLabel("Configuration details (not yet ready)");
    	title.setFont(new Font("Tahoma", Font.BOLD, 24));
    	title.setAlignmentX(CENTER_ALIGNMENT);
    	this.add(title);

    	// TODO this inside a JTabbedPane
    	// mainTabbedPane = new javax.swing.JTabbedPane();
    	JTabbedPane tabs = new JTabbedPane();
    	this.add(tabs);

    	JPanel mmssTab = new JPanel();
    	mmssTab.setName("basic");
    	tabs.add(mmssTab);
    	ConfigurePanel panel = new ConfigurePanel(mmssTab);

    	JPanel presetsTab = new JPanel();
    	presetsTab.setName("presets");
    	tabs.add(presetsTab);

    	JTabbedPane subtabs = new JTabbedPane();
    	presetsTab.add(subtabs);

    	for (int i = 0; i < 4; i++) {
        	JPanel tab = new JPanel();
        	tab.setName("tab " + i);
        	subtabs.add(tab);
        	ConfigurePanel tabPanel = new ConfigurePanel(tab);
        	// TODO entry widget not same as others, move into ConfigurePanel
        	// TODO ConfigurePanel needs optional widgets like this one
        	JTextField tabName = tabPanel.label_entry(tab, 0, 
        			"tab name", 
        			"title of this page of settings",
        			"{tab name}", 
        			"suggested: Invited or Plenary or Contributed or ...");
        	tab.add(tabName);
        	tabPanel.separator(tab, 1);
    	}
    	
    	// TODO make these buttons work
    	JPanel buttonPanel = new JPanel();
    	this.add(buttonPanel);
    	buttonPanel.setLayout(new FlowLayout());
    	buttonPanel.add(new JButton("Ok"));
    	buttonPanel.add(new JButton("Cancel"));
    	buttonPanel.add(new JButton("Accept"));
    	
    	pack();
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
            }
        });
    }

}
