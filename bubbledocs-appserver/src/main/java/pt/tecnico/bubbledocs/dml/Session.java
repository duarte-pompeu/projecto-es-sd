package pt.tecnico.bubbledocs.dml;

import java.util.Random;
import org.joda.time.DateTime;

public class Session extends Session_Base {
    
	private final static int EXPIRATION = 120; //minutes
	
    public Session() {
        super();
        this.setToken(generateRandomToken());
        touch();        
    }
    
    public boolean isExpired() {
    	return this.getExpiration().isBeforeNow();
    }
    
    //time is the instant being tested
    public boolean isExpired(DateTime time) {
    	return this.getExpiration().isBefore(time);
    }
    
    //Like unix touch, this refreshes the access and expiration time
    public void touch() {
    	DateTime now = new DateTime();
        
        this.setAccess(now);
        this.setExpiration(now.plusMinutes(EXPIRATION));
    }
    
    //return a randomly generated alphanumeric string, with at most 32 characters.
    private String generateRandomToken() {
    	Random rng = BubbleDocs.getInstance().getRng();
    	StringBuilder builder = new StringBuilder();

    	for (int i=0; i<3; ++i) {
    		//generate a positive 63 bit number and write it as a base 36 number
    	    builder.append(Long.toString((rng.nextLong() >>> 1), 36));	    
    	}

    	if (builder.length() >= 32)
    	    return builder.substring(0,32);
    	else
    	    return builder.toString();
    }
    
}
