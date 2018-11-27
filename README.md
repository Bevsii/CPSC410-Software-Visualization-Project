# CPSC410-Software-Visualization-project
Software visualization project for UBC CPSC 410 2018 w1

Note: The JSON-simple Java toolkit is required for reading JSON files in this project. You will have to add the jar file to the project dependencies in IntelliJ under project structure>modules>dependencies tab>add new dependency

Get the JSON-simple .jar here:
https://code.google.com/archive/p/json-simple/downloads

Note: A few constraints must be met in the python code for this program to work correctly:
- One class per file
- All python files are in the same directory
- Each class definition, method definition, and method calls must be on their own line

## How to build (Java Project):

- Open intelliJ
- Go to File>Project Structure>Artifacts (Note, if you already see a JAR artifact in the list then skip to the last step of this section
- Press the "+" sign
- Select JAR>From Modules With Dependencies
- Set "Main" as your main class
- Press OK, and then apply your changes
- Once that is done go to Build>Build Artifacts> Select JAR file

## How to Run Analysis:

- Once you finished building your jar file navigate to it's location in your terminal
- run "java -jar 410-proj2.jar arg" where 'arg' is the path to the python code directory
