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

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Manages the resource configuration file
 */
public class ManageRcFile {

	private ManageRcFile() {
		RC_FILE = null;
		userSettingsFile = null;
	}
	
	public void readRcFile() {
		//boolean exists = new File(RC_FILE).exists();
		//if (exists) {
		//	// TODO
		//}
	}
	
	public void writeRcFile() {
		Document doc = null;
		try {
			doc = XmlSupport.makeNewXmlDomDoc();
		} catch (ParserConfigurationException e) {
			// not expected to fail here
			e.printStackTrace();
		}

        Element root = XmlSupport.xmlRootElement(doc, ROOTNODE);
        root.setAttribute("version", VERSION);
        String comment = "\n"+ConfigFile.getInstance().toString()+"\n";
        root.appendChild(doc.createComment(comment));
        XmlSupport.attachXmlText(doc, 
        		XmlSupport.attachXmlElement(doc, root, "timestamp"), 
        		XmlSupport.timeStamp());
        XmlSupport.attachXmlElement(doc, root, "timestamp");
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
	 * Example code to test this class
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
	}

	private static String RC_FILE;
	private static String userSettingsFile;
	private static final ManageRcFile INSTANCE = new ManageRcFile();
	private static String ROOTNODE = "CoundownJ.rc";
	private static String VERSION = "1.0";
}
