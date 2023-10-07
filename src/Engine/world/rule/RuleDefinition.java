package Engine.world.rule;

import Engine.InvalidValue;
import Engine.generated.PRDAction;
import Engine.generated.PRDRule;
import Engine.utilites.Utilites;
import Engine.world.rule.action.ActionInterface;
import Engine.world.rule.action.actionFactory;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RuleDefinition {
    private String m_name;
    private Integer m_ticks = 1;
    private double m_probability = 1;
    private List<ActionInterface> m_actions;
    private Random random;

    public RuleDefinition(String name, int ticks, int probability){
        m_name = name;
        m_ticks = ticks;
        m_probability = probability;
        m_actions = new ArrayList<>();
        random = new Random();
    }

    public RuleDefinition(PRDRule rule, Utilites util) throws InvalidValue {
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
}
