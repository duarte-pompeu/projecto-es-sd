package pt.tecnico.sd.id.cli.handler;

import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

//Client ticket handler
public class TicketHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String TICKET_NAMESPACE = "urn:pt:tecnico:sd";
	public static final String TICKET_PROPERTY = "ticket";
	public static final String TICKET_HEADER = "ticketHeader";
	
	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		// TODO Auto-generated method stub
		boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		try {
			//inbound
			if (!outbound) {
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();

				//It has no header
				if (sh == null) {
					return true;
				}

				Name name = se.createName(TICKET_HEADER, "t", TICKET_NAMESPACE);			
				@SuppressWarnings("rawtypes")
				Iterator it = sh.getChildElements(name);

				//The header has no children
				if (!it.hasNext()) {
					return true;
				}

				SOAPElement element = (SOAPElement) it.next();

				String ticketBlob = element.getValue();

				smc.put(TICKET_PROPERTY, ticketBlob);
				smc.setScope(TICKET_PROPERTY, Scope.APPLICATION);		
			}
		} catch (SOAPException e) {
			System.err.println("Failed to get SOAPHEADER");
			return false;
		}

		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
		
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

}
