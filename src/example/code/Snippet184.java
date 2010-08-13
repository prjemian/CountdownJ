package example.code;

/*
 * Spinner example snippet: create and initialize a spinner widget
 * 
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.jemian.countdownj.ConfigureDialog;

public class Snippet184 {

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);

    //Spinner spinner = new Spinner(shell, SWT.BORDER);
    //spinner.setMinimum(0);
    //spinner.setMaximum(1000);
    //spinner.setSelection(500);
    //spinner.setIncrement(1);
    //spinner.setPageIncrement(100);
    //spinner.pack();

    ConfigureDialog cd = new ConfigureDialog(shell);
    int ans = cd.open();
    System.out.println("discussion (s): " + cd.getDiscussionTime_s());
    System.out.println("overtime reminder (s): " + cd.getOvertimeReminder_s());

    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}
