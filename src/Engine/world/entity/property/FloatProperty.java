package Engine.world.entity.property;

import Engine.InvalidValue;
import Engine.generated.PRDEnvProperty;
import Engine.world.expression.expressionType;

import java.io.Serializable;
import java.util.Random;

public class FloatProperty extends Property implements Serializable {

    private String m_name;
    private float m_property;
    private Double m_lowRange, m_highRang;
    private boolean haveRange;
    //private int lastTickChanged = 0;


    public FloatProperty(propertyDifenichan propertyDifenichan) throws InvalidValue{
        super(propertyType.FLOAT);
        m_name = propertyDifenichan.getName();
        haveRange = propertyDifenichan.haveRange();
        if(haveRange) {
            m_lowRange = propertyDifenichan.getLowRange();
            m_highRang = propertyDifenichan.getHighRange();
        }
        init(propertyDifenichan);
   /*     if(propertyDifenichan.getType() != expressionType.FLOAT){
            //exepcen
        }
        if(propertyDifenichan.isRandom()){
            Random random = new Random();
            if(haveRange){
                m_property = (float) (random.nextFloat() * (m_highRang - m_lowRange) + m_lowRange);
            }
            else{
                m_property = random.nextInt() + random.nextFloat();
            }
        }
        else {
            m_property = propertyDifenichan.getInit().getFloat();
            if(haveRange && (m_property > m_highRang || m_property < m_lowRange)){
                throw new InvalidValue("In property " + m_name + " value is out of range");
            }
        }*/
    }

    public FloatProperty(propertyDifenichan propertyDifenichan, PropertyInterface secondaryProperty){
        super(propertyType.FLOAT);
        m_name = propertyDifenichan.getName();
        if(secondaryProperty.getType() == propertyType.FLOAT){
            haveRange = propertyDifenichan.haveRange();
            if(haveRange) {
                m_lowRange = propertyDifenichan.getLowRange();
                m_highRang = propertyDifenichan.getHighRange();
                if((float)secondaryProperty.getValue() <= m_highRang && (float)secondaryProperty.getValue() >= m_lowRange){
                    m_property = (float)secondaryProperty.getValue();
                }
                else{
                    init(propertyDifenichan);;
                }
            }else{
                m_property = (float)secondaryProperty.getValue();
            }
        }else{
            init(propertyDifenichan);
        }
    }

    private void init(propertyDifenichan propertyDifenichan){
        if(propertyDifenichan.isRandom()){
            Random random = new Random();
            if(haveRange){
                m_property = (float) (random.nextFloat() * (m_highRang - m_lowRange) + m_lowRange);
            }
            else{
                m_property = random.nextInt() + random.nextFloat();
            }
        }
        else {
            if(propertyDifenichan.getInit().getType() == expressionType.FLOAT) {
                m_property = propertyDifenichan.getInit().getFloat();
            }else if(propertyDifenichan.getInit().getType() == expressionType.INT){
                m_property = propertyDifenichan.getInit().getInt();
            }else{
                throw new InvalidValue("In property " + m_name + " value is of the wrong type");
            }
            if(haveRange && (m_property > m_highRang || m_property < m_lowRange)){
                throw new InvalidValue("In property " + m_name + " value is out of range");
            }
        }
    }

    public FloatProperty(EnvironmentDifenichan environmentDifenichan) throws InvalidValue{
        super(propertyType.FLOAT);
        m_name = environmentDifenichan.getName();
        haveRange = environmentDifenichan.haveRange();
        if(haveRange) {
            m_lowRange = environmentDifenichan.getLowRange();
            m_highRang = environmentDifenichan.getHighRange();
        }
        if(environmentDifenichan.getType() != expressionType.FLOAT){
            //exepcen
        }
        if(environmentDifenichan.isRandom()){
            Random random = new Random();
            if(haveRange){
                m_property = (float) (random.nextFloat() * (m_highRang - m_lowRange) + m_lowRange);
            }
            else{
                m_property = random.nextInt() + random.nextFloat();
            }
        }
        else {
            if(environmentDifenichan.getInit().getType() == expressionType.FLOAT) {
                m_property = environmentDifenichan.getInit().getFloat();
            }else if(environmentDifenichan.getInit().getType() == expressionType.INT){
                m_property = environmentDifenichan.getInit().getInt();
            }else{
                throw new InvalidValue("In property " + m_name + " value is of the wrong type");
            }
            if(haveRange && (m_property > m_highRang || m_property < m_lowRange)){
                throw new InvalidValue("In property " + m_name + " value is out of range");
            }
        }
    }

    public FloatProperty(PRDEnvProperty envProperty){
        super(propertyType.FLOAT);
        m_name = envProperty.getPRDName();
        m_lowRange = envProperty.getPRDRange().getFrom();
        m_highRang = envProperty.getPRDRange().getTo();
        m_property = 10;    //TODO get from user
    }

    @Override
    public void addToProperty(float add, int currTick){
        if(!haveRange || (m_property + add <= m_highRang && m_property + add >= m_lowRange)) {
            addDeltaTicksChanged(currTick - lastTickChanged);
            lastTickChanged = currTick;
            m_property += add;
        }
    }
    @Override
    public void addToProperty(int add, int currTick){
        if(!haveRange || (m_property + add <= m_highRang && m_property + add >= m_lowRange)) {
            addDeltaTicksChanged(currTick - lastTickChanged);
            lastTickChanged = currTick;
            m_property += add;
        }
    }
    @Override
    public String getName(){
        return m_name;
    }
    @Override
    public Object getValue(){
        return m_property;
    }
    @Override
    public void setProperty(float value, int currTick){
        if(!haveRange || (value <= m_highRang && value >= m_lowRange)) {
            addDeltaTicksChanged(currTick - lastTickChanged);
            lastTickChanged = currTick;
            m_property = value;
        }
    }
    @Override
    public void setProperty(int value, int currTick){
        if(!haveRange || (value <= m_highRang && value >= m_lowRange)){
            addDeltaTicksChanged(currTick - lastTickChanged);
            lastTickChanged = currTick;
            m_property = value;
        }
    }
//    @Override
//    public DTOEnvironmentVariablesValues makeDtoEnvironment(){
//        return new DTOEnvironmentVariablesValues(m_name, m_property);
//    }
}
