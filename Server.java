import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;

public class Server implements Runnable{

    private DatagramSocket serverRecivingSocket;
    private byte[] recivedBytes = new byte[1024];
    private DatagramPacket datagramPacket = new DatagramPacket(recivedBytes, recivedBytes.length);
    static HashMap<String, ClientHandler> userNameMap = new HashMap<>();

    public Server(DatagramSocket serverRecivingSocket){
        this.serverRecivingSocket = serverRecivingSocket;
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        recieve();
        broadcast();    
    }

    private void recieve() { //will recieve all incoming pakets from clients
        // i should change this to only recive the login packets then the client handlers will recive on differnent ports so no interference
        new Thread(new Runnable(){
            public void run(){
                while (true){
                    try {
                        serverRecivingSocket.receive(datagramPacket);
                        String stringFromClient = new String(recivedBytes).trim();
                        String[] infoArray;

                        switch (Integer.parseInt(stringFromClient.substring(0, 2))) {

                            case 0: //login packet, creates handler and adds to hashMap
                                InetAddress inetAddress = datagramPacket.getAddress();
                                int clientPort = datagramPacket.getPort();
                                DatagramSocket newSocket = new DatagramSocket();

                                infoArray = stringFromClient.substring(2).split(","); // client sends comma delim message

                                // TODO need to create and set the playerControlledEntity server side then add it to the list
                                userNameMap.put(infoArray[0], new ClientHandler(infoArray[0], new PlayerShip(0, 50, 50, Faction.PLAYER_1, GameSprite.spriteList.get(0)), inetAddress, clientPort, newSocket)); 
                                System.out.println("Server: client logged in");
                                break;
                            
                            case 1: // disconect packet, remove client, client handler and entity
                                // client sends 01 + clientName
                                infoArray = stringFromClient.substring(2).split(",");

                                //removes the players ship from the game
                                Main.entityList.remove(userNameMap.get(infoArray[0]).getPlayerControlledEntity());

                                // removes the clientHandler so they dont get messages
                                userNameMap.remove(infoArray[0]);

                                break;
                            
                            // case 2: //inputs
                            //     // client sends 02 + userName,boolean,boolean,boolean ect...
                            //     // TODO figure out best order for booleans
                            //     infoArray = stringFromClient.substring(2).split(",");

                            //     System.out.println("Server: info array recieved: "+ Arrays.toString(infoArray)); 
                            //     Boolean[] booleans = new Boolean[infoArray.length - 1];
                            //     for (int i=1; i<infoArray.length; i++){
                            //         booleans[i - 1] = Boolean.parseBoolean(infoArray[i]);
                            //     }
                            //     System.out.println("Server: Boolean array: "+ Arrays.toString(booleans)); 
                            //     System.out.println("Server: userHashMap: " + userNameMap); 
                                

                            //     userNameMap.get(infoArray[0]).setInputBooleans(booleans);

                            //     System.out.println("Server: recived boolean inputs");

                            //     break;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // runs on native thread
    private void broadcast() {// TODO call all clientHandlers send(), called by tick() in Main
        // while(false){
        //     //waits
        //     long time = System.currentTimeMillis();
        //     while(System.currentTimeMillis() - time < 100){}

        //     for(ClientHandler clntHdlr : userNameMap.values()){
        //         clntHdlr.sendToClient();
        //     }
        // }
    }
    
}
