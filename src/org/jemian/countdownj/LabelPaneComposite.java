package org.jemian.countdownj;

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class LabelPaneComposite extends Composite {
	Label t;
	Device device;
	final Color black;
	final Color white;
	final Color red;
	final Color yellow;
	final Color green;

	public LabelPaneComposite(Composite parent) {
		super(parent, SWT.NONE);
		this.setLayout(new FillLayout());

		int options = SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL;
		options = SWT.CENTER | SWT.READ_ONLY;

		this.t = new Label(this, options);
		this.t.setText("initial text string");

		this.device = parent.getDisplay();

		this.black = new Color(device, 0, 0, 0);
		this.white = new Color(device, 255, 255, 255);
		this.red = new Color(device, 255, 0, 0);
		this.yellow = new Color(device, 255, 255, 0);
		this.green = new Color(device, 0, 255, 0);

		this.t.setForeground(this.white);
		this.t.setBackground(this.black);

		this.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Resize:
					SetTextSize();
					// System.out.println("composite resized");
					break;
				}
			}
		});
	}

	public void SetTextSize() {
		Font f = this.t.getFont();
		FontData fd = f.getFontData()[0];
		Rectangle clientRect = this.getClientArea();
		// Layout layout = this.getLayout();

		int height = clientRect.height;
		if (height == 0)
			height = 20;
		fd.setHeight(height / 2);

		Font font = new Font(this.device, fd);
		this.t.setFont(font);
	}

	public void SetText(String text) {
		this.t.setText(text);
	}

	public String GetText() {
		return this.t.getText();
	}

	public void SetForegroundColor(Color color) {
		this.t.setForeground(color);
	}

	public void SetNamedForegroundColor(String colorName) {
		Color color = this.white;
		if (colorName == "black")
			color = this.black;
		if (colorName == "white")
			color = this.white;
		if (colorName == "red")
			color = this.red;
		if (colorName == "yellow")
			color = this.yellow;
		if (colorName == "green")
			color = this.green;
		this.SetForegroundColor(color);
	}

}