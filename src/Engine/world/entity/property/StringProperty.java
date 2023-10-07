package Engine.world.entity.property;

import Engine.generated.PRDEnvProperty;
import Engine.world.expression.expressionType;

import java.io.Serializable;
import java.util.Random;

public class StringProperty extends Property implements Serializable {

    private String m_name;
    private String m_property;
    //private int lastTickChanged = 0;


    public StringProperty(propertyDifenichan propertyDifenichan) {
        super(propertyType.STRING);
        m_name = propertyDifenichan.getName();
        if (propertyDifenichan.getType() != expressionType.STRING){
            //exception
        }
        init(propertyDifenichan);
/*        if(propertyDifenichan.isRandom()){
            final int maxTabsInRandomString = 50;
            m_property = generateRandomString(maxTabsInRandomString);
        }
        else{
            m_property = propertyDifenichan.getInit().getString();
        }*/
    }

    public StringProperty(propertyDifenichan propertyDifenichan, PropertyInterface secondaryProperty){
        super(propertyType.STRING);
        m_name = propertyDifenichan.getName();
        if(secondaryProperty.getType() == propertyType.STRING){
            m_property = (String) secondaryProperty.getValue();
        }
        else{
            init(propertyDifenichan);
        }
    }
    private void init(propertyDifenichan propertyDifenichan){
        if(propertyDifenichan.isRandom()){
            final int maxTabsInRandomString = 50;
            m_property = generateRandomString(maxTabsInRandomString);
        }
        else{
            m_property = propertyDifenichan.getInit().getString();
        }
    }
    public StringProperty(EnvironmentDifenichan environmentDifenichan){
        super(propertyType.STRING);
        m_name = environmentDifenichan.getName();
        if (environmentDifenichan.getType() != expressionType.STRING){
            //exception
        }
        if(environmentDifenichan.isRandom()){
            final int maxTabsInRandomString = 50;
            m_property = generateRandomString(maxTabsInRandomString);
        }
        else{
            m_property = environmentDifenichan.getInit().getString();
        }
    }

    public StringProperty(PRDEnvProperty envProperty){
        super(propertyType.STRING);
        m_name = envProperty.getPRDName();
        m_property = "";    //TODO get from user
    }

    private String generateRandomString(int maxTabsInRandomString){
        Random random = new Random();
        int numOfTabs = random.nextInt(maxTabsInRandomString + 1);
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?,_-.()";
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < numOfTabs; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomString.append(randomChar);
        }
        return randomString.toString();
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
    public void setProperty(String i_property, int currTick) {
        addDeltaTicksChanged(currTick - lastTickChanged);
        lastTickChanged = currTick;
        m_property = i_property;
    }

//    @Override
//    public DTOEnvironmentVariablesValues makeDtoEnvironment(){
//        return new DTOEnvironmentVariablesValues(m_name, m_property);
//    }
}
