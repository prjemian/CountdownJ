package example.code;


/* From http://java.sun.com/docs/books/tutorial/index.html */
/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Schedule a task that executes once every second.
 */

public class AnnoyingBeep {
  Toolkit toolkit;

  Timer timer;

  public AnnoyingBeep() {
    toolkit = Toolkit.getDefaultToolkit();
    timer = new Timer();
    timer.schedule(new RemindTask(), 0, //initial delay
        1 * 1000); //subsequent rate
  }

  class RemindTask extends TimerTask {
    int numWarningBeeps = 3;

    public void run() {
      if (numWarningBeeps > 0) {
        toolkit.beep();
        System.out.println("Beep!");
        numWarningBeeps--;
      } else {
        toolkit.beep();
        System.out.println("Time's up!");
        //timer.cancel(); //Not necessary because we call System.exit
        System.exit(0); //Stops the AWT thread (and everything else)
      }
    }
  }

  public static void main(String args[]) {
    System.out.println("About to schedule task.");
    new AnnoyingBeep();
    System.out.println("Task scheduled.");
  }
}
