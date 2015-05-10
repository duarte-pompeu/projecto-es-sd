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
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class ClientHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    public static final String CONTEXT_PROPERTY = "my.property";
    public static final String STORE_CONTENT_MAC = "store.content.mac";
    
    public static final String STORE_NAME = "mac";
    public static final String STORE_PREFIX = "sd-store-cli";
    public static final String STORE_NAMESPACE = "http://www.example.com";
    

    //
    // Handler interface methods
    //
    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        System.out.println("AddHeaderHandler: Handling message.");

        Boolean outboundElement = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {
            if (outboundElement.booleanValue()) {
                System.out.println("Writing header in outbound SOAP message...");

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
                System.out.println("VALUE: " + valueString);
                element.addTextNode(valueString);

            } else {
                System.out.println("Reading header in inbound SOAP message...");

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
                Name name = se.createName("myHeader", "d", "http://demo");
                Iterator it = sh.getChildElements(name);
                // check header element
                if (!it.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                SOAPElement element = (SOAPElement) it.next();

                // get header element value
                String valueString = element.getValue();
                int value = Integer.parseInt(valueString);

                // print received header
                System.out.println("Header value is " + value);

                // put header in a property context
                smc.put(CONTEXT_PROPERTY, value);
                // set property scope to application client/server class can access it
                smc.setScope(CONTEXT_PROPERTY, Scope.APPLICATION);

            }
        } catch (Exception e) {
            System.out.print("Caught exception in handleMessage: ");
            System.out.println(e);
            System.out.println("Continue normal processing...");
        }

        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        System.out.println("Ignoring fault message...");
        return true;
    }

    public void close(MessageContext messageContext) {
    }

}
