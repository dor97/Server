package Engine;

public class InvalidValue extends RuntimeException{
    private String entityName = null;
    public InvalidValue(String message) {
        super(message);
    }

    public void setEntityName(String name){
        entityName = name;
    }

    public String getEntityName(){
        return entityName;
    }
}
