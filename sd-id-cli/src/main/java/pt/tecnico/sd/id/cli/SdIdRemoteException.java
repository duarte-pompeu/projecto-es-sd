package pt.tecnico.sd.id.cli;

public class SdIdRemoteException extends Exception {

	private static final long serialVersionUID = -5818599358259141878L;

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
