public class MethodCount {
    MethodObject methodObject;
    int calledCount;

    public MethodCount(MethodObject method){
        this.methodObject = method;
        this.calledCount = 0;
    }

    public void addCount(){
        this.calledCount++;
    }

    public MethodObject getMethod(){
        return this.methodObject;
    }
}
