import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    // fields -----------------------------------------------
    static final int GAME_WIDTH = 1600; //may change to full screen
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * (5f/9f));//change for different dimensions
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    private boolean running = true; // probably not nessary
    private Image image;
    private Graphics graphics;
    // private GameSprite[] sprites; //may be easier as arrayList
    private Client client;

    public GamePanel(Client client){ //TODO add args for *sprites, and client
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        this.client = client;

        new Thread(this).start();
    }

    public void paint(Graphics g){
        //TODO figure out what this does
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }

    public void draw(Graphics g){ //FIXME never called, needs to be called somehow (game loop?)
        // TODO draws all the sprites
        // FIXME this dont work probably
        String[][] drawSprites = Client.spritesToDraw;

        //test print
        String printString = new String("[");
        for (String[] i : drawSprites){
            printString += Arrays.toString(i) + ",";
        }
        printString += "]";
        // System.out.println("GamePanel: drawSpites array: " + printString);


        if (drawSprites != null) {
            // for player sprite TODO test this
            String[] playerArray = drawSprites[0];
            drawSprites = Arrays.copyOfRange(drawSprites, 1, drawSprites.length);
            String playerSprite = playerArray[0]; // throws nullPointerException sometiems for some reason
            for (GameSprite gs : GameSprite.spriteList){
                if(gs.toString().equals(playerSprite)){ //does this work
                    gs.drawSprite(g, GAME_WIDTH/2, GAME_HEIGHT/2, Double.parseDouble(playerArray[3])); //x,y,theta
                    // System.out.println("GamePanel:");
                }
            }

            double playerX = Double.parseDouble(playerArray[1]);
            double playerY = Double.parseDouble(playerArray[2]);
            double offsetX = GAME_WIDTH/2 - playerX;
            double offsetY = GAME_HEIGHT/2 - playerY;

            for(String[] s : drawSprites) {
                String spriteName = s[0];
                for (GameSprite gs : GameSprite.spriteList){
                    if(gs.toString().equals(spriteName)){ //does this work
                        gs.drawSprite(g, Double.parseDouble(s[1]) + offsetX, Double.parseDouble(s[2]) + offsetY, Double.parseDouble(s[3])); //x,y,theta
                        // System.out.println("GamePanel:");
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        //client side game loop
        // TODO review and correct this game loop
        while(running){
            long time = System.currentTimeMillis();
            while(System.currentTimeMillis() - time > 50){}
            
            repaint();
        }    
    }
    
    // inner class ---------------------------------------
    class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            client.keyPressed(e);
            System.out.println("GamePanel: key pressed");
        }
        public void keyReleased(KeyEvent e){
            client.keyReleased(e);
            System.out.println("GamePanel: key relesed");
        }
    }


}
