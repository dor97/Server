package Engine.world;

import DTO.DTOEnvironmentVariables;
import DTO.DTOSimulationDetails;
import DTO.DTOTerminationData;
import DTO.terminationType;
import Engine.InvalidValue;
import Engine.UnsupportedFileTypeException;
import Engine.allReadyExistsException;
import Engine.generated.PRDEntity;
import Engine.generated.PRDEnvProperty;
import Engine.generated.PRDRule;
import Engine.generated.PRDWorld;
import Engine.utilites.Utilites;
import Engine.world.entity.EntityDifenichan;
import Engine.world.entity.property.EnvironmentDifenichan;
import Engine.world.rule.Rule;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class worldDifenichan {

    private Map<String, EnvironmentDifenichan> m_environmentsDifenichen = new HashMap<>();
    private Map<String, EntityDifenichan> m_entitiesDifenichan = new HashMap<>();
    private List<Rule> m_rules = new ArrayList<>();
    private Integer m_cols, m_rows;
    private String name;
    private Integer sleep;
    private Integer runningCounter = 0;
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "Engine.generated";


    public void loadFile(InputStream inputStream)throws NoSuchFileException , UnsupportedFileTypeException, allReadyExistsException, InvalidValue, JAXBException, FileNotFoundException {
        //PRDWorld xmlWorld = new PRDWorld();

        //load file
        PRDWorld xmlWorld = deserializeFrom(inputStream);

//        try {
//            xmlWorld = deserializeFrom(inputStream);
//        } catch (JAXBException | FileNotFoundException e) {
//            throw e;
//        }

        m_cols = xmlWorld.getPRDGrid().getColumns();
        m_rows = xmlWorld.getPRDGrid().getRows();
        if(m_rows > 100 || m_rows < 10 || m_cols > 100 || m_cols < 10){
            throw new InvalidValue("Size of grid is not valid");
        }

        //entitys
        for(PRDEntity e : xmlWorld.getPRDEntities().getPRDEntity()){
            m_entitiesDifenichan.put(e.getName(), new EntityDifenichan(e));
        }
        //environment
        for(PRDEnvProperty p : xmlWorld.getPRDEnvironment().getPRDEnvProperty()){
            if(m_environmentsDifenichen.containsKey(p.getPRDName())){
                throw new allReadyExistsException("environment variables " + p.getPRDName() + " all ready exists.");
            }
            m_environmentsDifenichen.put(p.getPRDName(), new EnvironmentDifenichan(p));
        }

        //environment values
//        for(PRDEnvProperty envProperty : xmlWorld.getPRDEvironment().getPRDEnvProperty()){
//            if(m_environments.containsKey(envProperty.getPRDName())){
//                throw new allReadyExistsException("enviroments varuble " + envProperty.getPRDName() + " all ready exists");
//            }
//            if(envProperty.getType().equals("decimal")) {
//                m_environments.put(envProperty.getPRDName(), new DecimalProperty(envProperty));
//            } else if(envProperty.getType().equals("float")){
//                m_environments.put(envProperty.getPRDName(), new FloatProperty(envProperty));
//            } else if (envProperty.getType().equals("string")) {
//                m_environments.put(envProperty.getPRDName(), new StringProperty(envProperty));
//            } else if (envProperty.getType().equals("boolean")) {
//                m_environments.put(envProperty.getPRDName(), new BooleanProperty(envProperty));
//            }
//        }

        Utilites util = new Utilites(m_entitiesDifenichan, m_environmentsDifenichen, m_rows, m_cols);
        //util.Init(m_environments, m_entitiesDifenichan, m_environmentsDifenichen);

        //rules
        for(PRDRule rule : xmlWorld.getPRDRules().getPRDRule()){
            m_rules.add(new Rule(rule, util));
        }

        name = xmlWorld.getName();
        sleep = xmlWorld.getSleep();
//        Optional<List<Object>> secondOrTicks = Optional.ofNullable((List<Object>) xmlWorld.getPRDTermination().getPRDBySecondOrPRDByTicks());
//        secondOrTicks.ifPresent(t -> getTermination(t));

//        m_ticks = new expression();
//        Optional<Integer> time = Optional.ofNullable().ifPresent(t -> t.getCount()))));
//        time.ifPresent((t) -> m_ticks.setValue(t));
//
//        m_secondToWork = new expression();
//        Optional<Integer> second = Optional.ofNullable(((PRDBySecond)xmlWorld.getPRDTermination().getPRDBySecondOrPRDByTicks().get(1)).getCount());
//        second.ifPresent((s) -> m_secondToWork.setValue(s));

//        if(m_ticks.getType() == null && m_secondToWork.getType() == null && xmlWorld.getPRDTermination().getPRDByUser() == null){
//            throw new InvalidValue("No termination method was added");
//        }

        //return xmlWorld.getPRDThreadCount();
        //return 3; //TODO change
    }

    public void loadFileOld(String xmlFile)throws NoSuchFileException , UnsupportedFileTypeException, allReadyExistsException, InvalidValue, JAXBException, FileNotFoundException {
        PRDWorld xmlWorld = new PRDWorld();

        //load file
        Path path = Paths.get(xmlFile);
        if (Files.exists(path) && Files.isRegularFile(path)) {
            // Check if the file extension is .xml
            String fileName = path.getFileName().toString();
            if (!fileName.endsWith(".xml")) {
                throw new UnsupportedFileTypeException("Not an XML file: " + xmlFile);
            }
        } else {
            throw new NoSuchFileException("File does not exist: " + xmlFile);
        }

        try {
            InputStream inputStream = new FileInputStream(new File(xmlFile));
            xmlWorld = deserializeFrom(inputStream);
        } catch (JAXBException | FileNotFoundException e) {
            throw e;
        }

        m_cols = xmlWorld.getPRDGrid().getColumns();
        m_rows = xmlWorld.getPRDGrid().getRows();
        if(m_rows > 100 || m_rows < 10 || m_cols > 100 || m_cols < 10){
            throw new InvalidValue("Size of grid is not valid");
        }

        //entitys
        for(PRDEntity e : xmlWorld.getPRDEntities().getPRDEntity()){
            m_entitiesDifenichan.put(e.getName(), new EntityDifenichan(e));
        }
        //environment
        for(PRDEnvProperty p : xmlWorld.getPRDEnvironment().getPRDEnvProperty()){
            if(m_environmentsDifenichen.containsKey(p.getPRDName())){
                throw new allReadyExistsException("environment variables " + p.getPRDName() + " all ready exists.");
            }
            m_environmentsDifenichen.put(p.getPRDName(), new EnvironmentDifenichan(p));
        }

        //environment values
//        for(PRDEnvProperty envProperty : xmlWorld.getPRDEvironment().getPRDEnvProperty()){
//            if(m_environments.containsKey(envProperty.getPRDName())){
//                throw new allReadyExistsException("enviroments varuble " + envProperty.getPRDName() + " all ready exists");
//            }
//            if(envProperty.getType().equals("decimal")) {
//                m_environments.put(envProperty.getPRDName(), new DecimalProperty(envProperty));
//            } else if(envProperty.getType().equals("float")){
//                m_environments.put(envProperty.getPRDName(), new FloatProperty(envProperty));
//            } else if (envProperty.getType().equals("string")) {
//                m_environments.put(envProperty.getPRDName(), new StringProperty(envProperty));
//            } else if (envProperty.getType().equals("boolean")) {
//                m_environments.put(envProperty.getPRDName(), new BooleanProperty(envProperty));
//            }
//        }

        Utilites util = new Utilites(m_entitiesDifenichan, m_environmentsDifenichen, m_rows, m_cols);
        //util.Init(m_environments, m_entitiesDifenichan, m_environmentsDifenichen);

        //rules
        for(PRDRule rule : xmlWorld.getPRDRules().getPRDRule()){
            m_rules.add(new Rule(rule, util));
        }

        name = xmlWorld.getName();
        sleep = xmlWorld.getSleep();
//        Optional<List<Object>> secondOrTicks = Optional.ofNullable((List<Object>) xmlWorld.getPRDTermination().getPRDBySecondOrPRDByTicks());
//        secondOrTicks.ifPresent(t -> getTermination(t));

//        m_ticks = new expression();
//        Optional<Integer> time = Optional.ofNullable().ifPresent(t -> t.getCount()))));
//        time.ifPresent((t) -> m_ticks.setValue(t));
//
//        m_secondToWork = new expression();
//        Optional<Integer> second = Optional.ofNullable(((PRDBySecond)xmlWorld.getPRDTermination().getPRDBySecondOrPRDByTicks().get(1)).getCount());
//        second.ifPresent((s) -> m_secondToWork.setValue(s));

//        if(m_ticks.getType() == null && m_secondToWork.getType() == null && xmlWorld.getPRDTermination().getPRDByUser() == null){
//            throw new InvalidValue("No termination method was added");
//        }

        //return xmlWorld.getPRDThreadCount();
        //return 3; //TODO change
    }

//    private void getTermination(List<Object> secondOrTicks){
//        if(secondOrTicks.size() == 0){
//            return;
//        }
//        if(secondOrTicks.size() == 1) {
//            if (secondOrTicks.get(0) instanceof PRDByTicks) {
//                m_ticks.setValue(((PRDByTicks) (secondOrTicks.get(0))).getCount());
//            }
//            if (secondOrTicks.get(0) instanceof PRDBySecond) {
//                m_secondToWork.setValue(((PRDBySecond) (secondOrTicks.get(0))).getCount());
//            }
//            return;
//        }
//        if(secondOrTicks.size() == 2) {
//            if (secondOrTicks.get(0) instanceof PRDByTicks) {
//                m_ticks.setValue(((PRDByTicks) (secondOrTicks.get(0))).getCount());
//                m_secondToWork.setValue(((PRDBySecond) (secondOrTicks.get(1))).getCount());
//
//            }
//            if (secondOrTicks.get(0) instanceof PRDBySecond) {
//                m_secondToWork.setValue(((PRDBySecond) (secondOrTicks.get(0))).getCount());
//                m_ticks.setValue(((PRDByTicks) (secondOrTicks.get(1))).getCount());
//            }
//        }
//    }

    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }

    public DTOSimulationDetails getSimulationDetails(){
        DTOSimulationDetails DTO = new DTOSimulationDetails();

        for(EntityDifenichan entity : m_entitiesDifenichan.values()){
            DTO.addEntity(entity.makeDtoEntity());
        }

        for(Rule rule : m_rules){
            DTO.addRule(rule.makeDtoRule());
        }

        DTO.setEnvironments(getEnvironmentDetails());

        DTO.setGridSize(m_rows, m_cols);

        DTOTerminationData terminationData = new DTOTerminationData();
//        if(m_ticks.getType() != null){
//            terminationData.putData(terminationType.TICKS.toString(), ((Integer)m_ticks.getInt()).toString());
//            //DTO.(terminationType.TICKS, m_ticks.getInt()));
//        }
//
//        if(m_secondToWork.getType() != null){
//            terminationData.putData(terminationType.SECOND.toString(), ((Integer)m_secondToWork.getInt()).toString());
//            //DTO.addTermination(new DTOTerminationData(terminationType.SECOND, m_secondToWork.getInt()));
//        }
        DTO.setTermination(terminationData);

        DTO.setName(name);

        return DTO;
    }

    public List<DTOEnvironmentVariables> getEnvironmentDetails(){
        List<DTOEnvironmentVariables> DTOList = new ArrayList<>();

        for(EnvironmentDifenichan environmentDifenichan : m_environmentsDifenichen.values()){
            DTOList.add(environmentDifenichan.makeDtoEnvironment());
        }

        return DTOList;
    }

    public Integer getRunningCounter(){
        return runningCounter;
    }

    public void increaseRunningCounter(){
        runningCounter++;
    }

    public Integer getSleep(){
        return sleep;
    }

    public Integer getCols(){
        return m_cols;
    }

    public Integer getRows(){
        return m_rows;
    }

    public final List<Rule> getRules(){
        return m_rules;
    }

    public final Map<String, EntityDifenichan> getEntityDifenichan(){
        return m_entitiesDifenichan;
    }

    public final Map<String, EnvironmentDifenichan> getEnvironmentsDifenichen(){
        return m_environmentsDifenichen;
    }

    public final String getName(){
        return name;
    }
}
