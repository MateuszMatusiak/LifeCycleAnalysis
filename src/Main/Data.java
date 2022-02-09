package Main;

public class Data implements Comparable{
    String id;
    String action;
    Integer timeToFailure;

    public Data(String id, String action, Integer timeToFailure) {
        this.id = id;
        this.action = action;
        this.timeToFailure = timeToFailure;
    }

    public Data() {
    }

    public Data(Data d){
        this.id=d.id;
        this.action=d.action;
        this.timeToFailure=d.timeToFailure;
    }

    @Override
    public int compareTo(Object o) {
        Data t = (Data) o;
        if (this.timeToFailure > t.timeToFailure) {
            return 1;
        } else if (this.timeToFailure < t.timeToFailure)
            return -1;
        return 0;
    }
}
