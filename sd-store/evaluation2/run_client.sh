#!/bin/bash

SD_STORE_CLI=../../sd-store-cli/pom.xml

if [ $1 == local_test ]
then
	mvn -f $SD_STORE_CLI compile test

elif [ $1 == ws_test ]
then
	mvn -f $SD_STORE_CLI -Dtest=WebServiceRemoteTestIT compile  test

elif [ $1 == fe_test ]
then
	mvn -f $SD_STORE_CLI -Dtest=FrontEndRemoteTestIT compile  test
else
	echo $1 is not a valid command
	echo valid commands:
	echo "   * local_test"
	echo "   * ws_test"
	echo "   * fe_test"
fi