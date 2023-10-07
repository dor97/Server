package Engine;

import Engine.world.World;

public class simulationsStatus {
    private Integer m_simulationId;
    private Thread m_runningThread = null;
    //private myTask m_task = null;

    private Status m_status = Status.WAITINGTORUN;

    private isPause isPause = new isPause();

    private World world;
    private Thread taskThread = null;

    public void setTaskThread(Thread thread){
        taskThread = thread;
    }

    public Thread getTaskThread(){
        return taskThread;
    }
    public void setWorld(World world){
        this.world = world;
    }

    public World getWorld(){
        return world;
    }

    public isPause getIsPause(){
        return isPause;
    }

    public void setIsPause(boolean pause){
        isPause.setPause(pause);
    }

    public void setSimulationId(Integer simulationId){
        m_simulationId = simulationId;
    }

    public void setStatus(Status status){
        m_status = status;
    }

    public void setRunningThread(Thread runningThread){
        m_runningThread = runningThread;
    }

//    public void setTask(myTask task){
//        m_task = task;
//    }

    public Thread getRunningThread(){
        return m_runningThread;
    }

//    public myTask getTask(){
//        return m_task;
//    }

    public Status getStatus(){
        return m_status;
    }

    public Integer getSimulationId(){
        return m_simulationId;
    }


}
