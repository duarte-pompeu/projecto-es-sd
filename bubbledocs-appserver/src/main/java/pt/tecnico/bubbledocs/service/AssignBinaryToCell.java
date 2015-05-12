package pt.tecnico.bubbledocs.service;
//imports
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.BinaryFunction;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.NullContentException;

public class AssignBinaryToCell extends SessionService {
	private int docId;
	private String cellId;
	private String FunctionExp;
	private String Result = "";


	public AssignBinaryToCell(String CellId, String Function, int CsId, String UserToken) {
		super(UserToken);
		this.docId = CsId;
		this.cellId = CellId;
		this.FunctionExp = Function;

	}	
	
	@Override
	public void dispatchAfterSuperService() throws BubbleDocsException 	{
		CalcSheet sheet = BubbleDocs.getInstance().getCalcSheetById(docId);	
		
		ParseBinaryFunction parse = new ParseBinaryFunction(sheet, FunctionExp);
		parse.execute();
		
		BinaryFunction function = parse.getResult();
				
		sheet.setContent(super.user, function, cellId);		
		
		try {
			Result = Integer.toString(function.getValue());
		} catch (NullContentException e) {
			Result = "#VALUE";
		}
		
	}

	public final String getResult() {
		return Result;
	}
}
