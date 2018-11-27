import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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
            while ((currentLine = reader.readLine())!=null) {

                System.out.println("RorLog for Static: checking " + currentLine.toString() + " in " + file.getName()+"\n"+"------");
                if (currentLine.length() > 5) { // Assert that length is greater than 5 so we don't run into issues with smaller lines
                    if (currentLine.substring(0, 5).equals("class")) {
                        fileHasClass = true;
                        //This is a class isolate the class name and print it
                        String className = currentLine.substring(5, currentLine.length() - 1);
                        printClass(className, writer);
                    }
                }
                if (currentLine.trim().length() > 3) {
                    currentLine = currentLine.trim();
                    if (currentLine.startsWith("def")) {

                        currentLine = currentLine.substring(4); // Removing "def " from the current line

                        String methodName = getMethodName(currentLine);

                        if (hasFoundFirstMethod) {
                            writer.print(",\n");
                        } else {
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
            currentLine = reader.readLine();
            while (currentLine != null) {

                System.out.println("RorLog: Dynamic currentLine is: "+currentLine.toString()+" from file: "+file.getName());
                // Skip comments
                if (currentLine.length() > 0 && currentLine.substring(0,1).equals("#")){
                    // skip to next line
                    currentLine = reader.readLine();
                    if(currentLine == null){
                        break;
                    }
                }

                pythonWriter.println(currentLine + "\n");
                // if line begins with def
                System.out.println("RorLog: now currentLine is:"+currentLine);
                if (currentLine.length() > 3){
                    if(currentLine.trim().startsWith("def")){
                        currentLine = currentLine.substring(4); // Removing "def " from the current line

                        String methodName = getMethodName(currentLine);

                        // Get argument values and print log().
                        pythonWriter.println("paramsDict = [locals()[arg] for arg in inspect.getargspec(" + methodName + ").args]\n" +
                                            "log(paramsDict)\n");
                    }
                }
                currentLine = reader.readLine();
            }
            // Print endlog()
            pythonWriter.println("endlog()");

            // Get path to Python File for logging (assuming we ALWAYS start with Main.py)
            //      - Current location is CPSC410-Softwa...\JavaProject\src
            //      - Want to move outside of src and JavaProject into
            //      - Once in CPSC410-Softwa..., move into Python410
            String path = "..\\..\\Python410";
            String pathToPythonFile = path + "\\LOG_Main.py";

            // Execute python code:
            try {
                // /wait to wait for command to finish executing (as in, wait until we have all the files done.
                String command = "python /c start /wait python " + pathToPythonFile;
                // Add parameters accordingly
                String params = "";
                Process p = Runtime.getRuntime().exec(command + params);

                JSONParser parser = new JSONParser();
                try {
                    Object obj = parser.parse(new FileReader("..\\..\\Python410\\dynamic.json"));
                    JSONObject jsonObject =  (JSONObject) obj;
                    // TODO: Write dynamic.json into writer

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                System.out.println("Cannot begin logging. Check the Python logging path.");
            }
        }
    }
}