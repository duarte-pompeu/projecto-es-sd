package pt.tecnico.bubbledocs.service;
//imports
import pt.tecnico.bubbledocs.domain.API;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
//Is it a Reference?
//incorrect Cell or Reference given
//User doesnt have write permissions
//token in session?
//Content Null

public class AssignReferenceCell extends SessionService {
	private String result;
	private int docID;
	private String cellID;
	private String refID;

	public AssignReferenceCell(String tokenUser, int docID, String cellID, String reference) {
		super(tokenUser);
		
		this.docID = docID;
		this.cellID = cellID;
		this.refID = reference;

	}

	@Override
	protected void afterSuperAction() throws BubbleDocsException {
		result = API.onCSAssignReference(super.token, docID, cellID, refID);
	}

	public final String getResult() {
		return result;
	}
}
