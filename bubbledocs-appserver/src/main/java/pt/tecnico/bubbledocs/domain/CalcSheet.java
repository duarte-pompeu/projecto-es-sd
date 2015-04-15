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
    
    /**
     * Authors: Duarte
     * Checks if user has writer permissions over referenced sheet.
     * @param writer
     * @return
     */
    /*
    public boolean allowedToWrite(User user){
    	for(CalcSheet cs: user.getWriteableCalcSheetSet()){
    		if(cs.getId().equals(this.getId()))
    			return true;
    	}
    	
    	return false;
    }
    */
    
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
    		throw new NotFoundException("" + line + ";" + column + " is out of bounds.");
    	}
    	
    	for (Cell cell : this.getCellSet()) {
    		if (cell.getLine() == line && cell.getColumn() == column) {
    			return cell;
    		}
    	}
    	throw new RuntimeException("Error. Cell with position " + line + ";" + column + " does not exist when it should");
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
    	HashMap<String, Boolean> map=new HashMap<String, Boolean>();
    	
    	for (Element cell : cells) {
    	    Cell c = new Cell();
    	    c.importFromXML(cell);
    	    addCell(c);
    	    map.put(c.getId(), true);
    	}
    	for (int i=1; i<=this.getLines(); ++i) {
    		for (int j=1; j<=this.getColumns(); ++j) {
    			if(!map.containsKey(String.valueOf(i)+";"+String.valueOf(j)))
    				this.addCell(new Cell(i, j));
    		}
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
    	
    	for(Cell c: this.getCellSet()){
    		if(c.getContent()!=null)
    			element.addContent(c.exportToXML());
    	}
    		
	
    	return element;
    	}
    
}
