package example.code;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Label;

public class RadioButtonTest {
	public static void main(String[] args) {
		Frame f = new Frame("RadioButton Group");
		Label l1 = new Label("What is your rating:");
		f.setLayout(new GridLayout(0, 1));
		CheckboxGroup group = new CheckboxGroup();
		f.add(l1);
		f.add(new Checkbox("Excellent", group, true));
		f.add(new Checkbox("VeryGood", group, false));
		f.add(new Checkbox("Good", group, false));
		f.add(new Checkbox("Average", group, false));
		f.add(new Checkbox("Poor", group, false));
		f.setSize(250, 200);
		f.setVisible(true);
		f.pack();
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}
}