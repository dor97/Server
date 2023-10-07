package Engine.world.entity.property;

import DTO.DTOEnvironmentVariables;
import DTO.DTOPropertyType;
import Engine.InvalidValue;
import Engine.generated.PRDEnvProperty;
import Engine.world.expression.expression;
import Engine.world.expression.expressionType;

import java.io.Serializable;

public class EnvironmentDifenichan implements Serializable {
    private String m_name;
    private expressionType m_type;
    private Double m_lowRange, m_highRang;
    private Boolean m_randomlyIneceat = true;
    private expression m_init;
    private Boolean m_haveRange = false;

    public EnvironmentDifenichan(PRDEnvProperty p){
        m_name = p.getPRDName();
        m_type = myType(p.getType());
        if(p.getPRDRange() != null) {
            m_haveRange = true;
            m_lowRange = p.getPRDRange().getFrom();
            m_highRang = p.getPRDRange().getTo();
            if(m_highRang < m_lowRange){
                //excepcen
            }
        }
        //m_randomlyIneceat = p.getPRDValue().isRandomInitialize();
        //if(m_randomlyIneceat == true){
        //    return;
        //}
        m_init = new expression();
        //m_init.convertValueInString(p.getPRDValue().getInit());
        //if(m_type != m_init.getType()){
        //   //exepcen
        //}

    }

    public EnvironmentDifenichan(EnvironmentDifenichan p){
        m_name = p.getName();
        m_type = p.getType();
        if(p.haveRange()) {
            m_haveRange = true;
            m_lowRange = p.getLowRange();
            m_highRang = p.getHighRange();
            if(m_highRang < m_lowRange){
                //excepcen
            }
        }
        //m_randomlyIneceat = p.getPRDValue().isRandomInitialize();
        //if(m_randomlyIneceat == true){
        //    return;
        //}
        m_init = new expression();
        if(!m_randomlyIneceat) {
            m_init.convertValueInString(p.getInit().getValue().toString());
        }
        //m_init.convertValueInString(p.getPRDValue().getInit());
        //if(m_type != m_init.getType()){
        //   //exepcen
        //}

    }

    public expressionType getType(){
        return m_type;
    }

    public double getLowRange(){
        return m_lowRange;
    }

    public double getHighRange(){
        return m_highRang;
    }

    public expression getInit(){
        return m_init;
    }

    public boolean isRandom(){
        return m_randomlyIneceat;
    }

    public String getName(){
        return m_name;
    }

    private expressionType myType(String t){
        if(t.equals("decimal")){
            return expressionType.INT;
        } else if (t.equals("float")) {
            return expressionType.FLOAT;
        } else if (t.equals("string")) {
            return expressionType.STRING;
        } else if (t.equals("boolean")) {
            return expressionType.BOOL;
        }
        else{
            return  null;
            //exepcen
        }
    }

    public DTOEnvironmentVariables makeDtoEnvironment(){
        DTOPropertyType type;
        if(m_type == expressionType.INT) {
            type = DTOPropertyType.INT;
        } else if (m_type == expressionType.FLOAT) {
            type = DTOPropertyType.FLOAT;
        } else if (m_type == expressionType.STRING) {
            type = DTOPropertyType.STRING;
        } else {
            type = DTOPropertyType.BOOL;
        }
        DTOEnvironmentVariables DTO;
        if(m_haveRange){
            DTO = new DTOEnvironmentVariables(m_name, type, m_haveRange, m_highRang, m_lowRange);
        }
        else{
            DTO = new DTOEnvironmentVariables(m_name, type, m_haveRange);
        }

        DTO.putData("Name", m_name);
        DTO.putData("Type", type.toString());
        DTO.putData("HaveRange", m_haveRange.toString());
        if(m_haveRange){
            DTO.putData("lowRange", m_lowRange.toString());
            DTO.putData("highRange", m_highRang.toString());
        }
        DTO.putData("Random", m_randomlyIneceat.toString());


        return DTO;
    }

    public void setWithString(String value){
        m_init.convertValueInString(value);
        if(m_init.getType() == expressionType.INT && m_type == expressionType.FLOAT){
            m_init.setValue((float)(m_init.getInt()));
        }

        if(m_init.getType() != m_type){
            throw new InvalidValue("In environment variabale " + m_name + " got wrong value type");
        }

        if(m_haveRange){
            if(m_init.getType() == expressionType.INT){
                if(m_init.getInt() > m_highRang || m_init.getInt() < m_lowRange){
                    throw new InvalidValue("In environment variabale " + m_name + " got value out of range(" + m_lowRange + "-" + m_highRang + ")");
                }
            } else if (m_init.getType() == expressionType.FLOAT) {
                if(m_init.getFloat() > m_highRang || m_init.getFloat() < m_lowRange){
                    throw new InvalidValue("In environment variabale " + m_name + " got value out of range("+ m_lowRange + "-" + m_highRang + ")");
                }
            }else {
                throw new InvalidValue("In environment variabale " + m_name + " of type " + m_type + " got range");
            }
        }
        m_randomlyIneceat = false;
    }

    public void setWithDto(DTOEnvironmentVariables environmentVariables) throws InvalidValue{
        setWithString(environmentVariables.getValue());
//        m_init.convertValueInString(environmentVariables.getValue());
//
//        if(m_init.getType() != getDtoType(environmentVariables.getVariableType())){
//            throw new InvalidValue("In environment variabale " + m_name + " got wrong value type");
//        }
//
//        if(m_haveRange){
//            if(m_init.getType() == expressionType.INT){
//                if(m_init.getInt() > m_highRang || m_init.getInt() < m_lowRange){
//                    throw new InvalidValue("In environment variabale " + m_name + " got value out of range");
//                }
//            } else if (m_init.getType() == expressionType.FLOAT) {
//                if(m_init.getFloat() > m_highRang || m_init.getFloat() < m_lowRange){
//                    throw new InvalidValue("In environment variabale " + m_name + " got value out of range");
//                }
//            }else {
//                throw new InvalidValue("In environment variabale " + m_name + " of type " + m_type + " got range");
//            }
//        }
//        m_randomlyIneceat = false;
    }

    public expressionType getDtoType(DTOPropertyType propertyType){
        if(propertyType == DTOPropertyType.INT){
            return expressionType.INT;
        } else if (propertyType == DTOPropertyType.FLOAT) {
            return expressionType.FLOAT;
        } else if (propertyType == DTOPropertyType.STRING) {
            return expressionType.STRING;
        }else {
            return expressionType.BOOL;
        }
    }

    public boolean haveRange(){
        return m_haveRange;
    }
}
