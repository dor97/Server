package Engine.world.expression;

import java.io.Serializable;

public class expression implements Serializable {
    private Integer valueInt = null;
    private Float valueFloat = null;
    private String valueString = null;
    private Boolean valueBool = null;
    private expressionType m_type = null;

    public expression(){

    }
    public expression(int value){
        valueInt = value;
        m_type = expressionType.INT;
    }
    public expression(Object value){
        if(value instanceof Integer){
            valueInt = (Integer) value;
            m_type = expressionType.INT;
        }
        if(value instanceof Float){
            valueFloat = (Float) value;
            m_type = expressionType.FLOAT;
        }
        if(value instanceof String){
            valueString = (String)value;
            m_type = expressionType.STRING;
        }
        if(value instanceof  Boolean){
            valueBool = (Boolean)value;
            m_type = expressionType.BOOL;
        }
        //exepcen
    }

    public void setValue(Object value){
        if(value instanceof Integer){
            valueInt = (Integer) value;
            m_type = expressionType.INT;
        }
        if(value instanceof Float){
            valueFloat = (Float) value;
            m_type = expressionType.FLOAT;
        }
        if(value instanceof String){
            valueString = (String)value;
            m_type = expressionType.STRING;
        }
        if(value instanceof  Boolean){
            valueBool = (Boolean)value;
            m_type = expressionType.BOOL;
        }
        //exepcen
    }

    public void setValue(int value){
        valueInt = value;
        m_type = expressionType.INT;
    }
    public void setValue(float value){
        valueFloat = value;
        m_type = expressionType.FLOAT;
    }
    public void setValue(boolean value){
        valueBool = value;
        m_type = expressionType.BOOL;
    }
    public void setValue(String value){
        valueString = value;
        m_type = expressionType.STRING;
    }

    public void convertValueInString(String value){
        try {
            setValue(Integer.parseInt(value));
            return; // No need to proceed further
        } catch (NumberFormatException e) {
            // Not an int
        }

        // Try to convert to float
        try {
            setValue(Float.parseFloat(value));
            return; // No need to proceed further
        } catch (NumberFormatException e) {
            // Not a float
        }

        // Try to convert to boolean
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            setValue(Boolean.parseBoolean(value));
            return; // No need to proceed further
        }
        setValue(value);
    }

    public Object getValue(){
        switch (m_type){
            case INT:
                return valueInt;
            case FLOAT:
                return valueFloat;
            case STRING:
                return valueString;
            case BOOL:
                return valueBool;
            default:
                return null;
        }
    }

    public int getInt(){
        return valueInt;
    }
    public float getFloat(){
        return valueFloat;
    }
    public String getString() {return valueString;}
    public boolean getBool(){return valueBool;}
    public expressionType getType(){
        return m_type;
    }
    @Override
    public String toString(){
        return getValue().toString();
    }
}
