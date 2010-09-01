package example.code;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

public class TestXml {

	public static void readExample(String filename, String[] nodes)
			throws XPathExpressionException {
		// 1. Instantiate an XPathFactory.
		XPathFactory factory = XPathFactory.newInstance();

		// 2. Use the XPathFactory to create a new XPath object
		javax.xml.xpath.XPath xpath = factory.newXPath();

		// open the XML document
		InputSource source = new InputSource(filename);

		// 3. Compile an XPath string into an XPathExpression
		// 4. Evaluate XPathExpression against XML document
		for (int i = 0; i < nodes.length; i++)
			System.out.println(nodes[i] + ": "
					+ xpath.compile(nodes[i]).evaluate(source));
	}

	public static void main(String args[]) {
		try {
			String[] nodes = "/CountdownJ/author /CountdownJ/copyright /CountdownJ/email"
					.split(" ");
			TestXml.readExample("config.xml", nodes);

			nodes = "/project/@name //isset/@property //property/@environment //condition/@value"
					.split(" ");
			TestXml.readExample("hostarch.xml", nodes);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
