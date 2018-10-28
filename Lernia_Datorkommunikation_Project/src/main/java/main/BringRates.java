package main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
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
	}

}
