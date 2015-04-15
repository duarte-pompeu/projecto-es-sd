package pt.tecnico.bubbledocs.service;
//imports
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException; //Is it a Reference?
import pt.tecnico.bubbledocs.exceptions.NotFoundException; //incorrect Cell or Reference given
import pt.tecnico.bubbledocs.exceptions.PermissionException; //User doesnt have write permissions
//token in session?
//Content Null

public class AssignReferenceCell extends BubbleDocsService {
	private String result;
	private String accessToken;
	private int docID;
	private String cellID;
	private String refID;

	public AssignReferenceCell(String tokenUser, int docID, String cellID, String reference) {
		this.accessToken = tokenUser;
		this.docID = docID;
		this.cellID = cellID;
		this.refID = reference;

	}

	@Override
	protected void dispatch() throws InvalidFormatException, NotFoundException, PermissionException {
		BubbleDocs bd = BubbleDocs.getInstance();
		Content content = bd.assignReference(accessToken, docID, cellID, refID);
		
		try {
		result = Integer.toString(content.getValue());
		} catch (NullPointerException e) {
			result = "#VALUE";
		}
	}

	public final String getResult() {
		return result;
	}
}
