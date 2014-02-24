package org.jemian.countdownj;

/*
    CountdownJ, (c) 2010 Pete R. Jemian <prjemian@gmail.com>
    See LICENSE (GPLv3) for details.
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
	 * @param timerEventCaller : to call timerEventCaller.doTimerEvent("mm:ss") every interval_ms
	 */
	public ClockTimer(TimerEventImpl callback) {
		timerEventCaller = callback;
	}

	/**
	 * alternate interface (for development only)
	 * @param i
	 */
	public ClockTimer(int i) {
		timerEventCaller = null;
	}

	/**
	 * receive the timer update
	 */
	static class UpdateTask extends TimerTask {

		ClockTimer ct;

		/**
		 * pass and save the reference of the outer class
		 * @param clockTimer
		 */
		public UpdateTask(ClockTimer clockTimer) {
			ct = clockTimer;
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
		if (!counting) {
			time_s = seconds;
			update();
		}
	}
	
	/**
	 * increment the programmed countdown interval
	 */
	public void incrTime_s(double seconds) {
		if (!counting) {
			time_s += seconds;
			update();
		}
	}
	
	/**
	 * zero out the programmed countdown interval
	 */
	public void clearCounter() {
		setTime_s(0);
		endTime = 0;
		update();
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
		if (timerEventCaller != null) {
			timerEventCaller.doTimerEvent(mmss);
		} else {
			// development only
			String format = "ClockTimer:  %s  <%.2f>";
			System.out.println(String.format(format, mmss, time_s));
			if (time_s <= -2)
				System.exit(0);
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
	 * test this routine (quits at time_s <= -2)
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
	private TimerEventImpl timerEventCaller;
	private boolean counting = false;
	private double time_s = 0;
	private int interval_ms = 50;
	private double endTime = 0;
	private int initialDelay = 0;
}
