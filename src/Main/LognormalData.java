package Main;

public class LognormalData extends Data {
    Integer rank;
    Double estimateOfSurvival;
    Double plottingPositions;
    Double logTime;


    public LognormalData(String id, String action, Integer timeToFailure) {
        this.id = id;
        this.action = action;
        this.timeToFailure = timeToFailure;
    }
    public LognormalData(Data data){
        this.id = data.id;
        this.action = data.action;
        this.timeToFailure = data.timeToFailure;
    }
}
