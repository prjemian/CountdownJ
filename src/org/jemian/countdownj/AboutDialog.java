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
import java.awt.EventQueue;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * the ConfigureDialog dialog for the CountdownJ program
 */
public class AboutDialog extends JDialog {

	// @see http://download.oracle.com/javase/tutorial/uiswing/components/dialog.html
	
	/**
	 * Creates ConfigureDialog dialog
	 * @param parent AWT Frame that owns this dialog
	 * @param modal whether to make this a modal dialog
	 */
    public AboutDialog(Frame parent) {
        super(parent, true);  // always make this a modal dialog
        create();
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    }
    
    private void create() {
    	setLayout(new GridBagLayout());

    	JPanel aboutBoxPanel = new JPanel();
    	aboutBoxPanel.setName("About");
    	aboutBoxPanel.setAlignmentX(CENTER_ALIGNMENT);
    	aboutBoxPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                aboutBoxPanel.getBorder()));
    	add(aboutBoxPanel, makeConstraints(0, 0, 1.0, 1.0, 1, 1));
    	
    	JButton btnOk = new JButton("Ok");
    	add(btnOk, makeConstraints(0, 1, 0.0, 0.0, 1, 1));
		btnOk.addActionListener( // bind a button click to this action
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doOkAction();
					}
				});

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
    		// CANTFIX Linux-only bug: license_text starts scrolled to the bottom.
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
    	c.insets = new Insets(2, 2, 2, 2);
    	return c;
    }
    
    /**
     * respond to the "Ok" button
     */
    private void doOkAction() {
        setVisible(false);	// dismiss
    }

	/**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
            	AboutDialog dialog = new AboutDialog(new javax.swing.JFrame());
                dialog.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
                // =========================================
                // =========================================
                dialog.setVisible(true);	// run the dialog and wait ...
                // =========================================
                dialog.dispose();
                //System.exit(0);
            }
        });
    }

	private static final String LICENSE_FILE = "/LICENSE";

	//private String svnRev = "$Revision$";
	private String svnId = "$Id$";

	private static final long serialVersionUID = 4648189036267507189L;
}
