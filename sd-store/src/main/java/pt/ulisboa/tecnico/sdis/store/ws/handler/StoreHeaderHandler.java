package pt.ulisboa.tecnico.sdis.store.ws.handler;

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

import pt.ulisboa.tecnico.sdis.store.service.SDStoreService;
import pt.ulisboa.tecnico.sdis.store.ws.SDStoreMain;

public class StoreHeaderHandler implements SOAPHandler<SOAPMessageContext> {

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
    

    public Set<QName> getHeaders() {
        return null;
    }
    
    public void debug(String s){
    	if(DEBUG){
    		System.out.println(s);
    	}
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
                Name name = se.createName(STORE_NAME2, STORE_PREFIX2, STORE_NAMESPACE2);
                SOAPHeaderElement element = sh.addHeaderElement(name);

                // add header element value
                String valueString = new String(SDStoreService.lastSeq() + ";" + SDStoreService.lastUserNumber());
                debug("VALUE: " + valueString);
                element.addTextNode(valueString);  

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
                Name name = se.createName(STORE_NAME, STORE_PREFIX, STORE_NAMESPACE);
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
                SDStoreMain.RECEIVED_MAC_STR = valueString;
                
                // put header in a property context
                smc.put(STORE_CONTENT_MAC, valueString);
                // set property scope to application client/server class can access it
                smc.setScope(STORE_CONTENT_MAC, Scope.APPLICATION);
                
                // get ID
                Name name2 = se.createName(STORE_NAME3, STORE_PREFIX3, STORE_NAMESPACE3);
                Iterator it2 = sh.getChildElements(name2);
                
                // check header element
                if (!it2.hasNext()) {
                	debug("Header element not found.");
                    return true;
                }
                SOAPElement element2 = (SOAPElement) it2.next();

                // get header element value
                String valueString2 = element2.getValue();

                // print received header
                debug("Header value is " + valueString2);
                SDStoreMain.RECEIVED_CLIENT_ID = valueString2;
                
                // put header in a property context
                smc.put(STORE_CLIENT_ID, valueString);
                // set property scope to application client/server class can access it
                smc.setScope(STORE_CLIENT_ID, Scope.APPLICATION);
                
                
                
                
                

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

