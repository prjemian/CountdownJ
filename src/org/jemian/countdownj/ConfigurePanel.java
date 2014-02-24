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

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


/**
 * ConfigurePanel is used by the Configure dialog for the main
 * and preset configurations.  By choice the JTextField entry widgets
 * are exposed to the caller.
 * 
 * @author Pete
 *
 */
public class ConfigurePanel extends JPanel {

	/**
	 * Creates JPanel ConfigurePanel.
	 * Initial talk configuration is the default settings.
	 * Caller has set the layout manager of parent to be GridBagLayout.
	 * @param parent of this panel
	 * @param preset is this a preset panel (true) or a basic panel (false)
	 */
    public ConfigurePanel(Container parent, boolean preset) {
    	commonConstructor(parent, preset, new TalkConfiguration());
    }

	/**
	 * Creates JPanel ConfigurePanel.
	 * Caller supplies initial configuration of the talk.
	 * @param parent of this panel
	 * @param preset is this a preset panel (true) or a basic panel (false)
	 * @param config configuration of this talk
	 */
    public ConfigurePanel(Container parent, boolean preset, TalkConfiguration config) {
    	commonConstructor(parent, preset, config);
    }
    
    /**
     * Called by Constructor methods
     * @param parent
     * @param preset
     * @param config
     */
    private void commonConstructor(Container parent, boolean preset, TalkConfiguration config) {
    	this.parent = parent;
    	this.preset = preset;
    	initializePanel(parent);
    	initialTalkConfig = config;
    	setConfig(initialTalkConfig, false);
    }


	/**
     * setup each row of the panel
     * @param parent
     */
    private void initializePanel(final Container parent) {
    	int row = 2;
    	if (this.preset) {
	    	name = label_entry(parent, row++, 
	    			"name", 
	    			"short description (1 word) of these settings",
	    			"{name}", 
	    			"enter either minutes:seconds (12:30) or seconds (750)");
	    	// Change the tab title when this JPanel is wrapped in a JTabbedPane.
	    	// The only time this does not happen is during development when
	    	// this panel is tested by itself.
	    	name.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					String str = name.getText();
					int sel = name.getCaretPosition();
					str = str.substring(0, sel) + e.getKeyChar() + str.substring(sel);
					setTabTitle(str);
				}

				@Override
				public void keyReleased(KeyEvent e) {}
				
				@Override
				public void keyPressed(KeyEvent e) {}
			});
	    	separator(parent, row++);
	    	presentation = label_entry(parent, row++, 
	    			"time allowed for presentation", 
	    			"includes discussion time at end of the presentation period",
	    			"{mm:ss | seconds}", 
	    			"Suggested: Invited, Plenary, Contributed, ...");
    	}
    	discussion = label_entry(parent, row++, 
    			"time allowed for discussion", 
    			"discussion is the last part of the presentation period",
    			"{mm:ss | seconds}", 
    			"enter either minutes:seconds (12:30) or seconds (750)");
    	overtime = label_entry(parent, row++, 
    			"overtime reminder interval", 
    			"3 bells will sound in overtime at this interval",
    			"{mm:ss}", 
    			"{mm:ss | seconds}, suggested: between 10 and 60 seconds");

    	separator(parent, row++);

    	msg_pretalk = label_entry(parent, row++, 
    			"pre-talk message", 
    			"message to show before presentation begins",
    			"{pre-talk message}", 
    			"suggested: Next presentation");
    	msg_presentation = label_entry(parent, row++, 
    			"presentation message", 
    			"message to show during presentation before discussion",
    			"{presentation message}", 
    			"suggested: Presentation");
    	msg_discussion = label_entry(parent, row++, 
    			"discussion message", 
    			"message to show during time allowed for discussion",
    			"{discussion message}", 
    			"suggested: Discussion");
    	msg_overtime = label_entry(parent, row++, 
    			"overtime message", 
    			"message to show when time has run out",
    			"{overtime message}", 
    			"suggested: Overtime");
    	msg_paused = label_entry(parent, row++, 
    			"paused message", 
    			"message to show if presentation has been paused",
    			"{paused message}", 
    			"suggested: Paused");

    	separator(parent, row++);

    	buttonRow(parent, row++);
    }

    /**
     * Creates a label and a text entry in the specified row.
     * Expects that a GridBagLayout is in use by the parent.
     * @param parent JPanel that contains these widgets
     * @param row of the GridBagLayout
     * @param labelText String
     * @param text default for the entry widget
     * @param tip for the entry widget, as well
     * @return the JTextField object
     */
    public JTextField label_entry(Container parent, int row, 
    		String labelText, String labelTip, String text, String tip) {
    	JLabel label = new JLabel(labelText);
    	label.setToolTipText(labelTip);
    	parent.add(label, makeConstraints(0, row, 0, 0, 1, 1));

    	JTextField object = new JTextField(text);
    	object.setToolTipText(tip);
    	parent.add(object, makeConstraints(1, row, 1, 0, 1, 1));

    	return object;
    }
    
    /**
     * draws a horizontal line across all columns
     * @param parent
     * @param row
     */
    public void separator(Container parent, int row) {
    	JSeparator line = new JSeparator();
    	parent.add(line, makeConstraints(0, row, 1, 0, 2, 1));
    }
    
    /**
     * draws a subpanel across all columns of the parent
     * with the buttons of this class
     * @param parent
     * @param row
     */
    private void buttonRow(Container parent, int row) {
    	JPanel panel = new JPanel();
    	parent.add(panel, makeConstraints(0, row, 1, 0, 2, 1));

    	BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
    	panel.setLayout(layout);

    	JButton btnDefaults = new JButton("set defaults");
    	btnDefaults.setToolTipText("set all fields to default values");
    	panel.add(btnDefaults);
    	btnDefaults.addActionListener(	// bind a button click to this action
    		new ActionListener() {
    		    public void actionPerformed(ActionEvent e) {
    		        setDefaults();
    		    }
    		}
    	);

    	JButton btnClear = new JButton("clear all");
    	btnClear.setToolTipText("clear all fields");
    	panel.add(btnClear);
    	btnClear.addActionListener(	// bind a button click to this action
        		new ActionListener() {
        		    public void actionPerformed(ActionEvent e) {
        		        clearAll();
        		    }
        		}
        	);

    	JButton btnReset = new JButton("reset");
    	btnReset.setToolTipText("set all fields to original values");
    	panel.add(btnReset);
    	btnReset.addActionListener(	// bind a button click to this action
        		new ActionListener() {
        		    public void actionPerformed(ActionEvent e) {
        		        resetAll();
        		    }
        		}
        	);

    	checkAudible = new JCheckBox("audible");
    	checkAudible.setToolTipText("decide to make audible beeps at planned intervals");
    	panel.add(checkAudible);
    	// no listeners/bindings needed
    }
    
    /**
	 * @return the initialTalkConfig
	 */
	public TalkConfiguration getInitialTalkConfig() {
		return initialTalkConfig;
	}

	/**
	 * @param initialTalkConfig the initialTalkConfig to set
	 */
	public void setInitialTalkConfig(TalkConfiguration initialTalkConfig) {
		this.initialTalkConfig = initialTalkConfig;
	}

	/**
	 * @return the parent
	 */
	public Container getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Container parent) {
		this.parent = parent;
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
     * set the defaults for all fields
     */
    public void setDefaults() {
    	// object is initialized with default content
    	TalkConfiguration defaults = new TalkConfiguration();

    	// transfer the default object content into the widget fields
    	if (this.preset) {
    		name.setText(defaults.getName());
    		presentation.setText(defaults.getPresentationStr());
    		setTabTitle(defaults.getName());
    	}
    	discussion.setText(defaults.getDiscussionStr());
    	overtime.setText(defaults.getOvertimeStr());
    	msg_pretalk.setText(defaults.getMsg_pretalk());
    	msg_presentation.setText(defaults.getMsg_presentation());
    	msg_discussion.setText(defaults.getMsg_discussion());
    	msg_overtime.setText(defaults.getMsg_overtime());
    	msg_paused.setText(defaults.getMsg_paused());
    	checkAudible.setSelected(true);
    }

    /**
     * clear all fields
     */
    public void clearAll() {
    	if (this.preset) {
    		name.setText("");
    		presentation.setText("");
    		setTabTitle("");
    	}
    	discussion.setText("");
    	overtime.setText("");
    	msg_pretalk.setText("");
    	msg_presentation.setText("");
    	msg_discussion.setText("");
    	msg_overtime.setText("");
    	msg_paused.setText("");
    	checkAudible.setSelected(false);
    }

    /**
     * reset all fields
     */
    public void resetAll() {
    	// change the widget fields back to the original the object content
    	if (this.preset) {
    		name.setText(initialTalkConfig.getName());
    		presentation.setText(initialTalkConfig.getPresentationStr());
    		setTabTitle(initialTalkConfig.getName());
    	}
    	discussion.setText(initialTalkConfig.getDiscussionStr());
    	overtime.setText(initialTalkConfig.getOvertimeStr());
    	msg_pretalk.setText(initialTalkConfig.getMsg_pretalk());
    	msg_presentation.setText(initialTalkConfig.getMsg_presentation());
    	msg_discussion.setText(initialTalkConfig.getMsg_discussion());
    	msg_overtime.setText(initialTalkConfig.getMsg_overtime());
    	msg_paused.setText(initialTalkConfig.getMsg_paused());
    	checkAudible.setSelected(initialTalkConfig.isAudible());
    }

    /**
     * get talk configuration from the widget fields
     */
    public TalkConfiguration getConfig() {
    	// transfer the content of the widget fields to the object
    	TalkConfiguration talk = new TalkConfiguration();
    	if (this.preset) {
    		talk.setName(name.getText());
    		talk.setPresentationStr(presentation.getText());
    	}
    	talk.setDiscussionStr(discussion.getText());
    	talk.setOvertimeStr(overtime.getText());
    	talk.setMsg_discussion(msg_discussion.getText());
    	talk.setMsg_overtime(msg_overtime.getText());
    	talk.setMsg_paused(msg_paused.getText());
    	talk.setMsg_presentation(msg_presentation.getText());
    	talk.setMsg_pretalk(msg_pretalk.getText());
    	talk.setAudible(checkAudible.isSelected());
    	return talk;
    }

    /**
     * set widget fields from talk configuration
     * @param talk settings for this talk
     */
    public void setConfig(TalkConfiguration talk, boolean setInitial) {
    	// transfer the object content into the widget fields
    	if (setInitial)
    		initialTalkConfig = talk.deepCopy();
    	if (this.preset) {
    		name.setText(talk.getName());
    		presentation.setText(talk.getPresentationStr());
    	}
    	discussion.setText(talk.getDiscussionStr());
    	overtime.setText(talk.getOvertimeStr());
    	msg_discussion.setText(talk.getMsg_discussion());
    	msg_overtime.setText(talk.getMsg_overtime());
    	msg_paused.setText(talk.getMsg_paused());
    	msg_presentation.setText(talk.getMsg_presentation());
    	msg_pretalk.setText(talk.getMsg_pretalk());
    	checkAudible.setSelected(talk.isAudible());
    }

	/**
	 * standard call to set tab title
	 * @param panel
	 * @param index
	 * @param title
	 */
	private void setTabTitle(String title) {
		if (sameClassAs("javax.swing.JPanel", parent)) {
			Container grandparent = parent.getParent();
			if (sameClassAs("javax.swing.JTabbedPane", grandparent)) {
				JTabbedPane tabbedPane = (JTabbedPane) grandparent;
				int index = tabbedPane.getSelectedIndex();
				tabbedPane.setTitleAt(index, title);
			}
		}
	}

	/**
	 * This test needs a short name
	 * @param name
	 * @param object
	 * @return
	 */
	private boolean sameClassAs(String name, Container object) {
		return name.compareTo(object.getClass().getCanonicalName()) == 0;
	}

    /**
     * for development only: test this panel
     * @param args
     */
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Create and set up the window.
                JFrame frame = new JFrame("ConfigurePanel demo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Set up the content pane.
                Container panel = frame.getContentPane();
                panel.setLayout(new GridBagLayout());
                TalkConfiguration config = new TalkConfiguration();
                config.setName("Plenary");
                config.setPresentationStr("30:00");
                config.setDiscussionStr("10:00");
                config.setOvertimeStr("0:20");
                config.setMsg_presentation("talk");
                config.setMsg_overtime("stop talking now");
                config.setMsg_discussion("Questions?");
                config.setMsg_paused("a delay");
                config.setMsg_pretalk("");
                ConfigurePanel cp = new ConfigurePanel(panel, true, config);

                //Display the window.
                frame.pack();
                frame.setVisible(true); 
                System.out.println(cp.getConfig().toString());
            }
        });
    }	

	/**
	 * universal version identifier for a Serializable class
	 * @see http://www.javapractices.com/topic/TopicAction.do?Id=45
	 * @note prefix a field with "transient" to indicate it should be serialized
	 */
	private static final long serialVersionUID = 5977631140257178413L;

	transient private Container parent;
	private JTextField name;
	private JTextField presentation;
	private JTextField discussion;
	private JTextField overtime;
	private JTextField msg_pretalk;
	private JTextField msg_presentation;
	private JTextField msg_discussion;
	private JTextField msg_overtime;
	private JTextField msg_paused;
	private JCheckBox checkAudible;
	private boolean preset;

	private TalkConfiguration initialTalkConfig;
}
