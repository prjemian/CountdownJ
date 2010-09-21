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

import java.io.*;

public class XmlFileFilter implements FilenameFilter {
	String ext;

	public XmlFileFilter() {
		ext = ".xml";
	}

	public boolean accept(File dir, String name) {
		boolean test = false;
		System.out.printf("XmlFileFilter(\"%s\", \"%s\")\n", dir, name);
		test = name.toLowerCase().endsWith(".xml");	// ends with ".xml"
		if (test) {
			// TODO validate aginst XML schema: "schema.xsd"
		}
		return test;
	}
}