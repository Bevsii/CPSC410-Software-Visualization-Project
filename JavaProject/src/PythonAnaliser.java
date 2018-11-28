import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
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
        Iterator<File> files = pythonFiles.iterator();
        while(files.hasNext()) {
            File file = files.next();
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

                if(files.hasNext()) {
                    writer.println(INDENT + INDENT + "},");
                }
                else {
                    writer.println(INDENT + INDENT + "}");
                }
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


            // Copy over python file
            String pythonFileName = file.getName();
            String logPythonFileName = pythonFileName;
            File outputPython = new File (logPythonFileName);
            boolean dynamicHasClass = false;
            String className = "";
            String methodName = "";
            String varINDENT = "    ";
            outputPython.createNewFile();

            PrintWriter pythonWriter = new PrintWriter(new FileOutputStream(outputPython), true);

            pythonWriter.print("from _LOGGER_ import startlog, log, endlog\n" +
                    "import dynamic\n" +
                    "import inspect\n" +
                    "startlog()\n");

            // Loop over all lines in file
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {

                // if from ... import ... statement,
                //      i.e. (from folder.class import ...)
                if(currentLine.trim().startsWith("from")){
                    // Remove "from " from currentLine (folder.class import ...)
                    String tempLine = currentLine;
                    currentLine = currentLine.substring(5);
                    boolean hasReachedClassName = false;
                    // While currentLine hasn't been completely consumed, and we haven't reached the "." in the "folder.class" statement,
                    while(currentLine != null && !hasReachedClassName) {
                        String c = currentLine.substring(0,1);
                        currentLine = currentLine.substring(1);
                        if(c.equals(".")){
                            hasReachedClassName = true;
                        }
                    }
                    // If we have truncated the from folder.class import ... statement to:
                    //      - .class import ...,
                    // we can now write as: "from class import ..."
                    if(hasReachedClassName){
                        pythonWriter.write("from " + currentLine + "\n");
                    }
                    else {
                        pythonWriter.write(tempLine);
                        System.out.println("JLog: Possible import mismatch. Check DynamicAnalysis FROM case.");
                    }
                }
                // If the file has a class, defs have two indents, and there is a class name.
                else if (currentLine.length() > 5 && currentLine.substring(0, 5).equals("class")) {
                    varINDENT = "        ";
                    className = currentLine.substring(6, currentLine.length() - 1);
                    dynamicHasClass = true;
                    pythonWriter.write(currentLine + "\n");
                }
                // if method def,
                else if(currentLine.trim().startsWith("def")){
                    String tempLine = currentLine;
                    // Removing "def " from the current line
                    currentLine = currentLine.substring((4 + varINDENT.length() - 4));

                    if(dynamicHasClass) {
                        methodName = className + "." + getMethodName(currentLine);
                    }
                    else {
                        methodName = getMethodName(currentLine);
                    }

                    // Get argument values and print log().
                    /*
                    pythonWriter.write(tempLine + "\n" +
                            varINDENT + "paramsDict = [locals()[arg] for arg in inspect.getargspec(" + methodName + ").args]\n" +
                            varINDENT + "log(paramsDict)\n");
                    */
                    pythonWriter.write(tempLine + "\n" +
                            varINDENT + "paramsDict = []\n" +
                            varINDENT + "for arg in inspect.getfullargspec(" + methodName + ").args:\n" +
                            varINDENT + "    " + "if not isinstance(type(arg), type):\n" +
                            varINDENT + "        " + "paramsDict.append(locals()[arg])\n" +
                            varINDENT + "    " + "else:\n"+
                            varINDENT + "        " + "paramsDict.append(arg)\n" +
                            varINDENT + "log(paramsDict)\n");

                }
                else{
                    pythonWriter.write(currentLine + "\n");
                }
            }
            // Print endlog()
            pythonWriter.write("\n\nendlog()\n");
            // Close the writer.
            pythonWriter.close();
        }

        // Execute python code:
        try {
            // /wait to wait for command to finish executing (as in, wait until we have all the files done.
            String command = "python /wait Main.py";
            // Process p = Runtime.getRuntime().exec(command);
            ProcessBuilder pb = new ProcessBuilder("C:\\Users\\konpeitoBOX\\AppData\\Local\\Programs\\Python\\Python37-32\\python.exe", "/wait","Main.py");
            Process p = pb.start();

            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader("dynamic.json"));
                JSONObject jsonObject =  (JSONObject) obj;
                // TODO: Write dynamic.json into writer
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                ObjectWriter prettyWriter = mapper.writerWithDefaultPrettyPrinter();
                prettyWriter.writeValue(new File("Analysis.JSON"), jsonObject);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("JLog: Cannot begin logging. " + e);
        }
    }

    // Merges the second file on to the first file
    // NOTE: Call this after you call writer.close on any of the files that are passed as arguments
    public void MergeOutputFiles(File firstFile, File secondFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(secondFile));
        String currentLine;
        BufferedWriter writer = new BufferedWriter(new FileWriter(firstFile, true));

        while ((currentLine = reader.readLine())!=null) {
            writer.append(currentLine + "\n");
        }

        writer.close();
    }
}