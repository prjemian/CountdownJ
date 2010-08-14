package example.code;

//Send questions, comments, bug reports, etc. to the authors:

//Rob Warner (rwarner@interspatial.com)
//Robert Harris (rbrt_harris@yahoo.com)

//http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/FormLayoutComplex.htm

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;

public class MyFormLayoutTest {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		FormLayout layout = new FormLayout();
		shell.setLayout(layout);
		
		int inset = 5;

		Button one = new Button(shell, SWT.PUSH);
		one.setText("One");
		FormData data = new FormData();
		data.top = new FormAttachment(0, inset);
		data.left = new FormAttachment(0, inset);
		data.bottom = new FormAttachment(70, -inset);
		data.right = new FormAttachment(50, -inset);
		one.setLayoutData(data);

		Composite composite = new Composite(shell, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		composite.setLayout(gridLayout);
		Button two = new Button(composite, SWT.PUSH);
		two.setText("two");
		GridData gridData = new GridData(GridData.FILL_BOTH);
		two.setLayoutData(gridData);
		Button three = new Button(composite, SWT.PUSH);
		three.setText("three");
		gridData = new GridData(GridData.FILL_BOTH);
		three.setLayoutData(gridData);
		Button four = new Button(composite, SWT.PUSH);
		four.setText("four");
		gridData = new GridData(GridData.FILL_BOTH);
		four.setLayoutData(gridData);
		data = new FormData();
		data.top = new FormAttachment(0, inset);
		data.left = new FormAttachment(one, inset);
		data.bottom = new FormAttachment(50, -inset);
		data.right = new FormAttachment(100, -inset);
		composite.setLayoutData(data);

		Button five = new Button(shell, SWT.PUSH);
		five.setText("five");
		FormData data5 = new FormData();
		data5.top = new FormAttachment(one, inset);
		data5.left = new FormAttachment(0, inset);
//		data5.bottom = new FormAttachment(80, -inset);
		data5.right = new FormAttachment(100, -inset);
//		five.setLayoutData(data5);

		Button six = new Button(shell, SWT.PUSH);
		six.setText("six");
		data = new FormData();
		//data.top = new FormAttachment(five, inset);
		data.left = new FormAttachment(0, inset);
		data.bottom = new FormAttachment(100, -inset);
		data.right = new FormAttachment(100, -inset);
		six.setLayoutData(data);

		data5.bottom = new FormAttachment(six, -inset);
		five.setLayoutData(data5);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
