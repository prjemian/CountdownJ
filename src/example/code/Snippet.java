package example.code;

import java.io.*;
// DOM classes.
import org.w3c.dom.*;
//JAXP 1.1
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

public class Snippet {

	// @see http://www.javazoom.net/services/newsletter/xmlgeneration.html
	// also
	// @see http://www.totheriver.com/learn/xml/xmltutorial.html
	// @see http://articles.techrepublic.com.com/5100-10878_11-1044810.html

	public static void main(String args[]) throws FileNotFoundException,
			ParserConfigurationException, TransformerException {
		// PrintWriter from a Servlet
		PrintWriter out = new PrintWriter("simple.xml");
		// Create XML DOM document (Memory consuming).
		Document xmldoc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();
		Element e = null;
		Node n = null;
		// Document.
		xmldoc = impl.createDocument(null, "USERS", null);
		// Root element.
		Element root = xmldoc.getDocumentElement();
		String[] id = { "PWD122", "MX787", "A4Q45" };
		String[] type = { "customer", "manager", "employee" };
		String[] desc = { "Tim@Home", "Jack&Moud", "John D'oé" };
		for (int i = 0; i < id.length; i++) {
			// Child i.
			e = xmldoc.createElementNS(null, "USER");
			e.setAttributeNS(null, "ID", id[i]);
			e.setAttributeNS(null, "TYPE", type[i]);
			n = xmldoc.createTextNode(desc[i]);
			e.appendChild(n);
			root.appendChild(e);
		}

		// Serialisation through Tranform.
		DOMSource domSource = new DOMSource(xmldoc);
		StreamResult streamResult = new StreamResult(out);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();

		serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "users.dtd");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");

		serializer.transform(domSource, streamResult);
	}
}
