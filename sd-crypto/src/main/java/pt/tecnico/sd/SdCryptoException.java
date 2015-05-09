package pt.tecnico.sd;

@SuppressWarnings("serial")
public class SdCryptoException extends RuntimeException {

	public SdCryptoException() {
		super();
	}
	
	public SdCryptoException(String message) {
		super(message);
	}

	public SdCryptoException(Throwable cause) {
		super(cause);
	}

	public SdCryptoException(String message, Throwable cause) {
		super(message, cause);
	}

	public SdCryptoException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
