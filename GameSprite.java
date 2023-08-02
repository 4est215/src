import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class GameSprite {
    // static arrayList may be better for sending the id of the sprite so the client knows what to display
    static ArrayList<GameSprite> spriteList = new ArrayList<>();
    private String spriteName; 
    private Image[] idleImages;
    private int idleImageCount;
    private int spriteWidth;
    private int spriteHeight;
    // private double angleInDegrees;


    public GameSprite(String spriteName,Image[] images){
        this.spriteName = spriteName;
        idleImages = images;
        idleImageCount = 0;
        spriteWidth = images[0].getWidth(null);
        spriteHeight = images[0].getHeight(null);

        spriteList.add(this);
    }

    public Image getNextImage(){
        if(idleImageCount >= idleImages.length){
            idleImageCount = 0;
        }
        int temp = idleImageCount;
        idleImageCount++;
        return idleImages[temp];
    }

    @Override
    public String toString(){
        return spriteName;
    }

    public void drawSprite(Graphics g, double reletiveXPos, double reletiveYPos, double angleInDegrees) {
        //TODO should add the spite to the screen, will exapnd on later for differnt animations
        Image currentImage = getNextImage();
        // Image currentImage = idleImages[0]; // this only uses the first image
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform transform = new AffineTransform();

        //these postions will be calculated in client using info recived from server/clientHandler
        transform.translate(reletiveXPos, reletiveYPos);
        
        // FIXME rotates off center and wrong way
        transform.rotate(Math.toRadians(-angleInDegrees), (currentImage.getWidth(null)/2), (currentImage.getHeight(null)/2)); 

        g2d.drawImage(currentImage, transform, null);
        


        // test stuff, the problem is the image somehow
        /* Graphics2D g2d = (Graphics2D)g;
        Image testImage = new ImageIcon("SpaceOnline\\src\\game_asteriod.png").getImage();
        g2d.setColor(Color.MAGENTA);
        AffineTransform at = new AffineTransform();
        at.translate(100, 100);
        g2d.drawImage(testImage, at,null); */
        
    }

    public int getWidth() {
        return spriteWidth;
    }

    public int getHeight() {
        return spriteHeight;
    }
}
