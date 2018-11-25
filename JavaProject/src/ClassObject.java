import java.util.ArrayList;

public class ClassObject {

    String name;
    ArrayList<MethodObject> methodObjects;

    public ClassObject(String className){

        this.name = className;
        this.methodObjects = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public void addMethod(MethodObject newMethod){
        if (!this.methodObjects.contains(newMethod)) {
            this.methodObjects.add(newMethod);
            System.out.println("added "+ newMethod.getName()+ " to "+this.name);
        }
        else{
            //do nothing?
        }
    }

}
