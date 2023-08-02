import java.awt.Graphics;

public abstract class MobileEntity extends GameEntity{
    protected double xVelocity;
    protected double yVelocity;
    protected double angularThrust;
    protected double cardinalThrust;
    protected double driveSpeed;
    protected double breakForce;
    protected double angularVelocity;//clockwise is negative
    protected double lastX;
    protected double lastY;
    

    public MobileEntity(float angleInDegrees, double xPosition, double yPosition,
            Faction faction, GameSprite sprite) {
        super(angleInDegrees, xPosition, yPosition, faction, sprite);
        
    }

    @Override
    public abstract void move();

    // @Override
    // public abstract void draw(Graphics g);

    @Override
    public abstract void checkCollisions();

    @Override
    public void tick() {
        move();
        checkCollisions();
    }
    
}