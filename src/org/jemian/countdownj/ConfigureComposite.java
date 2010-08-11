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

public class ConfigureComposite extends Composite {

	public ConfigureComposite(Composite parent) {
		super(parent, SWT.BORDER);
		this.setLayout(new FillLayout());
		final Button b1 = new Button(this, SWT.PUSH);
		b1.setText("configure");
	}

}
