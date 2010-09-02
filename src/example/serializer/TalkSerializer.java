package example.serializer;

import java.io.*;

import org.jemian.countdownj.Swing.TalkConfiguration;

public class TalkSerializer {
	public static void main(String[] args) {
		TalkConfiguration talk = new TalkConfiguration();
		talk.setAudible(true);
		talk.setPresentation(5 * 60);
		talk.setDiscussion(60);
		talk.setOvertime(15);
		talk.setMsg_pretalk("coming up next");
		talk.setMsg_presentation("listen up");
		talk.setMsg_discussion("questions?");
		talk.setMsg_overtime("stop");
		talk.setMsg_paused("... waiting ...");
		talk.setName("5 minutes");
		try {
			FileOutputStream fileOut = new FileOutputStream("talkconfig.serial");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(talk);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
}
