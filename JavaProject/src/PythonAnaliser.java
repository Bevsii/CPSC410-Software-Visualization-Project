import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PythonAnaliser {
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

    public void StaticAnalysis(File outputFile) throws IOException {
        for(File file : pythonFiles) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;

            while (null != (currentLine = reader.readLine())) {
                //System.out.println(currentLine);

                //TODO: Skip comments

                if (currentLine.length() > 5) { // Assert that length is greater than 5 so we don't run into issues with smaller lines
                    if (currentLine.substring(0, 5).equals("class")) {
                        //This is a class isolate the class name and print it
                        String className = currentLine.substring(5, currentLine.length() - 1);
                        printClass(className);
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

                        printMethodName(methodName);
                    }
                }
            }
        }
    }

    private void printClass(String className){
        System.out.println("going to do something here");
        System.out.println("BevLog: Class name: " + className);
    }

    private void printMethodName(String methodName){
        System.out.println("BevLog: Method name: " + methodName);
    }

    public void DynamicAnalysis(File outputFIle) throws IOException {

    }
}