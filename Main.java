//this will do the math for the game and give the server the results to send to the clients

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends WindowAdapter {
    static ArrayList<GameEntity> entityList = new ArrayList<>();
    static Server server; 
    static Client client;

    public static void addNonPlayers(){
        entityList.add(new SimpleAsteriod(0, 15, 15, Faction.NEUTRAL, GameSprite.spriteList.get(1)));
    }


    public static void initSprites() throws IOException{ // need to make image array for each GameSprite
        // Image[] playerShipImages = {new ImageIcon("src\\player_ship.png").getImage()};
        Image[] playerShipImages = {ImageIO.read(Main.class.getResourceAsStream("player_ship.png"))};
        new GameSprite("PlayerSpaceShip", playerShipImages);

        // Image[] asteriodImages = {new ImageIcon("src\\game_asteriod.png").getImage()};
        Image[] asteriodImages = {ImageIO.read(Main.class.getResourceAsStream("game_asteriod.png"))};
        new GameSprite("Asteriod", asteriodImages);

        Image[] railSlugImages = {ImageIO.read(Main.class.getResourceAsStream("rail_slug.png"))};
        new GameSprite("Railslug", railSlugImages);
    }
    
    public static void main(String[] args) throws IOException{
        System.out.println("Starting game");
        initSprites();
        addNonPlayers();

        int testing = JOptionPane.showConfirmDialog(null, "Quick test?");
        if (testing == 0) {
            try {
                server = new Server(new DatagramSocket(2222));
                client = new Client("TestName", InetAddress.getByName("localhost"), 2222);
            } catch (SocketException | UnknownHostException e) {
                e.printStackTrace();
            }
            new Thread(client).start();
            new GameFrame(new GamePanel(client));
            new GameDriver();
        }
        
        else{
            int a = JOptionPane.showConfirmDialog(null, "Start server?");
            if (a == 0){
                try {
                    int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port"));
                    server = new Server(new DatagramSocket(port)); 
                    new GameDriver();
                } catch (HeadlessException | SocketException e) {
                    e.printStackTrace();
                }
            }

            int b = JOptionPane.showConfirmDialog(null, "Start Client?");
            if (b == 0){
                try {
                    String userName = JOptionPane.showInputDialog("Enter Username");
                    InetAddress address = InetAddress.getByName(JOptionPane.showInputDialog("Enter IP Address"));
                    int port = Integer.parseInt(JOptionPane.showInputDialog("Enter Port"));
                    // TODO start the Client for this machine 
                    client = new Client(userName, address, port);
                    new Thread(client).start();

                } catch (HeadlessException | UnknownHostException e) {
                    e.printStackTrace();
                }

                //set up the game
                new GameFrame(new GamePanel(client));
            }
        }
        
    }

}
