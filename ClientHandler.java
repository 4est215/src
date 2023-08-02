import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientHandler implements Runnable{ // doesnt need to be runnable if the runnable server calls its methods
    
    private String clientsName;
    private PlayerShip playerControlledEntity;
    private DatagramSocket sendingSocket;
    private InetAddress clientsAddress;
    private int clientsPort;
    private DatagramSocket recivingSocket;
    private byte[] receiveingBuffer = new byte[1024];
    private DatagramPacket recivingPacket = new DatagramPacket(receiveingBuffer, 1024);
    private String[] infoArray;
    private byte[] sendingArray;

    //needs booleans for buttons pressed
    private Boolean forwardForce;
    private Boolean leftForce;
    private Boolean rightForce;
    private Boolean backwardForce;
    private Boolean clockwiseTorque;
    private Boolean counterClockwiseTorque;
    private Boolean autoStopping;
    //probaaly dont need array
    // private Boolean[] pressedKeys = {forwardForce, leftForce, rightForce, backwardForce, clockwiseTorque, counterClockwiseTorque};

    public GameEntity getPlayerControlledEntity(){
        return playerControlledEntity;
    }

    public ClientHandler(String clientsName, PlayerShip playerControlledEntity, InetAddress clientsAddress, int clientsPort, DatagramSocket recivingSocket) {
        this.clientsName = clientsName;
        this.playerControlledEntity = playerControlledEntity;
        this.clientsAddress = clientsAddress;
        this.clientsPort = clientsPort;
        this.recivingSocket = recivingSocket;

        try {
            sendingSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //sends the client the new unique port it will send to avoid interference (hopefully) 
        String newPortString = new String(Integer.toString(recivingSocket.getLocalPort()));
        try {
            sendingSocket.send(new DatagramPacket(newPortString.getBytes(), newPortString.getBytes().length, clientsAddress, clientsPort));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ClientHandler: Port sent back");
        new Thread(this).start();
    }




    

    public void setSendingArray(){ //find the nearby entities and primitvly sends the info to the client
        // TODO should be a tick() in PlayerShip
        // playerControlledEntity.move();
        // playerControlledEntity.checkCollisions();

        double playerX = playerControlledEntity.getCenterX();
        double playerY = playerControlledEntity.getCenterY();

        String stringToSend = new String(playerControlledEntity.getSprite().toString() + "," + playerX + "," + playerY + "," + playerControlledEntity.getAngleInDegrees() + ":");
        for(GameEntity e : Main.entityList){
            if (e != playerControlledEntity){    
                double entityX = e.getCenterX();
                double entityY = e.getCenterY();
                // stringToSend += playerControlledEntity.getSprite().toString() + "," + 100d + "," + 100d + ":"; //TODO make client display the players ship
                if(Point2D.distance(entityX, entityY, playerX, playerY) <= 2000){ //if within units pixles
                    stringToSend += e.getSprite().toString() + "," + roundHundreth(entityX) + "," + roundHundreth(entityY) + "," + roundHundreth(e.getAngleInDegrees()) +":";
                }
            }
        }

        stringToSend = stringToSend.substring(0, stringToSend.length()-1).trim(); //removes extraneous colon
        sendingArray = stringToSend.getBytes();
    }


    public void sendToClient() {
        setSendingArray(); 
        try {
            sendingSocket.send(new DatagramPacket(sendingArray, sendingArray.length, clientsAddress, clientsPort));
            System.out.println("Client Handler sent to client: (" + clientsAddress + "," + clientsPort + "):" + new String(sendingArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This works
    // sets inputList of the playerControlledEntity
    public void setInputBooleans(Boolean[] booleans) {
        boolean[] entityInputList = playerControlledEntity.getInputList();
        for(int i=0; i<booleans.length; i++){
            entityInputList[i] =  booleans[i];
        }
        // System.out.println("Client Handler: entities booleans : " + Arrays.toString(playerControlledEntity.getInputList()));
    }

    // calls the entity's move function so it calculates the new position
    // TODO kinda redundant could remove, never used
    // public void clientMove(){ // TODO
    //     playerControlledEntity.move();
    // }

    public void receiveFromClient(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    try {
                        recivingSocket.receive(recivingPacket);
                        String stringFromClient = new String(recivingPacket.getData()).trim();

                        switch (Integer.parseInt(stringFromClient.substring(0, 2))) {
                            
                            case 1: // disconect packet, remove client, client handler and entity
                                // client sends 01 + clientName
                                infoArray = stringFromClient.substring(2).split(",");

                                //removes the players ship from the game
                                Main.entityList.remove(Server.userNameMap.get(infoArray[0]).getPlayerControlledEntity());

                                // removes the clientHandler so they dont get messages
                                Server.userNameMap.remove(infoArray[0]);

                                break;
                            
                            case 2: //inputs
                                // client sends 02 + userName,boolean,boolean,boolean ect...
                                // TODO figure out best order for booleans
                                infoArray = stringFromClient.substring(2).split(",");

                                System.out.println("ClientHandler: info array recieved: "+ Arrays.toString(infoArray)); 
                                Boolean[] booleans = new Boolean[infoArray.length - 1];
                                for (int i=1; i<infoArray.length; i++){
                                    booleans[i - 1] = Boolean.parseBoolean(infoArray[i]);
                                }
                                // System.out.println("Server: Boolean array: "+ Arrays.toString(booleans)); 
                                // System.out.println("Server: userHashMap: " + userNameMap); 
                                
                                // gets the clientHandler of the player and passes in the booleans recived from client
                                // Server.userNameMap.get(infoArray[0]).setInputBooleans(booleans);
                                // above but shorter TODO does this work
                                setInputBooleans(booleans);

                                System.out.println("ClientHandler: recived boolean inputs");

                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    @Override
    public void run() {
        receiveFromClient();

        // while(true){
        //     long time = System.currentTimeMillis();
        //     while(System.currentTimeMillis() - time < 25){}

        //     sendToClient();
        // }
    }


    public double roundHundreth(double doubleToRound){
        return (double) Math.round(doubleToRound * 100) / 100;
    }
    
}