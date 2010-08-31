package org.jemian.countdownj.Swing;

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
import java.util.HashMap;

import javax.swing.BorderFactory;
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
        settings = new HashMap<String, ConfigurePanel>();
        create();
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
    	ConfigurePanel panel = new ConfigurePanel(basicTab);
    	settings.put("basic", panel);

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
        	tab.setName(name);	// FIXME pick the supplied name
        	tab.setLayout(new GridBagLayout());
        	subtabs.add(tab);

        	// This part of the GUI is constructed irregularly.
        	// The bootom ConfigurePanel comes first since it creates the panel
        	ConfigurePanel tabPanel = new ConfigurePanel(tab);
        	settings.put(name, tabPanel);

        	tabPanel.separator(tab, 1);

        	// Label text entry at the top of panel
        	JLabel label = new JLabel("tab name");
        	label.setToolTipText("title of this page of settings");
        	tab.add(label, makeConstraints(0, 0, 0.0, 0.0, 1, 1));

        	JTextField object = new JTextField(name);
        	object.setToolTipText("suggested: Invited or Plenary or Contributed or ...");
        	tab.add(object, makeConstraints(1, 0, 1.0, 0.0, 1, 1));
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
    	// TODO replace with actual Software License text from LICENSE file
    	String license_text = "";
    	String phrase = "\n This could be a lot of stuff to say.";
    	for (int i = 0; i < 100; i++)
    		license_text = license_text.concat(phrase);
    	TextArea area = new TextArea(license_text);
    	area.setEditable(false);
    	aboutBoxPanel.add(area, makeConstraints(0, row++, 1.0, 1.0, 1, 1));

    	// + + + + + + + + + + + + + + + + + + + + + + + +
    	// set entry fields from current values

        // FIXME need to set the entry fields from current values

    	// + + + + + + + + + + + + + + + + + + + + + + + +

    	pack();
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

	private HashMap<String, ConfigurePanel> settings;
	public final static int NUMBER_OF_TABS = 4;
	private int buttonPressed;
	public static final int NO_BUTTON_PRESSED = 0;
	public static final int OK_BUTTON = 1;
	public static final int CANCEL_BUTTON = 2;

	//########### SVN repository information ###################
	//# $Date$
	//# $Author$
	//# $Revision$
	//# $URL$
	//# $Id$
	//########### SVN repository information ###################
	private String svnRev = "$Revision$";
	private String svnId = "$Id$";
}
