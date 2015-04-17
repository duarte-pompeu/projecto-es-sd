#!/bin/bash

SD_ID=../pom.xml

# run test servers
mvn -f $SD_ID test

# run server
mvn -f $SD_ID exec:java
