//package pt.tecnico.bubbledocs.service;
//
//import org.junit.Test;
//
//public class AssignLiteralCellTest extends BubbleDocsServiceTest {
//	/* FIXME: o Eclipse não está a reconhecer bem as classes no meu PC
//	 * vou submeter coisas comentadas para tentar adiantar trabalho
//	 * enquanto não resolvo o problema
//	 * 
//	 * Para fazer toggle comentários rapidamente: Ctrl+A -> Ctrl+Shit+C
//	 * -Pompeu
//	 */
//    
//	String userName;
//	int docId;
//	
//	@Override
//	// TODO: login user before inserting
//	public void populate4Test(){
//		// TODO:
//		// populate userName
//		// populate docId
//		
//	}
//	
//	@Test(expected = BubbleDocsException.class)
//	public void cellDoesntExist(){
//		Integer my_int = 0;
//		
//		// TODO:
//		//make sure this is an impossible cellID
//		int bad_cell_id = -123;
//		AssignLiteralCell service = new AssignLiteralCell(userName, docId, bad_cell_id, my_int);
//		service.dispatch();
//	}
//	
//	//TODO: ALCTest complete stub method
//	@Test
//	public void docDoesntExist(){
//		
//	}
//	
//	@Test
//	//TODO: ALCTest complete stub method
//	public void cellIsProtected(){
//		
//	}
//	
//	@Test
//	//TODO: ALCTest complete stub method
//	public void noWriteAccess(){
//		
//	}
//}
