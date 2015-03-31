package pt.tecnico.bubbledocs.domain;

import java.util.Random;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exceptions.InvalidValueException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.RepeatedIdentificationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

/**
 * @author Diogo, Marcos, Tiago, Duarte
 * This class represents the bubbleDocs application.
 * Contains the set of users and the set of calcSheets.
 * Generate unique an unique Id for each new calcSheet.
 */
public class BubbleDocs extends BubbleDocs_Base {

	Random rng;
	/**
	 * This method makes a connection to the database, returning the instance of bubbledocs saved there.
	 * If the database is empty then it creates a new instance.
	 * @return the bubbleDocs instance
	 */
	public static BubbleDocs getInstance() {
		BubbleDocs pb;

		pb = FenixFramework.getDomainRoot().getBubbleDocs();
		if(pb==null)
			pb = new BubbleDocs(); 

		return pb;
	}

	/**
	 * The bubbleDocs constructor, only to be used when the database is empty.
	 */
	private BubbleDocs() {
		FenixFramework.getDomainRoot().setBubbleDocs(this);
		this.setIdCounter(1);
		this.rng = new Random();
	}
	
	/**
	 * Gets the random number generator (RNG) associated with bubbledocs.
	 * @return this random number generator
	 */
	public Random getRng() {
		if (rng == null) this.rng = new Random();
		return this.rng;
	}


	/**
	 * This variable is used during the xml importation of a calcSheet.
	 * It is used to identify the calcSheet which is currently being imported.
	 */
	public static CalcSheet currentSheet;


	/**
	 * Searches the calcSheet set and returns the calcSheet with a specific name.
	 * @param name the calcSheet
	 * @return The desired calcSheet. If it is not found then null is returned.
	 */
	public CalcSheet getCalcSheetByName(String name)throws NotFoundException {
		for(CalcSheet c: this.getCalcSheetSet()){
			if(c.getName().equals(name))
				return c;
		}
		throw new NotFoundException("No calcSheet has this: '" + name + "' as name.");
	}
	
	/**
	 * Searches the calcSheet set and returns the calcSheet with the id
	 * @param id
	 * @return the calcSheet with id if it exists.
	 * @throws NotFoundException if there is no calcSheet with that id
	 */
	public CalcSheet getCalcSheetById(int id) throws NotFoundException {
		for (CalcSheet c : this.getCalcSheetSet()) {
			if (c.getId() == id) {
				return c;
			}
		}
		
		throw new NotFoundException("No calcSheet has this: '" + id + "' as id.");
	}	


	/**
	 * Searches the user set and returns the user with a specific user name.
	 * @param name the user's user name
	 * @return The desired user. If it is not found, a NotFoundException is thrown.
	 */
	public User getUser(String username) throws NotFoundException {

		for(User tempUser: this.getUserSet()){
			if (tempUser.getUserName().equals(username)){
				return tempUser;
			}
		}


		throw new NotFoundException("NotFoundException: " + 
				" User " + username + " not found.");
	}



	/**
	 * Searches the calcSheet set removes the calcSheet with a specific name, created by a specific user.
	 * @param name the calcSheet
	 * @param user the user
	 */
	public void removeCalcSheet(String name, String user) {

		for(CalcSheet c: this.getCalcSheetSet()){
			if( c.getCreator().getUserName().compareTo(user)==0 &&
					c.getName().compareTo(name)==0)
				this.removeCalcSheet(c);
		}
	}


	/**
	 * This method generates a unique calcSheet id.
	 * @return The unique calcSheet id generated.
	 */
	public synchronized int getUniqueId() {
		int id = this.getIdCounter();
		this.setIdCounter(id + 1);
		return id;
	}

	/**
	 * @param name
	 * @return
	 */
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

	public static FunctionArgument parseArgumentName(String name){
		switch(name){
		case "literalArgument": return new LiteralArgument();
		case "referenceArgument": return new ReferenceArgument();
		default: return null;
		}	
	}
	
	
	
	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public User login(String username, String password) throws NotFoundException {
		User out = null;
		try {
			out = getUser(username); //throws exception se nao existir

			if (!password.equals(out.getPassword())) {
				throw new NotFoundException(); 
			}
			
			
			
		} catch (NotFoundException e) {
			throw new NotFoundException("Invalid username or password");
		}

		return out; 

	}
	
	
	public void refreshSessions() {
		for (Session session : this.getSessionSet()) {
			if (session.isExpired()) {
				session.delete();
				//anything else to remove the session completely?
			}
		}
	}
	
	/**
	 * 
	 * @param user
	 */
	public String addSession(User user) {
		if (user.getSession() != null) {
			user.getSession().touch();
			return user.getSession().getToken(); //the user is in session
		}
		
		Session newSession = new Session(user);
		this.addSession(newSession);
		return newSession.getToken();
	}

	public Session getSessionFromToken(String token) {
		Session session = null;

		for (Session s : this.getSessionSet()) {
			if (s.getToken().equals(token)) {
				session = s;
				break;
			}
		}
		

		if (session == null || session.isExpired()) {
			this.refreshSessions();
			throw new UserNotInSessionException();
		}

		return session;
	}

	/**
	 * @param userName
	 * @param name
	 * @param password
	 * @return
	 */
	public User addUser(String username, String name, String password) {
		if (username.equals("") || username == null) {
			throw new InvalidValueException();
		}
		
		//if the user already exists, don't create a new one. 
		try {
			User user = getUser(username);
			throw new RepeatedIdentificationException();
		} catch (NotFoundException e) {} //Hmm, this isn't very natural code. should this return null?
		
		User newuser;
		
		if (username.equals("root"))
			newuser = new SuperUser(username, name, password);
		else
			newuser = new User(username, name, password);
		
		BubbleDocs.getInstance().addUser(newuser);
		
		return newuser;
	}

	/**
	 * @param userName
	 */
	public void deleteUser(String username) {
		User user = this.getUser(username);
		for (CalcSheet sheet : user.getCreatedCalcSheetSet()) {
			sheet.deleteAllCells();
		}
		
		for (CalcSheet writing : user.getWriteableCalcSheetSet()) {
			user.removeWriteableCalcSheet(writing);
			writing.removeWritingUser(user);
		}
		
		for (CalcSheet reading : user.getReadableCalcSheetSet()) {
			user.removeReadableCalcSheet(reading);
			reading.removeReadingUser(user);
		}
		
		Session session = user.getSession();
		if (session !=  null) {
			session.setUser(null);
			session.delete();
		}
		
		user.setBubbleDocs(null);
		
		this.removeUser(user);
		
		user.delete();
	}


	/* The semantics of permissions is the following:
	 *  * read  - the user can read that calcSheet
	 *  * write - the user can write that calcSheet
	 *  A writer MUST be a reader. So write permission MUST include read permission  
	 */


	//author is the user that is doing the action.
	/**
	 * @param author the user adding another user
	 * @param userName the userName of the user being added
	 * @param calcSheet the related CalcSheet
	 */
	public void addReader(User author, String username, CalcSheet calcSheet) {
		//PRECOND: author owns or can write this file
		//PRECOND: username is not already in this association
		checkAuthorsPermission(author, calcSheet);
		User user = author.getUserName().equals(username) ? author : getUser(username);

		if (calcSheet.getReadingUserSet().contains(user)) return; //do nothing if in this list.

		calcSheet.getReadingUserSet().add(user);
		user.getReadableCalcSheetSet().add(calcSheet);
	}



	/**
	 * @param author
	 * @param username
	 * @param calcSheet
	 */
	public void addWriter(User author, String username, CalcSheet calcSheet) {
		//PRECOND: author owns or can write this file
		//PRECOND: username MUST be able to read this file
		checkAuthorsPermission(author, calcSheet);
		User user = author.getUserName().equals(username) ? author : getUser(username);

		if (calcSheet.getWritingUserSet().contains(user)) return; //do nothing if in this list.
		if (!calcSheet.getReadingUserSet().contains(user)) {
			this.addReader(author, username, calcSheet); //to conform with the permission's specification. 
		}

		calcSheet.getWritingUserSet().add(user); 
		user.getWriteableCalcSheetSet().add(calcSheet);
	}

	/**
	 * @param author
	 * @param username
	 * @param calcSheet
	 */
	public void removeReader(User author, String username, CalcSheet calcSheet) {
		//PRECOND: author owns or can write this file
		//PRECOND: username can read this file and CANNOT write this file
		checkAuthorsPermission(author, calcSheet);
		User user = author.getUserName().equals(username) ? author : getUser(username);

		if (!calcSheet.getReadingUserSet().contains(user)) return; //do nothing if it's not a reader
		if (calcSheet.getWritingUserSet().contains(user)) {
			this.removeWriter(author, username, calcSheet); 
		}

		calcSheet.getReadingUserSet().remove(user);
		user.getReadableCalcSheetSet().remove(calcSheet);		
	}

	/**
	 * @param author
	 * @param username
	 * @param calcSheet
	 */
	public void removeWriter(User author, String username, CalcSheet calcSheet) {
		//PRECOND: author owns or can write this file
		//PRECOND: username can write this file.
		checkAuthorsPermission(author, calcSheet);
		User user = author.getUserName().equals(username) ? author : getUser(username);

		if (!calcSheet.getWritingUserSet().contains(user)) return; //do nothing if it's not a writer

		calcSheet.getWritingUserSet().remove(user);
		user.getWriteableCalcSheetSet().remove(calcSheet);	
	}

	//if the current user (author) isn't the file creator, or has write permissions
	//he can't do permission related actions
	private void checkAuthorsPermission(User author, CalcSheet sheet) {
		if (!(author.getCreatedCalcSheetSet().contains(sheet) || author.getWriteableCalcSheetSet().contains(sheet))) {
			throw new PermissionException(author.getUserName() + " doesn't have permission to complete this action.");
		}		
	}

	/**
	 * @param calcSheet
	 */
	public void removeCalcSheet(CalcSheet calcSheet) {
		this.getCalcSheetSet().remove(calcSheet);
		calcSheet.deleteAllCells();

	}

}
