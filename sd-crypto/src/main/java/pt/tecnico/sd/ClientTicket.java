package pt.tecnico.sd;

import javax.crypto.SecretKey;

public class ClientTicket {
	String ticketBlob;
	SecretKey sessionKey;
	
	public ClientTicket(String ticketBlob, byte[] authenticator) {
		this.ticketBlob = ticketBlob;
		this.sessionKey = SdCrypto.generateKey(authenticator);
	}
	
	public String getTicketBlob() {
		return ticketBlob;
	}
	
	public SecretKey getSessionKey() {
		return sessionKey;
	}
}
