package example.code;

/*
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java 
 * language and environment is gratefully acknowledged.
 * 
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Simple Save Dialog demo.
 * @version $Id: SaveDialog.java,v 1.4 2000/11/25 17:54:19 ian Exp $
 */
public class SaveDialog extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4129441775561453293L;
	boolean unsavedChanges = false;
	Button quitButton;

	/** "main program" method - construct and show */
	public static void main(String[] av) {
		// create a SaveDialog object, tell it to show up
		new SaveDialog().setVisible(true);
	}

	/** Construct the object including its GUI */
	public SaveDialog() {
		super("SaveDialog");
		Container cp = getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(new Label("Press this button to see the Quit dialog: "));
		cp.add(quitButton = new Button("Quit"));
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("In Exit Button's action handler");
				if (okToQuit()) {
					setVisible(false);
					dispose();
					System.exit(0);
				}
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
				System.exit(0);
			}
		});

		pack();
	}

	boolean okToQuit() {
		String[] choices = { "Yes, Save and Quit", "No, Quit without saving",
				"CANCEL" };
		int result = JOptionPane.showOptionDialog(this,
				"You have unsaved changes. Save before quitting?", "Warning",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
				null, choices, choices[0]);

		// Use of "null" as the Icon argument is contentious... the
		// document says you can pass null, but it does seem to
		// generate a lot of blather if you do, something about
		// a NullPointerException :-) ...

		if (result >= 0)
			System.out.println("You clicked " + choices[result]);

		switch (result) {
		case -1:
			System.out.println("You killed my die-alog - it died");
			return false;
		case 0: // save and quit
			System.out.println("Saving...");
			// mainApp.doSave();
			return true;
		case 1: // just quit
			return true;
		case 2: // cancel
			return false;
		default:
			throw new IllegalArgumentException("Unexpected return " + result);
		}
	}
}
