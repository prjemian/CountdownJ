package example.xstream;

// http://xstream.codehaus.org/tutorial.html

import org.jemian.countdownj.Swing.TalkConfiguration;

import com.thoughtworks.xstream.XStream;

public class XmlTest {

	public static void main(String[] args) {
		TalkConfiguration talk = new TalkConfiguration();

		XStream xstream = new XStream();	
//		xstream.alias("talk", TalkConfiguration.class);
//		xstream.aliasAttribute(org.jemian.countdownj.Swing.TalkConfiguration.class, "name", "name");
//		xstream.useAttributeFor(org.jemian.countdownj.Swing.TalkConfiguration.class, "name");
		String xml = xstream.toXML(talk);
		System.out.println(xml);
		
		TalkConfiguration chat = (TalkConfiguration) xstream.fromXML(xml);
		System.out.println(talk);
		System.out.println(chat);
		System.out.println(chat.toString().compareTo(talk.toString()));
	}
}
