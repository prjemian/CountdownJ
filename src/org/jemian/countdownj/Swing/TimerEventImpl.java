package org.jemian.countdownj.Swing;

public class TimerEventImpl implements TimerEvent {

	@Override
	public void doTimerEvent(String str) {
		// fallback implementation of the callback routine
		String cl = getClass().getCanonicalName();
		System.out.println(cl + ".doTimerEvent() (fallback) " + str);
		
	}

}
