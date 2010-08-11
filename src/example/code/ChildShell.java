package example.code;

import org.eclipse.swt.widgets.*;

/**
 * ChildShell -- looks like a dialog box
 * @author Pete
 *
 */
public class ChildShell {
	ChildShell(Shell parent) {
		Shell child = new Shell(parent);
		child.setSize(200, 200);
		child.open();
	}
}