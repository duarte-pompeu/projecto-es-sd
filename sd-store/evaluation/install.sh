#!/bin/bash

SD_STORE=../pom.xml
SD_STORE_CLI=../../sd-store-cli/pom.xml
SD_JUDDI=../../sd-juddi/pom.xml

# install juddi module
mvn -f $SD_JUDDI compile install

# compile sd-store
mvn -f $SD_STORE compile

# compile sd-store-cli			
mvn -f $SD_STORE_CLI compile

# start tomcat
startup.sh

