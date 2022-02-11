package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Worker class for peer receiver
 * @author Joe Domabyl V
 */
public class ReceiverWorker extends Thread {
    Socket clientConnection = null;
    ObjectInputStream fromPeer = null;
    ObjectOutputStream toPeer = null;
    String message = null;
    
    public ReceiverWorker(Socket clientConnection) {
        this.clientConnection = clientConnection;
    }
    
    @Override
    public void run() {
        try
        {
            // establish IO streams
            fromPeer = new ObjectInputStream(
                    clientConnection.getInputStream());
            toPeer = new ObjectOutputStream(
                    clientConnection.getOutputStream());
            
            // read the object sent by the server
            message = (String) fromPeer.readObject();
            
            // close the connection
            clientConnection.close();
            
            System.out.println(message);
        }
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(ReceiverWorker.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }
}
