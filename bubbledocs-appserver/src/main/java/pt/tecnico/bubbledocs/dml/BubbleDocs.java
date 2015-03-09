package pt.tecnico.bubbledocs.dml;

public class BubbleDocs extends BubbleDocs_Base {
    
    public BubbleDocs() {
        super();
    }
    
    //for now only used for the xml import
    public static CalcSheet currentSheet;
    
    
    public static Content parseName(String name){
    	switch(name){
    	case "literal": return new Literal();
    	case "reference": return new Reference();
    	case "add": return new Add();
    	case "sub": return new Sub();
    	case "mul": return new Mul();
    	case "div": return new Div();
    	case "avg": return new Avg();
    	case "prd": return new Prd();
    	default: return null;
    	}	
    }
}
