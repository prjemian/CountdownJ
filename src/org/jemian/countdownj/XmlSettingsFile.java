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

import java.io.IOException;
import java.util.HashMap;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

// TODO Class methods do not use class data consistently.  Some are public.

/**
 * Filters filenames for use in FileDialogs.
 * @note Not supported in Sun's reference implementation on Windows
 *
 */
public class XmlSettingsFile {

	/**
	 * Routines that manage XML files with the user-configurable
	 * setting for this program.
	 */
	public XmlSettingsFile() {
		doc = XmlSupport.makeNewXmlDomDoc();
		config = new HashMap<String, TalkConfiguration>();
		for (int i = 0; i < keys.length; i++) {
			TalkConfiguration talk = new TalkConfiguration();
			config.put(keys[i], talk);
		}
	}

	/**
	 * Routines that manage XML files with the user-configurable
	 * setting for this program.
	 * @param config  Supplied configuration
	 */
	public XmlSettingsFile(HashMap<String, TalkConfiguration> config) {
		doc = XmlSupport.makeNewXmlDomDoc();
		this.config = config;
	}
	
	/**
	 * write the settings to an XML settings file named by the user
	 * @param filename
	 */
	public void writeFullConfiguration(String filename) {
		XmlSupport.writeDomToFile(writeFullConfigurationToDom(), filename);
	}
	
	/**
	 * Read an XML settings file managed by the user.
	 * Assume the XML file has been validated against our schema before this method is called.
	 * @param settingsFile
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	public HashMap<String, TalkConfiguration> readFullConfiguration(String settingsFile) 
	throws SAXException, IOException, XPathExpressionException {
		Document doc;
		doc = XmlSupport.openXmlDoc(settingsFile);
		HashMap<String, TalkConfiguration> cfg = null;

		String base = "/TalkConfiguration";
		String program = XmlSupport.getString(doc, base+"/@programName");
		String version = XmlSupport.getString(doc, base+"/@version");
		if (XmlSupport.strEq(program, programName) & XmlSupport.strEq(version, VERSION)) {
			// assume file is valid at this point
			cfg = new HashMap<String, TalkConfiguration>();
			for (int i = 0; i < keys.length; i++) {
				// configuration
				String talkNodeXpath;
				talkNodeXpath = String.format(base+"/talk[@id=\"%s\"]", keys[i]);
				Node node = XmlSupport.getNode(doc, talkNodeXpath);
				if (node != null) {
					int timeDiscussion = XmlSupport.getInteger(node, "seconds/@discussion");
					int timeOvertime = XmlSupport.getInteger(node, "seconds/@overtime");
					int timePresentation = 0;
					String name = "";
					if (!XmlSupport.strEq(keys[i], "basic")) {
						timePresentation = XmlSupport.getInteger(node, "seconds/@presentation");
						name = XmlSupport.getString(node, "@name");
					}
					boolean audible = (Boolean) XmlSupport.getObject(node, "audible", XPathConstants.BOOLEAN);
					String strPretalk = XmlSupport.getString(node, "message[@name=\"pretalk\"]");
					String strPresentation = XmlSupport.getString(node, "message[@name=\"presentation\"]");
					String strDiscussion = XmlSupport.getString(node, "message[@name=\"discussion\"]");
					String strOvertime = XmlSupport.getString(node, "message[@name=\"overtime\"]");
					String strPaused = XmlSupport.getString(node, "message[@name=\"paused\"]");
	
					TalkConfiguration talk = new TalkConfiguration();
					talk.setDiscussion(timeDiscussion);
					talk.setOvertime(timeOvertime);
					if (!XmlSupport.strEq(keys[i], "basic")) {
						talk.setPresentation(timePresentation);
						talk.setName(name);
					}
					talk.setAudible(audible);
					talk.setMsg_pretalk(strPretalk);
					talk.setMsg_presentation(strPresentation);
					talk.setMsg_discussion(strDiscussion);
					talk.setMsg_overtime(strOvertime);
					talk.setMsg_paused(strPaused);
					cfg.put(keys[i], talk);
				}
			}
			// System.out.println(cfg);
		}
		return cfg;
	}
	
	/**
	 * write the configuration to a DOM Document object
	 * @return the DOM Document object
	 */
	public Document writeFullConfigurationToDom() {
        //create the root element and add it to the document
        Element root = XmlSupport.xmlRootElement(doc, ROOTNODE);
        root.setAttribute("version", VERSION);
        root.setAttribute("programName", programName);
        root.appendChild(
        		doc.createComment("\n"+ConfigFile.getInstance().toString()+"\n"));
        XmlSupport.attachXmlText(doc, 
        		XmlSupport.attachXmlElement(doc, root, "timestamp"), 
        		XmlSupport.timeStamp());
		for (int i = 0; i < keys.length; i++)
			writeTalkConfiguration (doc, root, keys[i], config.get(keys[i]));
		return doc;
	}
	
	/**
	 * write the configuration for one talk object to the DOM Document
	 * @param doc
	 * @param parent
	 * @param id
	 * @param talk
	 */
	private void writeTalkConfiguration (Document doc, 
			Element parent, String id, TalkConfiguration talk) {
        Element talkNode = XmlSupport.attachXmlElement(doc, parent, "talk");
        talkNode.setAttribute("id", id);
        if (!XmlSupport.strEq("basic", id)) {
	        talkNode.appendChild(
	        		doc.createComment("name is used for the tab to be selected"));
	        talkNode.setAttribute("name", talk.getName());
        }

        Element timeNode = XmlSupport.attachXmlElement(doc, talkNode, "seconds");
        if (!XmlSupport.strEq("basic", id)) {
        	timeNode.setAttribute("presentation", 
        			String.format("%d", talk.getPresentation()));
        }
        timeNode.setAttribute("discussion", 
        		String.format("%d", talk.getDiscussion()));
        timeNode.setAttribute("overtime", 
        		String.format("%d", talk.getOvertime()));

        XmlSupport.attachXmlText(doc, 
        		XmlSupport.attachXmlElement(doc, talkNode, "audible"), 
        		talk.isAudible() ? "true" : "false");

        talkNode.appendChild(doc.createComment("messages should be brief"));
        Element node = XmlSupport.attachXmlElement(doc, talkNode, "message");
        node.setAttribute("name", "pretalk");
        XmlSupport.attachXmlText(doc, node, talk.getMsg_pretalk());

        node = XmlSupport.attachXmlElement(doc, talkNode, "message");
        node.setAttribute("name", "presentation");
        XmlSupport.attachXmlText(doc, node, talk.getMsg_presentation());

        node = XmlSupport.attachXmlElement(doc, talkNode, "message");
        node.setAttribute("name", "discussion");
        XmlSupport.attachXmlText(doc, node, talk.getMsg_discussion());

        node = XmlSupport.attachXmlElement(doc, talkNode, "message");
        node.setAttribute("name", "overtime");
        XmlSupport.attachXmlText(doc, node, talk.getMsg_overtime());

        node = XmlSupport.attachXmlElement(doc, talkNode, "message");
        node.setAttribute("name", "paused");
        XmlSupport.attachXmlText(doc, node, talk.getMsg_paused());
    }

	/**
	 * Example code to test this class
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		HashMap<String, TalkConfiguration> theConfig;
		theConfig = new HashMap<String, TalkConfiguration>();
		for (int i = 0; i < keys.length; i++) {
			TalkConfiguration talk = new TalkConfiguration();
			talk.setDiscussion((i+1)*60);
			talk.setOvertime((i+1)*10);
			talk.setName("talk " + i);
			talk.setAudible((i % 2) == 0);
			theConfig.put(keys[i], talk);
		}
		XmlSettingsFile test = new XmlSettingsFile(theConfig);
		Document xmldoc = test.writeFullConfigurationToDom();
		String testFile = "test.xml";
		XmlSupport.writeDomToFile(xmldoc, testFile);

		System.out.println(test.readFullConfiguration(testFile));
		System.out.println(test.readFullConfiguration("example.xml"));
	}

	private Document doc;
	private static final String[] keys = 
		{"basic", "preset1", "preset2", "preset3", "preset4"};
	private HashMap<String, TalkConfiguration> config;
	private static final ConfigFile configFile = ConfigFile.getInstance();
	private static final String programName = configFile.getName();
	private static final String VERSION = configFile.getVersion();
	private static final String ROOTNODE = "TalkConfiguration";
}
