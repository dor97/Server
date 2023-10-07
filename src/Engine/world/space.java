package Engine.world;

public class space {
    private boolean isOccupied = false;
    private int m_x, m_y;
    private String entityName = "";

    public space(int x, int y){
        m_x = x;
        m_y = y;
    }
    public void setToOccupied(String entityName){
        isOccupied = true;
        this.entityName = entityName;
    }

    public void setToFree(){
        isOccupied = false;
        this.entityName = "";
    }

    public boolean isOccupied(){
        return isOccupied;
    }

    public int getX(){
        return m_x;
    }

    public int getY(){
        return m_y;
    }

    public String getEntityName(){
        return entityName;
    }
}
