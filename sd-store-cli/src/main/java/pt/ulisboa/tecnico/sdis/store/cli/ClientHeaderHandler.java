package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class ClientHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    public static final String CONTEXT_PROPERTY = "my.property";
    public static final String STORE_CONTENT_MAC = "store.content.mac";
    
    public static final String STORE_NAME = "mac";
    public static final String STORE_PREFIX = "sd-store-cli";
    public static final String STORE_NAMESPACE = "http://www.example.com";

    public static final boolean DEBUG = false;
    
    public void debug(String s){
    	if(DEBUG){
    		System.out.println(s);
    	}
    }

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        debug("AddHeaderHandler: Handling message.");
        Boolean outboundElement = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {
            if (outboundElement.booleanValue()) {
            	debug("Writing header in outbound SOAP message...");

                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();

                // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();
                
                if(StoreClient.MAC == null){
                	return true;
                }

                // add header element (name, namespace prefix, namespace)
                Name name = se.createName(STORE_NAME, STORE_PREFIX, STORE_NAMESPACE);
                SOAPHeaderElement element = sh.addHeaderElement(name);

                // add header element value
                String valueString = printBase64Binary(StoreClient.MAC);
                debug("VALUE: " + valueString);
                element.addTextNode(valueString);

            } else {
                
            }
        } catch (Exception e) {
            System.out.print("Caught exception in handleMessage: ");
            System.out.println(e);
            System.out.println("Continue normal processing...");
        }

        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        debug("Ignoring fault message...");
        return true;
    }

    public void close(MessageContext messageContext) {
    }
}
