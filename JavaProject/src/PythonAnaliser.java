import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PythonAnaliser {
    public static final String INDENT = "  ";

    private String fileLocation;
    private File directory;
    private List<File>  pythonFiles = new ArrayList<File>();

    public PythonAnaliser(String fileLocation) throws Exception {
        this.fileLocation = fileLocation;
        File pythonFolder = new File(fileLocation);

        //Check that folder actually exists, otherwise throw an error
        if(!pythonFolder.exists()){
            throw new Exception("File location not found!");
        }

        for(File pythonFile : pythonFolder.listFiles()){
            // If it is a python file
            if (pythonFile.getName().contains(".py")){
                pythonFiles.add(pythonFile);
            }
        }
    }

    public void StaticAnalysis(PrintWriter writer) throws IOException {

        writer.println(INDENT + "\"static\" : [");

        for(File file : pythonFiles) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;

            while (null != (currentLine = reader.readLine())) {
                //System.out.println(currentLine);

                if (currentLine.length() > 5) { // Assert that length is greater than 5 so we don't run into issues with smaller lines
                    if (currentLine.substring(0, 5).equals("class")) {
                        //This is a class isolate the class name and print it
                        String className = currentLine.substring(5, currentLine.length() - 1);
                        printClass(className, writer);
                    }
                }
                if (currentLine.length() > 3){
                    currentLine = currentLine.trim();
                    if(currentLine.substring(0,3).equals("def")){

                        currentLine = currentLine.substring(4); // Removing "def " from the current line

                        String methodName = "";
                        boolean hasReachedParameters = false;
                        for (int i = 0; i < currentLine.length(); i++){
                            char c = currentLine.charAt(i);

                            if (!hasReachedParameters){
                                if (c != '('){
                                    methodName = methodName + c;
                                } else{
                                    hasReachedParameters = true;
                                }
                            }
                        }

                        printMethodName(methodName, writer);
                    }
                }
            }
            writer.println(INDENT + INDENT + INDENT + "]");
            writer.println(INDENT + INDENT + "}");
        }
    }

    private void printClass(String className, PrintWriter writer){
        writer.println(INDENT + INDENT + "{\"" + className + "\" : [");
        System.out.println("BevLog: Class name: " + className);
    }

    private void printMethodName(String methodName, PrintWriter writer){
        writer.println(INDENT + INDENT + INDENT + "\"" + methodName + "\"");
        System.out.println("BevLog: Method name: " + methodName);
    }

    public void DynamicAnalysis(PrintWriter writer) throws IOException {

    }
}