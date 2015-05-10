package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.jdom2.Element;



/**
 * @author pc-w
 *
 */
public class Range extends Range_Base implements Iterable<Cell>{
    
    /**
     * 
     */
    public Range() {
        super();
    }
    

	/**
	 * @param firstLine
	 * @param firstColumn
	 * @param lastLine
	 * @param lastColumn
	 * @param sheet
	 */
	public Range (int firstLine, int firstColumn, int lastLine,
			int lastColumn, CalcSheet sheet){
		super();
		init(firstLine, firstColumn, lastLine, lastColumn, sheet);
	}
	
	/**
	 * @param firstLine
	 * @param firstColumn
	 * @param lastLine
	 * @param lastColumn
	 * @param sheet
	 */
	public void init(int firstLine, int firstColumn, int lastLine,
			int lastColumn, CalcSheet sheet){
		
		// TODO: these values aren't used (yet)
//		int column;
//		int line;
		
		
		//TODO: THIS NEXT CODE WAS BEFORE THE RECENT CHANGE. CHECK THE DML FILE
	/*	for(line = firstLine; line <= lastLine; line++){
			for(column = firstColumn; column <= lastColumn; column++){
				this.getCellsSet().add(sheet.getCell(line, column));
			}
		}*/ 
		
		this.setLeftUpCell(sheet.getCell(firstLine, firstColumn));
		this.setRightDownCell(sheet.getCell(lastLine, lastColumn));
	}
	
	/*
	Duarte: if all we want is to iterate, we can abstract from the
	specific implementation (array? arraylist? etc.)
	and return a generic iterable
	*/
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Cell> iterator(){
		int downLine, rightmostColumn;
		
		// TODO: the values of these variables aren't used (yet)
		// int leftmostColumn, upperLine;
		
		
		ArrayList<Cell> a=new ArrayList<Cell>();
		
		// TODO: the values of these variables aren't used (yet)
		//upperLine=this.getLeftUpCell().getLine();
		//leftmostColumn=this.getLeftUpCell().getColumn();
		downLine=this.getRightDownCell().getLine();
		rightmostColumn=this.getRightDownCell().getColumn();
		
		for(int i=1; i<=downLine; i++)
			for(int j=1; j<=rightmostColumn; j++)
				a.add(this.getCalcSheet().getCell(i, j));
		
		return a.iterator();
	}
	
}
