package pt.tecnico.sd;

import javax.crypto.SecretKey;

import org.joda.time.DateTime;

public class ClientTicket {
	private String ticketBlob;
	private SecretKey sessionKey;
	
	public ClientTicket(String ticketBlob, byte[] authenticator) {
		this.ticketBlob = ticketBlob;
		this.sessionKey = SdCrypto.generateKey(authenticator);
	}
	
	public String getTicketBlob() {
		return ticketBlob;
	}
	
	public Ticket getTicket(SecretKey serviceKey) {
		return new Ticket(ticketBlob, serviceKey);
	}
	
	public SecretKey getSessionKey() {
		return sessionKey;
	}

}
