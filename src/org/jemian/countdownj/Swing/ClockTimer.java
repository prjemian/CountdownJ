package org.jemian.countdownj.Swing;

// TODO needs copyright and license header

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import java.util.Timer;
import java.util.TimerTask;

/**
* Operate the clock for countdown
* generate periodic updates when the clock is running
* @author Pete
*
*/
public class ClockTimer {

	/**
	 * @param timer : java.util.Timer object
	 * @param counting : flag to describe if the timer is running
	 * @param time_s : the preset or remaining time
	 * @param interval_ms : reporting interval
	 * @param endTime : (internal) when the presentation should end
	 * @param initialDelay : always ZERO for this class
	 * @param caller : to call caller.doTimer("mm:ss") every interval_ms
	 * TODO target method caller.doTimer() will be moved to a new class
	 */
	public ClockTimer(GuiSwing callback) {
		caller = callback;
	}

	/**
	 * alternate interface (for development only)
	 * @param i
	 */
	public ClockTimer(int i) {
		caller = null;
	}

	/**
	 * receive the timer update
	 */
	class UpdateTask extends TimerTask {

		ClockTimer ct;

		/**
		 * pass and save the reference of the outer class
		 * @param clockTimer
		 */
		public UpdateTask(ClockTimer clockTimer) {
			// TODO: Can this be found without passing from the caller?
			this.ct = clockTimer;
		}

		/**
		 * called periodically (interval_ms) by the running timer
		 * calls the parent class update() method
		 */
		public void run() {
			ct.update();
		}
	}
	
	/**
	 * report if the timer is running
	 */
	public boolean isCounting() {
		return counting;
	}
	
	/**
	 * return the programmed or remaining countdown interval
	 * negative if overtime
	 */
	public double getTime_s() {
		return time_s;
	}
	
	/**
	 * define the programmed countdown interval
	 */
	public void setTime_s(double seconds) {
		if (!counting)  time_s = seconds;
	}
	
	/**
	 * increment the programmed countdown interval
	 */
	public void incrTime_s(double seconds) {
		if (!counting)  time_s += seconds;
	}
	
	/**
	 * zero out the programmed countdown interval
	 */
	public void clearCounter() {
		setTime_s(0);
		endTime = 0;
	}
	
	/**
	 * current system time as floating-point number
	 * @return
	 */
	private double _now_() {
		return System.currentTimeMillis()*0.001;
	}
	
	/**
	 * start the countdown clock
	 */
	public void start() {
		endTime = _now_() + time_s;
      counting = true;
		timer = new Timer();
		timer.schedule(new UpdateTask(this), initialDelay, interval_ms);
		update();
	}
	
	/**
	 * stop the countdown clock
	 */
	public void stop() {
      counting = false;
      endTime = 0;
      if (timer != null)
      	timer.cancel();
	}
	
	/**
	 * update the countdown clock
	 */
	public void update() {
		if (endTime > 0)
			time_s = endTime - _now_();
		String mmss;
		if (counting || time_s != 0)
			mmss = toString();
		else
			mmss = "";
		if (caller != null) {
			// TODO target method doTimer() will be moved to a new class
			caller.doTimer(mmss);
		} else {
			// development only
			System.out.println("ClockTimer: " + mmss);
		}
	}
	
	/**
	 * render the value of interval as mm:ss
	 */
	public String toString() {
		int basis = Math.abs((int) (time_s + 0.5));		// roundoff
		int m = basis / 60;
		int s = basis % 60;
		String mmss = String.format("%02d", m) 
					+ ":" 
					+ String.format("%02d", s);
		return mmss;
	}

	/**
	 * test this routine (needs to be interrupted with ^C or similar)
	 * @param args
	 */
	public static void main(String args[]) {
		System.out.println("About to schedule task.");
		ClockTimer ct = new ClockTimer(0);
		ct.setTime_s(15);
		ct.start();
		System.out.println("Task scheduled.");
	}

	private Timer timer;
	private boolean counting = false;
	private double time_s = 0;
	private int interval_ms = 50;
	private double endTime = 0;
	private int initialDelay = 0;
	private GuiSwing caller = null;
}
