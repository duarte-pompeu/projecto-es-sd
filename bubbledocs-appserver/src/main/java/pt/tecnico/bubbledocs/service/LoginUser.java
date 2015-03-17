package pt.tecnico.bubbledocs.service;

// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class LoginUser extends BubbleDocsService {

    private String userToken;

    public LoginUser(String username, String password) {
	// add code here
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
	// add code here
    }

    public final String getUserToken() {
	return userToken;
    }
}
