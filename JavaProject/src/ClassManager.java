import java.util.ArrayList;

public class ClassManager {

    ArrayList<ClassObject> classObjects;

    private static ClassManager instance = null;

    private ClassManager() throws Exception{
        if (instance != null){
            throw new Exception("ClassManager instance already exists");
        }
        classObjects = new ArrayList<>();
    }


    public static ClassManager getInstance() throws Exception{
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

    public void addClass(ClassObject classObject){
        this.classObjects.add(classObject);
        System.out.println("just added class: "+classObject.getName()+" to "+this);
    }


}
