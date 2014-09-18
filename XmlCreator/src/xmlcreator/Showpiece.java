/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmlcreator;

/**
 *
 * @author Пользователь
 */
public class Showpiece {
    
    public Showpiece(int x, int y,double phi, String name, String description, String audio, int time) {
        this.id=countId;
        countId++;
        this.x = x;
        this.y = y;
        this.phi=phi;
        this.name = name;
        this.description = description;
        this.audio = audio;
        this.time = time;
    }

    private static int countId=0;
    private int id;
    private int x,y;
    private double phi;
    private String name;
    private String description;
    private String audio;
    private int time;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the audio
     */
    public String getAudio() {
        return audio;
    }

    /**
     * @param audio the audio to set
     */
    public void setAudio(String audio) {
        this.audio = audio;
    }

    /**
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the phi
     */
    public double getPhi() {
        return phi;
    }

    /**
     * @param phi the phi to set
     */
    public void setPhi(double phi) {
        this.phi = phi;
    }

}
