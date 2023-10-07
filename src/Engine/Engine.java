package Engine;

//import App.ExecutionListItem;
//import App.QueueManagement;
import DTO.*;
import Engine.world.World;
import Engine.world.worldDifenichan;
import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.REBIND;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class Engine {
    private Map<Integer, World> worldsList = new HashMap<>();
    private World cuurentSimuletion;
    private Integer simulationNum = 0;
    private int numOfThreads = 1;
    private ExecutorService threadPool = Executors.newFixedThreadPool(3);
    private Integer poolSize = 0;
    private String m_fileName = null;
    private Boolean isFileLoadedInSimulation = false;
    private Map<Integer, simulationsStatus> simStatus = new HashMap<>();
    private worldDifenichan worldDif = null;
    //private Map<Integer, String> simulationsExceptions;
    private Map<String,List<Integer>> newlyFinishedSimulationIds = new HashMap<>();
    private List<DTOSimulationIdAndUserName> finishedSimulationForAdmins = new ArrayList<>();
    private Thread taskThread = null;
    private Boolean isTreadPoolShoutDown = false;
    private Map<String, worldDifenichan> worldDifenichanCollecen = new HashMap<>();
    private List<String> worldDifenichanCollecenNames = new ArrayList<>();
    //private Map<String, Map<applicationDetails, simulationApprovementManager>> approvementManager = new HashMap<>();
    private Map<Integer, simulationApprovementManager> approvManager = new HashMap<>();
    private Map<String, World> userCurrentSimulation= new HashMap<>();
    private Integer IDToRunSimulation = 0;

    public final List<String> _getWorldDifenichanCollecen(Integer index){
        synchronized (worldDifenichanCollecenNames) {
            return worldDifenichanCollecenNames.stream().skip(index).collect(Collectors.toList());
        }
    }
    public Integer _getNumOfCounterRunningSimulation(String name){
        synchronized (worldDifenichanCollecen){
            if (!worldDifenichanCollecen.containsKey(name)) {
                throw new DTO.InvalidValue("Simulation name does not exist");
                //return null;
            }
            return worldDifenichanCollecen.get(name).getRunningCounter();
        }
    }

    public List<DTORequestData> _getSystemData(){
        List<DTORequestData> systemData = new ArrayList<>();

        simStatus.entrySet().stream().forEach(entry -> {
            DTORequestData data = new DTORequestData();
            data.setSimulationId(entry.getKey());
            data.setUserName(entry.getValue().getWorld().getUserName());
            data.setSimulationName(entry.getValue().getWorld().getSimulationName());
            data.setRequestId(entry.getValue().getWorld().getRequestId());
            systemData.add(data);
            });

        return systemData;
    }

    public Integer _askToRunASimulation(String simulationName, String userName, Integer amountToRun, Integer ticks, Integer sec){
        if(!worldDifenichanCollecen.containsKey(simulationName)){
            throw new DTO.InvalidValue("simulation name does not exist");
            //return -1;  //TODO maybe null
        }
        Integer id;
        synchronized (IDToRunSimulation){
            id = IDToRunSimulation;
            IDToRunSimulation++;
        }
        synchronized (approvManager){
            approvManager.put(id, new simulationApprovementManager(simulationName, userName, amountToRun, ticks, sec, id));
        }
        return id;
//
//
//        synchronized (approvementManager) {
//            applicationDetails details = new applicationDetails(simulationName, approvementStatus.WAITING, ticks, sec);
//            if (approvementManager.containsKey(userName)) {
//                if (approvementManager.get(userName).containsKey(details)) {
//                    approvementManager.get(userName).get(details).addToAmountToRun(amountToRun);
//                } else {
//                    approvementManager.get(userName).put(details, new simulationApprovementManager(simulationName, userName, amountToRun, ticks, sec));
//                }
//            } else {
//                Map<applicationDetails, simulationApprovementManager> temp = new HashMap<>();
//                temp.put(details, new simulationApprovementManager(simulationName, userName, amountToRun, ticks, sec));
//                approvementManager.put(userName, temp);
//            }
//        }
    }

    public Boolean _approveSimulation(Integer id){
//        if(approvementManager.containsKey(userName)) {
//            applicationDetails details = new applicationDetails(simulationName, approvementStatus.WAITING, ticks, sec);
//            if (approvementManager.get(userName).containsKey(details)) {
//                simulationApprovementManager manager = approvementManager.get(userName).get(details);
//                approvementManager.get(userName).remove(details);
//                applicationDetails temp = new applicationDetails(simulationName, approvementStatus.APPROVED, ticks, sec);
//                manager.setStatus(approvementStatus.APPROVED);
//                approvementManager.get(userName).put(temp, manager);
//                return true;
//            }
//        }
        synchronized (approvManager){
            if(approvManager.containsKey(id)) {
                approvManager.get(id).setStatus(approvementStatus.APPROVED);
                return true;
            }
            throw new DTO.InvalidValue("request id does not exist");
            //return false;
        }
    }

    public Boolean _denySimulation(Integer id){
//        if(approvementManager.containsKey(userName)) {
//            applicationDetails details = new applicationDetails(simulationName, approvementStatus.WAITING, ticks, sec);
//            if (approvementManager.get(userName).containsKey(simulationName)) {
//                simulationApprovementManager manager = approvementManager.get(userName).get(details);
//                approvementManager.get(userName).remove(details);
//                applicationDetails temp = new applicationDetails(simulationName, approvementStatus.DENIED, ticks, sec);
//                manager.setStatus(approvementStatus.DENIED);
//                approvementManager.get(userName).put(temp, manager);
//                //approvementManager.get(userName).get(simulationName).setStatus(approvementStatus.DENIED);
//                return true;
//            }
//        }
//        return false;
        synchronized (approvManager){
            if(approvManager.containsKey(id)) {
                approvManager.get(id).setStatus(approvementStatus.DENIED);
                return true;
            }
            throw new DTO.InvalidValue("request id does not exist");
            //return false;
        }
    }

//    public final Map<String, Map<applicationDetails, simulationApprovementManager>> _getApprovementManager(){
//        return approvementManager;
//    }

    public DTO.approvementStatus getDTOApprovementStatus(approvementStatus status){
        if (status.equals(approvementStatus.APPROVED)){
            return DTO.approvementStatus.APPROVED;
        }
        if (status.equals(approvementStatus.DENIED)){
            return DTO.approvementStatus.DENIED;
        }
        if (status.equals(approvementStatus.WAITING)){
            return DTO.approvementStatus.WAITING;
        }
        return DTO.approvementStatus.USED;
    }
    public final Map<Integer, DTOsimulationApprovementManager> _getApprovementManager(){
        return approvManager.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(),
                                                                        entry -> new DTOsimulationApprovementManager(entry.getValue().getSimulationName(), entry.getValue().getUserName(), getDTOApprovementStatus(entry.getValue().getStatus()), entry.getValue().getAmountToRun(), entry.getValue().getAmountRun(), entry.getValue().getTicks(), entry.getValue().getSec(), entry.getValue().getId(), entry.getValue().getCurrentRun(), entry.getValue().getDone())));
    }

//    public final Map<applicationDetails, simulationApprovementManager> _getApprovementManager(String userName){
//        return approvementManager.get(userName);
//    }

    public final Map<Integer, DTOsimulationApprovementManager> _getApprovementManager(String userName){
        return approvManager.entrySet().stream().filter(entry -> entry.getValue().getUserName().equals(userName)).collect(Collectors.toMap(entry -> entry.getKey(),
                entry -> new DTOsimulationApprovementManager(entry.getValue().getSimulationName(), entry.getValue().getUserName(), getDTOApprovementStatus(entry.getValue().getStatus()), entry.getValue().getAmountToRun(), entry.getValue().getAmountRun(), entry.getValue().getTicks(), entry.getValue().getSec(), entry.getValue().getId(), entry.getValue().getCurrentRun(), entry.getValue().getDone())));
    }

    public void startThreadPool(Integer numberOFThreads){
        if(threadPool != null && !threadPool.isShutdown()){
            isTreadPoolShoutDown = true;
            threadPool.shutdownNow();
        }

        threadPool = Executors.newFixedThreadPool(numberOFThreads);
        isTreadPoolShoutDown = false;
    }

    public void _loadSimulationDefinition(InputStream inputStream) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        worldDifenichan worldDif = new worldDifenichan();
        worldDif.loadFile(inputStream);
        synchronized (worldDifenichanCollecen) {
            if (worldDifenichanCollecen.containsKey(worldDif.getName())) {
                throw new allReadyExistsException("simulation name all ready exists");
            }
            worldDifenichanCollecenNames.add(worldDif.getName());
            worldDifenichanCollecen.put(worldDif.getName(), worldDif);
        }
    }

    public void _loadSimulationDefinition(String fileName) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        worldDifenichan worldDif = new worldDifenichan();
        worldDif.loadFileOld(fileName);
        synchronized (worldDifenichanCollecen) {
            if (worldDifenichanCollecen.containsKey(worldDif.getName())) {
                throw new allReadyExistsException("simulation name all ready exists");
            }
            worldDifenichanCollecenNames.add(worldDif.getName());
            worldDifenichanCollecen.put(worldDif.getName(), worldDif);
        }
    }

    public void loadSimulationFromDefinition(World world, Integer id, String userName){
        //world = new World();
        simulationApprovementManager manager = approvManager.get(id);
        //simulationApprovementManager manager = approvementManager.get(userName).get(simulationName);
        synchronized (worldDifenichanCollecen) {
            world.loadSimulation(worldDifenichanCollecen.get(manager.getSimulationName()), manager.getTicks(), manager.getSec(), userName);
        }
    }

    public void loadSimulation(String fileName) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        try {
            cuurentSimuletion = new World(0);
            numOfThreads = cuurentSimuletion.loadFile(fileName);
            isFileLoadedInSimulation = true;
            if(m_fileName == null || !m_fileName.equals(fileName)){
                loadNewFile(fileName);
            }
        }catch (Exception e){
            m_fileName = null;
            cuurentSimuletion = null;
            throw e;
        }
    }

    private void loadNewFile(String fileName)throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException{
        if(threadPool != null && !threadPool.isShutdown()){
            isTreadPoolShoutDown = true;
            threadPool.shutdownNow();
        }
        m_fileName = fileName;
        synchronized (simStatus) {
            simStatus.clear();
        }
        synchronized (newlyFinishedSimulationIds){
            newlyFinishedSimulationIds.clear(); //TODO maybe not (i.e delete this line)
        }
        threadPool = Executors.newFixedThreadPool(numOfThreads);
        isTreadPoolShoutDown = false;
    }

    public List<DTOEnvironmentVariablesValues> setSimulation()throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException{
        if(!isFileLoadedInSimulation){
            loadSimulation(m_fileName);
        }
        return cuurentSimuletion.setSimulation();
    }

    public List<Integer> updateNewlyFinishedSimulation(String userName, Integer index){
        List<Integer> res = new ArrayList<>();
        newlyFinishedSimulationIds.entrySet().stream().filter(entry -> entry.getKey().equals(userName)).forEach(entry -> entry.getValue().stream().skip(index).forEach(id -> res.add(id)));
        //newlyFinishedSimulationIds.clear();
        return res;
    }

    public List<DTOSimulationIdAndUserName> updateNewlyFinishedSimulation(Integer index){
        List<DTOSimulationIdAndUserName> res = new ArrayList<>();
        //newlyFinishedSimulationIds.entrySet().stream().filter(entry -> entry.getKey().equals(userName)).forEach(entry -> entry.getValue().stream().skip(index).forEach(id -> res.add(id)));
        finishedSimulationForAdmins.stream().skip(index).forEach(data -> res.add(data));
        //newlyFinishedSimulationIds.clear();
        return res;
    }

//    public void updateNewlyFinishedSimulationInLoop(ObservableList<ExecutionListItem> simulations){
//        Thread thread = new Thread(() -> {  while(true)
//                                            {updateNewlyFinishedSimulation(simulations);
//                                            try{Thread.sleep(200);}catch (InterruptedException e){}}
//                                         });
//        thread.setDaemon(true);
//        thread.start();
//    }
//
//    public List<Integer> updateNewlyFinishedSimulation(ObservableList<ExecutionListItem> simulations){
//        synchronized (newlyFinishedSimulationIds){
//            ObservableList<ExecutionListItem> toRemove = FXCollections.observableArrayList();
//            for(Integer id : newlyFinishedSimulationIds){
//                for(ExecutionListItem executionListItem : simulations){
//                    if(executionListItem.getID().equals(id)){
//                        toRemove.add(executionListItem);
//                    }
//                }
//            }
//
//            Platform.runLater(() -> {for(ExecutionListItem executionListItem : toRemove){//TODO make logic when simulation ended
//                simulations.remove(executionListItem);
//                simulations.add(new ExecutionListItem(executionListItem.getID(), true));
//
//            }});
//            List<Integer> res = new ArrayList<>();
//            newlyFinishedSimulationIds.stream().forEach(id -> res.add(id));
//            newlyFinishedSimulationIds.clear();
//            return res;
//        }
//    }

    public DTO.Status getSimulationStatus(Integer simulationNum){
        synchronized (simStatus){
            if(!simStatus.containsKey(simulationNum)){
                throw new DTO.InvalidValue("Simulation id does not exist");
            }
            return getDTOStatus(simStatus.get(simulationNum).getStatus());
        }
    }

    public Map<Integer, DTO.Status> getAllSimulationStatus(){
        synchronized (simStatus){
            return simStatus.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> getDTOStatus(entry.getValue().getStatus())));
        }
    }

    public DTO.Status getDTOStatus(Status status){
        if(status.equals(Status.WAITINGTORUN)){
            return DTO.Status.WAITINGTORUN;
        }
        if(status.equals(Status.RUNNING)){
            return DTO.Status.RUNNING;
        }
        return DTO.Status.FINISHED;
    }

    public Set<Integer> getSimulationInSystem(){
        return simStatus.keySet();
    }

    public Map<String, Integer> threadPoolDetails() {
        if (threadPool != null && !threadPool.isTerminated()) {
            Integer poolSize = 0;
            Integer finedSimulation = 0;
            Integer witting = 0;
//            synchronized (this.poolSize){
//                poolSize = this.poolSize;
//            }
//            synchronized (this) {
//                finedSimulation = worldsList.size();
//            }
            synchronized (simStatus) {
                for (simulationsStatus simulationsStatus : simStatus.values()) {
                    if (simulationsStatus.getStatus() == Status.WAITINGTORUN) {
                        witting++;
                    } else if (simulationsStatus.getStatus() == Status.RUNNING) {
                        poolSize++;
                    } else if (simulationsStatus.getStatus() == Status.FINISHED) {
                        finedSimulation++;
                    }
                }
            }
            Map<String, Integer> res = new HashMap<>();
            res.put("Waiting", witting);
            res.put("Running", poolSize);
            res.put("Finished", finedSimulation);
            return res;
        }
        throw new DTO.InvalidValue("thread pool does not exist");
        //return null;
    }

//    public void bindAndGetThreadPoolDetails2(ObservableList<QueueManagement> threadPoolList){
//        Thread thread = new Thread(() -> {  while(true)
//        {threadPoolDetails2(threadPoolList);
//            try{Thread.sleep(200);}catch (InterruptedException e){}}
//        });
//        thread.setDaemon(true);
//        thread.start();
//    }
//    public void threadPoolDetails2(ObservableList<QueueManagement> threadPoolList){
//        if(threadPool != null && !threadPool.isTerminated()){
//            Integer poolSize = 0;
//            Integer finedSimulation = 0;
//            Integer witting = 0;
////            synchronized (this.poolSize){
////                poolSize = this.poolSize;
////            }
////            synchronized (this) {
////                finedSimulation = worldsList.size();
////            }
//            synchronized(simStatus) {
//                for (simulationsStatus simulationsStatus : simStatus.values()) {
//                    if (simulationsStatus.getStatus() == Status.WAITINGTORUN) {
//                        witting++;
//                    } else if (simulationsStatus.getStatus() == Status.RUNNING) {
//                        poolSize++;
//                    } else if (simulationsStatus.getStatus() == Status.FINISHED) {
//                        finedSimulation++;
//                    }
//                }
//            }
//            Platform.runLater(() -> threadPoolList.clear());
//            setThreadPoolProperties2(threadPoolList, witting, "Waiting");
//            setThreadPoolProperties2(threadPoolList, poolSize, "Running");
//            setThreadPoolProperties2(threadPoolList, finedSimulation, "Finished");
//        }
//    }
//
//    private void setThreadPoolProperties2(ObservableList<QueueManagement> threadPoolList, Integer value, String status){
//        Platform.runLater(() -> threadPoolList.add(new QueueManagement(status, value)));
//    }

    public void bindAndGetThreadPoolDetails(IntegerProperty wit, IntegerProperty run, IntegerProperty fin){
        Thread thread = new Thread(() -> {  while(true)
                                            {threadPoolDetails(wit, run, fin);
                                            try{Thread.sleep(200);}catch (InterruptedException e){}}
                                             });
        thread.setDaemon(true);
        thread.start();
    }
    public void threadPoolDetails(IntegerProperty wit, IntegerProperty run, IntegerProperty fin){
        if(threadPool != null && !threadPool.isTerminated()){
            Integer poolSize = 0;
            Integer finedSimulation = 0;
            Integer witting = 0;
//            synchronized (this.poolSize){
//                poolSize = this.poolSize;
//            }
//            synchronized (this) {
//                finedSimulation = worldsList.size();
//            }
            synchronized(simStatus) {
                for (simulationsStatus simulationsStatus : simStatus.values()) {
                    if (simulationsStatus.getStatus() == Status.WAITINGTORUN) {
                        witting++;
                    } else if (simulationsStatus.getStatus() == Status.RUNNING) {
                        poolSize++;
                    } else if (simulationsStatus.getStatus() == Status.FINISHED) {
                        finedSimulation++;
                    }
                }
            }
            setThreadPoolProperties(wit, witting);
            setThreadPoolProperties(run, poolSize);
            setThreadPoolProperties(fin, finedSimulation);
        }
    }

    private void setThreadPoolProperties(IntegerProperty prop, Integer value){
        Platform.runLater(() -> prop.set(value));
    }

    public void disposeOfThreadPool(){
        if(threadPool != null && !threadPool.isTerminated()){
            isTreadPoolShoutDown = true;
            threadPool.shutdownNow();
        }
    }

//    public void bindToWhenFines(BooleanProperty isFines){
//        cuurentSimuletion.bindToWhenFines(isFines);
//    }

    public List<DTOEnvironmentVariablesValues> _prepareSimulation(Integer requestId, String userName, Map<String, String> environmentsValues, Map<String, Integer> entitiesPopulation) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException,  InvalidValue, ReferenceNotInitializedException{
//        if(approvementManager.containsKey(userName) || approvementManager.get(userName).containsKey(simulationName) || !approvementManager.get(userName).get(simulationName).getStatus().equals(approvementStatus.APPROVED)){
//            return null;
//        }
//        applicationDetails details = new applicationDetails(simulationName, approvementStatus.APPROVED, ticks, sec);
//        synchronized (approvementManager) {
//            if (!approvementManager.containsKey(userName) || !approvementManager.get(userName).containsKey(details)) {
//                return null;
//            }
//            approvementManager.get(userName).get(details).decreasedAmount();
//            applicationDetails temp = new applicationDetails(simulationName, approvementStatus.USED, ticks, sec);
//            if (approvementManager.get(userName).containsKey(temp)) {
//                approvementManager.get(userName).get(temp).addToAmountToRun(1);
//            } else {
//                approvementManager.get(userName).put(temp, new simulationApprovementManager(simulationName, userName, 1, ticks, sec));
//            }
//        }
        synchronized (approvManager){
            if(!approvManager.containsKey(requestId) || !approvManager.get(requestId).getStatus().equals(approvementStatus.APPROVED) || approvManager.get(requestId).getRemainingRun().equals(0)) {
                throw new DTO.InvalidValue("request id does not exist, request does not approved or out of running simulation");
                //return null;
            }
        }

        World world = new World(requestId);
        loadSimulationFromDefinition(world, requestId, userName);
        //world.setRequestId(requestId);

        for(String name : environmentsValues.keySet()){
            addEnvironmentVariableValue(world, name, environmentsValues.get(name));
        }

        for(String name : entitiesPopulation.keySet()){
            addPopulationToEntity(world, name, entitiesPopulation.get(name));
        }

        List<DTOEnvironmentVariablesValues> environmentVariablesValues = world.setSimulation();
        synchronized (userCurrentSimulation) {
            userCurrentSimulation.put(userName, world);
        }

        return environmentVariablesValues;
    }

    public int _startSimulation(String userName)throws InvalidValue, ReferenceNotInitializedException{
        int simulationNum;
        if(!userCurrentSimulation.containsKey(userName)){
            throw new InvalidValue("user name is not exising");
        }
        Integer id;
        synchronized (userCurrentSimulation.get(userName)){
            id = userCurrentSimulation.get(userName).getRequestId();
        }
        if(!approvManager.containsKey(id)){
            throw new DTO.InvalidValue("request id does not exist");
            //return -1;
        }
        String simulationName;
        synchronized (approvManager.get(id)){
            if(!approvManager.containsKey(id) || !approvManager.get(id).getStatus().equals(approvementStatus.APPROVED) || approvManager.get(id).getRemainingRun().equals(0)) {
                throw new DTO.InvalidValue("request id does not exist, request does not approved or out of running simulation");
                //return -1;
            }
            //userName = approvManager.get(id).getUserName();
            simulationName = approvManager.get(id).getSimulationName();
            approvManager.get(id).increaseAmountRun();
            if(approvManager.get(id).getRemainingRun().equals(0)){
                approvManager.get(id).setStatus(approvementStatus.USED);
            }
        }
        synchronized (userCurrentSimulation) {
            simulationNum = activeSimulation(userCurrentSimulation.get(userName), id);
            userCurrentSimulation.remove(userName);
        }
        synchronized (worldDifenichanCollecen){
            worldDifenichanCollecen.get(simulationName).increaseRunningCounter();
        }
        return simulationNum;
    }

//    public DTOSimulationStartDetails startSimulation(String userName, String simulationName, myTask aTask, Map<String, String> environmentsValues, Map<String, Integer> entitiesPopulation) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException,  InvalidValue, ReferenceNotInitializedException{
//        if(approvementManager.containsKey(userName) || approvementManager.get(userName).containsKey(simulationName) || !approvementManager.get(userName).get(simulationName).getStatus().equals(simulationApprovementManager.approvementStatus.APPROVED)){
//            return null;
//        }
//        World world = new World();
//        loadSimulationFromDefinition(world, simulationName);
//
//        for(String name : environmentsValues.keySet()){
//            addEnvironmentVariableValue(world, name, environmentsValues.get(name));
//        }
//
//        for(String name : entitiesPopulation.keySet()){
//            addPopulationToEntity(world, name, entitiesPopulation.get(name));
//        }
//
//        List<DTOEnvironmentVariablesValues> environmentVariablesValues = world.setSimulation();
//        int simulationNum = activeSimulation(world, aTask);
//        DTOSimulationStartDetails simulationStartDetails = new DTOSimulationStartDetails(simulationNum, environmentVariablesValues);
//
//        return simulationStartDetails;
//
//    }

    public int activeSimulation(World world, Integer id)throws InvalidValue, ReferenceNotInitializedException{
        int simulationNum;
        synchronized (this.simulationNum) {
            simulationNum = ++this.simulationNum;
        }
        if(world == null){
            throw new ReferenceNotInitializedException("Simulation wasn't load");
        }
        //simulationNum++;
        world.setNumSimulation(simulationNum);
        simulationsStatus temp = new simulationsStatus();
        temp.setSimulationId(simulationNum);
        //temp.setTask(aTask);
        temp.setWorld(world);
        synchronized (simStatus) {
            simStatus.put(simulationNum, temp);
        }
        isFileLoadedInSimulation = false;
        synchronized (poolSize){
            poolSize++;
        }
        threadPool.execute(() -> activeSimulationUsingThread(simulationNum, id));
        //world = null;
        //cuurentSimuletion.startSimolesan();
        //worldsList.put(simulationNum, cuurentSimuletion);
        return simulationNum;
    }

//    public int activeSimulation()throws InvalidValue, ReferenceNotInitializedException{
//        int simulationNum;
//        synchronized (this.simulationNum) {
//            simulationNum = ++this.simulationNum;
//        }
//        if(cuurentSimuletion == null){
//            throw new ReferenceNotInitializedException("Simulation wasn't load");
//        }
//        //simulationNum++;
//        cuurentSimuletion.setNumSimulation(simulationNum);
//        simulationsStatus temp = new simulationsStatus();
//        temp.setSimulationId(simulationNum);
//        //temp.setTask(aTask);
//        temp.setWorld(cuurentSimuletion);
//        synchronized (simStatus) {
//            simStatus.put(simulationNum, temp);
//        }
//        isFileLoadedInSimulation = false;
//        synchronized (poolSize){
//            poolSize++;
//        }
//        threadPool.execute(() -> activeSimulationUsingThread(simulationNum));
//        cuurentSimuletion = null;
//        //cuurentSimuletion.startSimolesan();
//        //worldsList.put(simulationNum, cuurentSimuletion);
//        return simulationNum;
//    }

    private void activeSimulationUsingThread(Integer simulationNum, Integer id){
        Boolean pause;
        World world;
        synchronized (poolSize){
            poolSize--;
        }
        synchronized (simStatus) {
            world = simStatus.get(simulationNum).getWorld();
            simStatus.get(world.getNumSimulation()).setStatus(Status.RUNNING);
            simStatus.get(world.getNumSimulation()).setRunningThread(Thread.currentThread());
            //pause = simStatus.get(world.getNumSimulation()).getIsPause();
        }
        synchronized (approvManager.get(id)){
            approvManager.get(id).increaseCurrentRun();
        }
        try {
            world.startSimolesan(simStatus.get(world.getNumSimulation()).getIsPause());
        }catch (Exception e){
            world.setException(e.getMessage());
            //simulationsExceptions.put(world.getNumSimulation(), e.getMessage());
        }finally {
            world.setSimulationEnded();
        }
        synchronized (approvManager.get(id)){
            approvManager.get(id).decreaseCurrentRun();
            approvManager.get(id).increaseDone();
        }
        synchronized (simStatus) {
            if(simStatus != null && simStatus.size() != 0) {
                simStatus.get(world.getNumSimulation()).setStatus(Status.FINISHED);
                simStatus.get(world.getNumSimulation()).setRunningThread(null);
            }
        }
        synchronized (this) {
            if(worldsList != null) { // && worldsList.size() == 0
                worldsList.put(simulationNum, world);
            }
        }
        if(newlyFinishedSimulationIds != null) {
            synchronized (newlyFinishedSimulationIds){
                finishedSimulationForAdmins.add(new DTOSimulationIdAndUserName(world.getUserName(), world.getNumSimulation()));
                 // && newlyFinishedSimulationIds.size() == 0
                if(newlyFinishedSimulationIds.containsKey(world.getUserName())) {
                    newlyFinishedSimulationIds.get(world.getUserName()).add(world.getNumSimulation());
                }else{
                    newlyFinishedSimulationIds.put(world.getUserName(), new ArrayList<>(Arrays.asList(world.getNumSimulation())));
                }
            }
        }
    }

    public Boolean isSimulationGotError(Integer simulationNum){
        synchronized (simStatus){
            if(!simStatus.containsKey(simulationNum)){
                throw new DTO.InvalidValue("simulation id does not exist");
            }
            return !simStatus.get(simulationNum).getWorld().getException().equals("");
        }
    }

    public String simulationGotError(Integer simulationNum){
        synchronized (simStatus){
            if(!simStatus.containsKey(simulationNum)){
                throw new DTO.InvalidValue("simulation id does not exist");
            }
            return simStatus.get(simulationNum).getWorld().getException();
        }
    }

    public void pauseSimulation(Integer numSimulation){
        if(!simStatus.containsKey(numSimulation)){
            throw new DTO.InvalidValue("simulation id does not exist");
        }
        synchronized (simStatus.get(numSimulation).getIsPause()) {
            simStatus.get(numSimulation).setIsPause(true);
        }
    }

    public void resumeSimulation(Integer numSimulation){
        if(!simStatus.containsKey(numSimulation)){
            throw new DTO.InvalidValue("simulation id does not exist");
        }
        synchronized (simStatus.get(numSimulation).getIsPause()) {
            simStatus.get(numSimulation).setIsPause(false);
            simStatus.get(numSimulation).getIsPause().notify();
        }
    }

    public void stopSimulation(Integer numSimulation){
        if(!simStatus.containsKey(numSimulation)){
            throw new DTO.InvalidValue("simulation id does not exist");
        }
        synchronized (simStatus.get(numSimulation).getRunningThread()) {
            simStatus.get(numSimulation).getRunningThread().interrupt();
        }
    }

    public void moveOneStep(Integer simulationNum){
        if(!simStatus.containsKey(simulationNum)){
            throw new DTO.InvalidValue("simulation id does not exist");
        }
        simStatus.get(simulationNum).getWorld().moveOneStep(simStatus.get(simulationNum).getIsPause());
    }

    public DTOMap getMap(Integer simulationNum) {
        if(!simStatus.containsKey(simulationNum)){
            throw new DTO.InvalidValue("simulation id does not exist");
        }
        return simStatus.get(simulationNum).getWorld().getMap(simStatus.get(simulationNum).getIsPause());
    }

    public DTODataForReRun getDataForRerun(Integer simulationNum){
        if(worldsList.containsKey(simulationNum)) {
            return worldsList.get(simulationNum).getDataForRerun();
        }
        throw new DTO.InvalidValue("simulationNum id does not exist");
        //return null;
    }

//    public void getDataUsingTask(myTask task, Integer simulationId){
//        World world;
//        Thread taskThread = new Thread(task);
//        taskThread.setDaemon(true);
////        synchronized (this.taskThread) {
////            this.taskThread = taskThread;
////        }
//        taskThread.setName("TaskThread");
//        synchronized (simStatus) {
//            world = simStatus.get(simulationId).getWorld();
//            simStatus.get(simulationId).setTask(task);
//            simStatus.get(simulationId).setTaskThread(taskThread);
//        }
//        task.loadWorld(world);
//        taskThread.start();
//
//
//    }

//    public void stopGettingDataUsingTask(myTask task, Integer simulationNum){
//        synchronized (simStatus) {
////            synchronized (this.taskThread) {
////            if(taskThread != null) {
////                taskThread.interrupt();
////                task.cancel();
////                }
////            }
//            synchronized (simStatus.get(simulationNum).getWorld()) {
//                simStatus.get(simulationNum).getTaskThread().interrupt();
//            }
//        }
//
//    }

    public void saveSystemState(String simulationName) throws FileNotFoundException, IOException{
        String fileName = simulationName + ".ser";
//        try (FileOutputStream fileOut = new FileOutputStream(fileName); //"serializedObject.ser"
//             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
//        }
        FileOutputStream fileOut = new FileOutputStream(fileName); //"serializedObject.ser"
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        synchronized (this) {
            out.writeObject(worldsList);
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void loadSystemState(String simulationName) throws FileNotFoundException, IOException, ClassNotFoundException{
        String fileName = simulationName + ".ser";
//        try (FileInputStream fileIn = new FileInputStream(fileName); // serializedObject.ser
//             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            FileInputStream fileIn = new FileInputStream(fileName); // serializedObject.ser
            ObjectInputStream in = new ObjectInputStream(fileIn);

        synchronized (this) {
            worldsList = (Map<Integer, World>) in.readObject();
            synchronized (simulationName) {
                simulationNum = worldsList.keySet().size();
            }
        }
//        }catch (FileNotFoundException e){
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public DTOSimulationDetails getSimulationDetails(String name){
        synchronized (worldDifenichanCollecen) {
            if (worldDifenichanCollecen.get(name) == null) {
                throw  new DTO.InvalidValue("simulation name does not exist");
                //return null;
            }
            return worldDifenichanCollecen.get(name).getSimulationDetails();
        }
    }

    public DTOSimulationsDetails getSimulationsDetails(){
        DTOSimulationsDetails simulationsDetails = new DTOSimulationsDetails();
        synchronized (worldDifenichanCollecen){
            worldDifenichanCollecen.entrySet().stream().forEach(entry -> simulationsDetails.addSimulationDetails(entry.getKey(), entry.getValue().getSimulationDetails()));
        }
        return simulationsDetails;
    }

    public DTORunningSimulationDetails getRunningSimulationDTO(Integer id){
        if(!simStatus.containsKey(id)){
            throw new DTO.InvalidValue("simulation id does not exist");
        }
        synchronized (simStatus.get(id)){
            //return simStatus.get(id).getWorld().getRunningSimulationDTO();
            return simStatus.get(id).getWorld().getSimulationRunningDetailsDTO();
        }
    }

    public Boolean isSimulationRunning(Integer id){
        if(!simStatus.containsKey(id)){
            throw new DTO.InvalidValue("simulation id does not exist");
        }
        synchronized (simStatus.get(id)){
            return simStatus.get(id).getStatus().equals(Status.RUNNING);
        }
    }

    public List<DTOEnvironmentVariables> getEnvironmentDetails(){ return cuurentSimuletion.getEnvironmentDetails();}

    public void addEnvironmentDto(DTOEnvironmentVariables dtoEnvironmentVariables) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException  {
        if(!isFileLoadedInSimulation){
            loadSimulation(m_fileName);
        }
        cuurentSimuletion.addEnvironmentDto(dtoEnvironmentVariables);
    }

    public void addPopulationToEntity(String entityName, int population) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        if(!isFileLoadedInSimulation){
            loadSimulation(m_fileName);
        }
        cuurentSimuletion.addPopulationToEntity(entityName, population);
    }

    public void addPopulationToEntity(World world, String entityName, int population) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
//        if(!isFileLoadedInSimulation){
//            loadSimulation(m_fileName);
//        }
//        if(population.equals("")){
//            return;
//        }
        world.addPopulationToEntity(entityName, population);
    }

    public void addEnvironmentVariableValue(String name, String value) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
//        if(!isFileLoadedInSimulation){
//            loadSimulation(m_fileName);
//        }
        cuurentSimuletion.addEnvironmentValue(name, value);
    }

    public void addEnvironmentVariableValue(World world, String name, String value) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
//        if(!isFileLoadedInSimulation){
//            loadSimulation(m_fileName);
//        }
        if(value.equals("")){
            return;
        }
        world.addEnvironmentValue(name, value);
    }

    public DTOSimulation getSimulationsDto() {
//        Map<Integer, String> simulations = new HashMap<>();
//
//        for(Integer id : worldsList.keySet()){
//            simulations.put(id, worldsList.get(id).getSimulationTime());
//        }
        synchronized (this) {
            Map<Integer, String> simulations = worldsList.entrySet().stream().filter(worldEntry -> worldEntry.getValue().getSimulationTime() != null).collect(Collectors.toMap(Map.Entry::getKey, worldEntry -> worldEntry.getValue().getSimulationTime()));
            return new DTOSimulation(simulations);
        }
        //return new DTOSimulation(simulations);
    }

    public DTOSimulationDetailsPostRun getPostRunData(int id) {
        synchronized (simStatus) {
            if(!simStatus.containsKey(id)){
                throw  new DTO.InvalidValue("simulation id does not exist");
                //return null;
            }
            //return worldsList.get(id).getPostRunData();
            return simStatus.get(id).getWorld().getPostRunData();
        }
    }

    //public World getCurrentSimulation (){
//        return cuurentSimuletion;
//    }
}
