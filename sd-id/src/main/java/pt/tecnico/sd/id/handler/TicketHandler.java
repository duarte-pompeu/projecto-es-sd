package pt.tecnico.sd.id.handler;

import java.util.Set;

import javax.xml.soap.*;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class TicketHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String TICKET_NAMESPACE = "urn:pt:tecnico:sd";
	public static final String TICKET_PROPERTY = "ticket";
	public static final String TICKET_HEADER = "ticketHeader";
	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		if (outbound) {
			String ticketBlob = (String) context.get(TICKET_PROPERTY);
			if (ticketBlob == null) return true;
			//put ticket blob in header
			try {
				SOAPMessage msg = context.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader header = se.getHeader();
				
				if (header == null) {
					header = se.addHeader();
				}
				
				Name name = se.createName(TICKET_HEADER, "t", TICKET_NAMESPACE);
				SOAPHeaderElement element = header.addHeaderElement(name);
				
				element.addTextNode(ticketBlob);
				
			} catch (SOAPException e) {
				System.err.println("Failed to add SOAPHEADER");
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}
}
