package Engine;

import java.util.Objects;

public class applicationDetails {
    private approvementStatus status;
    private String simulationName;
    private Integer ticks;
    private Integer sec;

    public applicationDetails(String simulationName, approvementStatus status, Integer ticks, Integer sec){
        this.simulationName = simulationName;
        this.ticks = ticks;
        this.sec = sec;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        applicationDetails that = (applicationDetails) o;
        return status == that.status && Objects.equals(simulationName, that.simulationName) && Objects.equals(ticks, that.ticks) && Objects.equals(sec, that.sec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, simulationName, ticks, sec);
    }
}
