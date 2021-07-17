package src;


/**
 * InnerModel Tile
 */
public class Tile {
    private boolean lightOn;

    public Tile(boolean lightState) {
        this.lightOn = lightState;
    }

    public void setLightState(boolean newLightState) {
        this.lightOn = newLightState;
    }

    public boolean getLightState() {
        return lightOn;
    }

    public boolean isLightOn() {
        return lightOn;
    }    
}