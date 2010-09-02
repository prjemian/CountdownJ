package com.devx.tip13341;

// http://www.devx.com/tips/Tip/13341

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jemian.countdownj.Swing.TalkConfiguration;

/**
 * Exploit Serialization To Perform Deep Copy
 * 
 * The clone() method of java.lang.Object class makes a 
 * shallow copy of an object i.e. a copy that excludes the 
 * objects that the to-be-cloned object contains. Therefore, 
 * to produce a deep copy of your complex objects, you have 
 * to write your own clone() method so that you take care 
 * of copying every contained object. This, depending on 
 * the complexity of your objects, may entail a lot of code. 
 * Java's serialization mechanism provides a neat workaround 
 * to this situation.
 * 
 * To take advantage of serialization, you have to ensure 
 * that your objects and all their contained objects 
 * are serializable.
 * 
 * @author Behrouz Fallahi
 * @see http://www.devx.com/tips/Tip/13341
 *
 */
public class DeepCopyMaker {
	private DeepCopyMaker() {
		// I made constructor private so that DeepCopyMaker could not be created
	}

	/**
	 * deep copy via serialization
	 * @param obj2DeepCopy (obj2DeepCopy must be serializable)
	 * @return deep copy of obj2DeepCopy
	 * @throws Exception
	 */
	static public Object makeDeepCopy(Object obj2DeepCopy) throws Exception {
		// obj2DeepCopy must be serializable
		ObjectOutputStream outStream = null;
		ObjectInputStream inStream = null;

		try {
			// create byteOut
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			outStream = new ObjectOutputStream(byteOut);
			// serialize and write obj2DeepCopy to byteOut
			outStream.writeObject(obj2DeepCopy);
			// always flush your stream
			outStream.flush();

			// connect byteIn with byteOut
			ByteArrayInputStream byteIn = new ByteArrayInputStream(
					byteOut.toByteArray());
			inStream = new ObjectInputStream(byteIn);

			// read the serialized, and deep copied, object and return it
			return inStream.readObject();
		} catch (Exception e) {
			// handle the exception
			// it is not a bad idea to throw the exception, 
			// so that the caller of the
			// method knows something went wrong
			throw (e);
		} finally {
			// always close your streams in finally clauses
			outStream.close();
			inStream.close();
		}
	}

	/**
	 * demonstration of usage
	 * @param args
	 */
	public static void main(String[] args) {
		TalkConfiguration a = new TalkConfiguration();
		TalkConfiguration b = new TalkConfiguration();
		System.out.println("before");
		System.out.println(String.format("<%s %s />", "a", a));
		System.out.println(String.format("<%s %s />", "b", b));
		a.setName("adjusted");
		a.setPresentation(20);
		a.setDiscussion(10);
		a.setOvertime(5);
		a.setAudible(false);
		try {
			// casting is /always/ necessary
			b = (TalkConfiguration) DeepCopyMaker.makeDeepCopy(a);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		a.setDefaults();
		System.out.println("after");
		System.out.println(String.format("<%s %s />", "a", a));
		System.out.println(String.format("<%s %s />", "b", b));
	}
}
