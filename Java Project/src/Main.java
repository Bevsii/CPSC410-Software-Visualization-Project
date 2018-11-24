import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static String outputFileName = "Analysis.JSON";

    public static void main(String args[]) throws Exception {
        // TODO: FIND THE FILE OR THROW AN ERROR OTHERWISE
        if(args.length != 1){
            //throw new Exception("Error: There should only be one terminual argument");
        }
        String tempLocation = "410Python/School";

        PythonAnaliser pythonAnaliser = new PythonAnaliser(tempLocation);

        //Set up the path for the output file
        String fileSeparator =  System.getProperty("file.separator");
        String relativePath = "tmp" + fileSeparator + ".." + fileSeparator + outputFileName;

        // Create new output file with the given path
        // TODO: Figure out an appropriate place to save the output file that works on both windows and mac
        // File outputFile = new File(relativePath);
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
