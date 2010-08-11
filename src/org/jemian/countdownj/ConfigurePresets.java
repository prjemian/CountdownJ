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

public class ConfigurePresets extends Composite {

	public ConfigurePresets(Composite parent) {
		super(parent, SWT.BORDER);
		this.setLayout(new FillLayout());

		for (int i = 0; i < 4; i++) {
			final Button b1 = new Button(this, SWT.PUSH);
			b1.setText("button " + i);
		}
	}

}
