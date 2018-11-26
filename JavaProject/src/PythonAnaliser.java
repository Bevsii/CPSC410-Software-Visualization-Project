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
            boolean fileHasClass = false;
            boolean hasFoundFirstMethod = false;
            String currentLine;

            while (null != (currentLine = reader.readLine())) {

                if (currentLine.length() > 5) { // Assert that length is greater than 5 so we don't run into issues with smaller lines
                    if (currentLine.substring(0, 5).equals("class")) {
                        fileHasClass = true;
                        //This is a class isolate the class name and print it
                        String className = currentLine.substring(5, currentLine.length() - 1);
                        printClass(className, writer);
                    }
                }
                if (currentLine.length() > 3){
                    currentLine = currentLine.trim();
                    if(currentLine.substring(0,3).equals("def")){

                        currentLine = currentLine.substring(4); // Removing "def " from the current line

                        String methodName = getMethodName(currentLine);

                        if (hasFoundFirstMethod) {
                            writer.print(",\n");
                        }
                        else {
                            hasFoundFirstMethod = true;
                        }
                        printMethodName(methodName, writer);
                    }
                }
            }
            if(fileHasClass) {
                writer.print("\n");
                writer.println(INDENT + INDENT + INDENT + "]");
                writer.println(INDENT + INDENT + "}");
            }
        }

        writer.println(INDENT + "],");
    }

    private void printClass(String className, PrintWriter writer){
        writer.println(INDENT + INDENT + "{\"" + className + "\" : [");
        System.out.println("BevLog: Class name: " + className);
    }

    private void printMethodName(String methodName, PrintWriter writer){
        writer.print(INDENT + INDENT + INDENT + "\"" + methodName + "\"");
        System.out.println("BevLog: Method name: " + methodName);
    }

    private String getMethodName(String currentLine){
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

        return methodName;
    }

    public void DynamicAnalysis(PrintWriter writer) throws IOException {
        for (File file : pythonFiles){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;

            // Copy over python file
            String pythonFileName = file.getName();
            String logPythonFileName = "LOG_" + pythonFileName;
            File outputPython = new File (logPythonFileName);
            outputPython.createNewFile();

            PrintWriter pythonWriter = new PrintWriter(logPythonFileName);

            pythonWriter.println("from dynamic import log, startlog, endlog\n" +
                    "import dynamic\n" +
                    "import inspect\n" +
                    "startlog()\n");

            // Loop over all lines in file
            while (null != (currentLine = reader.readLine())) {
                // Skip comments
                if (currentLine.length() > 0 && currentLine.substring(0,1).equals("#")){
                    // skip to next line
                    currentLine = reader.readLine();
                }

                pythonWriter.println(currentLine + "\n");
                // if line begins with def
                if (currentLine.length() > 3){
                    if(currentLine.trim().substring(0,3).equals("def")){
                        currentLine = currentLine.substring(4); // Removing "def " from the current line

                        String methodName = getMethodName(currentLine);

                        // Get argument values and print log().
                        pythonWriter.println("paramsDict = [locals()[arg] for arg in inspect.getargspec(" + methodName + ").args]\n" +
                                            "log(paramsDict)\n");
                    }
                }
            }
            // Print endlog()
            pythonWriter.println("endlog()");

            //TODO: Get path to Python File for logging (assuming we ALWAYS start with Main.py)
            String path = "";
            String pathToPythonFile = path + "LOG_Main.py";

            // Execute python code:
            try {
                String command = "python /c start /wait python " + pathToPythonFile;
                // TODO: Add parameters accordingly; Delete if unnecessary
                String params = "";
                Process p = Runtime.getRuntime().exec(command + params);
                // TODO: Join Python output JSON with our Static JSON
            } catch (Exception e) {
                System.out.println("Cannot begin logging. Check the Python logging path.");
            }
        }
    }
}