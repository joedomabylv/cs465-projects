package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Receiver thread that listens for peer output
 * @author Joe Domabyl V
 */
public class Receiver extends Thread {
    
    // declare variables
    NodeInfo nodeInfo = null;
    ServerSocket peerReceiver = null;
    
    // constructor
    public Receiver(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
        
        // try to create the receiver
        try {
            // create a "listener" at the clients port
            System.out.println("Receiver is listening on port " + 
                    nodeInfo.peerPort);
            peerReceiver = new ServerSocket(nodeInfo.peerPort);
        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(
                    Level.SEVERE, null, ex);       
        }
    }
    
    @Override
    public void run() {

        // run receiver
        while(MeshClient.running)
        {
            try
            {
                
                // establish timeout in milliseconds such that if the client
                // sends a shutdown request, the receiver thread will stop
                // looking for a connection after 3 seconds
                peerReceiver.setSoTimeout(3000);
                
                // accept a new connection
                new ReceiverWorker(peerReceiver.accept()).start();
            }
            catch (SocketTimeoutException ex)
            {
                // do nothing, just check again
            }
            catch (IOException ex)
            {
                Logger.getLogger(Receiver.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
    }
    
}
