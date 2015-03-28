package pt.tecnico.bubbledocs.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//To test this you can run:
//    mvn test -Dtest=*Suite
//
//To run one individual test you can run
//    mvn test -Dtest=Login*
//or  mvn test -Dtest=*Literal*
//and of course you can use eclipse.
//Add more tests when they are done.

@RunWith(Suite.class)
@SuiteClasses({ AssignLiteralCellTest.class, LoginUserTest.class, ExportDocumentTest.class })
public class ServiceTestSuite {

}
