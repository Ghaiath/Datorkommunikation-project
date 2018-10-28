package main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BringRates {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException,
		TransformerFactoryConfigurationError, TransformerException, XPathExpressionException, SQLException {
	String dbUrl = "jdbc:mysql://localhost:3306/currency";
	String uname = "root";
	String pwd = "";
	String query = "CREATE TABLE exchangerate(id integer primary key auto_increment, CurrencyCode varchar(25) not null unique, Rate varchar(25) not null, LastUpdated date not null)";

	Connection conn = DriverManager.getConnection(dbUrl, uname, pwd);
	conn.createStatement().executeUpdate(query);
	
	String XMLpath = "E:\\System Integtator\\Data communication project\\2.xml";
	File file = new File(XMLpath);
	System.out.println(file.getAbsoluteFile());
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	factory.setNamespaceAware(true);
	DocumentBuilder builder = factory.newDocumentBuilder();
	Document xmlDoc = builder.parse(new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml").openStream());
	//clean(xmlDoc);
	
	xmlDoc.getDocumentElement().normalize();
	System.out.println("Root element of the doc is " + xmlDoc.getDocumentElement().getNodeName());

	XPathFactory xfact = XPathFactory.newInstance();
	XPath xpath = xfact.newXPath();
	xpath.setNamespaceContext(new NamespaceContext() {

		@Override
		public Iterator getPrefixes(String arg0) {
			return null;
		}

		@Override
		public String getPrefix(String arg0) {
			return null;
		}

		@Override
		public String getNamespaceURI(String prefix) {
			switch (prefix) {
			case "df":
				return "http://www.ecb.int/vocabulary/2002-08-01/eurofxref";
			case "gesmes":
				return "http://www.gesmes.org/xml/2002-08-01";
			}
			if ("gesmes".equals(prefix)) {
				return "http://www.gesmes.org/xml/2002-08-01";
			}
			return null;
		}
	});
	
	PreparedStatement stmt = conn
			.prepareStatement("INSERT INTO exchangerate(CurrencyCode, Rate, LastUpdated) VALUES(?, ?, str_to_date(?, '%Y-%m-%d'))");
	// Date
	XPathExpression date = xpath.compile("/*[name()='gesmes:Envelope']/*[name()='Cube']/*[name()='Cube']/@*");
	Node dates = (Node) date.evaluate(xmlDoc, XPathConstants.NODE);
	System.out.println(dates.getNodeValue());

	// Attributes
	XPathExpression currency = xpath
			.compile("/*[name()='gesmes:Envelope']/*[name()='Cube']/*[name()='Cube']/*[name()='Cube']");
	NodeList attributes = (NodeList) currency.evaluate(xmlDoc, XPathConstants.NODESET);
	for (int x = 0; x < attributes.getLength(); x++) {
		//System.out.println(attributes.getLength());
		Node node = attributes.item(x);
		List<String> columns = Arrays.asList(getAttrValue(node, "currency"), getAttrValue(node, "rate"), dates.getNodeValue());
		for (int n = 0; n < columns.size(); n++) {
			stmt.setString(n+1, columns.get(n));
			//System.out.println(columns.size() + "    " + n + "    " + columns.get(n));
		}
		stmt.execute();
		System.out.println(stmt);
	}
	
	}
	
	static private String getAttrValue(Node node, String attrName) {
		if (!node.hasAttributes())
			return "";
		NamedNodeMap nmap = node.getAttributes();
		if (nmap == null)
			return "";
		Node n = nmap.getNamedItem(attrName);
		if (n == null)
			return "";
		return n.getNodeValue();
	}

}
