import java.util.ArrayList;

public class MethodObject {
    String name;
    ArrayList<MethodCount> calledBy;
    ArrayList<MethodObject> methodsCalled; //not sure if we need this or if we just need to inform other methods

    public MethodObject(String nm){
        this.name = nm;
        this.calledBy = new ArrayList<>();
    }

    public void updateName(String newName) {
        this.name = newName;
    }

    public String getName(){
        return this.name;
    }

    public void callMethod(MethodObject methodToCall){
        //TODO: let the method we are calling know that it is being called (use methodToCall.calledBy(this)?
    }

    public void calledBy(MethodObject methodCalling){
        //TODO: method calling this should be added to calledBy array, but we also want to store the number of times
        //TODO: that the method is called by methodCalling
        boolean alreadyCalled = false;
        for (int i = 0; i<calledBy.size()){
            if (this.calledBy.get(i).getMethod().equals(methodCalling)){
                alreadyCalled = true;
            }
        }
        if (!alreadyCalled){
            MethodCount newMethod = new MethodCount(methodCalling);
            newMethod.addCount();
            this.calledBy.add(newMethod);
        }
    }

}
