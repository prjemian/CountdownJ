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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlSettingsFile {

	public XmlSettingsFile(HashMap<String, TalkConfiguration> config) 
	throws ParserConfigurationException {
		doc = makeNewXmlDoc();
		this.config = config;
	}
	
	public Document readFullConfiguration(String settingsFile) 
	throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		// TODO write this section
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(settingsFile);

		// prepare to search using XPath expressions
		XPathFactory xpFactory = XPathFactory.newInstance();
		XPath xpath = xpFactory.newXPath();

		String program = getString(doc, xpath, "/TalkConfiguration/@programName");
		String version = getString(doc, xpath, "/TalkConfiguration/@version");
		if ((program.compareTo(programName) == 0) & (version.compareTo(VERSION) == 0)) {
			// assume file is valid at this point
			HashMap<String, TalkConfiguration> cfg = new HashMap<String, TalkConfiguration>();
			for (int i = 0; i < keys.length; i++) {
				// configuration
				String talkNodeXpath;
				talkNodeXpath = String.format("/TalkConfiguration/talk[@id=\"%s\"]", keys[i]);
				Node node = getNode(doc, xpath, talkNodeXpath);

				int timeDiscussion = getInteger(node, xpath, "seconds/@discussion");
				int timeOvertime = getInteger(node, xpath, "seconds/@overtime");
				int timePresentation = 0;
				String name = "";
				if (keys[i].compareTo("basic") != 0) {
					timePresentation = getInteger(node, xpath, "seconds/@presentation");
					name = getString(node, xpath, "@name");
				}
				boolean audible = (Boolean) getObject(node, xpath, "audible", XPathConstants.BOOLEAN);
				String strPretalk = getString(node, xpath, "message[@name=\"pretalk\"]");
				String strPresentation = getString(node, xpath, "message[@name=\"presentation\"]");
				String strDiscussion = getString(node, xpath, "message[@name=\"discussion\"]");
				String strOvertime = getString(node, xpath, "message[@name=\"overtime\"]");
				String strPaused = getString(node, xpath, "message[@name=\"paused\"]");

				TalkConfiguration talk = new TalkConfiguration();
				talk.setDiscussion(timeDiscussion);
				talk.setOvertime(timeOvertime);
				if (keys[i].compareTo("basic") != 0) {
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
			// if we got here, then copy to the global array
			// TODO write the copy
			// TODO update the widgets in the dialog?
			System.out.println(cfg);
		}
		return doc;	// FIXME This cannot be correct!
	}
	
	private Object getObject(Document d, XPath xpath, String xpathExpr, QName objectType) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathExpr);
		Object obj = expr.evaluate(d, objectType);
		return obj;
	}
	
	private Object getObject(Node n, XPath xpath, String xpathExpr, QName objectType) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathExpr);
		Object obj = expr.evaluate(n, objectType);
		return obj;
	}
	
	private Node getNode(Document d, XPath xpath, String xpathExpr) throws XPathExpressionException {
		return (Node) getObject(d, xpath, xpathExpr, XPathConstants.NODE);
	}
	
	/*
	 * XPathConstants.NODESET
     * XPathConstants.BOOLEAN
     * XPathConstants.NUMBER
     * XPathConstants.STRING
     * XPathConstants.NODE
	 */
	private Integer getInteger(Node n, XPath xpath, String xpathExpr) throws XPathExpressionException {
		return new Integer(getDouble(n, xpath, xpathExpr).intValue());
	}

	private Double getDouble(Node n, XPath xpath, String xpathExpr) throws XPathExpressionException {
		return (Double) getObject(n, xpath, xpathExpr, XPathConstants.NUMBER);
	}	
	
	private String getString(Node n, XPath xpath, String xpathExpr) throws XPathExpressionException {
		return (String) getObject(n, xpath, xpathExpr, XPathConstants.STRING);
	}
	
	private String getString(Document d, XPath xpath, String xpathExpr) throws XPathExpressionException {
		return (String) getObject(d, xpath, xpathExpr, XPathConstants.STRING);
	}
	
	/**
	 * 
	 * @return the doc
	 */
	public Document writeFullConfiguration() {
        //create the root element and add it to the document
        Element root = xmlRootElement(doc, ROOTNODE);
        root.setAttribute("version", VERSION);
		// TODO add date & time file was written and program that wrote it
        root.setAttribute("programName", programName);
        root.appendChild(
        		doc.createComment("\n"+ConfigFile.getInstance().toString()+"\n"));
        attachXmlText(doc, 
        		attachXmlElement(doc, root, "timestamp"), 
        		timeStamp());
		for (int i = 0; i < keys.length; i++)
			writeTalkConfiguration (doc, root, keys[i], config.get(keys[i]));
		return doc;
	}
	
	private void writeTalkConfiguration (Document doc, 
			Element parent, String id, TalkConfiguration talk) {
        Element talkNode = attachXmlElement(doc, parent, "talk");
        talkNode.setAttribute("id", id);
        if ("basic".compareTo(id) != 0) {
	        talkNode.appendChild(
	        		doc.createComment("name is used for the tab to be selected"));
	        talkNode.setAttribute("name", talk.getName());
        }

        Element timeNode = attachXmlElement(doc, talkNode, "seconds");
        if ("basic".compareTo(id) != 0) {
        	timeNode.setAttribute("presentation", 
        			String.format("%d", talk.getPresentation()));
        }
        timeNode.setAttribute("discussion", 
        		String.format("%d", talk.getDiscussion()));
        timeNode.setAttribute("overtime", 
        		String.format("%d", talk.getOvertime()));

        attachXmlText(doc, 
        		attachXmlElement(doc, talkNode, "audible"), 
        		talk.isAudible() ? "true" : "false");

        talkNode.appendChild(doc.createComment("messages should be brief"));
        Element node = attachXmlElement(doc, talkNode, "message");
        node.setAttribute("name", "pretalk");
        attachXmlText(doc, node, talk.getMsg_pretalk());

        node = attachXmlElement(doc, talkNode, "message");
        node.setAttribute("name", "presentation");
        attachXmlText(doc, node, talk.getMsg_presentation());

        node = attachXmlElement(doc, talkNode, "message");
        node.setAttribute("name", "discussion");
        attachXmlText(doc, node, talk.getMsg_discussion());

        node = attachXmlElement(doc, talkNode, "message");
        node.setAttribute("name", "overtime");
        attachXmlText(doc, node, talk.getMsg_overtime());

        node = attachXmlElement(doc, talkNode, "message");
        node.setAttribute("name", "paused");
        attachXmlText(doc, node, talk.getMsg_paused());
    }

	/**
	 * initialize a new DOM document
	 * @return
	 * @throws ParserConfigurationException
	 */
	private Document makeNewXmlDoc() throws ParserConfigurationException {
        // We need an XML DOM Document
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        return docBuilder.newDocument();
	}

	public static Document openXmlDoc(String filename) throws Exception {
		// Step 1: create a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// Step 2: create a DocumentBuilder
		DocumentBuilder db = dbf.newDocumentBuilder();

		// Step 3: parse the input file to get a Document object
		Document doc = db.parse(new File(filename));
		return doc;
	}
	
	/**
	 * Define the root node in the DOM document
	 * @param doc
	 * @param name
	 * @return
	 */
	private Element xmlRootElement(Document doc, String name) {
        Element root = doc.createElement(name);
        doc.appendChild(root);
        return root;
	}
	
	/**
	 * add another XML element to the DOM document
	 * @param doc
	 * @param parent
	 * @param name
	 * @return
	 */
	private Element attachXmlElement(Document doc, Element parent, String name) {
        Element node = doc.createElement(name);
        parent.appendChild(node);
        return node;
	}
	
	/**
	 * add a text node to the DOM document
	 * @param doc
	 * @param parent
	 * @param text
	 */
	private void attachXmlText(Document doc, Element parent, String text) {
		parent.appendChild(doc.createTextNode(text));
	}

	/**
	 * This method writes a DOM document to a file
	 * @see http://www.exampledepot.com/egs/javax.xml.transform/WriteDom.html
	 * @param doc
	 * @param filename
	 */
	public void writeXmlFile(Document doc, String filename) {
		try {
			// Prepare the DOM document for writing
			Source source = new DOMSource(doc);

			// Prepare the output file
			File file = new File(filename);
			Result result = new StreamResult(file);

			// Write the DOM document to the file
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			//xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			//xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			xformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
		} catch (TransformerException e) {
		}
	}
	
	/**
	 * This method will read an XML file into a DOM document
	 * @return
	 * @throws Exception 
	 */
	public static Document readXmlFile(String filename) throws Exception {
		return openXmlDoc(filename);
		
	}

	/**
	 * Get a single node from an XML file indexed by an XPath expression.
	 * This code is inefficient for many reads from the same file.
	 * @param filename
	 * @param xpathExpr
	 * @return
	 * @throws XPathExpressionException
	 */
	public static String readXpathNode(String filename, String xpathExpr)
			throws XPathExpressionException {
		// 1. Instantiate an XPathFactory.
		XPathFactory factory = XPathFactory.newInstance();

		// 2. Use the XPathFactory to create a new XPath object
		javax.xml.xpath.XPath xpathObject = factory.newXPath();

		// open the XML document
		InputSource source = new InputSource(filename);

		// 3. Compile an XPath string into an XPathExpression
		// 4. Evaluate XPathExpression against XML document
		return xpathObject.compile(xpathExpr).evaluate(source);
	}

	/**
	 * Allocates a Date object and initializes it so that it 
	 * represents the time at which it was allocated, measured 
	 * to the nearest millisecond.
	 */
	private String timeStamp() {
        Date dateNow = new Date ();
        String dateFormat = "yyyy-MM-dd+hh:mm:ss";
        SimpleDateFormat timeFormat = new SimpleDateFormat(dateFormat);
        String formattedDate = timeFormat.format( dateNow );
        StringBuilder timeStamp = new StringBuilder( formattedDate );
        // now convert to ISO8601 format (change "+" to "T")
        String[] parts = timeStamp.toString().split("\\+");
		return parts[0] + "T" + parts[1];
    }

	public static void main(String[] args) throws Exception {
		HashMap<String, TalkConfiguration> theConfig = new HashMap<String, TalkConfiguration>();
		for (int i = 0; i < keys.length; i++) {
			TalkConfiguration talk = new TalkConfiguration();
			talk.setDiscussion((i+1)*60);
			talk.setOvertime((i+1)*10);
			talk.setName("talk " + i);
			talk.setAudible((i % 2) == 0);
			theConfig.put(keys[i], talk);
		}
		XmlSettingsFile test = new XmlSettingsFile(theConfig);
		Document xmldoc = test.writeFullConfiguration();
		String testFile = "test.xml";
		test.writeXmlFile(xmldoc, testFile);
		
		test.readFullConfiguration(testFile);
	}

	private Document doc;
	private static final String[] keys = 
		{"basic", "preset1", "preset2", "preset3", "preset4"};
	private HashMap<String, TalkConfiguration> config;
	private static final String programName = "CountdownJ";		// can we get this from config.xml?
	private static final String VERSION = "1.0j";		// can we get this from config.xml?
	private static final String ROOTNODE = "TalkConfiguration";
}
