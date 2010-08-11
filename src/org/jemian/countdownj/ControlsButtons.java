package org.jemian.countdownj;

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ControlsButtons extends Composite {

	public ControlsButtons(Gui gui, Composite parent, String label) {
		super(parent, SWT.BORDER);
		this.setLayout(new FillLayout());

		final Gui top = gui;

		for (int i = 0; i < 4; i++) {
			final Button b1 = new Button(this, SWT.PUSH);
			b1.setText("<" + label + " " + i + ">");
			b1.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					switch (e.type) {
					case SWT.Selection:
						top.buttonResponder(b1, b1.getText());
						break;
					}
				}
			});
		}

		final Button start = new Button(this, SWT.PUSH);
		start.setText("<" + label + " start>");
		start.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					top.buttonResponder(start, start.getText());
					break;
				}
			}
		});

		final Button stop = new Button(this, SWT.PUSH);
		stop.setText("<" + label + " stop>");
		stop.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					top.buttonResponder(stop, stop.getText());
					break;
				}
			}
		});
	}

	public void setButtonText(String label, String text) {
		
	}
}
