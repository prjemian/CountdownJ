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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class ConfigureDialog extends Dialog {

	int overtimeReminder_s;
	int discussionTime_s;
	String dialogTitle;

	public ConfigureDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	public ConfigureDialog(Shell parent, int style) {
		super(parent, style);
		setText("Input Dialog");
		setDiscussionTime_s(5*60);		// fallback default: 5 minutes
		setOvertimeReminder_s(60);		// fallback default: 1 minute
		setText("A Dialog");
	}

	public boolean open() {
		final Shell parent = getParent();
		int options = SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL;
		final Shell dialog = new Shell(parent, options);
		dialog.setSize(350, 100);
		dialog.setText(dialogTitle);
		int defaultDiscussionAllowanceMin = 10;
		int defaultDiscussionAllowanceMax = 90*60;
		int defaultReminderOTMin = 10;
		int defaultReminderOTMax = 24*60*60;
		
		final boolean[] result = new boolean[1];

		GridLayout layout = new GridLayout(2, true);
		layout.makeColumnsEqualWidth = false;
		dialog.setLayout(layout);

		GridData griddata = new GridData(GridData.FILL_HORIZONTAL);
		Label textDisc = new Label(dialog, 0);
		textDisc.setLayoutData(griddata);
		String fmt = "%s (seconds) [%d..%d]:"; 
		String msg = String.format(fmt, 
				"discussion allowance",
				defaultDiscussionAllowanceMin,
				defaultDiscussionAllowanceMax
				);
		textDisc.setText(msg);
	    final Spinner spinnerDisc = new Spinner(dialog, SWT.BORDER);
	    spinnerDisc.setMinimum(defaultDiscussionAllowanceMin);
	    spinnerDisc.setMaximum(defaultDiscussionAllowanceMax);
	    spinnerDisc.setSelection(discussionTime_s);
	    spinnerDisc.setIncrement(1);
	    spinnerDisc.setPageIncrement(100);

		griddata = new GridData(GridData.FILL_HORIZONTAL);
		Label textOT = new Label(dialog, 0);
		textOT.setLayoutData(griddata);
		fmt = "%s (seconds) [%d..%d]:"; 
		msg = String.format(fmt, 
				"Overtime Reminder beep interval",
				defaultReminderOTMin,
				defaultReminderOTMax
				);
	    textOT.setText(msg);
	    final Spinner spinnerOT = new Spinner(dialog, SWT.BORDER);
	    spinnerOT.setMinimum(defaultReminderOTMin);
	    spinnerOT.setMaximum(defaultReminderOTMax);
	    spinnerOT.setSelection(overtimeReminder_s);
	    spinnerOT.setIncrement(1);
	    spinnerOT.setPageIncrement(100);

	    Composite subComp = new Composite(dialog, 0);
	    FillLayout subFill = new FillLayout(SWT.HORIZONTAL);
	    subComp.setLayout(subFill);
	    new Label(subComp, 0);		// spacer
	    final Button okBtn = new Button(subComp, SWT.PUSH);
	    okBtn.setText("Ok");
	    new Label(subComp, 0);		// spacer
	    final Button cancelBtn = new Button(subComp, SWT.PUSH);
	    cancelBtn.setText("Cancel");
	    new Label(subComp, 0);		// spacer

	    Listener buttonListener = new Listener () {
			public void handleEvent (Event event) {
				result[0] = event.widget == okBtn;
				discussionTime_s = spinnerDisc.getSelection();
				overtimeReminder_s = spinnerOT.getSelection();
				dialog.close ();
				//System.out.println("result: " + result[0]);
			}
		};
		
		// TODO: Do we need this listener?  
		// Seems to be provided as default behavior.
//		Listener escapeListener = new Listener() {
//			public void handleEvent(Event event) {
//				switch (event.detail) {
//				case SWT.TRAVERSE_ESCAPE:
//					result[0] = event.widget == okBtn;
//					parent.close();
//					event.detail = SWT.TRAVERSE_NONE;
//					event.doit = false;
//					//System.out.println("result: " + result[0]);
//					break;
//				}
//			}
//		};

		//parent.addListener(SWT.Traverse, escapeListener);
		
		okBtn.addListener(SWT.Selection, buttonListener);
		cancelBtn.addListener(SWT.Selection, buttonListener);

	    dialog.open();
	    dialog.pack();
		Display display = parent.getDisplay();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result[0];
	}

	/**
	 * set the title of the dialog
	 * must call before open()
	 * @param text : dialog title
	 */
	public void setText(String text) {
		dialogTitle = text;
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