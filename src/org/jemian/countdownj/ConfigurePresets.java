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

public class ConfigurePresets extends Composite {

	Hashtable<String,Button> buttons;

	public ConfigurePresets(Gui gui, Composite parent) {
		super(parent, SWT.NONE);
		this.setLayout(new FillLayout());

		final Gui top = gui;
		buttons = new Hashtable<String, Button>();

		for (int i = 0; i < 4; i++) {
			final Button btn = new Button(this, SWT.PUSH);
			btn.setText("<button " + i + ">");
			btn.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					switch (e.type) {
					case SWT.Selection:
						top.buttonResponder(btn, btn.getText());
						break;
					}
				}
			});
			String key = String.format("%s-%d", "configPre", i);
			buttons.put(key, btn);
		}
	}

	public void setButtonText(String key, String text) {
		if (buttons.containsKey(key)) {
			Button btn = buttons.get(key);
			btn.setText(text);
		}
	}

	public Enumeration<String> getButtonKeys() {
		return buttons.keys();
	}

	public Button getButton(String key) {
		Button result = null;
		if (buttons.containsKey(key)) {
			result = buttons.get(key);
		}
		return result;
	}

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
