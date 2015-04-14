package pt.tecnico.sd.id;

public class SdIdClient {
    private static SdIdClient instance = null;
    //port etc etc

    private SdIdClient() {

    }

    public static SdIdClient getInstance() {
	if (instance == null) {
	    try {
		instance = new SdIdClient();
	    } catch (/*Some*/Exception e) {
		instance = null;
		throw new /*Some*/Exception();
	    }
	}

	return instance;
    }
    
    public void createUser() {

    }
    
    public void renewPassword() {

    }
    
    public void removeUser() {

    }
    
    public byte[] requestAuthentication() {
	return null;
    }
}
