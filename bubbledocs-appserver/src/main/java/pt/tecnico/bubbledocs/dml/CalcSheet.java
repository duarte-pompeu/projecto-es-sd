package pt.tecnico.bubbledocs.dml;

import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;
import org.joda.time.LocalDate;

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
        //TODO
    	super();
    	//get unique id
    	this.setId(BubbleDocs.getInstance().getUniqueId());
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
    public boolean hasCell(int id) {

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
    public Content getContent(User reader, int line, int column) {
    	if (!(reader.getReadableCalcSheetSet().contains(this))) throw new PermissionException();
    	
    	return this.getCellByIndex(line, column).getContent();
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
    	throws PermissionException{    	
    	
    	Cell cell = this.getCellByIndex(line, column);
    	String cellId = cell.getId();
    	
    	setContent(writer, content, cellId);
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
    
    public void setContent(User writer, Content content, String cellId) throws PermissionException{
    	
    	if (!(writer.getWriteableCalcSheetSet().contains(writer))){
    		throw new PermissionException();
    	}
    	
    	this.getCell(cellId).setContent(content);
    }
    
    
    //These methods are implemented in BubbleDocs because users are only supposed
    //to get their own user.
    
    /*
     * This adds another user to the list of users that can use this file.
     * The file permission can be read-only or read-write
     */
    /**
     * @param author
     * @param username
     */
    public void addReader(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username is not already in this list
    	BubbleDocs.getInstance().addReader(author, username, this);    	
    }
    
    

	/**
	 * @param author
	 * @param username
	 */
	public void addWriter(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username MUST be able to read this file
		BubbleDocs.getInstance().addWriter(author, username, this);
    }
    
    /*
     * This removes a user that is in the list of users that can use 
     * a calcsheet.
     */
    /**
     * @param author
     * @param username
     */
    public void removeReader(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username can read this file and CANNOT write this file
    	BubbleDocs.getInstance().removeReader(author, username, this);
    }
    
    /**
     * @param author
     * @param username
     */
    public void removeWriter(User author, String username) {
    	//PRECOND: author owns or can write this file
    	//PRECOND: username can read this file and CANNOT write this file
    	BubbleDocs.getInstance().removeWriter(author, username, this);
    }
    
    /**
     * @param line
     * @param column
     * @return
     */
    private boolean outsideBounds(int line, int column) {
    	return line < 1 || column < 1 || line > this.getLines() || column > this.getColumns();
    }
    
    /**
     * @param line
     * @param column
     * @return
     */
    private Cell getCellByIndex(int line, int column) {
    	if (outsideBounds(line, column)) {
    		throw new IllegalArgumentException("Out of bounds");
    	}
    	
    	for (Cell cell : this.getCellSet()) {
    		if (cell.getLine() == line && cell.getColumn() == column) {
    			return cell;
    		}
    	}
    	throw new NotFoundException();
    }
    
    /**
     * @return
     */
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
    
    /**
     * @param calcSheetElement
     */
    public void importFromXML(Element calcSheetElement) {
    	Iterator<User> it=BubbleDocs.getInstance().getUserSet().iterator();
    	User u;
    	do{
    		u= it.next();
    		if(u.getUserName().compareTo(calcSheetElement.getAttribute("creator").getValue())==0)
    			break;
    	}while(it.hasNext());
    	if(u!=null)
    		this.setCreator(u);
    	this.setDate(new LocalDate(calcSheetElement.getAttribute("date").getValue()));
    	this.setId(new Integer(calcSheetElement.getAttribute("id").getValue()));
    	this.setName(new String(calcSheetElement.getAttribute("name").getValue()));
    	this.setLines(new Integer(calcSheetElement.getAttribute("lines").getValue()));
    	this.setColumns(new Integer(calcSheetElement.getAttribute("columns").getValue()));
    	
    	//to aid in the importation of references and ranges
    	BubbleDocs.currentSheet=this;
    	
    	List<Element> cells = calcSheetElement.getChildren();
    	
    	for (Element cell : cells) {
    	    Cell c = new Cell();
    	    c.importFromXML(cell);
    	    addCell(c);
    	}
    	this.setBubbleDocs(BubbleDocs.getInstance());
    }

    /**
     * @return
     */
    public Element exportToXML() {
    	Element element = new Element("calcSheet");
    	try{
    	element.setAttribute("creator", this.getCreator().getUserName());
    	element.setAttribute("date", this.getDate().toString());
    	element.setAttribute("id", this.getId().toString());
    	element.setAttribute("name", this.getName().toString());
    	element.setAttribute("lines", this.getLines().toString());
    	element.setAttribute("columns", this.getColumns().toString());
    	
    	}catch(Exception e){System.out.println(e.toString());}
    	
    	for(Cell c: this.getCellSet())
    		element.addContent(c.exportToXML());
	
    	return element;
    	}
    
}
