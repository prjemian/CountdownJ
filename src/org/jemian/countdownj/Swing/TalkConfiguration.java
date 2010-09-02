package org.jemian.countdownj.Swing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.devx.tip13341.DeepCopyMaker;

//TODO needs copyright and license header

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

/**
 * Configuration parameters for running the clock timer for a single presentation
 */
public class TalkConfiguration implements Serializable {
	/**
	 * Instance of the TalkConfiguration parameters for a single presentation
	 */
	public TalkConfiguration() {
		setDefaults();
	}
	
	/**
	 * set all private fields to default values,
	 * as defined in this method
	 */
	public void setDefaults() {
    	name = "talk";
		audible = true;
		discussion = 5*60;
    	overtime = 60;
		presentation = 15*60;
    	msg_discussion = "Discussion";
    	msg_overtime = "Overtime";
    	msg_paused = "Paused";
    	msg_presentation = "Presentation";
    	msg_pretalk = "Next Presentation";
	}
	
	public String toString() {
		StringBuffer buff = new StringBuffer("");
		String format = " %s=\"%s\""; 
		buff.append(String.format(format, "name", name));
		buff.append(String.format(format, "audible", audible));
		buff.append(String.format(format, "discussion", secondsToMmss(discussion)));
		buff.append(String.format(format, "overtime", secondsToMmss(overtime)));
		buff.append(String.format(format, "presentation", secondsToMmss(presentation)));
		buff.append(String.format(format, "msg_discussion", msg_discussion));
		buff.append(String.format(format, "msg_overtime", msg_overtime));
		buff.append(String.format(format, "msg_paused", msg_paused));
		buff.append(String.format(format, "msg_presentation", msg_presentation));
		buff.append(String.format(format, "msg_pretalk", msg_pretalk));
		return buff.toString();
	}
	
	/**
	 * Serializable: receives object from stream
	 * used by Serializable objects
	 * @see http://java.sun.com/developer/technicalArticles/Programming/serialization/
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) 
	throws IOException, ClassNotFoundException {
		// our "pseudo-constructor"
		in.defaultReadObject();
	}
	
	/**
	 * Serializable: sends object to stream
	 * used by Serializable objects
	 * @see http://www.tutorialspoint.com/java/java_serialization.htm
	 * @see http://www.devx.com/tips/Tip/13341
	 * also:
	 * @see http://java.sun.com/developer/technicalArticles/Programming/serialization/
	 * @see http://java.sun.com/j2se/1.4.2/docs/guide/serialization/index.html
	 * @see http://www.javaworld.com/javaworld/javatips/jw-javatip76.html
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream out) 
	throws IOException {
		out.defaultWriteObject();
	}

	/**
	 * localize & standardize the call to deepcopy 
	 * routine using Serializable implementation
	 * @param talk
	 * @return
	 */
	public TalkConfiguration deepCopy() {
		Object object = null;
		try {
			object = DeepCopyMaker.makeDeepCopy(this);
		} catch (Exception e) {
			// This should not ever fail.  Sound the alarm if it does.
			e.printStackTrace();
		}
		return (TalkConfiguration) object;
	}

	/**
	 * @return
	 */
	public String getPresentationStr() {
		return secondsToMmss(this.presentation);
	}

	/**
	 * @param discussion
	 */
	public void setPresentationStr(String presentation) {
		this.presentation = mmssToSeconds(presentation);
	}

	/**
	 * @return
	 */
	public String getDiscussionStr() {
		return secondsToMmss(this.discussion);
	}

	/**
	 * @param discussion
	 */
	public void setDiscussionStr(String discussion) {
		this.discussion = mmssToSeconds(discussion);
	}

	/**
	 * @return
	 */
	public String getOvertimeStr() {
		return secondsToMmss(this.overtime);
	}

	/**
	 * @param overtime
	 */
	public void setOvertimeStr(String overtime) {
		this.overtime = mmssToSeconds(overtime);
	}

	/**
	 * @return
	 */
	public int getPresentation() {
		return presentation;
	}

	/**
	 * @param discussion
	 */
	public void setPresentation(int presentation) {
		this.presentation = presentation;
	}

	/**
	 * @return
	 */
	public int getDiscussion() {
		return discussion;
	}

	/**
	 * @param discussion
	 */
	public void setDiscussion(int discussion) {
		this.discussion = discussion;
	}

	/**
	 * @return
	 */
	public int getOvertime() {
		return overtime;
	}

	/**
	 * @param overtime
	 */
	public void setOvertime(int overtime) {
		this.overtime = overtime;
	}

	/**
	 * @return
	 */
	public String getMsg_pretalk() {
		return msg_pretalk;
	}

	/**
	 * @param msgPretalk
	 */
	public void setMsg_pretalk(String msgPretalk) {
		msg_pretalk = msgPretalk;
	}

	/**
	 * @return
	 */
	public String getMsg_presentation() {
		return msg_presentation;
	}

	/**
	 * @param msgPresentation
	 */
	public void setMsg_presentation(String msgPresentation) {
		msg_presentation = msgPresentation;
	}

	/**
	 * @return
	 */
	public String getMsg_discussion() {
		return msg_discussion;
	}

	/**
	 * @param msgDiscussion
	 */
	public void setMsg_discussion(String msgDiscussion) {
		msg_discussion = msgDiscussion;
	}

	/**
	 * @return
	 */
	public String getMsg_overtime() {
		return msg_overtime;
	}

	/**
	 * @param msgOvertime
	 */
	public void setMsg_overtime(String msgOvertime) {
		msg_overtime = msgOvertime;
	}

	/**
	 * @return
	 */
	public String getMsg_paused() {
		return msg_paused;
	}

	/**
	 * @param msgPaused
	 */
	public void setMsg_paused(String msgPaused) {
		msg_paused = msgPaused;
	}

	/**
	 * @return if client tool should make noise at planned intervals
	 */
	public boolean isAudible() {
		return audible;
	}

	/**
	 * @param audible Client tool should make noise at planned intervals
	 */
	public void setAudible(boolean audible) {
		this.audible = audible;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * converts integer seconds into mm:ss format
	 * @param seconds
	 * @return mm:ss String
	 */
	private String secondsToMmss(int seconds) {
		return String.format("%d:%02d", seconds/60, seconds%60);
	}

	/**
	 * converts mm:ss String into integer seconds
	 * @param mmss mm:ss String
	 * @return seconds or -1 if NumberFormatException
	 */
	private int mmssToSeconds(String mmss) {
		int seconds = 0;
		if (mmss.length() > 0) {
			String[] split = mmss.split(":");
			switch (split.length) {
			case 1:
				try {
					seconds = Integer.parseInt(mmss);
				} catch (NumberFormatException e) {
					seconds = -1;	// this is an error in mmss format
				}
				break;
	
			case 2:
				try {
					int mm = Integer.parseInt(split[0]);
					int ss = Integer.parseInt(split[1]);
					seconds = mm*60 + ss;
				} catch (NumberFormatException e) {
					seconds = -1;	// this is an error in mmss format
				}
				break;
	
			default:
				seconds = -1;	// this is an error in mmss format
				break;
			}
		} else 
			seconds = 0;  // empty string
		return seconds;
	}

	private String name;
	private int presentation;
	private int discussion;
	private int overtime;
	private String msg_pretalk;
	private String msg_presentation;
	private String msg_discussion;
	private String msg_overtime;
	private String msg_paused;
	private boolean audible;

	/**
	 * Serializable objects need this to 
	 * identify when the interface changes.
	 */
	private static final long serialVersionUID = 1160508479712297454L;}
