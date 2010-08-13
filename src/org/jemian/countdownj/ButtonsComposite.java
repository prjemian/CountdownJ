package org.jemian.countdownj;

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Create the button panels for the CountdownJ project
 * @author Pete
 *
 */
public class ButtonsComposite extends Composite {

	Hashtable<String, Button> buttons;

	/**
	 * Create a Composite with the specified number of buttons.
	 * @param gui  : enables gui.buttonResponder(btn, btn.getText());
	 * @param parent : for the Composite
	 * @param label : btn.setText("<" + label + " " + i + ">");
	 * @param qty: makes this many buttons
	 */
	public ButtonsComposite(Gui gui, Composite parent, String label, int qty) {
		super(parent, SWT.NONE);
		this.setLayout(new FillLayout());

		final Gui top = gui;
		buttons = new Hashtable<String, Button>();

		for (int i = 0; i < qty; i++) {
			final Button btn = new Button(this, SWT.PUSH);
			btn.setText("<" + label + " " + i + ">");
			btn.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					switch (e.type) {
					case SWT.Selection:
						top.buttonResponder(btn, btn.getText());
						break;
					}
				}
			});
			String key = String.format("%s-%d", label, i);
			buttons.put(key, btn);
		}
	}

	/**
	 * set the text of the button identified by key
	 * @param key
	 * @param text
	 */
	public void setButtonText(String key, String text) {
		if (buttons.containsKey(key)) {
			Button btn = buttons.get(key);
			btn.setText(text);
		}
	}

	/**
	 * Get the set of buttons.
	 * The keys are based on the initial button text defined in the constructor.
	 * @return Enumeration<String>
	 */
	public Enumeration<String> getButtonKeys() {
		return buttons.keys();
	}

	/**
	 * return the button identified by key
	 * @param key
	 * @return Button object or null
	 */
	public Button getButton(String key) {
		Button result = null;
		if (buttons.containsKey(key)) {
			result = buttons.get(key);
		}
		return result;
	}

	/**
	 * return the enumeration key for the Button
	 * @param btn
	 * @return key (String) or empty string
	 */
	public String getButtonKey(Button btn) {
		String result = "";
		if (buttons.contains(btn)) {
			for (Enumeration<String> e = getButtonKeys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				if (btn == buttons.get(key)) {
					result = key;
					break;
				}
			}
		}
		return result;
	}
}
