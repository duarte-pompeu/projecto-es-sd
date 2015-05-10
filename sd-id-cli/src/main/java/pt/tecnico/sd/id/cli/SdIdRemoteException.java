package pt.tecnico.sd.id.cli;

@SuppressWarnings("serial")
public class SdIdRemoteException extends Exception {

	public SdIdRemoteException() {		
	}

	public SdIdRemoteException(String message) {
		super(message);
	}

	public SdIdRemoteException(Throwable cause) {
		super(cause);
	}

	public SdIdRemoteException(String message, Throwable cause) {
		super(message, cause);
	}

	public SdIdRemoteException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
