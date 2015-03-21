package pt.tecnico.bubbledocs.service;

// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class ExportDocument extends BubbleDocsService {
    private byte[] docXML;

    public byte[] getDocXML() {
	return docXML;
    }

    public ExportDocument(String userToken, String docName) {
	// add code here
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
	// add code here
    }
}
