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
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * Filters filenames for use in FileDialogs.
 * @note Not supported in Sun's reference implementation on Windows
 *
 */
public class XmlFileFilter implements FilenameFilter {
	//String extension;
	private String schemaFile;
	private Source schemaSource;
	private Schema schema;
	private SchemaFactory schemaFactory;
	private Validator schemaValidator;

	/**
	 * Support the FilenameFilter interface for FileDialog objects
	 * Note this interface is not supported in Sun's reference implementation on Windows.
	 * THUS, this code is not used as a filter.  
	 * It provides XML file validation.  Perhaps it needs to be renamed.
	 */
	public XmlFileFilter() {
		//extension = ".xml";
		schemaFile = "/schema.xsd";
		schemaSource = new StreamSource(getClass().getResourceAsStream(schemaFile));
		// 1. Lookup a factory for the W3C XML Schema language
		schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		schema = null;
		try {
			schema = schemaFactory.newSchema(schemaSource);
			schemaValidator = schema.newValidator();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test if XML file is valid against our XML Schema
	 * Ignore any report if invalid
	 * @see http://www.ibm.com/developerworks/xml/library/x-javaxmlvalidapi.html
	 * @param file
	 * @return
	 */
	public boolean isValid(String file) {
		boolean test = false;
		try {
			String msg = null;
			msg = validateSettings(file);
			test = (msg == null);
		} catch (IOException e) {
		}
		return test;
	}

	/**
	 * Required support for FileFilter interface
	 */
	public boolean accept(File dir, String name) {
		boolean test = false;
		System.out.printf("XmlFileFilter(\"%s\", \"%s\")\n", dir, name);
		test = name.toLowerCase().endsWith(".xml"); // ends with ".xml"
		if (test) {
			test = isValid(name);	// ??? set the dir?
		}
		return test;
	}

	/**
	 * Check an XML file against our XML Schema
	 * @param xmlDoc
	 * @return null (valid) or String(errorReport)
	 * @throws IOException
	 */
	public String validateSettings(String xmlDoc) throws IOException {
		String result = null;
		// 1. Lookup a factory for the W3C XML Schema language
		//SchemaFactory factory = SchemaFactory
		//		.newInstance("http://www.w3.org/2001/XMLSchema");

		// 2. Compile the schema.
		// Here the schema is loaded from a java.io.File, but you could use
		// a java.net.URL or a javax.xml.transform.Source instead.
		//Schema schema = factory.newSchema(schemaSource);

		// 3. Get a validator from the schema.
		//Validator validator = schema.newValidator();

		// 4. Parse the document you want to check.
		Source source = new StreamSource(xmlDoc);

		try {
			// 5. Check the document
			schemaValidator.validate(source);
			//System.out.println(xmlDoc + " is valid.");
		} catch (SAXException ex) {
			//System.out.println(xmlDoc + " is not valid because ");
			//System.out.println(ex.getMessage());
			result = ex.getMessage();
		}
		return result;
	}
}
