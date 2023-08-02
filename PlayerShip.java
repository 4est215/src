import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.text.html.parser.Entity;

import java.awt.Graphics2D;
import java.awt.Point;

public class PlayerShip extends MobileEntity{
    /* contains...
    protected static int idCount = 0;
    protected int entityID;
    protected float angleInDegrees;
    protected double xPosition;
    protected double yPosition;
    protected int screenXPosition;
    protected int screenYPosition;
    protected Faction faction;
    protected String filePath;
    protected Image sprite;

    protected double xVelocity;
    protected double yVelocity;
    protected double angularThrust;
    protected double cardinalThrust;
    protected double driveSpeed;
    protected double angularVelocity;//clockwise is negative
    */

    private boolean dead = false;
    //buttons
    // private ArrayList<Boolean> inputList = new ArrayList<>(7);
    private boolean[] inputList = new boolean[8];
    // private boolean torqueClockwise = false;
    // private boolean torqueCounterCl = false;
    // private boolean forwardForce = false;

    // cooldown in ticks
    private int railcannonCooldown = 100;

    public boolean[] getInputList() {
        return inputList;
    }

    public PlayerShip(float angleInDegrees, double xPosition, double yPosition, Faction faction, GameSprite sprite) {

        super(angleInDegrees, xPosition, yPosition, faction, sprite);
        
        angularThrust = 0.05d;
        cardinalThrust = 0.1d;
        driveSpeed = 0.3d;
        breakForce = 0.9;


        //populates arraylist
        inputList[0] = false;
        inputList[1] = false;
        inputList[2] = false;
        inputList[3] = false;
        inputList[4] = false;
        inputList[5] = false;
        inputList[6] = false;
        inputList[7] = false;


        Main.entityList.add(this);
    }

    // private Boolean[] pressedKeys = {forwardForce, leftForce, rightForce, backwardForce, clockwiseTorque, counterClockwiseTorque};
    @Override
    public void move() {
        // last postion
        lastX = xPosition;
        lastY = yPosition;

        //rotaion
        if(inputList[4]) //clockwiseForce
            angularVelocity -= angularThrust;
        if(inputList[5]) //counterClockwiseForce
            angularVelocity += angularThrust;

        angleInDegrees += angularVelocity;
        if(angleInDegrees <= -360)
            angleInDegrees += 360;
        if(angleInDegrees >= 360)
            angleInDegrees -= 360;

        //cardinal FIXME this all needs testing and probaly fixing
        if(inputList[0]){ //forwardForce - w
            xVelocity += cardinalThrust * Math.cos(Math.toRadians(angleInDegrees));
            yVelocity += cardinalThrust * Math.sin(Math.toRadians(angleInDegrees)); //with this as += the coordinates are right but the display is wrong, perhaps its how reletive position is calculated
        }
        if(inputList[1]){ //leftForce - a
            xVelocity -= cardinalThrust * Math.sin(Math.toRadians(angleInDegrees));
            yVelocity += cardinalThrust * Math.cos(Math.toRadians(angleInDegrees));
        }
        if(inputList[2]){ //backforce - s
            yVelocity -= cardinalThrust * Math.sin(Math.toRadians(angleInDegrees));
            xVelocity -= cardinalThrust * Math.cos(Math.toRadians(angleInDegrees));
        }
        if(inputList[3]){ //rightforce - d
            yVelocity -= cardinalThrust * Math.cos(Math.toRadians(angleInDegrees));
            xVelocity += cardinalThrust * Math.sin(Math.toRadians(angleInDegrees)); //with this as += the coordinates are right but the display is wrong, perhaps its how reletive position is calculated
        }
        if(inputList[6]){// breakforce - b
            if(angularVelocity < 1 && angularVelocity < -1){
                angularVelocity = 0;
            }
            else {
                angularVelocity = angularVelocity * breakForce;
            }
            if(xVelocity < 1 && xVelocity < -1){
                xVelocity = 0;
            }
            else {
                xVelocity = xVelocity * breakForce;
            }
            if(yVelocity < 1 && yVelocity < -1){
                yVelocity = 0;
            }
            else {
                yVelocity = yVelocity * breakForce;
            }
        } 

        // setting new position
        xPosition += xVelocity;
        yPosition -= yVelocity;    
        
        
        // firing railcannon
        if(inputList[7]){
            if(railcannonCooldown < 1){
                fireRailCannon();
                railcannonCooldown = 100;
            }
        }
        railcannonCooldown--;
    }
    
    // TODO very primitive
    // TODO implement collisions for moving objects, may need hitbox field
    @Override
    public void checkCollisions() {
        for (GameEntity e : Main.entityList) {
            if(e.getFaction() != faction){
                // this just makes it hard to "crash", needs more functionallity
                if(Point.distance(xPosition, yPosition, e.xPosition, e.yPosition) < 50){
                    xPosition = lastX;
                    yPosition = lastY;
                }
            }
        }
    }


    // fires a railslug with the angle of the ship and added velocities
    public void fireRailCannon(){
        new RailSlug(angleInDegrees, xPosition, yPosition, xVelocity, yVelocity, faction);
    }
    


    
    //idk if we need this server side since theres no visual
    // @Override
    // public void draw(Graphics g) {
    //     Graphics2D g2d = (Graphics2D)g;
    //     AffineTransform tr = new AffineTransform();
    //     tr.translate((MyPanel.GAME_WIDTH/2) - (width/2) , (MyPanel.GAME_HEIGHT/2) -(height/2));
    //     tr.rotate(Math.toRadians(-angleInDegrees), sprite.getWidth(null)/2, sprite.getHeight(null)/2);
    //     g2d.drawImage(sprite, tr, null);
    // }
    // public void draw(Graphics g){}

    
}