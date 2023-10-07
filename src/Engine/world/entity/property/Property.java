package Engine.world.entity.property;

import DTO.DTOEnvironmentVariablesValues;
import org.omg.CORBA.INVALID_ACTIVITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Property implements PropertyInterface, Serializable {
    private propertyType m_type;
    protected int lastTickChanged = 0;
    private List<Integer> deltaTicksChangedValue = new ArrayList<>();

    public Property(propertyType type){
        m_type = type;
    }
    @Override
    public propertyType getType(){
        return m_type;
    }
    @Override
    public void addToProperty(int add, int currTick) {
        throw new INVALID_ACTIVITY("Trying to add decimal value to property of type " + m_type);
    }
    @Override
    public void addToProperty(float add, int currTick) {
        throw new INVALID_ACTIVITY("Trying to add float value to property of type " + m_type);
    }
    @Override
    public String getName() {
        return null;
    }
    @Override
    public Object getValue() {
        return null;
    }
    @Override
    public void setProperty(int v, int currTick) {
        throw new INVALID_ACTIVITY("Trying to set value of property type " + m_type + " with decimal value");
    }
    @Override
    public void setProperty(float v, int currTick) {
        throw new INVALID_ACTIVITY("Trying to set value of property type " + m_type + " with float value");
    }
    @Override
    public void setProperty(boolean v, int currTick) {
        throw new INVALID_ACTIVITY("Trying to set value of property type " + m_type + " with boolean value");
    }
    @Override
    public void setProperty(String v, int currTick) {
        throw new INVALID_ACTIVITY("Trying to set value of property type " + m_type + " with string value");
    }

    @Override
    public DTOEnvironmentVariablesValues makeDtoEnvironment(){
        return new DTOEnvironmentVariablesValues(getName(), getValue());
    }
    @Override
    public int getLastTickChanged(){
        return lastTickChanged;
    }
    @Override
    public void addDeltaTicksChanged(Integer delta){
        deltaTicksChangedValue.add(delta);
    }
    @Override
    public List<Integer> getDeltaTicksChangedValue(){
        return deltaTicksChangedValue;
    }

    @Override
    public Float getDeltaTicksChangedValueAve(){
        Float sum = 0f;
        for(Integer num : deltaTicksChangedValue){
            sum += num;
        }
        return sum / deltaTicksChangedValue.size();
    }

}
