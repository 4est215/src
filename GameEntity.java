import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public abstract class GameEntity { //FIXME shouldnt extend rectangle
    //fields----------------------------------------------------------------------------
    protected static int idCount = 0;
    protected int entityID;
    protected float angleInDegrees;
    protected double xPosition;
    protected double yPosition;
    protected int screenXPosition;
    protected int screenYPosition;
    protected Faction faction;
    protected GameSprite sprite;

    // getters and setters
    public static int getIdCount() {
        return idCount;
    }
    public static void setIdCount(int idCount) {
        GameEntity.idCount = idCount;
    }
    public int getEntityID() {
        return entityID;
    }
    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }
    public float getAngleInDegrees() {
        return angleInDegrees;
    }
    public void setAngleInDegrees(float angleInDegrees) {
        this.angleInDegrees = angleInDegrees;
    }
    public double getxPosition() {
        return xPosition;
    }
    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }
    public double getyPosition() {
        return yPosition;
    }
    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }
    public int getScreenXPosition() {
        return screenXPosition;
    }
    public void setScreenXPosition(int screenXPosition) {
        this.screenXPosition = screenXPosition;
    }
    public int getScreenYPosition() {
        return screenYPosition;
    }
    public void setScreenYPosition(int screenYPosition) {
        this.screenYPosition = screenYPosition;
    }
    public Faction getFaction() {
        return faction;
    }
    public void setFaction(Faction faction) {
        this.faction = faction;
    }
    public GameSprite getSprite() {
        return sprite;
    }
    public void setSprite(GameSprite sprite) {
        this.sprite = sprite;
    }

    //constructor----------------------------------------------------------------------------
    public GameEntity(float angleInDegrees, double xPosition, double yPosition, Faction faction, GameSprite sprite) {

        this.entityID = idCount++;

        this.angleInDegrees = angleInDegrees;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.faction = faction;

        this.sprite = sprite;
    }
    
    //methods--------------------------------------------------------------------------------
    public abstract void move();

    // public abstract void draw(Graphics g);

    public abstract void checkCollisions();

    public abstract void tick();
    
    public double getCenterX() {
        return xPosition; //TODO make this return center of entity
    }

    public double getCenterY() {
        return yPosition;
    }

    

}
