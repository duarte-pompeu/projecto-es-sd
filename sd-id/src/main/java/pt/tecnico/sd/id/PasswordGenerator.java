package pt.tecnico.sd.id;

import java.security.SecureRandom;


public class PasswordGenerator {
	SecureRandom rng = new SecureRandom();
	
	public byte[] generatePassword() {
		StringBuilder builder = new StringBuilder();
		
		//ten digits and 52 letters, both upper and lower case.
		//Java 8 streams and lambdas for the win!
		rng.ints(0, 62).limit(8)
		               .map(x -> {
			if      (x < 10)            return '0'+x;
			else if (x >= 10 && x < 36) return 'a'+x-10;
			else                        return 'A'+x-36;
		}).forEach(x -> builder.append((char) x));
		
		return builder.toString().getBytes();
	}
}
