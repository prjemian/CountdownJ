package example.xml;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.io.*;

public class MyDOMParserBean implements java.io.Serializable {

//	public MyDOMParserBean() {
//	}

	public static Document getDocument(String file) throws Exception {

		// Step 1: create a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// Step 2: create a DocumentBuilder
		DocumentBuilder db = dbf.newDocumentBuilder();

		// Step 3: parse the input file to get a Document object
		Document doc = db.parse(new File(file));
		return doc;
	}

	private static final long serialVersionUID = 638738816228219852L;
}
