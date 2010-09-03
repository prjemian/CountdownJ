package org.jemian.countdownj.Swing;

//TODO needs copyright and license header

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

public class TalkTimer extends TimerEventImpl {

	public TalkTimer(TalkConfiguration talk) {
		setTalk(talk);
		clockTimer = new ClockTimer(this);
	}

	public void doTimerEvent(String str) {
		if (str.compareTo(lastStr) != 0) {
			System.out.println(str);
			lastStr = str;
			if (getClockTime() < -2) {
				stop();
				System.out.println("doTimerEvent() done");
			}
		}
	}

	public String calcMsgText() {
		double time_s = clockTimer.getTime_s();
		String msgTextStr = "";
		if (clockTimer.isCounting()) {
			if (time_s < 0) {
				msgTextStr = talk.getMsg_overtime();
			} else {
				if (time_s > talk.getDiscussion()) {
					msgTextStr = talk.getMsg_presentation();
				} else  {
					msgTextStr = talk.getMsg_discussion();
				}
			}
		}
		return msgTextStr;
	}

	/**
	 * start the talk
	 */
	public void start() {
		clockTimer.start();
	}

	/**
	 * end the talk
	 */
	public void stop() {
		clockTimer.stop();
	}

	/**
	 * @return the talk
	 */
	public TalkConfiguration getTalk() {
		return talk;
	}

	/**
	 * @param talk the talk to set
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
	 * @param seconds the clockTime (seconds) to set
	 */
	public void setClockTime(double seconds) {
		clockTimer.setTime_s(seconds);
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		TalkTimer object = new TalkTimer(new TalkConfiguration());
		object.setClockTime(11);
		object.start();
		while (object.getClockTime() > 0) {
			Thread.sleep(1000);
		}
		System.out.println("main() done");
	}

	private TalkConfiguration talk;
	private ClockTimer clockTimer;
	String lastStr = "";
}
