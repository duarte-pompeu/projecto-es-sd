#!/bin/bash

# install juddi module
mvn compile install -f ../sd-juddi/pom.xml

# compile sd-id
mvn compile -f ./pom.xml		

# compile sd-id-cli			
mvn compile -f ../sd-store-cli/pom.xml

# start tomcat
startup.sh
