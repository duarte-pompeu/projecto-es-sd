# Bubble Docs Application Server

Folder structure (as of 21-04).

	.
	├── info
	│   └── retrospectiva-0.md
	├── pom.xml
	├── README.md
	└── src
	    ├── main
	    │   ├── dml
	    │   │   └── bubbledocs.dml
	    │   ├── java
	    │   │   └── pt
	    │   │       └── tecnico
	    │   │           └── bubbledocs
	    │   │               ├── BubbleApplication.java
	    │   │               ├── domain
	    │   │               │   ├── Add.java
	    │   │               │   ├── Avg.java
	    │   │               │   ├── BinaryFunction.java
	    │   │               │   ├── BubbleDocs.java
	    │   │               │   ├── CalcSheet.java
	    │   │               │   ├── Cell.java
	    │   │               │   ├── Content.java
	    │   │               │   ├── Div.java
	    │   │               │   ├── FunctionArgument.java
	    │   │               │   ├── Function.java
	    │   │               │   ├── LiteralArgument.java
	    │   │               │   ├── Literal.java
	    │   │               │   ├── Mul.java
	    │   │               │   ├── Prd.java
	    │   │               │   ├── RangeFunction.java
	    │   │               │   ├── Range.java
	    │   │               │   ├── ReferenceArgument.java
	    │   │               │   ├── Reference.java
	    │   │               │   ├── Session.java
	    │   │               │   ├── Sub.java
	    │   │               │   ├── SuperUser.java
	    │   │               │   └── User.java
	    │   │               ├── exceptions
	    │   │               │   ├── BubbleDocsException.java
	    │   │               │   ├── CannotLoadDocumentException.java
	    │   │               │   ├── CannotStoreDocumentException.java
	    │   │               │   ├── DuplicateEmailException.java
	    │   │               │   ├── DuplicateUsernameException.java
	    │   │               │   ├── InvalidEmailException.java
	    │   │               │   ├── InvalidFormatException.java
	    │   │               │   ├── InvalidUsernameException.java
	    │   │               │   ├── InvalidValueException.java
	    │   │               │   ├── LoginException.java
	    │   │               │   ├── NotFoundException.java
	    │   │               │   ├── NullContentException.java
	    │   │               │   ├── PermissionException.java
	    │   │               │   ├── RemoteInvocationException.java
	    │   │               │   ├── RepeatedIdentificationException.java
	    │   │               │   ├── UnavailableServiceException.java
	    │   │               │   └── UserNotInSessionException.java
	    │   │               └── service
	    │   │                   ├── AssignLiteralCell.java
	    │   │                   ├── AssignReferenceCell.java
	    │   │                   ├── BubbleDocsService.java
	    │   │                   ├── CreateSpreadSheet.java
	    │   │                   ├── CreateUser.java
	    │   │                   ├── DeleteUser.java
	    │   │                   ├── ExportDocument.java
	    │   │                   ├── LoginUser.java
	    │   │                   ├── remote
	    │   │                   │   ├── IDRemoteServices.java
	    │   │                   │   └── StoreRemoteServices.java
	    │   │                   └── RenewPassword.java
	    │   └── resources
	    │       ├── fenix-framework-jvstm-ojb.properties
	    │       └── log4j.properties
	    └── test
	        └── java
	            └── pt
	                └── tecnico
	                    └── bubbledocs
	                        └── service
	                            ├── AssignLiteralCellTest.java
	                            ├── AssignReferenceCellTest.java
	                            ├── BubbleDocsServiceTest.java
	                            ├── CreateCalcSheetTest.java
	                            ├── CreateUserTest.java
	                            ├── DeleteUserTest.java
	                            ├── ExportDocumentTest.java
	                            ├── LoginUserTest.java
	                            ├── RenewPasswordServiceTest.java
	                            └── ServiceTestSuite.java

19 directories, 67 files
