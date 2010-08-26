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
	 * universal version identifier for a Serializable class
	 * @see http://www.javapractices.com/topic/TopicAction.do?Id=45
	 */
	private static final long serialVersionUID = 5977631140257178413L;

	public JTextField discussion;
	public JTextField overtime;
	public JTextField msg_pretalk;
	public JTextField msg_presentation;
	public JTextField msg_discussion;
	public JTextField msg_overtime;
	public JTextField msg_paused;

	/**
	 * Creates JPanel ConfigurePanel
	 * @param parent of this panel
	 */
    public ConfigurePanel(Container parent) {
    	parent.setLayout(new GridBagLayout());
    	initializePanel(parent);
    }


	/**
     * setup each row of the panel
     * @param parent
     */
    private void initializePanel(Container parent) {
    	int row = 0;
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

    	GridBagConstraints c;
    	c = new GridBagConstraints();
    	c.fill = GridBagConstraints.BOTH;
    	c.gridx = 0;
    	c.gridwidth = 2;
    	c.gridy = row++;
    	c.insets = new Insets(4, 4, 4, 4);
    	JSeparator line = new JSeparator();
    	parent.add(line, c);

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
    private JTextField label_entry(Container parent, int row, 
    		String labelText, String labelTip, String text, String tip) {
    	GridBagConstraints c;
    	c = new GridBagConstraints();
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

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Create and set up the window.
                JFrame frame = new JFrame("ConfigurePanel demo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Set up the content pane.
                ConfigurePanel panel = new ConfigurePanel(frame.getContentPane());
                panel.discussion.setText("15:00");

                //Display the window.
                frame.pack();
                frame.setVisible(true);            }
        });
    }

}
