package org.jemian.countdownj.Swing;

//TODO needs copyright and license header

//########### SVN repository information ###################
//# $Date$
//# $Author$
//# $Revision$
//# $URL$
//# $Id$
//########### SVN repository information ###################

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		XPathFactory xpFactory = XPathFactory.newInstance();
		XPath xpath = xpFactory.newXPath();
		XPathExpression expr;
		name = getStringByXpath(xpath, "/CountdownJ/@name");
		version = getStringByXpath(xpath, "/CountdownJ/@version");
		description = getStringByXpath(xpath, "/CountdownJ/description");
		author = getStringByXpath(xpath, "/CountdownJ/author");
		email = getStringByXpath(xpath, "/CountdownJ/email");
		copyright = getStringByXpath(xpath, "/CountdownJ/copyright");
		license = getStringByXpath(xpath, "/CountdownJ/license");
	}

	/**
	 * Get the instance of this Singleton class.
	 * @return Xsect object
	 */
	public static ConfigFile getInstance () {
		return ConfigFile.CONFIGFILE;
	}
	
	private String getStringByXpath(XPath xpath, String xpathExpr) {
		XPathExpression expr;
		String result = null;
		try {
			expr = xpath.compile(xpathExpr);
			result = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			result = xpathExpr + " not found";
		}
		return result;
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
	private static InputStream xmlInputStream;
	private static final String RES_DIR = "/";
	private static final ConfigFile CONFIGFILE = new ConfigFile();
	private static final String xmlFile = "config.xml";
}
