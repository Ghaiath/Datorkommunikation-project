package main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

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
	}

}
