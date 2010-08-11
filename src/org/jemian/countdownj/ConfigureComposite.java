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

public class ConfigureComposite extends Composite {

	public ConfigureComposite(Gui gui, Composite parent) {
		super(parent, SWT.NONE);
		this.setLayout(new FillLayout());

		final Gui top = gui;

		final Button b1 = new Button(this, SWT.PUSH);
		b1.setText("<configure>");

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

}
