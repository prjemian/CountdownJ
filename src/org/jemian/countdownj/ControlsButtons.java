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

	public ControlsButtons(Composite parent) {
		super(parent, SWT.BORDER);
		this.setLayout(new FillLayout());

		for (int i = 0; i < 4; i++) {
			final Button b1 = new Button(this, SWT.PUSH);
			b1.setText("button " + i);
			b1.addListener(SWT.Selection, new Listener() {
				  public void handleEvent(Event e) {
				    switch (e.type) {
				    case SWT.Selection:
				      System.out.println("<" + b1.getText() + ">");
				      break;
				    }
				  }
				});
		}

		final Button start = new Button(this, SWT.PUSH);
		start.setText("start");
		start.addListener(SWT.Selection, new Listener() {
			  public void handleEvent(Event e) {
			    switch (e.type) {
			    case SWT.Selection:
			      System.out.println("<start>");
			      break;
			    }
			  }
			});

		final Button stop = new Button(this, SWT.PUSH);
		stop.setText("stop");
		stop.addListener(SWT.Selection, new Listener() {
			  public void handleEvent(Event e) {
			    switch (e.type) {
			    case SWT.Selection:
			      System.out.println("<stop>");
			      break;
			    }
			  }
			});
	}

}
