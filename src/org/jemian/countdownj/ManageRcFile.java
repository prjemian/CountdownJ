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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Manages the resource configuration file
 */
public class ManageRcFile {

	private ManageRcFile() {
		RC_FILE = null;
		userSettingsFile = null;
		settings = null;
	}
	
	public static void readRcFile() {
		if (RC_FILE != null) {
			boolean exists = new File(RC_FILE).exists();
			Document doc = null;
			if (exists) {
				try {
					doc = XmlSupport.readXmlFile(RC_FILE);
					String base = "/" + ROOTNODE;
					String version = XmlSupport.getString(doc, base+"/@version");
					if (XmlSupport.strEq(version, VERSION)) {
						//String timestamp = XmlSupport.getString(doc, base+"/timestamp");
						String userfile = XmlSupport.getString(doc, base+"/userSettingsFile");
						NodeList talks = (NodeList) XmlSupport.getObject(doc, base+"/talk", XPathConstants.NODESET);
						HashMap<String, TalkConfiguration> cfg;
						cfg = new HashMap<String, TalkConfiguration>();
						for (int i = 0; i < talks.getLength(); i++) {
							//
							Node talkNode = talks.item(i);
							String key = XmlSupport.getString(talkNode, "./@id");
							TalkConfiguration talk = new TalkConfiguration();
							talk.setName(XmlSupport.getString(talkNode, "./@name"));
							talk.setAudible(new Boolean(XmlSupport.getString(talkNode, "./audible")));
							talk.setPresentation(XmlSupport.getInteger(talkNode, "./seconds/@presentation"));
							talk.setDiscussion(XmlSupport.getInteger(talkNode, "./seconds/@discussion"));
							talk.setOvertime(XmlSupport.getInteger(talkNode, "./seconds/@overtime"));
							talk.setMsg_pretalk(XmlSupport.getString(talkNode, "./message[@name='pretalk']"));
							talk.setMsg_presentation(XmlSupport.getString(talkNode, "./message[@name='presentation']"));
							talk.setMsg_discussion(XmlSupport.getString(talkNode, "./message[@name='discussion']"));
							talk.setMsg_overtime(XmlSupport.getString(talkNode, "./message[@name='overtime']"));
							talk.setMsg_paused(XmlSupport.getString(talkNode, "./message[@name='paused']"));

							cfg.put(key, talk);
						}
						// wait to update class variables now
						settings = cfg;
						userSettingsFile = userfile;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeRcFile() {
		Document doc = XmlSupport.makeNewXmlDomDoc();
        Element root = XmlSupport.xmlRootElement(doc, ROOTNODE);
        root.setAttribute("version", VERSION);
        String comment = "\n"+ConfigFile.getInstance().toString()+"\n";
        root.appendChild(doc.createComment(comment));
        XmlSupport.attachXmlText(doc, 
        		XmlSupport.attachXmlElement(doc, root, "timestamp"), 
        		XmlSupport.timeStamp());
        Node node = XmlSupport.attachXmlElement(doc, root, "userSettingsFile");
        if (userSettingsFile != null)
        	XmlSupport.attachXmlText(doc, node, userSettingsFile);

		Iterator<String> iter = settings.keySet().iterator();
		while(iter.hasNext()) {
		    String key = (String) iter.next();
		    TalkConfiguration talk = settings.get(key);
		    // write the DOM parts now
		    Element talkElement = XmlSupport.attachXmlElement(doc, root, "talk");
		    talkElement.setAttribute("id", key);
		    talkElement.setAttribute("name", talk.getName());
		    Element secondsElement = XmlSupport.attachXmlElement(doc, talkElement, "seconds");
		    secondsElement.setAttribute("discussion", String.format("%d", talk.getDiscussion()));
		    secondsElement.setAttribute("overtime", String.format("%d", talk.getOvertime()));
		    secondsElement.setAttribute("presentation", String.format("%d", talk.getPresentation()));
	        XmlSupport.attachXmlText(doc, 
	        		XmlSupport.attachXmlElement(doc, talkElement, "audible"), 
	        		new Boolean(talk.isAudible()).toString());
	        Element msgElement;
	        msgElement = XmlSupport.attachXmlElement(doc, talkElement, "message");
	        msgElement.setAttribute("name", "pretalk");
	        XmlSupport.attachXmlText(doc, msgElement, talk.getMsg_pretalk());
	        msgElement = XmlSupport.attachXmlElement(doc, talkElement, "message");
	        msgElement.setAttribute("name", "presentation");
	        XmlSupport.attachXmlText(doc, msgElement, talk.getMsg_presentation());
	        msgElement = XmlSupport.attachXmlElement(doc, talkElement, "message");
	        msgElement.setAttribute("name", "discussion");
	        XmlSupport.attachXmlText(doc, msgElement, talk.getMsg_discussion());
	        msgElement = XmlSupport.attachXmlElement(doc, talkElement, "message");
	        msgElement.setAttribute("name", "overtime");
	        XmlSupport.attachXmlText(doc, msgElement, talk.getMsg_overtime());
	        msgElement = XmlSupport.attachXmlElement(doc, talkElement, "message");
	        msgElement.setAttribute("name", "paused");
	        XmlSupport.attachXmlText(doc, msgElement, talk.getMsg_paused());
		}

        XmlSupport.writeDomToFile(doc, RC_FILE);
	}

	/**
	 * @return the RC_FILE
	 */
	public static String getRC_FILE() {
		return RC_FILE;
	}

	/**
	 * @param rC_FILE the rC_FILE to set
	 */
	public static void setRC_FILE(String rC_FILE) {
		RC_FILE = rC_FILE;
		readRcFile();
	}

	/**
	 * @return the userSettingsFile
	 */
	public static String getUserSettingsFile() {
		return userSettingsFile;
	}

	/**
	 * @param userSettingsFile the userSettingsFile to set
	 */
	public static void setUserSettingsFile(String userSettingsFile) {
		ManageRcFile.userSettingsFile = userSettingsFile;
	}

	/**
	 * @return the instance
	 */
	public static ManageRcFile getInstance() {
		return INSTANCE;
	}

	/**
	 * @return the settings
	 */
	public static HashMap<String, TalkConfiguration> getSettings() {
		HashMap<String, TalkConfiguration> cfg = null;
		if (settings != null) {
			cfg = new HashMap<String, TalkConfiguration>();
			Iterator<String> iter = settings.keySet().iterator();
			while(iter.hasNext()) {
			    String key = (String) iter.next();
			    TalkConfiguration talk = settings.get(key);
			    cfg.put(key, talk.deepCopy());
			}
		}
		return cfg;
	}

	/**
	 * @param settings the settings to set
	 */
	public static void setSettings(HashMap<String, TalkConfiguration> settings) {
		ManageRcFile.settings = new HashMap<String, TalkConfiguration>();
		Iterator<String> iter = settings.keySet().iterator();
		while(iter.hasNext()) {
		    String key = (String) iter.next();
		    TalkConfiguration talk = settings.get(key);
		    ManageRcFile.settings.put(key, talk.deepCopy());
		}
	}

	/**
	 * Example code to test this class
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 * @throws Exception
	 */
	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException {
		XmlSettingsFile xsf = new XmlSettingsFile();
		settings = xsf.readFullConfiguration("example.xml");
		ManageRcFile.setRC_FILE("rcfile.xml");
		ManageRcFile.setUserSettingsFile("example.xml");
		ManageRcFile.setSettings(settings);
		System.out.println(ManageRcFile.getSettings());
		ManageRcFile.writeRcFile();
		ManageRcFile.readRcFile();
	}

	private static String RC_FILE;
	private static String userSettingsFile;
	private static HashMap<String, TalkConfiguration> settings;
	private static final ManageRcFile INSTANCE = new ManageRcFile();
	private static String ROOTNODE = "CoundownJ.rc";
	private static String VERSION = "1.0";
}
