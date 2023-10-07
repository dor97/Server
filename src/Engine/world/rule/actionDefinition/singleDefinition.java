package Engine.world.rule.actionDefinition;

import Engine.InvalidValue;
import Engine.generated2.PRDCondition;
import Engine.world.expression.expressionWithFunc;
import Engine.world.rule.action.single;

public class singleDefinition {
    private String m_entityName;
    private String m_propertyName;
    private single.opertor m_op;
    private expressionWithFunc m_exprecn;
    private int m_currTick;

    public singleDefinition(PRDCondition condition) throws InvalidValue {
        m_entityName = condition.getEntity();
        m_propertyName = condition.getProperty();
//        m_op = getOpFromString(condition.getOperator());
//        m_exprecn = new expressionWithFunc(util);
//        try {
//            m_exprecn.convertValueInString(condition.getValue());
//        }catch (InvalidValue e){
//            throw new InvalidValue(e.getMessage() + ". In single in action condition");
//        }
//        cheackUserInput();
    }

}
