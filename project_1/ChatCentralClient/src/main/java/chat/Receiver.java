package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Receiver thread that listens for server output
 * @author Joe
 */
public class Receiver extends Thread {
    
    // declare variables
    NodeInfo nodeInfo = null;
    ServerSocket serverReceiver = null;
    
    // default constructor
    public Receiver(NodeInfo nodeInfo){
        this.nodeInfo = nodeInfo;
        
        try {
            // create a "listener" at the clients port
            serverReceiver = new ServerSocket(nodeInfo.clientPort);
        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);       
        }
    }
    
    @Override
    public void run() {
        // run receiver loop
        while(true)
        {
            try
            {
                // accept a new connection
                new ReceiverWorker(serverReceiver.accept()).start();
            } catch (IOException ex)
            {
                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
