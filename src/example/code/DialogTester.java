package example.code;

/*
 * Spinner example snippet: create and initialize a spinner widget
 * 
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jemian.countdownj.ConfigureDialog;

public class DialogTester {

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);

    ConfigureDialog cd = new ConfigureDialog(shell);
    if (cd.open()) {
	    System.out.println("discussion (s): " + cd.getDiscussionTime_s());
	    System.out.println("overtime reminder (s): " + cd.getOvertimeReminder_s());
    }

    display.dispose();
  }
}
