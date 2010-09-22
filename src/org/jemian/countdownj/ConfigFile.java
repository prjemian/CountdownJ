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
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ConfigFile {

	/**
	 * Singleton class to provide contents of config.xml file.
	 * Do not make this constructor accessible as public.
	 */
	private ConfigFile() {
		String dataFile = RES_DIR + xmlFile;	// find at the root of the jar file

		// find the XML file in the JAR file
		InputStream in = getClass().getResourceAsStream(dataFile);
		if (in == null) {
		    throw new IllegalArgumentException(
		    		dataFile + " not found (actually: InputStream cannot be null");
		}

		// prepare the XML DOM resources
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(in);
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}

		try {
			name = XmlSupport.getString(doc, "/CountdownJ/@name");
			version = XmlSupport.getString(doc, "/CountdownJ/@version");
			description = XmlSupport.getString(doc, "/CountdownJ/description");
			author = XmlSupport.getString(doc, "/CountdownJ/author");
			email = XmlSupport.getString(doc, "/CountdownJ/email");
			copyright = XmlSupport.getString(doc, "/CountdownJ/copyright");
			license = XmlSupport.getString(doc, "/CountdownJ/license");
		} catch (XPathExpressionException e) {
			// do not expect this to fail here, but it could
			e.printStackTrace();
		}
	}

	/**
	 * Get the instance of this Singleton class.
	 * @return Xsect object
	 */
	public static ConfigFile getInstance () {
		return ConfigFile.CONFIGFILE;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @return the license
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the copyright
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}
	
	public String toString() {
		return name + " ("+version + ")\n" +
			description + "\n" +
			copyright + "\n" +
			"by: " + author + " <" + email + ">\n" +
			"\n" +
			license;
	}
	
	public static void main(String[] args) {
		ConfigFile cfg = ConfigFile.getInstance();
		System.out.println(cfg);
	}

	private String name;
	private String version;
	private String description;
	private String author;
	private String email;
	private String copyright;
	private String license;

	private static Document doc;
	private static final String RES_DIR = "/";
	private static final ConfigFile CONFIGFILE = new ConfigFile();
	private static final String xmlFile = "config.xml";
}
