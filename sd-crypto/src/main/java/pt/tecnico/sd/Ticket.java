package pt.tecnico.sd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import javax.crypto.SecretKey;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.joda.time.DateTime;

public class Ticket {
	
	//It's equal to BubbleDocs'
	private static final int EXPIRATION_MINUTES = 120;
	
	private String username;
	private String service;
	private DateTime since;
	private DateTime expire;
	private SecretKey sessionKey;
	
	public Ticket(String username, String service, SecretKey sessionKey) {
		this.username = username;
		this.service = service;
		this.since = DateTime.now();
		this.expire = this.since.plusMinutes(EXPIRATION_MINUTES);
		this.sessionKey = SdCrypto.generateRandomKey();
	}
	
	//Receives a base64 encoded blob and decrypts given the service key
	public Ticket(String base64blob, SecretKey serviceKey) {
		byte[] encryptedBlob=parseBase64Binary(base64blob);
		byte[] decryptedBlob = SdCrypto.decrypt(serviceKey, encryptedBlob);	
		
		
		SAXBuilder b=new SAXBuilder();
		Document xmlDoc;
		try {
			xmlDoc = b.build(new ByteArrayInputStream(decryptedBlob));
		} catch (JDOMException | IOException e) {
			throw new RuntimeException(e);
		}
		
        XPathFactory xFactory = XPathFactory.instance();

        XPathExpression<Element> expr = xFactory.compile("/ticket", Filters.element());
        List<Element> links = expr.evaluate(xmlDoc);
        Element ticketElement=links.get(0);

        //DEBUG
        //XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
        //System.out.println("Produced from import");
        //try {xmlOut.output(xmlDoc, System.out);} catch (Exception e) {}
      		
        username=ticketElement.getChild("username").getValue();
        service=ticketElement.getChild("service").getValue();
		since=DateTime.parse(ticketElement.getChild("since").getValue());
		expire=DateTime.parse(ticketElement.getChild("expire").getValue());
	}
	
	/*
	 * Recebe a chave de serviço (partilhada entre SD-ID e SD-STORE)
	 * produz um xml (com Format.getRawFormat()) como descrito em notas.txt
	 * cifra o xml produzido para um blob
	 * e codifica o blob em Base64 
	 * 
	 * Exemplo:
	 * 
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ticket>
	 *   <username>alice</username>
	 *   <service>SD-Store</service>
	 *   <since>2015-05-02T16:45:59.270+01:00</since>
	 *   <expire>2015-05-02T18:45:59.270+01:00</expire>
	 *   <sessionKey>HJtJRf3WJtzWN1HNFT6kf7VYcCWogJIc</sessionKey>
	 * </ticket>
	 * 
	 */
	public String getBlob(SecretKey serviceKey) {
		Element root = new Element("ticket");
		
		root.addContent(new Element("username")
		        .addContent(new Text(username)))
		      
		    .addContent(new Element("service")
		        .addContent(new Text(service)))
		      
		    .addContent(new Element("since")
		        .addContent(new Text(since.toString())))
		      
		    .addContent(new Element("expire")
		        .addContent(new Text(expire.toString())))
		    
		    .addContent(new Element("sessionKey")
		        .addContent(new Text(encodeSessionKey())));
		
		Document document = new Document(root);
		
		XMLOutputter xmlOut = new XMLOutputter(Format.getRawFormat());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		//DEBUG
		//XMLOutputter debugXmlOut = new XMLOutputter(Format.getPrettyFormat());
		
		try {
			xmlOut.output(document, out);
			
			/*
			xmlOut.output(document, System.out);
			System.out.println();
			debugXmlOut.output(document, System.out);
			*/
			
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
				
		byte[] plainTextData = out.toByteArray();
		byte[] encrypted = SdCrypto.encrypt(serviceKey, plainTextData);		
		
		return printBase64Binary(encrypted);
	}

	private String encodeSessionKey() {
		return new String(printBase64Binary(sessionKey.getEncoded()));
	}
	
	//test
	public static void main(String args[]) {
		//Service key example
		SecretKey serviceKey = SdCrypto.generateRandomKey();
		System.out.println("Here's the service key");
		System.out.println("Hexadecimal: " + printHexBinary(serviceKey.getEncoded()));
		System.out.println("Base64:      " + printBase64Binary(serviceKey.getEncoded()));
		System.out.println();
	
		//Let's create a ticket
		Ticket ticket = new Ticket("alice", "SD-Store", SdCrypto.generateRandomKey());
		String blob = ticket.getBlob(serviceKey);
		
		System.out.println();
		System.out.println("Encrypted - (alice can't decrypt this):");
		System.out.println(blob);
		
		System.out.println();
		Ticket recovered = new Ticket(blob, serviceKey);
		ticket.getBlob(serviceKey);
		
	}
}

