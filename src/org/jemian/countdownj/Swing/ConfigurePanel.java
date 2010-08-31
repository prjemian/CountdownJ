package org.jemian.countdownj.Swing;

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
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
	 * @param parent of this panel
	 */
    public ConfigurePanel(Container parent) {
    	parent.setLayout(new GridBagLayout());
    	initializePanel(parent);
    	initialTalkConfig = new TalkConfiguration();
    	setConfig(initialTalkConfig);
    }

	/**
	 * Creates JPanel ConfigurePanel.
	 * Caller supplies initial configuration of the talk.
	 * @param parent of this panel
	 */
    public ConfigurePanel(Container parent, TalkConfiguration config) {
    	parent.setLayout(new GridBagLayout());
    	initializePanel(parent);
    	initialTalkConfig = config;
    	setConfig(initialTalkConfig);
    }


	/**
     * setup each row of the panel
     * @param parent
     */
    private void initializePanel(Container parent) {
    	int row = 2;
    	presentation = label_entry(parent, row++, 
    			"time allowed for presentation (and discussion)", 
    			"discussion is the last part of the presentation period",
    			"{mm:ss | seconds}", 
    			"enter either minutes:seconds (12:30) or seconds (750)");
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
    			"suggested: Next Presentation");
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
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.BOTH;
    	c.gridx = 0;
    	c.gridy = row;
    	c.insets = new Insets(2, 4, 2, 8);
    	JLabel label = new JLabel(labelText);
    	label.setToolTipText(labelTip);
    	parent.add(label, c);

    	c = new GridBagConstraints();
    	c.fill = GridBagConstraints.BOTH;
    	c.gridx = 1;
    	c.gridy = row;
    	c.insets = new Insets(2, 4, 2, 4);
    	c.weightx = 1;
    	JTextField object = new JTextField(text);
    	object.setToolTipText(tip);
    	parent.add(object, c);

    	return object;
    }
    
    /**
     * draws a horizontal line across all columns
     * @param parent
     * @param row
     */
    public void separator(Container parent, int row) {
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.BOTH;
    	c.gridx = 0;
    	c.gridwidth = 2;
    	c.gridy = row;
    	c.insets = new Insets(4, 4, 4, 4);
    	JSeparator line = new JSeparator();
    	parent.add(line, c);
    }
    
    /**
     * draws a subpanel across all columns of the parent
     * with the buttons of this class
     * @param parent
     * @param row
     */
    private void buttonRow(Container parent, int row) {
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.BOTH;
    	c.gridx = 0;
    	c.gridwidth = 2;
    	c.gridy = row;
    	c.insets = new Insets(4, 4, 4, 4);
    	JPanel panel = new JPanel();
    	parent.add(panel, c);

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

    	JButton btnReset = new JButton("reset all");
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
     * set the defaults for all fields
     */
    public void setDefaults() {
    	TalkConfiguration defaults = new TalkConfiguration();
    	defaults.setDefaults();

    	presentation.setText(defaults.getPresentationStr());
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
    	presentation.setText("");
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
    	presentation.setText(initialTalkConfig.getPresentationStr());
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
    	TalkConfiguration config = new TalkConfiguration();
    	config.setPresentationStr(presentation.getText());
    	config.setDiscussionStr(discussion.getText());
    	config.setOvertimeStr(overtime.getText());
    	config.setMsg_discussion(msg_discussion.getText());
    	config.setMsg_overtime(msg_overtime.getText());
    	config.setMsg_paused(msg_paused.getText());
    	config.setMsg_presentation(msg_presentation.getText());
    	config.setMsg_pretalk(msg_pretalk.getText());
    	config.setAudible(checkAudible.isSelected());
    	return config;
    }

    /**
     * set widget fields from talk configuration
     */
    public void setConfig(TalkConfiguration config) {
    	presentation.setText(config.getPresentationStr());
    	discussion.setText(config.getDiscussionStr());
    	overtime.setText(config.getOvertimeStr());
    	msg_discussion.setText(config.getMsg_discussion());
    	msg_overtime.setText(config.getMsg_overtime());
    	msg_paused.setText(config.getMsg_paused());
    	msg_presentation.setText(config.getMsg_presentation());
    	msg_pretalk.setText(config.getMsg_pretalk());
    	checkAudible.setSelected(config.isAudible());
    }

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
                TalkConfiguration config = new TalkConfiguration();
                config.setPresentationStr("30:00");
                config.setDiscussionStr("10:00");
                config.setOvertimeStr("0:20");
                config.setMsg_presentation("talk");
                config.setMsg_overtime("stop talking now");
                config.setMsg_discussion("Questions?");
                config.setMsg_paused("a delay");
                config.setMsg_pretalk("");
                new ConfigurePanel(panel, config);

                //Display the window.
                frame.pack();
                frame.setVisible(true);            }
        });
    }

	/**
	 * universal version identifier for a Serializable class
	 * @see http://www.javapractices.com/topic/TopicAction.do?Id=45
	 */
	private static final long serialVersionUID = 5977631140257178413L;

	public JTextField presentation;
	public JTextField discussion;
	public JTextField overtime;
	public JTextField msg_pretalk;
	public JTextField msg_presentation;
	public JTextField msg_discussion;
	public JTextField msg_overtime;
	public JTextField msg_paused;
	public JCheckBox checkAudible;

	TalkConfiguration initialTalkConfig;
}
