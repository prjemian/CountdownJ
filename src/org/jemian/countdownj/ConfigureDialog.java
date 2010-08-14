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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class ConfigureDialog extends Dialog {

	int overtimeReminder_s;
	int discussionTime_s;

	public ConfigureDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	public ConfigureDialog(Shell parent, int style) {
		super(parent, style);
		setText("Input Dialog");
		// TODO: where did these values come from?
		setOvertimeReminder_s(60);
		setDiscussionTime_s(5*60);
	}

	public int open() {
		Shell parent = getParent();
		Shell dialog = new Shell(parent, SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL);
		dialog.setSize(350, 100);
		dialog.setText("A Dialog");

		GridLayout layout = new GridLayout(2, true);
		layout.makeColumnsEqualWidth = false;
		dialog.setLayout(layout);

		GridData griddata = new GridData(GridData.FILL_HORIZONTAL);
		Label textOT = new Label(dialog, 0);
		textOT.setLayoutData(griddata);
	    textOT.setText("Overtime Reminder beep interval (seconds) [10..1000]: ");
	    Spinner spinnerOT = new Spinner(dialog, SWT.BORDER);
	    spinnerOT.setMinimum(10);
	    spinnerOT.setMaximum(1000);
	    spinnerOT.setSelection(overtimeReminder_s);
	    spinnerOT.setIncrement(1);
	    spinnerOT.setPageIncrement(100);

		griddata = new GridData(GridData.FILL_HORIZONTAL);
		Label textDisc = new Label(dialog, 0);
		textDisc.setLayoutData(griddata);
		textDisc.setText("discussion allowance (seconds) [0..10000]: ");
	    Spinner spinnerDisc = new Spinner(dialog, SWT.BORDER);
	    spinnerDisc.setMinimum(10);
	    spinnerDisc.setMaximum(1000);
	    spinnerDisc.setSelection(discussionTime_s);
	    spinnerDisc.setIncrement(1);
	    spinnerDisc.setPageIncrement(100);

	    dialog.open();
	    dialog.pack();
		Display display = parent.getDisplay();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return 42;
	}
	
	/**
	 * @return the overtimeReminder_s
	 */
	public int getOvertimeReminder_s() {
		return overtimeReminder_s;
	}

	/**
	 * @param overtimeReminder_s the overtimeReminder_s to set
	 */
	public void setOvertimeReminder_s(int overtimeReminder_s) {
		this.overtimeReminder_s = overtimeReminder_s;
	}

	/**
	 * @return the discussionTime_s
	 */
	public int getDiscussionTime_s() {
		return discussionTime_s;
	}

	/**
	 * @param discussionTime_s the discussionTime_s to set
	 */
	public void setDiscussionTime_s(int discussionTime_s) {
		this.discussionTime_s = discussionTime_s;
	}
	
}