package example.serializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.jemian.countdownj.TalkConfiguration;

public class TalkDeserializer {
	public static void main(String[] args) {
		TalkConfiguration talk = null;
		try {
			FileInputStream fileIn = new FileInputStream("talkconfig.serial");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			talk = (TalkConfiguration) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Employee class not found");
			c.printStackTrace();
			return;
		}
		System.out.println("Deserialized TalkConfiguration...\n" + talk);
	}
}
