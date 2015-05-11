package pt.ulisboa.tecnico.sdis.store.cli;

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
    public static final String STORE_CONTENT_TAG = "store.content.tag";
    public static final String STORE_CLIENT_ID = "store.content.id";
    
    //used for MAC integrity verification
    public static final String STORE_NAME = "mac";
    public static final String STORE_PREFIX = "sd-store-cli";
    public static final String STORE_NAMESPACE = "http://www.example.com";
    
    //used for tags in quorums
    public static final String STORE_NAME2 = "tag";
    public static final String STORE_PREFIX2 = "sd-store";
    public static final String STORE_NAMESPACE2 = "http://www.example.com";
    
    //used for ids
    public static final String STORE_NAME3 = "id";
    public static final String STORE_PREFIX3 = "sd-store-client";
    public static final String STORE_NAMESPACE3 = "http://www.example.com";
    
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

                // add header element (name, namespace prefix, namespace)
                Name name = se.createName(STORE_NAME, STORE_PREFIX, STORE_NAMESPACE);
                SOAPHeaderElement element = sh.addHeaderElement(name);

                // add header element value
                String propertyValue = (String) smc.get(STORE_CONTENT_MAC);
                String valueString = propertyValue;
                debug("VALUE: " + valueString);
                
                if(valueString != null){
                	element.addTextNode(valueString);
                }
                
                
                
                //add id
                if(smc.get(STORE_CLIENT_ID) == null){
                	return true;
                }
                
                String propertyValue2 = (String) smc.get(STORE_CLIENT_ID);
                Name name2 = se.createName(STORE_NAME3, STORE_PREFIX3, STORE_NAMESPACE3);
                SOAPHeaderElement element2 = sh.addHeaderElement(name2);
                String valueString2 = propertyValue2;
                debug("VALUE: " + valueString2);
                element2.addTextNode(valueString2);

            } else {
            	debug("Reading header in inbound SOAP message...");

                // get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();

                // check header
                if (sh == null) {
                    System.out.println("Header not found.");
                    return true;
                }

                // get first header element
                Name name = se.createName(STORE_NAME2, STORE_PREFIX2, STORE_NAMESPACE2);
               
                Iterator it = sh.getChildElements(name);
                
                // check header element
                if (!it.hasNext()) {
                	debug("Header element not found.");
                    return true;
                }
                SOAPElement element = (SOAPElement) it.next();

                // get header element value
                String valueString = element.getValue();

                // print received header
                debug("Header value is " + valueString);
                
                // put header in a property context
                smc.put(STORE_CONTENT_TAG, valueString);
                // set property scope to application client/server class can access it
                smc.setScope(STORE_CONTENT_TAG, Scope.APPLICATION);
            }
        } catch (Exception e) {
            System.out.print("Caught exception in handleMessage: ");
            System.out.println(e);
            e.printStackTrace();
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
