package Engine;

public class simulationApprovementManager {
    private approvementStatus status;
    private String simulationName;
    private String userName;
    private Integer amountToRun;
    private Integer ticks;
    private Integer sec;
    private Integer id;
    private Integer amountRun = 0;
    private Integer currentRun = 0;
    private Integer done = 0;

    public simulationApprovementManager(String simulationName, String userName, Integer amountToRun, Integer ticks, Integer sec, Integer id){
        this.simulationName = simulationName;
        this.userName = userName;
        this.amountToRun = amountToRun;
        this.ticks = ticks;
        this.sec = sec;
        this.id = id;
        status = approvementStatus.WAITING;
    }

    public void increaseCurrentRun(){
        currentRun++;
    }

    public void decreaseCurrentRun(){
        currentRun--;
    }

    public Integer getCurrentRun(){
        return currentRun;
    }

    public void increaseDone(){
        done++;
    }

    public Integer getDone(){
        return done;
    }

    public Integer getId(){
        return id;
    }

    public Integer getAmountRun(){
        return amountRun;
    }

    public void setStatus(approvementStatus status){
        this.status = status;
    }

    public Integer getTicks(){
        return ticks;
    }

    public Integer getSec(){
        return sec;
    }

    public approvementStatus getStatus(){
        return status;
    }

    public String getSimulationName(){
        return simulationName;
    }

    public String getUserName(){
        return userName;
    }

    public Integer getAmountToRun(){
        return amountToRun;
    }
    public Integer getRemainingRun(){
        return amountToRun - amountRun;
    }

    //public void addToAmountToRun(Integer add){
//        amountToRun += add;
//    }

    public void increaseAmountRun(){
        amountRun += 1;
    }

}
