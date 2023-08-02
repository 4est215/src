public class RailSlug extends MobileEntity{
    private double baseXVelocity;
    private double baseYVelocity;
    

    public RailSlug(float angleInDegrees, double xPosition, double yPosition, double xVel, double yVel, Faction faction) {
        super(angleInDegrees, xPosition, yPosition, faction, GameSprite.spriteList.get(2));

        lastX = xPosition;
        lastY = yPosition;

        this.xVelocity = xVel;
        this.yVelocity = yVel;

        // yVelocity += cardinalThrust * Math.sin(Math.toRadians(angleInDegrees));
        // xVelocity += cardinalThrust * Math.cos(Math.toRadians(angleInDegrees));
        baseXVelocity = 10 * Math.cos(Math.toRadians(angleInDegrees));
        baseYVelocity = 10 * Math.sin(Math.toRadians(angleInDegrees));

        Main.entityList.add(this);
    }

    // FIXME not taking adeqaute velocity from spaceship for some reason
    @Override
    public void move() {
        lastX = xPosition;
        lastY = yPosition;
        xPosition += baseXVelocity + xVelocity;
        yPosition -= baseYVelocity + yVelocity;
    }

    @Override
    public void checkCollisions() {
        // does nothing cuz slugs penetrate 
    }

    
}
