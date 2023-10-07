package Engine.world;

public class consistencyAndAvr {
    private String name;
    private Float consistency;
    private Float avr = null;

    public consistencyAndAvr(String name, Float consistency, Object avr){
        this.consistency = consistency;
        this.name = name;
        if(avr instanceof Integer){
            this.avr = (Float) ((float)((Integer) avr));
        }
        if(avr instanceof Float) {
            this.avr = (Float) avr;
        }
    }

    public Float getConsistency(){
        return consistency;
    }

    public Float getAvr(){

        return avr;
    }

    public String getName(){
        return name;
    }
}
