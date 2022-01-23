package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverWorker extends Thread {
    
    Socket serverConnection = null;
    ObjectInputStream fromServer = null;
    ObjectOutputStream toServer = null;
    String message = null;
    
    public ReceiverWorker(Socket serverConnection) {
        this.serverConnection = serverConnection;
    }
    
    @Override
    public void run() {
        try
        {
            // establish IO streams
            fromServer = new ObjectInputStream(serverConnection.getInputStream());
            toServer = new ObjectOutputStream(serverConnection.getOutputStream());
            
            // read the object sent by the server
            message = (String) fromServer.readObject();
            
            // close the connection
            serverConnection.close();
            
            System.out.println(message);
        } catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(ReceiverWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
