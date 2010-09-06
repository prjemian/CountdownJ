package org.jemian.countdownj.Swing;

// CountdownJ, (c) 2010 Pete R. Jemian, See LICENSE (GPLv3) for details

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

public interface TimerEvent {

	// timers will call this method
	public void doTimerEvent(String str);

}
