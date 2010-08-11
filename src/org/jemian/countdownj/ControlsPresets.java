package org.jemian.countdownj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ControlsPresets extends Composite {

	public ControlsPresets(Composite parent) {
		super(parent, SWT.BORDER);
		this.setLayout(new FillLayout());

		for (int i = 0; i < 4; i++) {
			final Button b1 = new Button(this, SWT.PUSH);
			b1.setText("button " + i);
		}

		final Button start = new Button(this, SWT.PUSH);
		start.setText("start");

		final Button stop = new Button(this, SWT.PUSH);
		stop.setText("stop");
	}

}
