package chat;

import java.net.Socket;

/**
 *
 * @author Joe Domabyl V
 */
public class Sender extends Thread {
    
    // declare variables
    Boolean isJoined = false;
    Socket clientConnection = null;
    NodeInfo nodeInfo = null;

    // constructor
    public Sender(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }
    
    @Override
    public void run() {
        System.out.println("I AM THE SENDER");
    }
}
