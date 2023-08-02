import java.util.ArrayList;
import java.util.Collection;

public class GameDriver implements Runnable{

    public GameDriver(){
       new Thread(this).start(); 
    }
    @Override
    public void run() {
        while (true){
            long time = System.currentTimeMillis();
            while(System.currentTimeMillis() - time < 50){}

            ArrayList<GameEntity> cloneList = (ArrayList<GameEntity>) Main.entityList.clone();
            
            for (GameEntity entity : cloneList) {
                if(entity instanceof MobileEntity){
                    MobileEntity temp = (MobileEntity) entity;
                    temp.move();
                    temp.checkCollisions();
                }
            }

            Collection<ClientHandler> tempValues = Server.userNameMap.values();
            for (ClientHandler ch : tempValues) {
                ch.sendToClient();
            }
        }
    }
    
}
