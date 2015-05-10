package pt.tecnico.bubbledocs.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jdom2.Element;
import org.joda.time.LocalDate;

import pt.tecnico.bubbledocs.exceptions.InvalidValueException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;



/**
 * @author pc-w
 *
 */
public class CalcSheet extends CalcSheet_Base {
		
	/**
	 * 
	 */
	public CalcSheet() {
		super();
	}
	
    /**
     * @param name
     * @param lines
     * @param columns
     */
    public CalcSheet(String name, int lines, int columns) {
    	super();
    	
    	//check if name is null
    	if (name.equals(null))
    		throw new NullPointerException("CalcSheet name can't be null");
    	
    	//check if Row Value is greater then 0
    	 if(lines < 1)
    		 throw new InvalidValueException(lines + " isn't greater then zero.");
    	
    	//check if Column Value is greater then 0
    	 if(columns < 1)
    		 throw new InvalidValueException(columns + " isn't greater then zero.");
    	
    	
    	//get unique id
    	this.setId(BubbleDocs.getInstance().getUniqueId());
    	this.init(name, lines, columns);
    
    }
    
    public void init(String name, int lines, int columns){
    	
    	this.setName(name);
    	this.setLines(new Integer(lines));
    	this.setColumns(new Integer(columns));
    	this.setDate(new LocalDate());

    	for (int i=1; i<=lines; ++i) {
    		for (int j=1; j<=columns; ++j) {

    			this.addCell(new Cell(i, j));
    		}
    	} 
    	
    }
    
    
    //This method shouldn't be used by a user.
    /**
     * @param line
     * @param column
     * @return
     */
    public Cell getCell(int line, int column) {    	
    	return this.getCellByIndex(line, column); 
    }

    public Cell getCell(String id) {
    	//check if it's a valid id.
    	final String positiveNum = "[1-9](\\d)*";
    	final String pattern = positiveNum + ";" + positiveNum;
    	boolean matches = Pattern.compile(pattern).matcher(id).matches();
    	if (!matches) {
    		throw new NotFoundException("Invalid id format");
    	}
    	
    	for(Cell c : this.getCellSet()) {
    		if(c.getId().equals(id))
    			return c;
    	}
    	return null;

    }

    /**
     * @param id
     * @return
     */
    public boolean hasCell(String id) {

    	for(Cell c : this.getCellSet()) {
    		if(c.getId().equals(id))
    			return true;
    	}
    	return false;
    }

    /**
     * @param reader
     * @param line
     * @param column
     * @return
     */
    public Content getContent(User reader, int line, int column) throws PermissionException {
    	Cell cell = this.getCellByIndex(line, column);    	
    	return getContent(reader, cell);
    }
    
    public Content getContent(User reader, String cellId) throws PermissionException {
    	Cell cell = this.getCell(cellId);    	
    	return getContent(reader, cell);
    }
    
    private Content getContent(User reader, Cell cell) {
    	  //If this calcSheet is not one of the calcSheet readable by reader. 
    	if (!reader.canRead(this)) throw new PermissionException();    	
    	return cell.getContent();
    }
    
    //this may be changed to receive a string, and using a Content factory, create the Content
    /**
     * @param writer
     * @param content
     * @param line
     * @param column
     * 
     * Only used to convert a line and column to a cellID. 
     * Then directs the hard work (checking permissions and writing on cell) 
     * to setContent(User writer, Content content, int cellId)
     */
    public void setContent(User writer, Content content, int line, int column)
    	throws PermissionException {    	
    	Cell cell = this.getCellByIndex(line, column);
    	setContent(writer, content, cell);
    }
    
    /**
     * 
     * @param writer
     * @param content
     * @param cellId
     * @throws PermissionException
     * 
     * Sets cell content if user has enough permissions.
     */
    
    public void setContent(User writer, Content content, String cellId) throws PermissionException {
    	Cell cell = this.getCell(cellId);
    	if (cell == null) throw new NotFoundException(cellId + " does not exist");
    	setContent(writer, content, cell);
    }
    
    /**
     * Sets content with given cell. Used by setContent(User,Content,String) and setContent(User,Content,int,int)
     * @param writer
     * @param content
     * @param cell
     */
    private void setContent(User writer, Content content, Cell cell) {
    	if(!writer.canWrite(this) || cell.getProtect()) {
    		throw new PermissionException();
    	}
    	
    	cell.setContent(content);
    }
    
    /*
     * This adds another user to the list of users that can use this file.
     * The file permission can be read-only or read-write
     */
    public void addReader(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username is not already in this list
    	BubbleDocs.getInstance().addReader(author, username, this);    	
    }
    
    
	public void addWriter(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username MUST be able to read this file
		BubbleDocs.getInstance().addWriter(author, username, this);
    }
    
    /*
     * This removes a user that is in the list of users that can use 
     * a calcsheet.
     */
    public void removeReader(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username can read this file and CANNOT write this file
    	BubbleDocs.getInstance().removeReader(author, username, this);
    }
    
    
    public void removeWriter(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username can read this file and CANNOT write this file
    	BubbleDocs.getInstance().removeWriter(author, username, this);
    }
    
    
    private boolean outsideBounds(int line, int column) {
    	return line < 1 || column < 1 || line > this.getLines() || column > this.getColumns();
    }
    
    
    private Cell getCellByIndex(int line, int column) {
    	if (outsideBounds(line, column)) {
    		throw new NotFoundException("" + line + ";" + column + " is out of bounds.");
    	}
    	
    	for (Cell cell : this.getCellSet()) {
    		if (cell.getLine() == line && cell.getColumn() == column) {
    			return cell;
    		}
    	}
    	throw new RuntimeException("Error. Cell with position " + line + ";" + column + " does not exist when it should");
    }
    
    
    public void deleteAllCells() {
    	for (Cell cell : this.getCellSet()) {
    		cell.delete();
    		}
    	
    	for (User r : this.getReadingUserSet())
    		r.getReadableCalcSheetSet().remove(this);
    	this.getReadingUserSet().clear();// y u no work
    	
    	for (User w : this.getWritingUserSet())
    		w.getWriteableCalcSheetSet().remove(this);
    	this.getWritingUserSet().clear();
    	
    	
        this.setCreator(null);
    	//this.getRangeSet().clear();

    	setBubbleDocs(null);
    	deleteDomainObject();
    }
    

    protected String[][] getCellsMatrix(){
    	String [][] matrix = new String[this.getLines()][this.getColumns()];
    	int l, c;
    	
    	for(Cell ce: getCellSet()){
    		l = ce.getLine();
    		c = ce.getColumn();
    		
    		matrix[l-1][c-1] = ce.contentString();
    	}
    	
    	return matrix;
    }
    
    
    protected String markdownPrint(){
    	String[][] cellsMatrix = this.getCellsMatrix();
    	
    	String mark = new String();
    	int nLines = cellsMatrix.length;
    	int nCols = cellsMatrix[0].length;
    	int c, l;
    	
    	int cell_len = 5;
    	
    	//header
    	mark += "|";
    	for(c = 1; c <= nCols; c++){
    		mark += nChars(' ', cell_len);
    		mark += "|";
    	}
    	mark += "\n";
    	
    	// header-content division
    	mark += "|";
    	for(c = 1; c <= nCols; c++){
    		mark += nChars('-', cell_len);
    		mark += "|";
    	}
    	mark += "\n";
    	
    	
    	// table content
    	for(l = 1; l <= nLines; l++){
    		mark += "|";
    		
    		for(c = 1; c <= nCols; c++){
        		Content con = this.getCell(l, c).getContent();
        		if (con == null){
        			mark += nChars(' ', cell_len);
        			mark += "|";
        			continue;
        		}
        		
        		String con_str = con.toString();
        		if (con_str.length() > cell_len){
        			con_str = con_str.substring(0, cell_len -2) + "..";
        		}
        		
        		con_str += nChars(' ', cell_len - con_str.length());
        		mark += con_str;
        		mark += "|";
        	}
    		
    		mark += "\n";
    	}
    	
    	return mark;
    }

    
    private static String nChars(char ch, int n){
    	String out = new String();
    	
    	for(int i = 0; i < n; i++){
    		out += ch;
    	}
    	
    	return out;
    }
    
}
