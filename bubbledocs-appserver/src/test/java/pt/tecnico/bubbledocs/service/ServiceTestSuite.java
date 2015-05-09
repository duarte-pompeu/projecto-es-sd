package pt.tecnico.bubbledocs.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import pt.tecnico.bubbledocs.integration.*;
import pt.tecnico.bubbledocs.integration.component.CreateUserIntegratorTest;
import pt.tecnico.bubbledocs.integration.component.DeleteUserIntegratorTest;
import pt.tecnico.bubbledocs.integration.component.LoginUserIntegratorTest;
import pt.tecnico.bubbledocs.integration.component.ExportDocumentTest;
import pt.tecnico.bubbledocs.integration.component.RenewPasswordIntegratorTest;

//To test this you can run:
//    mvn test -Dtest=*Suite
//
//To run one individual test you can run
//    mvn test -Dtest=Login*
//or  mvn test -Dtest=*Literal*
//and of course you can use eclipse.
//Add more tests when they are done.

@RunWith(Suite.class)
@SuiteClasses({ 
	AssignLiteralCellTest.class, 
	AssignReferenceCellTest.class,
	AssignBinaryToCell.class,
	CreateCalcSheetTest.class,
	CreateUserIntegratorTest.class,
	DeleteUserIntegratorTest.class,
	ExportDocumentTest.class,
	LoginUserIntegratorTest.class,
	GetUserInfoTest.class,
	GetUsername4TokenTest.class,
	RenewPasswordIntegratorTest.class})
public class ServiceTestSuite {

}
