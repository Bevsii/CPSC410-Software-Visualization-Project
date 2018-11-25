import java.util.ArrayList;

public class ClassManager {

    ArrayList<ClassObject> classObjects;

    private static ClassManager instance = null;

    public ClassManager() throws Exception{
        if (instance != null){
            throw new Exception("ClassManager instance already exists");
        }
        classObjects = new ArrayList<>();
    }


    public ClassManager getInstance() throws Exception{
        if (instance != null){
            return instance;
        }
        else{
            try{
                instance = new ClassManager();
            }catch(Exception error) {
                throw error;
            }
        }
        return instance;
    }


}
