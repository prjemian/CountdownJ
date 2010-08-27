package org.jemian.countdownj.Swing;
 
/*
Java Swing, 2nd Edition
By Marc Loy, Robert Eckstein, Dave Wood, James Elliott, Brian Cole
ISBN: 0-596-00408-7
Publisher: O'Reilly 
*/
// HBoxWithStrut.java
//Another test of the box layout manager using the Box utility class.
//This version separates several components with a fixed width gap.
//

// http://www.java2s.com/Code/Java/Swing-JFC/BoxlayoutmanagerusingtheBoxutilityclass.htm

import java.awt.Button;

import javax.swing.Box;
import javax.swing.JFrame;

public class HBoxWithStrut extends JFrame {

  public HBoxWithStrut() {
    super("Box & Strut Frame");
    setSize(370, 80);
    Box box = Box.createHorizontalBox();
    setContentPane(box);
    for (int i = 0; i < 3; i++) {
      Button b = new Button("B" + i);
      box.add(b);
    }

    // Add a spacer between the first three buttons and the last three
    box.add(Box.createHorizontalStrut(10));
    for (int i = 3; i < 6; i++) {
      Button b = new Button("B" + i);
      box.add(b);
    }
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }

  public static void main(String args[]) {
    HBoxWithStrut bt = new HBoxWithStrut();
  }
}
