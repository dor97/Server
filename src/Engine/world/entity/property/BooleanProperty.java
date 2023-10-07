package Engine.world.entity.property;

import Engine.generated.PRDEnvProperty;
import Engine.world.expression.expressionType;

import java.io.Serializable;
import java.util.Random;

public class BooleanProperty extends Property implements Serializable {
    private String m_name;
    private boolean m_property;
    //private int lastTickChanged = 0;

    public BooleanProperty(propertyDifenichan propertyDifenichan) {
        super(propertyType.BOOL);
        m_name = propertyDifenichan.getName();
        if (propertyDifenichan.getType() != expressionType.BOOL){
            //exception
        }
        init(propertyDifenichan);
/*        if(propertyDifenichan.isRandom()){
            Random random = new Random();
            m_property = random.nextBoolean();
        }
        else{
            m_property = propertyDifenichan.getInit().getBool();
        }*/
    }

    public BooleanProperty(propertyDifenichan propertyDifenichan,  PropertyInterface secondaryProperty){
        super(propertyType.BOOL);
        m_name = propertyDifenichan.getName();
        if (secondaryProperty.getType() == propertyType.BOOL){
            m_property = (boolean) secondaryProperty.getValue();
        }
        else{
            init(propertyDifenichan);
        }
    }

    private void init(propertyDifenichan propertyDifenichan){
        if(propertyDifenichan.isRandom()){
            Random random = new Random();
            m_property = random.nextBoolean();
        }
        else{
            m_property = propertyDifenichan.getInit().getBool();
        }
    }

    public BooleanProperty(EnvironmentDifenichan environmentDifenichan){
        super(propertyType.BOOL);
        m_name = environmentDifenichan.getName();
        if (environmentDifenichan.getType() != expressionType.BOOL){
            //exception
        }
        if(environmentDifenichan.isRandom()){
            Random random = new Random();
            m_property = random.nextBoolean();
        }
        else{
            m_property = environmentDifenichan.getInit().getBool();
        }
    }

    public BooleanProperty(PRDEnvProperty envProperty){
        super(propertyType.BOOL);
        m_name = envProperty.getPRDName();
        m_property = true;    //TODO get from user
    }

    @Override
    public Object getValue() {
        return m_property;
    }
    @Override
    public String getName() {
        return m_name;
    }
    @Override
    public void setProperty(boolean i_property, int currTick) {
        addDeltaTicksChanged(currTick - lastTickChanged);
        lastTickChanged = currTick;
        m_property = i_property;
    }

//    @Override
//    public DTOEnvironmentVariablesValues makeDtoEnvironment(){
//        return new DTOEnvironmentVariablesValues(m_name, m_property);
//    }
}
