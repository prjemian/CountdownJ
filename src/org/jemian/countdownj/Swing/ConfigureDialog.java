package org.jemian.countdownj.Swing;

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
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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

    	JPanel fileIoTab = new JPanel();
    	fileIoTab.setName("Read/Save Settings");
    	fileIoTab.setAlignmentX(CENTER_ALIGNMENT);
    	fileIoTab.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                fileIoTab.getBorder()));
    	mainTabs.add(fileIoTab);

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
    	
    	//--------------------------------------------
    	//
    	//   |  /the/default/settings/file              |
    	//
    	//   |  /last/user/settings/file/selected.xml   |
    	//   [Open ...]     [Save]   [Save As ...]
    	//
    	//--------------------------------------------
    	
    	// needs two bordered panels stacked vertically

    	// demo
    	FlowLayout fl = new FlowLayout();
    	fileIoTab.setLayout(fl);
    	fileIoTab.add(new JButton("button 1"));
    	fileIoTab.add(new JButton("button 2"));
    	fileIoTab.add(new JButton("button 3"));
    	fileIoTab.add(new JButton("button 4"));
    	fileIoTab.add(new JButton("button 5"));
    	fileIoTab.add(new JButton("button 6"));

    	// + + + + + + + + + + + + + + + + + + + + + + + +
    	// About Box
    	ConfigFile cfg = ConfigFile.getInstance();

    	int row = 0;
    	aboutBoxPanel.setLayout(new GridBagLayout());
    	JLabel text = new JLabel(cfg.getName() + ", v" + cfg.getVersion());
    	text.setFont(new Font("Tahoma", Font.BOLD, 32));
    	text.setAlignmentX(CENTER_ALIGNMENT);
    	aboutBoxPanel.add(text, makeConstraints(0, row++, 1.0, 0.0, 1, 1));

    	text = new JLabel(cfg.getCopyright());
    	text.setFont(new Font("Courier", Font.PLAIN, 12));
    	aboutBoxPanel.add(text, makeConstraints(0, row++, 1.0, 0.0, 1, 1));

    	text = new JLabel("by " + cfg.getAuthor());
    	text.setFont(new Font("Tahoma", Font.PLAIN, 16));
    	aboutBoxPanel.add(text, makeConstraints(0, row++, 1.0, 0.0, 1, 1));

    	text = new JLabel("<"+cfg.getEmail()+">");
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
    		// The LICENSE file is at the root of the development tree.
    		// getResourceAsStream() expects to find it either:
    		//	  [1] root of ${bin.dir} during code development
    		//	  [2] root of JAR executable file
			// An ANT build target ("resources") copies it to ${bin.dir}.
    		// Another ANT target copies all of ${bin.dir} to the JAR
    		license_text = readResourceAsString(LICENSE_FILE);
    		// FIXME Linux-only bug: license_text starts scrolled to the bottom.
		} catch (IOException e1) {
			// backup license text if LICENSE cannot be found
			license_text = ConfigFile.getInstance().toString();
		}
    	TextArea area = new TextArea(license_text);
    	area.setEditable(false);
    	aboutBoxPanel.add(area, makeConstraints(0, row++, 1.0, 1.0, 1, 1));

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
     * Read a file resource into a string from the JAR file
     * @param filename
     * @return
     * @throws IOException
     * @note An alternate (not equivalent) method is
     *  org.apache.commons.io.FileUtils.readFileToString(file);
     * For example:  http://www.kodejava.org/examples/52.html
     */
    private String readResourceAsString(String resourceName) throws IOException {
    	InputStream input = getClass().getResourceAsStream(resourceName);
    	StringBuffer fileData = new StringBuffer();
        byte[] buf = new byte[1024];        
        int numRead=0;
        while((numRead=input.read(buf)) != -1){
            String str = new String(buf, 0, numRead);
        	fileData.append(str);
            buf = new byte[1024];
        }
        input.close();
    	return fileData.toString();
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
    
    private void doOkAction() {
        buttonPressed = OK_BUTTON;
        setVisible(false);
    }
    
    private void doCancelAction() {
        buttonPressed = CANCEL_BUTTON;
        setVisible(false);
    }
	
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
                System.exit(0);
            }
        });
    }

	private static final long serialVersionUID = -6246475747169907480L;

	private HashMap<String, TalkConfiguration> settings;
	private HashMap<String, ConfigurePanel> panel;
	private int buttonPressed;
	private static final String LICENSE_FILE = "/LICENSE";

	public static final int NUMBER_OF_TABS = 4;
	public static final int OK_BUTTON = 0;
	public static final int NO_BUTTON_PRESSED = 1;
	public static final int CANCEL_BUTTON = 2;

	//private String svnRev = "$Revision$";
	private String svnId = "$Id$";
}
