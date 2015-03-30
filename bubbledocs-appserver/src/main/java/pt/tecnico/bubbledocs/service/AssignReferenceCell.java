package pt.tecnico.bubbledocs.service;
//imports
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException; //token in session?
import pt.tecnico.bubbledocs.exceptions.InvalidFormatException; //Is it a Reference?
import pt.tecnico.bubbledocs.exceptions.NotFoundException; //incorrect Cell or Reference given
import pt.tecnico.bubbledocs.exceptions.PermissionException; //User doesnt have write permissions

public class AssignReferenceCell extends BubbleDocsService {
	private String result;
	private String accessToken;
	private int docId;
	private String cellId;
	private String refId;

	public AssignReferenceCell(String tokenUser, int docId, String cellId, String reference) {
		this.accessToken = tokenUser;
		this.docId = docId;
		this.cellId = cellId;
		this.refId = reference;

	}

	@Override
	protected void dispatch() throws InvalidFormatException, NotFoundException, PermissionException {
		//token in session
		BubbleDocs bd = BubbleDocs.getInstance();
		User user;
		try{
			user = getSessionFromToken(accessToken).getUser();
		}
		catch(BubbleDocsException e){
			throw e;
		}

		CalcSheet c1 = bd.getCalcSheetById(docId);

		Cell cell = c1.getCell(cellId);
		if (cell == null) throw new NotFoundException("Cell out of bounds in " + cellId);

		Cell refcell = c1.getCell(refId);
		if (refcell == null) throw new NotFoundException("Reference out of bounds in " + refId);

		c1.setContent(user, new Reference(c1.getCell(refId)), cellId);

		result = Integer.toString(cell.getContent().getValue());

	}

	public final String getResult() {
		return result;
	}
}
