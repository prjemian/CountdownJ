package org.jemian.countdownj.Swing;

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
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XmlFileFilter implements FilenameFilter {
	String ext;
	private String schemaFile;
	private Source schemaSource;
	private Schema schema;
	private SchemaFactory schemaFactory;
	private Validator schemaValidator;

	public XmlFileFilter() {
		ext = ".xml";
		schemaFile = "/schema.xsd";
		schemaSource = new StreamSource(getClass().getResourceAsStream(schemaFile));
		// 1. Lookup a factory for the W3C XML Schema language
		schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		schema = null;
		try {
			schema = schemaFactory.newSchema(schemaSource);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		schemaValidator = schema.newValidator();
	}

	private boolean isValid(String file) {
		boolean test = false;
		test = true;
		// ----
		// @see http://www.ibm.com/developerworks/xml/library/x-javaxmlvalidapi.html
		// ----
		try {
			test = validateSettings(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return test;
	}

	public boolean accept(File dir, String name) {
		boolean test = false;
		System.out.printf("XmlFileFilter(\"%s\", \"%s\")\n", dir, name);
		test = name.toLowerCase().endsWith(".xml"); // ends with ".xml"
		if (test) {
			test = isValid(name);	// TODO set the dir?
		}
		return test;
	}

	public boolean validateSettings(String xmlDoc) throws IOException {
		boolean test = false;
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
			System.out.println(xmlDoc + " is valid.");
			test = true;
		} catch (SAXException ex) {
			System.out.println(xmlDoc + " is not valid because ");
			System.out.println(ex.getMessage());
		}
		return test;
	}
}