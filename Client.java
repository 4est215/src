import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client implements Runnable{
    // fields ------------------------------------------
    private String clientName;
    private InetAddress inetAddress;
    private int port;
    private int handlerPort;
    private DatagramSocket datagramSocket; // may need 2 like in the clientHandler/ server
    private byte[] sendingBytes = new byte[1024];
    private byte[] receivingBytes = new byte[1024];

    static String[][] spritesToDraw;
    
    private Boolean forwardForce = false;
    private Boolean leftForce = false;
    private Boolean rightForce = false;
    private Boolean backwardForce = false;
    private Boolean clockwiseTorque = false;
    private Boolean counterClockwiseTorque = false;
    private Boolean autoStopping = false;

    private Boolean[] pressedKeys = {forwardForce, leftForce, rightForce, backwardForce, clockwiseTorque, counterClockwiseTorque, autoStopping};
    private boolean[] booleanArray = new boolean[8];

    // constructor -----------------------------------------
    public Client(String clientName, InetAddress inetAddress, int port){
        this.clientName = clientName;
        this.inetAddress = inetAddress;
        this.port = port;

        try {
            datagramSocket = new DatagramSocket(); // removed port
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void run() {
        // sendMessage();
        reciveFromServer();
    }



    private void sendMessage() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                

                // FIXME not sending at all for some reason 
                while(true){
                    long currentTime = System.currentTimeMillis();
                    while(System.currentTimeMillis() - currentTime < 250){} //waits for 250 millisecods

                    String sendingString = new String("02"+clientName + ",");
                    for(boolean b : booleanArray){ //changed from pressed keys to regular booleanArray
                        sendingString += b + ","; //may need casting
                    }
                    System.out.println("Client: sends " + sendingString);
                    sendingBytes = sendingString.substring(0, sendingString.length()).getBytes();

                    DatagramPacket packet = new DatagramPacket(sendingBytes, 0, sendingBytes.length, inetAddress, handlerPort);
                    try {
                        datagramSocket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void reciveFromServer() {

        // send the login

        sendingBytes = new String("00" + clientName).getBytes();
        DatagramPacket loginPacket =  new DatagramPacket(sendingBytes, 0, sendingBytes.length, inetAddress, port);
        try {
            datagramSocket.send(loginPacket);
            System.out.println("Client: login sent");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO recvieve list of sprites and their ablsoute position, then calculate reletive postion for the screen
        // FIXME I NEVER CALCULATED THE RELETIVE POSITIONS

        // recvies new port to send to
        DatagramPacket packetFromServer = new DatagramPacket(receivingBytes, 0, 1024); // removed address and port
        try {
            datagramSocket.receive(packetFromServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        handlerPort = Integer.parseInt(new String(receivingBytes).trim());
        // starts sending inputs to the handlers port
        this.sendMessage();
        
        while(true){
            try {
                byte[] locationBytes = new byte[1024];
                packetFromServer = new DatagramPacket(locationBytes, 1024);
                datagramSocket.receive(packetFromServer);
                String stringFromServer = new String(locationBytes, 0, locationBytes.length).trim();
                String[] spriteArray = stringFromServer.split(":");
                spritesToDraw = new String[spriteArray.length][];
                //loop takes the array of sprites and adds it to the static array so the GamePanel can draw it
                for (int i=0; i<spriteArray.length; i++){
                    spritesToDraw[i] = spriteArray[i].split(",");                
                }

                System.out.println("Client: recieved " + stringFromServer);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // might not be getting here might not work
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                booleanArray[0] = true;
                break;
            case KeyEvent.VK_A:
                booleanArray[1] = true;
                break;
            case KeyEvent.VK_S:
                booleanArray[2] = true;
                break;
            case KeyEvent.VK_D:
                booleanArray[3] = true;
                break;
            case KeyEvent.VK_E:
                booleanArray[4] = true;
                break;
            case KeyEvent.VK_Q:
                booleanArray[5] = true;
                break;
            case KeyEvent.VK_B:
                booleanArray[6] = true;
                break;
            case KeyEvent.VK_SPACE:
                booleanArray[7] = true;
                break;
        }
        
    }

    public void keyReleased(KeyEvent e){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                booleanArray[0] = false;
                break;
            case KeyEvent.VK_A:
                booleanArray[1] = false;
                break;
            case KeyEvent.VK_S:
                booleanArray[2] = false;
                break;
            case KeyEvent.VK_D:
                booleanArray[3] = false;
                break;
            case KeyEvent.VK_E:
                booleanArray[4] = false;
                break;
            case KeyEvent.VK_Q:
                booleanArray[5] = false;
                break;
            case KeyEvent.VK_B:
                booleanArray[6] = false;
                break;
            case KeyEvent.VK_SPACE:
                booleanArray[7] = false;
                break;
        }
    }
    
}
