package example.callbacks;

import org.jemian.countdownj.ClockTimer;
import org.jemian.countdownj.TimerEventImpl;

//TODO needs copyright and license header

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

/**
 * Demonstrate how the ClockTimer is used.
 * @author Pete
 *
 */
public class TestTimer extends TimerEventImpl {

	/**
	 * Save the timer object so we can stop it programatically
	 * @param timer
	 */
	public void setTimer(ClockTimer timer) {
		this.timer = timer;
	}

	@Override
	public void doTimerEvent(String str) {
		// example event handler
		String cl = getClass().getCanonicalName();
		String format =  cl + ".doTimerEvent() %s <%.2f>";
		String msg = String.format(format, str, timer.getTime_s());
		System.out.println(msg);
		if (timer.getTime_s() < -2)
			timer.stop();
	}

	/**
	 * Demonstrate the ClockTimer
	 * @param args
	 */
	public static void main(String args[]) {
		TestTimer test = new TestTimer();
		ClockTimer timer = new ClockTimer(test);
		test.setTimer(timer);
		timer.setTime_s(6);
		timer.start();
	}

	ClockTimer timer;
}
