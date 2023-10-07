package Engine.world.rule;

import DTO.DTORuleData;
import Engine.InvalidValue;
import Engine.generated.PRDAction;
import Engine.generated.PRDRule;
import Engine.utilites.Utilites;
import Engine.world.rule.action.ActionInterface;
import Engine.world.rule.action.actionFactory;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rule implements Serializable {
    private String m_name;
    private Integer m_ticks = 1;
    private double m_probability = 1;
    private List<ActionInterface> m_actions;
    private Random random;

    public Rule(String name, int ticks, int probability){
        m_name = name;
        m_ticks = ticks;
        m_probability = probability;
        m_actions = new ArrayList<>();
        random = new Random();
    }

    public Rule(Rule rule, Utilites util) throws InvalidValue {
        m_name = rule.getName();
        m_ticks = rule.getTicks();
        m_probability = rule.getProbability();
        random = new Random();
        m_actions = new ArrayList<>();
        actionFactory factory = new actionFactory();

        for(ActionInterface action : rule.getActions()){
            try {
                m_actions.add(action.clone(util, m_name));
            }
            catch (OBJECT_NOT_EXIST e){
                throw new OBJECT_NOT_EXIST(e.getMessage() + " referred to in rule " + m_name);
            }
            catch (ArithmeticException e){
                throw new ArithmeticException(e.getMessage() + " referred to in rule " + m_name);
            }
            catch (InvalidValue e){
                throw new InvalidValue(e.getMessage() + " referred to in rule " + m_name);
            }
            //catch (Exception e){
            //    throw new Exception(e.getMessage() + " referred to in rule " + m_name);
            //}

        }
    }


    public Rule(PRDRule rule, Utilites util) throws InvalidValue {
        m_name = rule.getName();
        try{
            m_ticks = rule.getPRDActivation().getTicks() != null ? rule.getPRDActivation().getTicks() : 1;
        }
        catch (NullPointerException e){

        }
        try {
            m_probability = rule.getPRDActivation().getProbability() != null ? rule.getPRDActivation().getProbability() : 1;
        }
        catch (NullPointerException e){

        }
        random = new Random();
        m_actions = new ArrayList<>();
        actionFactory factory = new actionFactory();
        for(PRDAction action : rule.getPRDActions().getPRDAction()){
            try {
                m_actions.add(factory.makeAction(action, util, m_name));
            }
            catch (OBJECT_NOT_EXIST e){
                throw new OBJECT_NOT_EXIST(e.getMessage() + " referred to in rule " + m_name);
            }
            catch (ArithmeticException e){
                throw new ArithmeticException(e.getMessage() + " referred to in rule " + m_name);
            }
            catch (InvalidValue e){
                throw new InvalidValue(e.getMessage() + " referred to in rule " + m_name);
            }
            //catch (Exception e){
            //    throw new Exception(e.getMessage() + " referred to in rule " + m_name);
            //}

        }
    }

    public Rule clone(Utilites util){
        return new Rule(this, util);
    }

    public List<ActionInterface> getActions(){
        return m_actions;
    }

    public Integer getTicks(){
        return m_ticks;
    }

    public String getName(){
        return m_name;
    }

    public ActionInterface getAction(String Action){
        return null;
    }

    public void addAction(ActionInterface ActionToAdd){
        m_actions.add(ActionToAdd);
    }
//    public boolean activeRule(Entity entity)throws InvalidValue{
//        for(ActionInterface action : m_actions){
//            if(action.getEntityName().equals(entity.getName()) && (entity.isPropertyExists(action.getPropertyName()) || action.getPropertyName() == null)){
//                if (action.activateAction(entity)){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public List<ActionInterface> getActionToActive(int currTick){
        if(currTick % getTick() == 0 && random.nextDouble() < getProbability()) {
            return m_actions;
        }
        return new ArrayList<>();
    }
    
    public double getProbability(){
        return m_probability;
    }
    
    public int getTick(){
        return m_ticks;
    }

    public DTORuleData makeDtoRule(){
        DTORuleData DTO = new DTORuleData(m_name, m_ticks, m_probability);

        for(ActionInterface action : m_actions){
            DTO.addAction(action.makeActionDto());
            DTO.addActionName(action.getName());
            DTO.increaseNumberOfActionByOne();
        }

        return DTO;
    }
}
