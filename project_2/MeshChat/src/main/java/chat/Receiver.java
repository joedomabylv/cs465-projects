package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Receiver thread that listens for peer output
 * @author Joe Domabyl V
 */
public class Receiver extends Thread {
    
    // declare variables
    NodeInfo nodeInfo = null;
    ServerSocket clientReceiver = null;
    
    // constructor
    public Receiver(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
        
        // try to create the receiver
        try {
            // create a "listener" at the clients port
            System.out.println("Receiver is listening on port " + 
                    nodeInfo.clientPort);
            clientReceiver = new ServerSocket(nodeInfo.clientPort);
        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(
                    Level.SEVERE, null, ex);       
        }
    }
    
    @Override
    public void run() {

        try
        {
            // accept a new connection
            new ReceiverWorker(clientReceiver.accept()).start();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(Receiver.class.getName()).log(
                    Level.SEVERE, null, ex);
        } 
    }
    
}
