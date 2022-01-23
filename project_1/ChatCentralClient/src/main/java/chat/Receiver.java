package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Receiver thread that listens for server output
 * @author Joe
 */
public class Receiver extends Thread {
    
    // declare variables
    NodeInfo nodeInfo = null;
    
    // default constructor
    public Receiver(NodeInfo nodeInfo){
        this.nodeInfo = nodeInfo;
    }
    
    @Override
    public void run() {

    }
    
}
