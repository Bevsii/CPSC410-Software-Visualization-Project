# CPSC410-Software-Visualization-project
Software visualization project for UBC CPSC 410 2018 w1

## Project Setup

Note: The JSON-simple Java toolkit is required for reading JSON files in this project. You will have to add the jar file to the project dependencies in IntelliJ under project structure>modules>dependencies tab>add new dependency

Get the JSON-simple .jar here:
https://code.google.com/archive/p/json-simple/downloads
Get the Jackson JSON Processor .jars here:
https://search.maven.org/search?q=g:com.fasterxml.jackson.core

Note: A few constraints must be met in the python code for this program to work correctly:
- One class per file
- All python files are in the same directory
- Each class definition, method definition, and method calls must be on their own line
- Spaces for indentation (otherwise the Java-to-Python code injection will have inconsistent indentation!)

In order to run all the programs you must:
- Download and install java and add it to your path/environment variables. The JDK must be over version 10.
- Download and install Python 3, and add it to your path/environment variables.
- Download and install NPM

## How to build (Java Project):

- Open intelliJ and select the JavaProject directory to open your project
- Go to File>Project Structure>Artifacts (Note, if you already see a JAR artifact in the list then skip to the last step of this section
- Press the "+" sign
- Select JAR>From Modules With Dependencies
- Set "Main" as your main class
- Press OK, and then apply your changes
- Once that is done go to Build>Build Artifacts> Select JAR file

## How to Run Analysis:

- Once you finished building your jar file navigate to it's location in your terminal
- run "java -jar 410-proj2.jar arg" where 'arg' is the path to the python code directory

## How to Run the Visualization:
- navigate to the UI app root folder and type in "npm start"
- Runs the app in the development mode.<br>
- Open [http://localhost:3000](http://localhost:3000) to view it in the browser.
- More detailed explanation and options are in the README file in the app folder
