#!/bin/bash

# install juddi module
mvn compile install -f ../sd-juddi/pom.xml

# compile sd-store
mvn compile -f ./pom.xml		

# compile sd-store-cli			
mvn compile -f ../sd-store-cli/pom.xml

# start tomcat
startup.sh

