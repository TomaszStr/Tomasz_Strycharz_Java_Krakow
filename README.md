# BasketSplitter

This is a Java application for splitting a list of items into delivery methods based on a provided configuration. Built in IntelliJ.

## Description

The `split` method in the `BasketSplitter` class takes a list of items and assigns them to delivery methods according to a predefined configuration. It aims to minimize the number of deliveries while maximizing the size of the largest group within those deliveries.

# Installing a Library using Maven

This README file provides instructions for installing a library in a project using Maven.

## Installation Process

1. **Locate the Library**: Find the library that you want to install in your project. This can be a library that you have developed yourself or one that is publicly available in a Maven repository.

2. **Add JAR to maven repository** mvn install:install-file -Dfile=pathToJarFile -DgroupId=group_name -DartifactId=artifactId -Dversion=version -Dpackaging=jar

3. **Add Dependency in pom.xml**: Open the `pom.xml` file of your Maven project. Inside the `<dependencies>` section, add the following XML snippet:

   ```xml
   <dependency>
       <groupId>library-group-id</groupId>
       <artifactId>library-artifact-id</artifactId>
       <version>library-version</version>
   </dependency>
4. Now you should be able to use BasketSplitter class.