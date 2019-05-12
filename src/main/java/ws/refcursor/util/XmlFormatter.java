package ws.refcursor.util;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

public class XmlFormatter {

	private static final Logger log = Logger.getLogger("XmlFormatter");
	
	public static String format(String unformattedXml) {
        try {
            final InputSource src = new InputSource(new StringReader(unformattedXml));
    		final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src)
    				.getDocumentElement();
            final Boolean keepDeclaration = Boolean.valueOf(unformattedXml.startsWith("<?xml"));

            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();    
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("XML 3.0 LS 3.0");
            
            LSSerializer ser = impl.createLSSerializer();
            ser.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
            ser.getDomConfig().setParameter("xml-declaration", keepDeclaration); 
            LSOutput out = impl.createLSOutput();
            StringWriter stringOut = new StringWriter();
            out.setCharacterStream(stringOut);
            ser.write(document, out);
          
            return stringOut.toString();
        } catch (Exception e) {
        	StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error(errors.toString());
			return "";
        }
 
    }
	
}
