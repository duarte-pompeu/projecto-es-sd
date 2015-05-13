#!/bin/bash

SD_STORE=../pom.xml

# run test servers
mvn -f $SD_STORE test

# run server
mvn -f $SD_STORE exec:java
