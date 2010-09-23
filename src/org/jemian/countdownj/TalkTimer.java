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

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import java.util.HashMap;

public class TalkTimer extends TimerEventImpl {

	public TalkTimer(CountdownJ countdownJ, TalkConfiguration talk) {
		// TalkTimer will call display.doDisplayCallback() with updates
		this.display = countdownJ;
		setTalk(talk);
		clockTimer = new ClockTimer(this);
		countdownJ = null;
		lastPhase = phase = PHASE_PRETALK;
		paused = false;
		messageTable = new HashMap<Integer, String>();
		colorTable = new HashMap<Integer, String>();

		/*
		 * HashMap is faster than a switch but means that 
		 * these values are set at the start of a talk.
		 * And, the code is cleaner.
		 */
		messageTable.put(PHASE_PRETALK, talk.getMsg_pretalk());
		messageTable.put(PHASE_PRESENTATION, talk.getMsg_presentation());
		messageTable.put(PHASE_DISCUSSION, talk.getMsg_discussion());
		messageTable.put(PHASE_OVERTIME, talk.getMsg_overtime());
		messageTable.put(PHASE_PAUSED, talk.getMsg_paused());
		colorTable.put(PHASE_PRETALK, "white");
		colorTable.put(PHASE_PRESENTATION, "green");
		colorTable.put(PHASE_DISCUSSION, "yellow");
		colorTable.put(PHASE_OVERTIME, "red");
		colorTable.put(PHASE_PAUSED, "lightblue");

		setClockTime(talk.getPresentation());
	}

	public void doTimerEvent(String mmss) {
		time = getClockTime();
		phase = calcPhase();		// always the first things to do here

		String msgTextStr = calcMsgText();
		int numBeeps = calcNumBeeps();
		String color = calcColor();

		if (display != null) {
			display.doDisplayCallback(time, mmss, msgTextStr, numBeeps, color);
		} else {
			// only called during execution of TalkTimer.main()
			String fmt = "doTimerEvent(): %g %d  %s  %s  %d  %s";
			String msg = String.format(fmt, time, phase, mmss, msgTextStr, numBeeps, color);
			System.out.println(msg);
			if (time < -5)
				stop();
		}

		lastPhase = phase;	// always the last things to do here
		lastTime = time;
	}

	/**
	 * determine at which phase of the talk we are in
	 * @return
	 */
	private int calcPhase() {
		int localPhase = PHASE_PRETALK;
		if (isCounting()) {
			if (time < 0) 
				localPhase = PHASE_OVERTIME;
			else {
				localPhase = (time > talk.getDiscussion()) 
					? PHASE_PRESENTATION : PHASE_DISCUSSION;
			}
		} else {
			localPhase = (paused) ? PHASE_PAUSED: PHASE_PRETALK;
		}
		return localPhase;
	}

	/**
	 * text widget below the time
	 * @return
	 */
	private String calcMsgText() {
		String msg = messageTable.get(phase);
		if (!isCounting() && (getClockTime() == 0))
			msg = "";
		return msg;
	}

	/**
	 * beep more when time is running out
	 * @return
	 */
	private int calcNumBeeps() {
		int numBeeps = 0;
		if (talk.isAudible()) {
			switch (phase) {
				case PHASE_DISCUSSION:
					if (lastPhase == PHASE_PRESENTATION)
						numBeeps = 1;
					break;
		
				case PHASE_OVERTIME:
					if (lastPhase == PHASE_DISCUSSION)
						numBeeps = 2;
					else if (lastPhase == PHASE_OVERTIME) {
						int overtimeReminder_s = talk.getOvertime();
						double t1 = Math.abs(time) % overtimeReminder_s;
						double t2 = Math.abs(lastTime) % overtimeReminder_s;
						if ( t1 < t2)
							numBeeps = 3;				
					}
					break;
			}
		}
		return numBeeps;
	}

	/**
	 * color for the display widgets
	 * @return
	 */
	private String calcColor() {
		return colorTable.get(phase);
	}
	
	/**
	 * Have the GUI identify itself here.
	 * This code will call it through display.doDisplayCallback()
	 * @param display
	 */
	public void setDisplay(CountdownJ display) {
		this.display = display;
	}

	/**
	 * start the talk
	 */
	public void start() {
		paused = false;
		clockTimer.start();
		lastTime = talk.getPresentation();
	}

	/**
	 * pause the talk
	 */
	public void pause() {
		clockTimer.stop();
		paused = true;
	}

	/**
	 * end the talk
	 */
	public void stop() {
		clockTimer.stop();
		paused = false;
	}

	/**
	 * @return the talk configuration
	 */
	public TalkConfiguration getTalk() {
		return talk;
	}

	/**
	 * @param talk set the talk configuration
	 */
	public void setTalk(TalkConfiguration talk) {
		this.talk = talk.deepCopy();
	}

	/**
	 * @return the clockTime (seconds)
	 */
	public double getClockTime() {
		return clockTimer.getTime_s();
	}
	
	/**
	 * clear the timer's counter
	 */
	public void clearCounter() {
		clockTimer.clearCounter();
		talk.setPresentation(0);	// should this happen?
	}

	/**
	 * @param seconds the clockTime (seconds) to set
	 */
	public void setClockTime(double seconds) {
		clockTimer.setTime_s(seconds);
	}
	
	public void incrementTime(int seconds) {
        if (!isCounting()) {
            clockTimer.incrTime_s(seconds);
            clockTimer.update();
        }
	}
	
	public boolean isCounting() {
        return clockTimer.isCounting();
	}
	
	public boolean isPaused() {
        return paused;
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		TalkConfiguration config = new TalkConfiguration();
		config.setPresentation(11);
		config.setDiscussion(5);
		config.setOvertime(2);
		TalkTimer talk = new TalkTimer(null, config);
		talk.setClockTime(14);
		talk.start();
		while (talk.getClockTime() > 0) {
			Thread.sleep(1000);
		}
		System.out.println("main() done");
	}

	private int phase;
	private int lastPhase;
	private double time;
	private double lastTime;
	private CountdownJ display;
	private TalkConfiguration talk;
	private ClockTimer clockTimer;
	private HashMap<Integer, String> messageTable;
	private HashMap<Integer, String> colorTable;
	private boolean paused;

	public static final int PHASE_PRETALK = 0;
	public static final int PHASE_PRESENTATION = 1;
	public static final int PHASE_DISCUSSION  = 2;
	public static final int PHASE_OVERTIME  = 3;
	public static final int PHASE_PAUSED  = 4;
}
