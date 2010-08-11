package org.jemian.countdownj;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Operate the clock 
 * generate updates when the clock is running
 * @author Pete
 *
 */
public class ClockTimer {

	Timer timer;
	boolean counting = false;
	double counter = 0;
	int interval_ms = 1000;
	double endTime = 0;
	int initialDelay = 0;
	Gui caller = null;

	/**
	 * @param timer : java.util.Timer object
	 * @param counting : flag to describe if the timer is running
	 * @param counter : the preset or remaining time
	 * @param interval_ms : reporting interval
	 * @param endTime : (internal) when the presentation should end
	 * @param initialDelay : always ZERO for this class
	 * @param caller : to call caller.callbackFunction("mm:ss") every interval_ms
	 */
	public ClockTimer(Gui callback) {
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
	class RemindTask extends TimerTask {

		ClockTimer ct;

		/**
		 * pass and save the reference of the outer class
		 * @param clockTimer
		 */
		public RemindTask(ClockTimer clockTimer) {
			this.ct = clockTimer;
		}

		/**
		 * called periodically by the running timer
		 */
		public void run() {
			ct.counter = ct.endTime - ct._now_();
			String mmss = ct.toString();
			if (ct.caller != null) {
				ct.caller.callbackFunction(mmss);
			} else {
				// development only
				System.out.println("ClockTimer: " + mmss);
			}
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
	public double GetCounter() {
		return counter;
	}
	
	/**
	 * define the programmed countdown interval
	 */
	public void SetCounter(double seconds) {
		if (!counting)  counter = seconds;
	}
	
	/**
	 * increment the programmed countdown interval
	 */
	public void AddCounter(double seconds) {
		if (!counting)  counter += seconds;
	}
	
	/**
	 * zero out the programmed countdown interval
	 */
	public void ClearCounter() {
		SetCounter(0);
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
		endTime = _now_() + counter;
        counting = true;
		timer = new Timer();
		timer.schedule(new RemindTask(this), initialDelay, interval_ms);
	}
	
	/**
	 * stop the countdown clock
	 */
	public void stop() {
        counting = false;
        timer.cancel();
	}
	
	/**
	 * render the value of interval as mm:ss
	 */
	public String toString() {
		int basis = Math.abs((int) (counter + 0.5));		// roundoff
		int h = basis / 60;
		int m = basis % 60;
		String mmss = String.format("%02d", h) 
					+ ":" 
					+ String.format("%02d", m);
		return mmss;
	}

	/**
	 * test this routine (needs to be interrupted with ^C or similar)
	 * @param args
	 */
	public static void main(String args[]) {
		System.out.println("About to schedule task.");
		ClockTimer ct = new ClockTimer(0);
		ct.SetCounter(15);
		ct.start();
		System.out.println("Task scheduled.");
	}

}
