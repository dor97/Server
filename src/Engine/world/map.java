package Engine.world;

import Engine.world.entity.Entity;
import DTO.space;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class map {
    private space[][] m_map;
    //private List<space> freeSpaces;
    private int m_rows, m_cols;
    private Random random = new Random();

    public map(int cols, int rows){
        m_map = new space[rows][cols];
        m_rows = rows;
        m_cols = cols;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m_map[i][j] = new space(i, j);
            }
        }
    }

    public space[][] getMap(){
        return m_map;
    }

    public Integer getRows(){
        return m_rows;
    }

    public Integer getCols(){
        return m_cols;
    }
    public void setLocations(final List<Entity> entities){
        ArrayList<space> freeSpaces = new ArrayList<>();
        for (int i = 0; i < m_rows; i++) {
            for (int j = 0; j < m_cols; j++) {
                if(!m_map[i][j].isOccupied()){
                    freeSpaces.add(m_map[i][j]);
                }
            }
        }
        entities.stream().forEach(entity -> setLocation(entity, freeSpaces));
    }

    private void setLocation(Entity entity, ArrayList<space> spaces){
        int index = random.nextInt(spaces.size());
        //int x = index / m_cols, y = index % m_cols;
        spaces.get(index).setToOccupied(entity.getName());
        if(entity.getPosition() != null){
            entity.getPosition().setToFree();
        }
        entity.setPosition(spaces.get(index));
        spaces.remove(index);
    }

    public void moveEntities(final List<Entity> entities){
        entities.stream().forEach(entity -> moveEntity(entity));
    }

    private void moveEntity(Entity entity){
        //int x = entity.getX(), y = entity.getY();
        space entityPosition = entity.getPosition();
        int x = entityPosition.getX() + m_rows, y = entityPosition.getY() + m_cols;
        ArrayList<space> freeSpacesNearEntity = new ArrayList<>();
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                if(!m_map[(x - 1 + 2*i) % m_rows][(y - 1 + 2*j) % m_cols].isOccupied()) {
                    freeSpacesNearEntity.add(m_map[(x - 1 + 2 * i) % m_rows][(y - 1 + 2 * j) % m_cols]);
                }
            }
        }

        if(freeSpacesNearEntity.size() == 0){
            return;
        }
        entity.getPosition().setToFree();
        Integer index = random.nextInt(freeSpacesNearEntity.size());
        freeSpacesNearEntity.get(index).setToOccupied(entity.getName());
        entity.setPosition(freeSpacesNearEntity.get(index));

    }

    public void deleteEntities(List<Entity> entities){
        if(entities == null){
            return;
        }
        entities.stream().forEach(entity -> m_map[entity.getPosition().getX()][entity.getPosition().getY()].setToFree());
    }

    public void createEntities(List<Entity> entities){
        if(entities == null){
            return;
        }
        setLocations(entities);
    }


}
