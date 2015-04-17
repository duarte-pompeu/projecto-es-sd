#!/bin/bash

#install bubbledocs
mvn -N install -f ../pom.xml

#install sd
mvn -N install -f ../sd/pom.xml

# compile sd-id
mvn package -f ./pom.xml

# compile sd-id-cli			
mvn package -DskipTests -f ../sd-id-cli/pom.xml

