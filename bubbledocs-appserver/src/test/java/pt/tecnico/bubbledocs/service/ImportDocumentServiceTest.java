package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import pt.tecnico.bubbledocs.service.remote.*;
import mockit.Mock;
import mockit.MockUp;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Add;
import pt.tecnico.bubbledocs.domain.CalcSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.LiteralArgument;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.ReferenceArgument;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exceptions.NotFoundException;
import pt.tecnico.bubbledocs.exceptions.PermissionException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class ExportDocumentTest extends BubbleDocsServiceTest {
	
	private final String U_USERNAME = "jubileu";
	private final String U_USERNAME2 = "olivar";
	private final String U_PASS = "password";
	private final String U_NAME = "Jubileu Mandafacas";
	private String U_TOKEN;
	private String no_permission_token;
	private User USER;
	private CalcSheet CS_EMPTY;
	private int CS_ID;
	private final String CS_NAME = "cs";
	private final int CS_ROWS = 3;
	private final int CS_LINES = 3;
	
	private String CELL_ID0;
	private String CELL_ID1;
	private String CELL_ID2;
	private String U_EMAIL="email@email.com";
	private String U_EMAIL2="email2@email.com";
	
	
	
	@Override
	public void populate4Test(){
		USER = createUser(U_USERNAME, U_PASS, U_NAME, U_EMAIL);
		U_TOKEN = addUserToSession(U_USERNAME);
	
		createUser(U_USERNAME2, U_PASS, U_NAME, U_EMAIL2);
		no_permission_token = addUserToSession(U_USERNAME2);
		
		CS_EMPTY = createSpreadSheet(USER, CS_NAME, CS_ROWS, CS_LINES);
		CS_ID = CS_EMPTY.getId();
		CELL_ID0 = this.getSpreadSheet(CS_NAME).getCell(1, 1).getId();
		CELL_ID1 = this.getSpreadSheet(CS_NAME).getCell(1, 2).getId();
		CELL_ID2 = this.getSpreadSheet(CS_NAME).getCell(2, 1).getId();
	}
	
	
	
	//Mock class simulating an unavailable SD-STORE service
	public static class MockSDStoreUnavailableContext extends MockUp<StoreRemoteServices>
	{
	   @Mock
	   public void $init() {}

	   @Mock
	   public byte[] loadDocument(String username, String docName) throws RemoteInvocationException
	   {
	      throw new RemoteInvocationException();
	   }
	}
	

	//Mock class simulating a context where SD-STORE cannot store a specific document
	public static class MockSDStoreCannotImportDocumentContext extends MockUp<StoreRemoteServices>
	{
	   @Mock
	   public void $init() {}

	   @Mock
	   public byte[] loadDocument(String username, String docName) throws RemoteInvocationException
	   {
	      throw new RemoteInvocationException();
	   }
	}

	
	}



