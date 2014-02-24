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

import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

/**
 * the ConfigureDialog dialog for the CountdownJ program
 */
public class ConfigureDialog extends JDialog {

	// @see http://download.oracle.com/javase/tutorial/uiswing/components/dialog.html
	
	/**
	 * Creates ConfigureDialog dialog
	 * @param parent AWT Frame that owns this dialog
	 * @param modal whether to make this a modal dialog
	 */
    public ConfigureDialog(Frame parent) {
        super(parent, true);  // always make this a modal dialog
        buttonPressed = NO_BUTTON_PRESSED;
        settings = new HashMap<String, TalkConfiguration>();
        panel = new HashMap<String, ConfigurePanel>();
        defaultFilePanelText = null;
        userFilePanelText = null;
        defaultSettingsFile = "{not defined yet}";
        userSettingsFile = "{not defined yet}";

        create();

        setTalkDefaultsAndWidgets();
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
    	add(mainTabs, makeConstraints(0, 1, 1.0, 1.0, 1, 1));
    	
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

    	JPanel fileIoTab = new JPanel();
    	fileIoTab.setName("Read/Save Settings");
    	fileIoTab.setAlignmentX(CENTER_ALIGNMENT);
    	fileIoTab.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                fileIoTab.getBorder()));
    	mainTabs.add(fileIoTab);

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
        	String tabName = name;
        	if (settings.get(name) != null)
        		tabName = settings.get(name).getName();
        	tab.setName(tabName);	// sets tab title
        	tab.setLayout(new GridBagLayout());
        	subtabs.add(tab);

        	panel.put(name, new ConfigurePanel(tab, true));
    	}

    	// + + + + + + + + + + + + + + + + + + + + + + + +
    	// Read/Save Settings panel
    	
    	fileIoTab.setLayout(new GridBagLayout());

    	int row = 0;
    	// empty space at top of panel
    	fileIoTab.add(new JPanel(), makeConstraints(0, row++, 1.0, 1.0, 1, 1));

    	GridBagConstraints c;
    	// JPanel for showing the default settings file
    	JPanel defaultFilePanel = createLabeledPanel(subtabs, " default settings file:");
    	fileIoTab.add(defaultFilePanel, makeConstraints(0, row++, 1.0, 0.0, 1, 1));
    	//---- layout this subpanel
    	defaultFilePanelText = new JTextField(defaultSettingsFile);
    	defaultFilePanelText.setEditable(false);
    	defaultFilePanelText.setBackground(Color.WHITE);
    	defaultFilePanelText.setBorder(null);
    	c = makeConstraints(0, 1, 1.0, 0.0, 1, 1);
    	c.insets = new Insets(4, 10, 10, 10);
    	defaultFilePanel.add(defaultFilePanelText, c);

    	// empty space at middle of panel
    	fileIoTab.add(new JPanel(), makeConstraints(0, row++, 1.0, 1.0, 1, 1));
    	
    	// JPanel for selecting/saving a user's settings file
    	JPanel userFilePanel = createLabeledPanel(subtabs, " user settings file:");
    	fileIoTab.add(userFilePanel, makeConstraints(0, row++, 1.0, 0.0, 1, 1));
    	//---- layout this subpanel
    	userFilePanelText = new JTextField(userSettingsFile);
    	c = makeConstraints(0, 1, 1.0, 0.0, 1, 1);
    	c.insets = new Insets(4, 10, 10, 10);
    	userFilePanel.add(userFilePanelText, c);
    	//----- now the buttons
    	JPanel userFilePanelButtons = new JPanel();
    	c = makeConstraints(0, 2, 1.0, 0.0, 1, 1);
    	c.insets = new Insets(10, 10, 10, 10);
    	userFilePanel.add(userFilePanelButtons, c);
    	JButton btnOpen = new JButton("Open ...");
    	userFilePanelButtons.add(btnOpen);
    	//---- button bindings
    	btnOpen.addActionListener( // bind a button click to this action
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doOpenAction();
					}
				});
    	//----
    	JButton btnSave = new JButton("Save");
    	userFilePanelButtons.add(btnSave);
    	btnSave.addActionListener( // bind a button click to this action
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doSaveAction();
					}
				});
    	//----
    	JButton btnSaveAs = new JButton("Save As ...");
    	userFilePanelButtons.add(btnSaveAs);
    	btnSaveAs.addActionListener( // bind a button click to this action
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doSaveAsAction();
					}
				});

    	// empty space at bottom of panel
    	fileIoTab.add(new JPanel(), makeConstraints(0, row++, 1.0, 2.0, 1, 1));

    	// + + + + + + + + + + + + + + + + + + + + + + + +

    	pack();	// adjust the sizes of everything to fit
    }

    /**
     * initially, assign defaults for each talk's settings
     */
    private void setTalkDefaultsAndWidgets() {
    	setBasicSettings(new TalkConfiguration());
    	for (int i = 0; i < NUMBER_OF_TABS; i++)
    		setPresetSettings(i+1, new TalkConfiguration());
    }
    
    /**
     * layout the top of a JPanel with a label
     * @param subtabs
     * @param label
     * @return
     */
    private JPanel createLabeledPanel(JTabbedPane subtabs, String label) {
    	JPanel thePanel = new JPanel();
    	thePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                subtabs.getBorder()));
    	//---- layout this subpanel
    	thePanel.setLayout(new GridBagLayout());
    	JTextField defaultFilePanelLabel = new JTextField(label);
    	defaultFilePanelLabel.setBackground(new Color(0x656565));
    	defaultFilePanelLabel.setForeground(Color.white);
    	defaultFilePanelLabel.setBorder(null);
    	defaultFilePanelLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
    	GridBagConstraints c = makeConstraints(0, 0, 1.0, 0.0, 1, 1);
    	c.insets = new Insets(4, 10, 4, 10);
    	thePanel.add(defaultFilePanelLabel, c);
    	return thePanel;
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
     * @param index [1..NUMBER_OF_TABS]
     * @return
     */
    private String getPresetTabKey(int index) {
    	String key = null;
    	if (1 <= index && index <= NUMBER_OF_TABS)
    		key = "preset" + index;
    	return key;
    }
    
    /**
     * respond to the "Ok" button
     */
    private void doOkAction() {
        buttonPressed = OK_BUTTON;
        setVisible(false);
    }
    
    /**
     * respond to the "Cancel" button
     */
    private void doCancelAction() {
        buttonPressed = CANCEL_BUTTON;
        setVisible(false);
    }
    
    /**
     * select a file to open with the FileDialog
     * @return full path file name or null
     */
    private String selectFileOpen() {
        FileDialog fc = new FileDialog(this, "Choose a file", FileDialog.LOAD);
        //@see http://docstore.mik.ua/orelly/java/awt/ch06_07.htm
        fc.setFile("*.xml");
		//---- complete all dialog setup before this next line
		fc.setVisible(true);
		String fn = fc.getFile();
		if (fn != null) {
			String dir = fc.getDirectory();
			// String delim = System.getProperty("file.separator");
			fn = dir + fn;
		}
		return fn;
    }
    
    /**
     * Select a valid XML settings file and open it
     */
    private void doOpenAction() {
        HashMap<String, TalkConfiguration> theCfg = null;
        boolean done = false;
        String fn = "";
        while (!done) {
        	fn = selectFileOpen();
        	if (fn == null)
        		done = true;
        	else {			
        		XmlFileFilter filter = new XmlFileFilter();
				try {
					String msg = filter.validateSettings(fn);
					if (msg == null) {
						done = true;
						// file selection complete, process the file
						XmlSettingsFile xsf = new XmlSettingsFile();
						theCfg = xsf.readFullConfiguration(fn);
					} else {
						String title = "invalid: " + fn;
		        		JOptionPane.showMessageDialog(null, 
		        				msg, title, 
		        				JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (IOException e) {
					e.printStackTrace();	// What happens to get here?
				} catch (XPathExpressionException e) {
					e.printStackTrace();	// Due to programmer error, can't evaluate XPathExpression
				} catch (SAXException e) {
					e.printStackTrace();	// What happens to get here?
				}
        	}
        }
        if (theCfg != null) {
			setBasicSettings(theCfg.get("basic"));
        	for (int i = 0; i < NUMBER_OF_TABS; i++) {
        		String key = String.format("preset%d", i+1);
        		setPresetSettings(i+1, theCfg.get(key));
        	}
        	setUserSettingsFile(fn);
        }
    }
    
    /**
     * respond to "Save" button
     */
    private void doSaveAction() {
        System.out.println("doSaveAction()");
        boolean undefined = userSettingsFile.startsWith("{");
        boolean exists = new File(userSettingsFile).exists();
        if (undefined | !exists)
        	doSaveAsAction();
        else
        	if (!saveToFile(userSettingsFile))
        		setUserSettingsFile("{undefined}");
    }
    
    /**
     * save the settings to the named file
     * @param filename
     * @return
     */
    private boolean saveToFile(String filename) {
		boolean success = false;
		XmlSettingsFile xsf = new XmlSettingsFile(settings);
		xsf.writeFullConfiguration(filename);
		success = true;	// only once written successfully
		return success;
    }
    
    /**
     * respond to "SaveAs ..." button
     */
    private void doSaveAsAction() {
        boolean done = false;
        System.out.println("doSaveAsAction()");
        while (!done) {
	        String fn = selectFileSaveAs(userSettingsFile);
	        // code already tests if selected file exists
	        if (fn == null)
	        	done = true;
	        else {
	        	if (saveToFile(fn)){
		        	done = true;	// only once written successfully
		        	setUserSettingsFile(fn);
	        	}
	        }
        }
    }
    
    /**
     * select a file to open with the FileDialog
     * @return file name or null
     */
    private String selectFileSaveAs(String oldName) {
        String title = "Save file to another location ...";
    	FileDialog fc = new FileDialog(this, title, FileDialog.SAVE);
        //@see http://docstore.mik.ua/orelly/java/awt/ch06_07.htm
        fc.setFile(oldName);
		//---- complete all dialog setup before this next line
		fc.setVisible(true);
		String fn = fc.getFile();
		if (fn != null) {
			String dir = fc.getDirectory();
			fn = dir + fn;
			if (!fn.toLowerCase().endsWith(".xml"))
				fn = fn + ".xml";
		}
		return fn;
    }
	
	/**
	 * find a talk by its key name
	 * @param key
	 * @return
	 */
    private TalkConfiguration getSettingsByKey(String key) {
		TalkConfiguration talk = null;
		if (settings.containsKey(key))
			talk = settings.get(key).deepCopy();
		return talk;
	}

    /**
	 * @return the settings of the basic panel
	 */
	public TalkConfiguration getBasicSettings() {
		// Copy widget values to local HashMap.
		// No need for deep copy here.
		TalkConfiguration talk = panel.get("basic").getConfig();
		settings.put("basic", talk);
		return getSettingsByKey("basic");
	}

	/**
	 * @param index index number [1..NUMBER_OF_TABS]
	 * @param talk ConfigurePanel object
	 */
	public void setBasicSettings(TalkConfiguration talk) {
		// update the local dictionary
		settings.put("basic", talk);
		// update the widgets
		panel.get("basic").setConfig(talk.deepCopy(), true);
	}

    /**
	 * @param index index number [1..NUMBER_OF_TABS]
	 * @return the settings of the named key or null
	 */
	public TalkConfiguration getPresetSettings(int index) {
		String key = getPresetTabKey(index);
		if (key != null) {
			// copy widget values to object in local HashMap
			// No need for deep copy here.
			TalkConfiguration talk = panel.get(key).getConfig();
			settings.put(key, talk);
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
			// update our local dictionary
			settings.put(key, talk);
			// update the widget fields
			panel.get(key).setConfig(talk.deepCopy(), true);
			// update the tab title
			setTabTitle(panel.get(key), index-1, talk.getName());
		}
	}

	/**
	 * @return the defaultSettingsFile
	 */
	public String getDefaultSettingsFile() {
		return defaultSettingsFile;
	}

	/**
	 * @param defaultSettingsFile the defaultSettingsFile to set
	 */
	public void setDefaultSettingsFile(String defaultSettingsFile) {
		this.defaultSettingsFile = defaultSettingsFile;
		if (defaultFilePanelText != null)
			defaultFilePanelText.setText(defaultSettingsFile);
	}

	/**
	 * @return the userSettingsFile
	 */
	public String getUserSettingsFile() {
		return userSettingsFile;
	}

	/**
	 * @param userSettingsFile the userSettingsFile to set
	 */
	public void setUserSettingsFile(String userSettingsFile) {
		this.userSettingsFile = userSettingsFile;
		if (userFilePanelText != null)
			userFilePanelText.setText(userSettingsFile);
	}

	/**
	 * @return the buttonPressed
	 */
	public int getButtonPressed() {
		return buttonPressed;
	}

	/**
	 * standard call to set tab title
	 * @param panel
	 * @param index
	 * @param title
	 */
	private void setTabTitle(ConfigurePanel panel, int index, String title) {
		Container grandparent = panel.getParent().getParent();
		JTabbedPane tabbedPane = (JTabbedPane) grandparent;
		tabbedPane.setTitleAt(index, title);
	}

	/**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
            	TalkConfiguration talk;
            	ConfigureDialog dialog = new ConfigureDialog(new javax.swing.JFrame());
                dialog.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
                // =========================================
                // set the Configurations
                talk = new TalkConfiguration();
                talk.setDiscussion(3*60);
                talk.setOvertime(15);
                talk.setName("adjusted");
        		talk.setAudible(false);
                dialog.setBasicSettings(talk);
                for (int i = 0; i < ConfigureDialog.NUMBER_OF_TABS; i++) {
                	TalkConfiguration item = new TalkConfiguration();
                	item.setPresentation((i+1)*5*60);
                	item.setDiscussion((i+1)*60);
                	item.setOvertime((i+1)*15);
                	item.setName(item.getPresentationStr() + " talk");
                	dialog.setPresetSettings(i+1, item);
                }
                dialog.setDefaultSettingsFile("/system/dependent/default/settings/file.xml");
                dialog.setUserSettingsFile("/user/selected/settings/file.xml");
                // =========================================
                dialog.setVisible(true);	// run the dialog and wait ...
                // =========================================
                int pressed = dialog.getButtonPressed();
                System.out.println("pressed: " + pressed);
                if (pressed == ConfigureDialog.OK_BUTTON) {
	                talk = dialog.getBasicSettings();
	                String str = String.format("<talk id=\"basic\" %s />", talk);
                    System.out.println(str);
	                for (int i = 0; i < ConfigureDialog.NUMBER_OF_TABS; i++) {
	                	talk = dialog.getPresetSettings(i+1);
	                	str = String.format("<talk id=\"preset%s\" %s />", i+1, talk);
	                    System.out.println(str);
	                }
                }
                dialog.dispose();
                //System.exit(0);
            }
        });
    }

	private static final long serialVersionUID = -6246475747169907480L;

	private HashMap<String, TalkConfiguration> settings;
	private HashMap<String, ConfigurePanel> panel;
	private int buttonPressed;
	private String defaultSettingsFile;
	private String userSettingsFile;
	private JTextField defaultFilePanelText;
	private JTextField userFilePanelText;

	public static final int NUMBER_OF_TABS = 4;
	public static final int OK_BUTTON = 0;
	public static final int NO_BUTTON_PRESSED = 1;
	public static final int CANCEL_BUTTON = 2;
}
