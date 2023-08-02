import java.awt.Color;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;


    public GameFrame(GamePanel gamePanel){
        //TODO set up the frame
        super("Game Name goes here");
        this.gamePanel = gamePanel;
        this.add(gamePanel);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        this.setVisible(true);
        this.setLocationRelativeTo(null);
        
    }
}
