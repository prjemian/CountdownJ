package org.jemian.countdownj.Swing;

//TODO needs copyright and license header

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

public class TimerEventImpl implements TimerEvent {

	@Override
	public void doTimerEvent(String str) {
		// fallback implementation of the callback routine
		String cl = getClass().getCanonicalName();
		System.out.println(cl + ".doTimerEvent() (fallback) " + str);
		
	}

}
