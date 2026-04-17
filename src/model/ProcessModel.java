package model;

public class ProcessModel {
    private String name;
    private int totalTime;
    private StateProcess state;
    private int executedTime;

    public ProcessModel(String name, int realTime){
        this.name = name;
        this.totalTime = realTime;
        this.executedTime = 0;
        this.state = StateProcess.NEW;
    }

    public int getExecutedTime() {
        return executedTime;
    }

    public void setExecutedTime(int executedTime) {
        this.executedTime = executedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRealTime() {
        return totalTime;
    }

    public void setRealTime(int realTime) {
        this.totalTime = realTime;
    }

    public StateProcess getState() {
        return state;
    }

    public void setState(StateProcess state) {
        this.state = state;
    }

    public void executorTick(){

        if(state == StateProcess.FINISHED){
            return;
        }

        executedTime++;

        if(executedTime >= totalTime){
            state = StateProcess.FINISHED;
        }
    }

    public double getProgress(){
        if(totalTime == 0){
            return 0;
        }

        double progress = (double) executedTime / totalTime;
        return Math.min(progress, 1.0);
    }

    public void reset(){
        this.state = StateProcess.READY;
        this.executedTime = 0;
    }
    
}
