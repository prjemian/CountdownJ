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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import javax.xml.transform.TransformerFactoryConfigurationError;
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

/**
 * General purpose methods for use with XML files
 */
public class XmlSupport {

	/**
	 * singleton
	 */
	private XmlSupport() {
		// prepare to search using XPath expressions
		xpFactory = XPathFactory.newInstance();
		xpath = xpFactory.newXPath();
	}
	
	/**
	 * @return the instance
	 */
	public static XmlSupport getInstance() {
		return INSTANCE;
	}

	/**
	 * Locate an object in a DOM Document object using an XPath expression.
	 * 
	 * objectType can be one of these.  It is necessary to cast the 
	 * result of this function to the desired Java type.
	 * XPathConstants.NODESET
     * XPathConstants.BOOLEAN
     * XPathConstants.NUMBER
     * XPathConstants.STRING
     * XPathConstants.NODE
	 * 
	 * @param d
	 * @param xpath
	 * @param xpathExpr
	 * @param objectType
	 * @return
	 * @throws XPathExpressionException
	 */
	public static Object getObject(Document d, 
			String xpathExpr, QName objectType) 
		throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathExpr);
		Object obj = expr.evaluate(d, objectType);
		return obj;
	}
	
	/**
	 * Locate an object from a DOM Node object using an XPath expression.
	 * @param n
	 * @param xpath
	 * @param xpathExpr
	 * @param objectType
	 * @return
	 * @throws XPathExpressionException
	 */
	public static Object getObject(Node n, 
			String xpathExpr, QName objectType) 
		throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathExpr);
		Object obj = expr.evaluate(n, objectType);
		return obj;
	}
	
	/**
	 * Locate a Node in a DOM Document object using an XPath expression.
	 * @param d
	 * @param xpath
	 * @param xpathExpr
	 * @return
	 * @throws XPathExpressionException
	 */
	public static Node getNode(Document d, 
			String xpathExpr) 
	throws XPathExpressionException {
		return (Node) getObject(d, xpathExpr, XPathConstants.NODE);
	}
	
	/**
	 * Locate an Integer from a DOM Node object using an XPath expression.
	 * @param n
	 * @param xpath
	 * @param xpathExpr
	 * @return
	 * @throws XPathExpressionException
	 */
	public static Integer getInteger(Node n, 
			String xpathExpr) 
	throws XPathExpressionException {
		return Integer.valueOf(getDouble(n, xpathExpr).intValue());
	}

	/**
	 * Locate a Double from a DOM Node object using an XPath expression.
	 * @param n
	 * @param xpath
	 * @param xpathExpr
	 * @return
	 * @throws XPathExpressionException
	 */
	public static Double getDouble(Node n, 
			String xpathExpr) 
	throws XPathExpressionException {
		return (Double) getObject(n, xpathExpr, XPathConstants.NUMBER);
	}	
	
	/**
	 * Locate a String from a DOM Node object using an XPath expression.
	 * @param n
	 * @param xpath
	 * @param xpathExpr
	 * @return
	 * @throws XPathExpressionException
	 */
	public static String getString(Node n, 
			String xpathExpr) 
	throws XPathExpressionException {
		return (String) getObject(n, xpathExpr, XPathConstants.STRING);
	}
	
	/**
	 * Locate a String in a DOM Document object using an XPath expression.
	 * @param d
	 * @param xpath
	 * @param xpathExpr
	 * @return
	 * @throws XPathExpressionException
	 */
	public static String getString(Document d, 
			String xpathExpr) 
	throws XPathExpressionException {
		return (String) getObject(d, xpathExpr, XPathConstants.STRING);
	}
	
	/**
	 * compare if two Strings are equal
	 * @param s1
	 * @param s2
	 * @return true if equal
	 */
	public static boolean strEq(String s1, String s2) {
		return (s1.compareTo(s2) == 0);
	}

	/**
	 * initialize a new (XML) DOM document
	 * @return
	 */
	public static Document makeNewXmlDomDoc() {
        DocumentBuilderFactory dbfac;
        dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
			docBuilder = dbfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			/*
			 * Since we don't actually attempt to configure the parser at all, we
			 * should never get a parser configuration exception.
			 */
			return null;
			// e.printStackTrace();
		}
        return docBuilder.newDocument();
	}

	/**
	 * Open filename as a DOM Document
	 * @param filename
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document openXmlDoc(String filename) 
		throws SAXException, IOException {
		// Step 1: create a DocumentBuilderFactory
		DocumentBuilderFactory dbf;
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true); // never forget this!

		// Step 2: create a DocumentBuilder
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			/*
			 * Since we don't actually attempt to configure the parser at all, we
			 * should never get a parser configuration exception.
			 */
			return null;
			// e.printStackTrace();
		}

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
	public static Element xmlRootElement(Document doc, String name) {
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
	public static Element attachXmlElement(Document doc, 
			Element parent, String name) {
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
	public static void attachXmlText(Document doc, 
			Element parent, String text) {
		parent.appendChild(doc.createTextNode(text));
	}
	
	/**
	 * add a text node to the DOM document
	 * @param doc
	 * @param parent
	 * @param text
	 */
	public static void attachXmlText(Document doc, 
			Node parent, String text) {
		parent.appendChild(doc.createTextNode(text));
	}

	/**
	 * This method writes a DOM (XML) document to a file
	 * @see http://www.exampledepot.com/egs/javax.xml.transform/WriteDom.html
	 * @param doc
	 * @param filename
	 */
	public static void writeDomToFile(Document doc, String filename) {
			// Prepare the DOM document for writing
			Source source = new DOMSource(doc);

			// Prepare the output file
			File file = new File(filename);
			Result result = new StreamResult(file);

			// Write the DOM document to the file
			Transformer xformer;
			try {
				xformer = TransformerFactory.newInstance().newTransformer();
				xformer.setOutputProperty(OutputKeys.INDENT, "yes");
				String strIndent = "{http://xml.apache.org/xslt}indent-amount";
				xformer.setOutputProperty(strIndent, "2");
				//xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				//xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				xformer.transform(source, result);
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	public static String timeStamp() {
        Date dateNow = new Date ();
        String dateFormat = "yyyy-MM-dd+hh:mm:ss";
        SimpleDateFormat timeFormat = new SimpleDateFormat(dateFormat);
        String formattedDate = timeFormat.format( dateNow );
        StringBuilder timeStamp = new StringBuilder( formattedDate );
        // now convert to ISO8601 format (change "+" to "T")
        String[] parts = timeStamp.toString().split("\\+");
		return parts[0] + "T" + parts[1];
    }
	
	/**
	 * 
	 * @param doc
	 * @param xpath
	 * @param xpathExpr
	 * @return
	 */
	public static String getStringByXpath(Document doc, String xpathExpr) {
		XPathExpression expr;
		String result = null;
		try {
			expr = xpath.compile(xpathExpr);
			/*
			 * XPathConstants.NODESET
		     * XPathConstants.BOOLEAN
		     * XPathConstants.NUMBER
		     * XPathConstants.STRING
		     * XPathConstants.NODE
			 */
			result = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			result = xpathExpr + " not found";
		}
		return result;
	}

	// prepare to search using XPath expressions
	private static XPathFactory xpFactory;
	private static XPath xpath;
	private static final XmlSupport INSTANCE = new XmlSupport();
}
