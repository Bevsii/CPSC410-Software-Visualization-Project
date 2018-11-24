import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static String outputFileName = "Analysis.JSON";

    // Arguments: Must be absolute path to the directory location containin python files
    public static void main(String args[]) throws Exception {
        // TODO: FIND THE FILE OR THROW AN ERROR OTHERWISE
        if(args.length != 1){
            //throw new Exception("Error: There should only be one terminual argument");
        }

        //Set up the path for the output file
        String fileSeparator =  System.getProperty("file.separator");

        String tempLocation = "E:"+ fileSeparator + "CPSC410-Software-Visualization-Project"+ fileSeparator+ "410Python" + fileSeparator + "School";

        PythonAnaliser pythonAnaliser = new PythonAnaliser(tempLocation);

        // Create new output file
        File outputFile = new File(outputFileName);

        if (outputFile.createNewFile()){
            System.out.println("Created output file");
        } else {
            System.out.println("Replaced output file");
            outputFile.delete();
            outputFile.createNewFile();
        }

        // TODO: STATIC ANALYSIS
        pythonAnaliser.DynamicAnalysis(outputFile);

        // TODO: DYNAMIC ANALYSIS
        pythonAnaliser.StaticAnalysis(outputFile);
    }
}
